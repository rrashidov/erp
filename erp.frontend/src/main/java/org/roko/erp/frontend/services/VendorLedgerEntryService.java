package org.roko.erp.frontend.services;

import org.roko.erp.dto.list.VendorLedgerEntryList;

public interface VendorLedgerEntryService {
    
    public VendorLedgerEntryList list(String code, int page);

}
