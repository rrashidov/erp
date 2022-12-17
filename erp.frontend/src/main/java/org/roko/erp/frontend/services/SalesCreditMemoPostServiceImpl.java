package org.roko.erp.frontend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SalesCreditMemoPostServiceImpl implements SalesCreditMemoPostService {

    @Autowired
    public SalesCreditMemoPostServiceImpl(SalesCreditMemoService salesCreditMemoSvc,
            SalesCreditMemoLineService salesCreditMemoLineSvc, PostedSalesCreditMemoService postedSalesCreditMemoSvc,
            PostedSalesCreditMemoLineService postedSalesCreditMemoLineSvc, ItemLedgerEntryService itemLedgerEntrySvc,
            CustomerLedgerEntryService customerLedgerEntrySvc,
            BankAccountLedgerEntryService bankAccountLedgerEntrySvc, BankAccountService bankAccountSvc,
            SalesCodeSeriesService salesCodeSeriesSvc) {
    }

    @Override
    @Transactional(rollbackFor = PostFailedException.class)
    public void post(String code) throws PostFailedException {
    }

}
