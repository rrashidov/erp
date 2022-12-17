package org.roko.erp.frontend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GeneralJournalBatchPostServiceImpl implements GeneralJournalBatchPostService {

    @Autowired
    public GeneralJournalBatchPostServiceImpl(GeneralJournalBatchService generalJournalBatchSvc,
            GeneralJournalBatchLineService generalJournalBatchLineSvc,
            CustomerLedgerEntryService customerLedgerEntrySvc, CustomerService customerSvc,
            VendorLedgerEntryService vendorLedgerEntrySvc, VendorService vendorSvc,
            BankAccountLedgerEntryService bankAccountLedgerEntrySvc, BankAccountService bankAccountSvc) {
    }

    @Override
    @Transactional(rollbackFor = PostFailedException.class)
    public void post(String code) throws PostFailedException {
    }

}
