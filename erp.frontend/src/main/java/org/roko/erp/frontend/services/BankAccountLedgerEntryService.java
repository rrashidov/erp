package org.roko.erp.frontend.services;

import java.util.List;

import org.roko.erp.frontend.model.BankAccount;
import org.roko.erp.frontend.model.BankAccountLedgerEntry;

public interface BankAccountLedgerEntryService {
    
    public void create(BankAccountLedgerEntry bankAccountLedgerEntry);

    public List<BankAccountLedgerEntry> findFor(BankAccount bankAccount);

    public List<BankAccountLedgerEntry> findFor(BankAccount bankAccount, int page);

    public int count(BankAccount bankAccount);
}
