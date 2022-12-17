package org.roko.erp.frontend.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PurchaseCreditMemoPostServiceImpl implements PurchaseCreditMemoPostService {

    public PurchaseCreditMemoPostServiceImpl(PurchaseCreditMemoService purchaseCreditMemoSvc,
            PurchaseCreditMemoLineService purchaseCreditMemoLineSvc,
            PostedPurchaseCreditMemoService postedPurchaseCreditMemoSvc,
            PostedPurchaseCreditMemoLineService postedPurchaseCreditMemoLineSvc,
            ItemLedgerEntryService itemLedgerEntrySvc, ItemService itemSvc,
            VendorLedgerEntryService vendorLedgerEntrySvc,
            BankAccountLedgerEntryService bankAccountLedgerEntrySvc, PurchaseCodeSeriesService purchaseCodeSeriesSvc) {
    }

    @Override
    @Transactional(rollbackFor = PostFailedException.class)
    public void post(String code) throws PostFailedException {
    }

}
