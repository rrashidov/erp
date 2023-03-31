package org.roko.erp.backend.beans.rabbitmq;

import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderPostReceiver {

    public void receiveMessage(String message) {
        System.out.println("Post purchase order " + message);
    }

}