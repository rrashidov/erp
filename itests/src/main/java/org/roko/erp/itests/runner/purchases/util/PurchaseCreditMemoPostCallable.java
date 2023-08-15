package org.roko.erp.itests.runner.purchases.util;

import java.util.List;
import java.util.concurrent.Callable;

import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.itests.clients.PurchaseCreditMemoClient;

public class PurchaseCreditMemoPostCallable implements Callable<Boolean> {

    private final PurchaseCreditMemoClient purchaseCreditMemoClient;
    private final Object barrier;
    private final String code;
    private final List<Object> feedbackList;

    public PurchaseCreditMemoPostCallable(PurchaseCreditMemoClient purchaseCreditMemoClient, Object barrier,
            String code, List<Object> feedbackList) {
        this.purchaseCreditMemoClient = purchaseCreditMemoClient;
        this.barrier = barrier;
        this.code = code;
        this.feedbackList = feedbackList;
    }

    @Override
    public Boolean call() throws Exception {
        synchronized (barrier) {
            try {
                feedbackList.add(new Object());
                barrier.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        purchaseCreditMemoClient.post(code);

        return purchaseCreditMemoPostedSuccessfully();
    }

    private Boolean purchaseCreditMemoPostedSuccessfully() {
        PurchaseDocumentDTO purchaseCreditMemo = purchaseCreditMemoClient.read(code);

        while ((purchaseCreditMemo != null) && (!purchaseCreditMemo.getPostStatus().equals("FAILED"))) {
            purchaseCreditMemo = purchaseCreditMemoClient.read(code);
        }

        return purchaseCreditMemo == null;
    }

}
