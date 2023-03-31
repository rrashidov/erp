package org.roko.erp.backend.beans.rabbitmq;

import org.springframework.stereotype.Component;

@Component
public class PurchaseCreditMemoPostReceiver {

    public void receiveMessage(String message) {
        System.out.println("Post purchase credit memo " + message);
    }

}