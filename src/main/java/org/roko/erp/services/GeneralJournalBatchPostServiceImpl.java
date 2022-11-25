package org.roko.erp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GeneralJournalBatchPostServiceImpl implements GeneralJournalBatchPostService {

    private GeneralJournalBatchService generalJournalBatchSvc;
    private GeneralJournalBatchLineService generalJournalBatchLineSvc;

    @Autowired
    public GeneralJournalBatchPostServiceImpl(GeneralJournalBatchService generalJournalBatchSvc,
            GeneralJournalBatchLineService generalJournalBatchLineSvc) {
        this.generalJournalBatchSvc = generalJournalBatchSvc;
        this.generalJournalBatchLineSvc = generalJournalBatchLineSvc;
    }

    @Override
    @Transactional(rollbackFor = PostFailedException.class)
    public void post(String code) throws PostFailedException {
        // TODO Auto-generated method stub

    }

}
