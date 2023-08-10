package org.roko.erp.itests.runner.sales;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.dto.SalesDocumentLineDTO;
import org.roko.erp.itests.clients.SalesCreditMemoClient;
import org.roko.erp.itests.clients.SalesCreditMemoLineClient;
import org.roko.erp.itests.runner.ITestFailedException;
import org.roko.erp.itests.runner.ITestRunner;
import org.roko.erp.itests.runner.sales.util.SalesCreditMemoPostCallable;
import org.roko.erp.itests.runner.util.BusinessLogicSetupUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParallelSalesCreditMemoPostTestRunner implements ITestRunner {

    private static final int PARALLEL_POST_CNT = 10;

    private static final Logger LOGGER = LoggerFactory.getLogger("erp.itests");

    private static final Object BARRIER = new Object();

    private List<Object> feedbackList = new LinkedList<>();
    private ExecutorService es = Executors.newFixedThreadPool(PARALLEL_POST_CNT);

    @Autowired
    private BusinessLogicSetupUtil util;

    @Autowired
    private SalesCreditMemoClient salesCreditMemoClient;

    @Autowired
    private SalesCreditMemoLineClient salesCreditMemoLineClient;

    @Override
    public void run() throws ITestFailedException {
        LOGGER.info("Running parallel sales credit memo post test");

        try {
            util.ensureBankAccounts();

            List<String> salesCreditMemoCodes = createSalesCreditMemos();

            List<Future<Boolean>> salesCreditMemoPostFutures = schedulePostOperations(salesCreditMemoCodes);

            waitPostOperationsReachBarrier();

            liftBarrier();

            verifySalesCreditMemoPostResults(salesCreditMemoPostFutures);
        } finally {
            cleanup();
        }

        LOGGER.info("Parallel sales credit memo post test passed");
    }

    private void cleanup() {
        es.shutdown();
    }

    private void verifySalesCreditMemoPostResults(List<Future<Boolean>> salesCreditMemoPostFutures) throws ITestFailedException {
        final List<Object> successfullyPostedSalesOrders = new ArrayList<>();

        salesCreditMemoPostFutures.stream()
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
                    String.format("Successfully posted sales credit memo count issue: expected %d, got %d", 1,
                            successfullyPostedSalesOrders.size()));
        }
    }

    private void liftBarrier() {
        synchronized (BARRIER) {
            BARRIER.notifyAll();
        }

        LOGGER.info("Sales Creedit Memos post barrier lifted");
    }

    private void waitPostOperationsReachBarrier() {
        int cnt = 0;
        while (cnt < PARALLEL_POST_CNT) {
            cnt = feedbackList.size();
        }

        LOGGER.info("Sales Credit Memo post threads reached the barrier");
    }

    private List<Future<Boolean>> schedulePostOperations(List<String> salesCreditMemoCodes) {
        List<Future<Boolean>> result = new LinkedList<>();

        salesCreditMemoCodes.stream()
            .forEach(code -> {
                result.add(es.submit(new SalesCreditMemoPostCallable(salesCreditMemoClient, BARRIER, code, feedbackList)));
            });
        return result;
    }

    private List<String> createSalesCreditMemos() {
        List<String> result = new LinkedList<>();

        for (int i = 0; i < PARALLEL_POST_CNT; i++) {
            result.add(createSalesCreditMemo());
        }

        LOGGER.info("Sales Credit Memos created");

        return result;
    }

    private String createSalesCreditMemo() {
        SalesDocumentDTO salesCreditMemo = new SalesDocumentDTO();
        salesCreditMemo.setCustomerCode(BusinessLogicSetupUtil.TEST_CUSTOMER_CODE);
        salesCreditMemo.setPaymentMethodCode(BusinessLogicSetupUtil.TEST_PAYMENT_METHOD_CODE);
        salesCreditMemo.setDate(new Date());

        String code = salesCreditMemoClient.create(salesCreditMemo);

        createSalesCreditMemoLine(code);
        
        return code;
    }

    private void createSalesCreditMemoLine(String code) {
        SalesDocumentLineDTO salesCreditMemoLine = new SalesDocumentLineDTO();
        salesCreditMemoLine.setItemCode(BusinessLogicSetupUtil.TEST_ITEM_CODE);
        salesCreditMemoLine.setQuantity(1.0);
        salesCreditMemoLine.setPrice(BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_BALANCE);
        salesCreditMemoLine.setAmount(BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_BALANCE);

        salesCreditMemoLineClient.create(code, salesCreditMemoLine);
    }
    
}
