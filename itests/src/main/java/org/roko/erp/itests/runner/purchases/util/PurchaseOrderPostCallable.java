package org.roko.erp.itests.runner.purchases.util;

import java.util.List;
import java.util.concurrent.Callable;

import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.itests.clients.PurchaseOrderClient;

public class PurchaseOrderPostCallable implements Callable<Boolean> {

    private PurchaseOrderClient purchaseOrderClient;
    private Object barrier;
    private String code;
    private List<Object> feedbackList;

    public PurchaseOrderPostCallable(PurchaseOrderClient purchaseOrderClient, Object barrier, String code,
            List<Object> feedbackList) {
                this.purchaseOrderClient = purchaseOrderClient;
                this.barrier = barrier;
                this.code = code;
                this.feedbackList = feedbackList;
    }

    @Override
    public Boolean call() throws Exception {
        synchronized(barrier){
            try {
                feedbackList.add(new Object());
                barrier.wait();
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }

        purchaseOrderClient.post(code);

        return purchaseOrderPostedSuccessfully();
    }

    private Boolean purchaseOrderPostedSuccessfully() {
        PurchaseDocumentDTO purchaseOrder = purchaseOrderClient.read(code);

        while ((purchaseOrder != null) && (!purchaseOrder.getPostStatus().equals("FAILED"))) {
            purchaseOrder = purchaseOrderClient.read(code);
        }

        return purchaseOrder == null;
    }

}
