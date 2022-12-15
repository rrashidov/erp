package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.BankAccount;
import org.roko.erp.backend.model.BankAccountLedgerEntry;
import org.roko.erp.dto.list.BankAccountLedgerEntryList;

public interface BankAccountLedgerEntryService {
    
    public void create(BankAccountLedgerEntry bankAccountLedgerEntry);

    public List<BankAccountLedgerEntry> findFor(BankAccount bankAccount);

    public List<BankAccountLedgerEntry> findFor(BankAccount bankAccount, int page);

    public BankAccountLedgerEntryList list(BankAccount bankAccount, int page);

}
