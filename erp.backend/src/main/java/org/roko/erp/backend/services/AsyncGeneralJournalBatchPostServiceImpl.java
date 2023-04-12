package org.roko.erp.backend.services;

import org.roko.erp.backend.model.DocumentPostStatus;
import org.roko.erp.backend.model.GeneralJournalBatch;
import org.roko.erp.backend.services.exc.PostFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsyncGeneralJournalBatchPostServiceImpl implements AsyncGeneralJournalBatchPostService {

    private GeneralJournalBatchService generalJournalBatchSvc;
    private GeneralJournalBatchPostService generalJournalBatchPostSvc;

    @Autowired
    public AsyncGeneralJournalBatchPostServiceImpl(GeneralJournalBatchService generalJournalBatchSvc, GeneralJournalBatchPostService generalJournalBatchPostSvc) {
        this.generalJournalBatchSvc = generalJournalBatchSvc;
        this.generalJournalBatchPostSvc = generalJournalBatchPostSvc;
    }

    @Override
    public void post(String code) {
        try {
            generalJournalBatchPostSvc.post(code);
        } catch (PostFailedException e) {
            updateGeneralJournalBatchPostStatus(code, e.getMessage());
        }
    }

    private void updateGeneralJournalBatchPostStatus(String code, String message) {
        GeneralJournalBatch generalJournalBatch = generalJournalBatchSvc.get(code);
        generalJournalBatch.setPostStatus(DocumentPostStatus.FAILED);
        generalJournalBatch.setPostStatusReason(message);
        generalJournalBatchSvc.update(code, generalJournalBatch);
    }
    
}
