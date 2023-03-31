package org.roko.erp.backend.beans.rabbitmq;

import org.springframework.stereotype.Component;

@Component
public class SalesCreditMemoPostReceiver {

    public void receiveMessage(String message) {
        System.out.println("Post sales credit memo " + message);
    }

}