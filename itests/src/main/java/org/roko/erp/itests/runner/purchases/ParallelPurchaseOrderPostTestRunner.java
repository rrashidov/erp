package org.roko.erp.itests.runner.purchases;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.dto.PurchaseDocumentLineDTO;
import org.roko.erp.itests.clients.PurchaseOrderClient;
import org.roko.erp.itests.clients.PurchaseOrderLineClient;
import org.roko.erp.itests.runner.ITestFailedException;
import org.roko.erp.itests.runner.ITestRunner;
import org.roko.erp.itests.runner.purchases.util.PurchaseOrderPostCallable;
import org.roko.erp.itests.runner.util.BusinessLogicSetupUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParallelPurchaseOrderPostTestRunner implements ITestRunner {

    private static final int PARALLEL_POST_CNT = 10;

    private static final Logger LOGGER = LoggerFactory.getLogger("erp.itests");

    private static final Object BARRIER = new Object();

    private List<Object> feedbackList = new LinkedList<>();
    private ExecutorService es = Executors.newFixedThreadPool(PARALLEL_POST_CNT);

    @Autowired
    private BusinessLogicSetupUtil util;

    @Autowired
    private PurchaseOrderClient purchaseOrderClient;

    @Autowired
    private PurchaseOrderLineClient purchaseOrderLineClient;

    @Override
    public void run() throws ITestFailedException {
        LOGGER.info("Running parallel purchase order post test");

        try {
            util.ensureBankAccounts();

            List<String> purchaseOrderCodes = createPurchaseOrders();

            List<Future<Boolean>> purchaseOrderPostFutures = schedulePostOperations(purchaseOrderCodes);

            waitPostOperationsReachBarrier();

            liftBarrier();

            verifyPurchaseOrderPostResults(purchaseOrderPostFutures);
        } finally {
            cleanup();
        }

        LOGGER.info("Parallel purchase order post test passed");
    }

    private void cleanup() {
        es.shutdown();
    }

    private void verifyPurchaseOrderPostResults(List<Future<Boolean>> purchaseOrderPostFutures)
            throws ITestFailedException {
        final List<Object> successfullyPostedPurchaseOrders = new ArrayList<>();

        purchaseOrderPostFutures.stream()
                .forEach(x -> {
                    try {
                        if (x.get()) {
                            successfullyPostedPurchaseOrders.add(new Object());
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        LOGGER.error("Problem getting post result", e);
                    }
                });

        if (successfullyPostedPurchaseOrders.size() != 1) {
            throw new ITestFailedException(
                    String.format("Successfully posted sales credit memo count issue: expected %d, got %d", 1,
                            successfullyPostedPurchaseOrders.size()));
        }
    }

    private void liftBarrier() {
        synchronized (BARRIER) {
            BARRIER.notifyAll();
        }

        LOGGER.info("Purchase Orders post barrier lifted");
    }

    private void waitPostOperationsReachBarrier() {
        int cnt = 0;
        while (cnt < PARALLEL_POST_CNT) {
            cnt = feedbackList.size();
        }

        LOGGER.info("Purchase Orders post threads reached the barrier");
    }

    private List<Future<Boolean>> schedulePostOperations(List<String> purchaseOrderCodes) {
        List<Future<Boolean>> result = new LinkedList<>();

        purchaseOrderCodes.stream()
                .forEach(code -> {
                    result.add(
                            es.submit(new PurchaseOrderPostCallable(purchaseOrderClient, BARRIER, code, feedbackList)));
                });

        LOGGER.info("Purchase Orders scheduled to be posted");

        return result;
    }

    private List<String> createPurchaseOrders() {
        List<String> result = new LinkedList<>();

        for (int i = 0; i < PARALLEL_POST_CNT; i++) {
            result.add(createPurchaseOrder());
        }

        LOGGER.info("Purchase Orders created");

        return result;
    }

    private String createPurchaseOrder() {
        PurchaseDocumentDTO purchaseOrder = new PurchaseDocumentDTO();
        purchaseOrder.setVendorCode(BusinessLogicSetupUtil.TEST_VENDOR_CODE);
        purchaseOrder.setPaymentMethodCode(BusinessLogicSetupUtil.TEST_PAYMENT_METHOD_CODE);
        purchaseOrder.setDate(new Date());

        String code = purchaseOrderClient.create(purchaseOrder);

        createPurchaseOrderLine(code);

        return code;
    }

    private void createPurchaseOrderLine(String code) {
        PurchaseDocumentLineDTO purchaseOrderLine = new PurchaseDocumentLineDTO();
        purchaseOrderLine.setItemCode(BusinessLogicSetupUtil.TEST_ITEM_CODE);
        purchaseOrderLine.setQuantity(1.0);
        purchaseOrderLine.setPrice(BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_BALANCE);
        purchaseOrderLine.setAmount(BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_BALANCE);

        purchaseOrderLineClient.create(code, purchaseOrderLine);
    }

}
