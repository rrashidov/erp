package org.roko.erp.backend.beans.rabbitmq;

import org.roko.erp.backend.services.AsyncPurchaseCreditMemoPostService;
import org.springframework.stereotype.Component;

@Component
public class PurchaseCreditMemoPostReceiver {

    private AsyncPurchaseCreditMemoPostService svc;

    public PurchaseCreditMemoPostReceiver(AsyncPurchaseCreditMemoPostService svcMock) {
        this.svc = svcMock;
    }

    public void receiveMessage(String message) {
        svc.post(message);
    }

}