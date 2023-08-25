package org.roko.erp.itests.runner.purchases.util;

import java.util.List;

import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.itests.clients.PurchaseOrderClient;
import org.roko.erp.itests.runner.util.AbstractBarrieredCallable;

public class PurchaseOrderPostCallable extends AbstractBarrieredCallable {

    private PurchaseOrderClient purchaseOrderClient;
    private String code;

    public PurchaseOrderPostCallable(PurchaseOrderClient purchaseOrderClient, Object barrier, String code,
            List<Object> feedbackList) {
        super(barrier, feedbackList);
        this.purchaseOrderClient = purchaseOrderClient;
        this.code = code;
    }

    @Override
    protected Boolean doCall() {
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
