package org.roko.erp.frontend.services;

import org.roko.erp.dto.list.ItemLedgerEntryList;

public interface ItemLedgerEntryService {

    public ItemLedgerEntryList list(String itemCode, int page);
    
}
