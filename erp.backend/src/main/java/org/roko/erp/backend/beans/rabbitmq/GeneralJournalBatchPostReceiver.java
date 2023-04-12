package org.roko.erp.backend.beans.rabbitmq;

import org.roko.erp.backend.services.AsyncGeneralJournalBatchPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GeneralJournalBatchPostReceiver {

    private AsyncGeneralJournalBatchPostService svc;

    @Autowired
    public GeneralJournalBatchPostReceiver(AsyncGeneralJournalBatchPostService svc) {
        this.svc = svc;
    }

    public void receiveMessage(String message) {
        svc.post(message);
    }

}