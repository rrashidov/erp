package org.roko.erp.itests.runner.sales;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.roko.erp.dto.ItemDTO;
import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.dto.SalesDocumentLineDTO;
import org.roko.erp.itests.clients.ItemClient;
import org.roko.erp.itests.clients.SalesOrderClient;
import org.roko.erp.itests.clients.SalesOrderLineClient;
import org.roko.erp.itests.runner.ITestFailedException;
import org.roko.erp.itests.runner.ITestRunner;
import org.roko.erp.itests.runner.sales.util.SalesOrderPostCallable;
import org.roko.erp.itests.runner.util.BusinessLogicSetupUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParallelSalesOrderPostTestRunner implements ITestRunner {

    private static final int PARALLEL_POST_CNT = 10;

    private static final Logger LOGGER = LoggerFactory.getLogger("erp.itests");

    private static final Object BARRIER = new Object();

    private List<Object> feedbackList = new LinkedList<>();
    private ExecutorService es = Executors.newFixedThreadPool(PARALLEL_POST_CNT);

    @Autowired
    private SalesOrderClient salesOrderClient;

    @Autowired
    private SalesOrderLineClient salesOrderLineClient;

    @Autowired
    private ItemClient itemClient;

    @Override
    public void run() throws ITestFailedException {
        LOGGER.info("Running parallel sales order post test");

        try {

            List<String> salesOrderCodes = createSalesOrders();

            List<Future<Boolean>> salesOrderPostFutures = schedulePostOperations(salesOrderCodes);

            waitPostOperationsReachBarrier();

            liftBarrier();

            verifySalesOrderPostResults(salesOrderPostFutures);
        } finally {
            cleanup();
        }

        LOGGER.info("Parallel sales order post test passed");
    }

    private void waitPostOperationsReachBarrier() {
        int cnt = 0;
        while (cnt < PARALLEL_POST_CNT) {
            cnt = feedbackList.size();
        }

        LOGGER.info("Sales Orders post threads reached the barrier");
    }

    private void cleanup() {
        es.shutdown();
    }

    private void liftBarrier() {
        synchronized (BARRIER) {
            BARRIER.notifyAll();
        }

        LOGGER.info("Sales Orders post barrier lifted");
    }

    private void verifySalesOrderPostResults(List<Future<Boolean>> salesOrderPostFutures) throws ITestFailedException {
        final List<Object> successfullyPostedSalesOrders = new ArrayList<>();

        salesOrderPostFutures.stream()
                .forEach(x -> {
                    try {
                        if (x.get()) {
                            successfullyPostedSalesOrders.add(new Object());
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        LOGGER.error("Problem getting post result", e);
                    }
                });

        if (successfullyPostedSalesOrders.size() != 1) {
            throw new ITestFailedException(
                    String.format("Successfully posted sales order count issue: expected %d, got %d", 1,
                            successfullyPostedSalesOrders.size()));
        }
    }

    private List<Future<Boolean>> schedulePostOperations(List<String> salesOrderCodes) {
        List<Future<Boolean>> result = new LinkedList<>();

        salesOrderCodes.stream()
                .forEach(code -> {
                    result.add(es.submit(new SalesOrderPostCallable(salesOrderClient, BARRIER, code, feedbackList)));
                });

        LOGGER.info("Sales Orders scheduled to be posted");

        return result;
    }

    private double getTestItemInventory() {
        ItemDTO item = itemClient.read(BusinessLogicSetupUtil.TEST_ITEM_CODE_2);
        return item.getInventory();
    }

    private List<String> createSalesOrders() {
        double itemInventory = getTestItemInventory();

        List<String> result = new LinkedList<>();

        for (int i = 0; i < PARALLEL_POST_CNT; i++) {
            result.add(createSalesOrder(itemInventory));
        }

        LOGGER.info("Sales Orders created");

        return result;
    }

    private String createSalesOrder(double itemInventory) {
        SalesDocumentDTO salesOrder = new SalesDocumentDTO();
        salesOrder.setCustomerCode(BusinessLogicSetupUtil.TEST_CUSTOMER_CODE);
        salesOrder.setPaymentMethodCode(BusinessLogicSetupUtil.DELAYED_PAYMENT_METHOD_CODE);
        salesOrder.setDate(new Date());

        String salesOrderCode = salesOrderClient.create(salesOrder);

        createSalesOrderLine(salesOrderCode, itemInventory);

        return salesOrderCode;
    }

    private void createSalesOrderLine(String salesOrderCode, double itemInventory) {
        SalesDocumentLineDTO salesDocumentLineDTO = new SalesDocumentLineDTO();
        salesDocumentLineDTO.setItemCode(BusinessLogicSetupUtil.TEST_ITEM_CODE_2);
        salesDocumentLineDTO.setQuantity(itemInventory);
        salesDocumentLineDTO.setPrice(1.0);
        salesDocumentLineDTO.setAmount(itemInventory);

        salesOrderLineClient.create(salesOrderCode, salesDocumentLineDTO);
    }

}
