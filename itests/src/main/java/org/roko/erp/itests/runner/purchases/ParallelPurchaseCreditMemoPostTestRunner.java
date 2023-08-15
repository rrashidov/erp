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
import org.roko.erp.itests.clients.ItemClient;
import org.roko.erp.itests.clients.PurchaseCreditMemoClient;
import org.roko.erp.itests.clients.PurchaseCreditMemoLineClient;
import org.roko.erp.itests.runner.ITestFailedException;
import org.roko.erp.itests.runner.ITestRunner;
import org.roko.erp.itests.runner.purchases.util.PurchaseCreditMemoPostCallable;
import org.roko.erp.itests.runner.util.BusinessLogicSetupUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParallelPurchaseCreditMemoPostTestRunner implements ITestRunner {

    private static final int PARALLEL_POST_CNT = 10;

    private static final Logger LOGGER = LoggerFactory.getLogger("erp.itests");

    private static final Object BARRIER = new Object();

    private List<Object> feedbackList = new LinkedList<>();
    private ExecutorService es = Executors.newFixedThreadPool(PARALLEL_POST_CNT);

    @Autowired
    private PurchaseCreditMemoClient purchaseCreditMemoClient;

    @Autowired
    private PurchaseCreditMemoLineClient purchaseCreditMemoLineClient;

    @Autowired
    private ItemClient itemClient;

    @Override
    public void run() throws ITestFailedException {
        LOGGER.info("Running parallel purchase credit memo post test");

        try {
            List<String> purchaseCreditMemoCodes = createPurchaseCreditMemos();

            List<Future<Boolean>> purchaseCreditMemoPostFutures = schedulePostOperations(purchaseCreditMemoCodes);

            waitPostOperationsReachBarrier();

            liftBarrier();

            verifyPurchaseCreditMemoPostResults(purchaseCreditMemoPostFutures);
        } finally {
            cleanup();
        }

        LOGGER.info("Parallel purchase credit memo post test passed");
    }

    private void cleanup() {
        es.shutdown();
    }

    private void verifyPurchaseCreditMemoPostResults(List<Future<Boolean>> purchaseCreditMemoPostFutures)
            throws ITestFailedException {
        final List<Object> successfullyPostedPurchaseCreditMemos = new ArrayList<>();

        purchaseCreditMemoPostFutures.stream()
                .forEach(x -> {
                    try {
                        if (x.get()) {
                            successfullyPostedPurchaseCreditMemos.add(new Object());
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        LOGGER.error("Problem getting post result", e);
                    }
                });

        if (successfullyPostedPurchaseCreditMemos.size() != 1) {
            throw new ITestFailedException(
                    String.format("Successfully posted purchase credit memo count issue: expected %d, got %d", 1,
                            successfullyPostedPurchaseCreditMemos.size()));
        }
    }

    private void liftBarrier() {
        synchronized (BARRIER) {
            BARRIER.notifyAll();
        }

        LOGGER.info("Purchase Credit Memos post barrier lifted");
    }

    private void waitPostOperationsReachBarrier() {
        int cnt = 0;
        while (cnt < PARALLEL_POST_CNT) {
            cnt = feedbackList.size();
        }

        LOGGER.info("Purchase Credit Memos post threads reached the barrier");
    }

    private List<Future<Boolean>> schedulePostOperations(List<String> purchaseCreditMemoCodes) {
        List<Future<Boolean>> result = new ArrayList<>();

        purchaseCreditMemoCodes.stream()
                .forEach(code -> {
                    result.add(es.submit(
                            new PurchaseCreditMemoPostCallable(purchaseCreditMemoClient, BARRIER, code, feedbackList)));
                });

        LOGGER.info("Purchase Credit Memos scheduled to be posted");

        return result;
    }

    private List<String> createPurchaseCreditMemos() {
        double itemInventory = itemClient.read(BusinessLogicSetupUtil.TEST_ITEM_CODE).getInventory();

        List<String> result = new ArrayList<>();

        for (int i = 0; i < PARALLEL_POST_CNT; i++) {
            result.add(createPurchaseCreditMemo(itemInventory));
        }

        LOGGER.info("Purchase Credit Memos created");

        return result;
    }

    private String createPurchaseCreditMemo(double itemInventory) {
        PurchaseDocumentDTO purchaseCreditMemo = new PurchaseDocumentDTO();
        purchaseCreditMemo.setVendorCode(BusinessLogicSetupUtil.TEST_VENDOR_CODE);
        purchaseCreditMemo.setPaymentMethodCode(BusinessLogicSetupUtil.TEST_PAYMENT_METHOD_CODE);
        purchaseCreditMemo.setDate(new Date());

        String code = purchaseCreditMemoClient.create(purchaseCreditMemo);

        createPurchaseCreditMemoLine(code, itemInventory);

        return code;
    }

    private void createPurchaseCreditMemoLine(String code, double itemInventory) {
        PurchaseDocumentLineDTO purchaseDocumentLine = new PurchaseDocumentLineDTO();
        purchaseDocumentLine.setItemCode(BusinessLogicSetupUtil.TEST_ITEM_CODE);
        purchaseDocumentLine.setQuantity(itemInventory);
        purchaseDocumentLine.setPrice(1.0);
        purchaseDocumentLine.setAmount(itemInventory);

        purchaseCreditMemoLineClient.create(code, purchaseDocumentLine);
    }

}
