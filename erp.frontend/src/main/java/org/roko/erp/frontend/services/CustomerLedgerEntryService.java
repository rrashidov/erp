package org.roko.erp.frontend.services;

import org.roko.erp.dto.list.CustomerLedgerEntryList;

public interface CustomerLedgerEntryService {
    
    public CustomerLedgerEntryList list(String customerCode, int page);

}
