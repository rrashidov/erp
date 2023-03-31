package org.roko.erp.backend.beans.rabbitmq;

import org.springframework.stereotype.Component;

@Component
public class SalesOrderPostReceiver {

    public void receiveMessage(String message) {
        System.out.println("Post sales order " + message);
    }

}