package org.roko.erp.backend.services;

import org.roko.erp.backend.model.DocumentPostStatus;
import org.roko.erp.backend.model.PurchaseOrder;
import org.roko.erp.backend.services.exc.PostFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsyncPurchaseOrderPostServiceImpl implements AsyncPurchaseOrderPostService {

    private PurchaseOrderService purchaseOrderSvc;
    private PurchaseOrderPostService purchaseOrderPostSvc;

    @Autowired
    public AsyncPurchaseOrderPostServiceImpl(PurchaseOrderService purchaseOrderSvc, PurchaseOrderPostService purchaseOrderPostSvc) {
        this.purchaseOrderSvc = purchaseOrderSvc;
        this.purchaseOrderPostSvc = purchaseOrderPostSvc;
    }

    @Override
    public void post(String code) {
        try {
            purchaseOrderPostSvc.post(code);
        } catch (PostFailedException e) {
            updatePurchaseOrderPostStatus(code, e.getMessage());
        }
    }

    private void updatePurchaseOrderPostStatus(String code, String message) {
        PurchaseOrder purchaseOrder = purchaseOrderSvc.get(code);
        purchaseOrder.setPostStatus(DocumentPostStatus.FAILED);
        purchaseOrder.setPostStatusReason(message);
        purchaseOrderSvc.update(code, purchaseOrder);
    }
    
}
