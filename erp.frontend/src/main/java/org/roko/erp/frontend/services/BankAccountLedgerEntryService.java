package org.roko.erp.frontend.services;

import org.roko.erp.dto.list.BankAccountLedgerEntryList;

public interface BankAccountLedgerEntryService {
    
    public BankAccountLedgerEntryList list(String bankAccountCode, int page);

}
