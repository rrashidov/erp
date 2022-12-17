package org.roko.erp.frontend.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderPostServiceImpl implements PurchaseOrderPostService {

    @Autowired
    public PurchaseOrderPostServiceImpl(PurchaseOrderService purchaseOrderSvc,
            PurchaseOrderLineService purchaseOrderLineSvc, PostedPurchaseOrderService postedPurchaseOrderSvc,
            PostedPurchaseOrderLineService postedPurchaseOrderLineSvc, ItemLedgerEntryService itemLedgerEntrySvc,
            VendorLedgerEntryService vendorLedgerEntrySvc, BankAccountLedgerEntryService bankAccountLedgerEntrySvc,
            BankAccountService bankAccountSvc, PurchaseCodeSeriesService purchaseCodeSeriesSvc) {
    }

    @Override
    @Transactional(rollbackOn = PostFailedException.class)
    public void post(String code) throws PostFailedException {
    }

}
