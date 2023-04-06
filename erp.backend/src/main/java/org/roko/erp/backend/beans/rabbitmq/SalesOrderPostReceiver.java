package org.roko.erp.backend.beans.rabbitmq;

import org.roko.erp.backend.services.AsyncSalesOrderPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SalesOrderPostReceiver {

    private AsyncSalesOrderPostService svc;

    @Autowired
    public SalesOrderPostReceiver(AsyncSalesOrderPostService svc) {
        this.svc = svc;
    }

    public void receiveMessage(String message) {
        svc.post(message);
    }

}