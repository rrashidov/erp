package org.roko.erp.frontend.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalesOrderPostServiceImpl implements SalesOrderPostService {

    @Autowired
    public SalesOrderPostServiceImpl(SalesOrderService salesOrderSvc, SalesOrderLineService salesOrderLineSvc,
            PostedSalesOrderService postedSalesOrderSvc, PostedSalesOrderLineService postedSalesOrderLineSvc,
            ItemService itemSvc, ItemLedgerEntryService itemLedgerEntrySvc, CustomerLedgerEntryService customerLedgerEntrySvc,
            BankAccountLedgerEntryService bankAccountLedgerEntrySvc, SalesCodeSeriesService salesCodeSeriesSvc) {
    }

    @Override
    @Transactional(rollbackOn = PostFailedException.class)
    public void post(String code) throws PostFailedException {
    }

}
