package org.roko.erp.backend.services;

import org.roko.erp.backend.model.DocumentPostStatus;
import org.roko.erp.backend.model.PurchaseCreditMemo;
import org.roko.erp.backend.services.exc.PostFailedException;
import org.springframework.beans.factory.annotation.Autowired;

public class AsyncPurchaseCreditMemoPostServiceImpl implements AsyncPurchaseCreditMemoPostService {

    private PurchaseCreditMemoService purchaseCreditMemoSvc;
    private PurchaseCreditMemoPostService purchaseCreditMemoPostSvc;

    @Autowired
    public AsyncPurchaseCreditMemoPostServiceImpl(PurchaseCreditMemoService purchaseCreditMemoSvc,
            PurchaseCreditMemoPostService purchaseCreditMemoPostSvc) {
                this.purchaseCreditMemoSvc = purchaseCreditMemoSvc;
                this.purchaseCreditMemoPostSvc = purchaseCreditMemoPostSvc;
    }

    @Override
    public void post(String code) {
        try {
            purchaseCreditMemoPostSvc.post(code);
        } catch (PostFailedException e) {
            updatePurchaseCreditMemoPostStatus(code, e.getMessage());
        }
    }

    private void updatePurchaseCreditMemoPostStatus(String code, String message) {
        PurchaseCreditMemo purchaseCreditMemo = purchaseCreditMemoSvc.get(code);
        purchaseCreditMemo.setPostStatus(DocumentPostStatus.FAILED);
        purchaseCreditMemo.setPostStatusReason(message);
        purchaseCreditMemoSvc.update(code, purchaseCreditMemo);
    }
    
}
