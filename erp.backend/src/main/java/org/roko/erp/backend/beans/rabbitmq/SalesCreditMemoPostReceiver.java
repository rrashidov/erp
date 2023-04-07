package org.roko.erp.backend.beans.rabbitmq;

import org.roko.erp.backend.services.AsyncSalesCreditMemoPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SalesCreditMemoPostReceiver {

    private AsyncSalesCreditMemoPostService svc;

    @Autowired
    public SalesCreditMemoPostReceiver(AsyncSalesCreditMemoPostService svc) {
        this.svc = svc;
    }

    public void receiveMessage(String message) {
        svc.post(message);
    }

}