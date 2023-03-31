package org.roko.erp.backend.beans.rabbitmq;

import org.springframework.stereotype.Component;

@Component
public class GeneralJournalBatchPostReceiver {

    public void receiveMessage(String message) {
        System.out.println("Post general journal batch " + message);
    }

}