package org.roko.erp.backend.beans.rabbitmq;

import org.roko.erp.backend.services.AsyncPurchaseOrderPostService;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderPostReceiver {

    private AsyncPurchaseOrderPostService svc;

    public PurchaseOrderPostReceiver(AsyncPurchaseOrderPostService svc) {
        this.svc = svc;
    }

    public void receiveMessage(String message) {
        svc.post(message);
    }

}