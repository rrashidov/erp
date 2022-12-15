package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.BankAccount;
import org.roko.erp.backend.model.BankAccountLedgerEntry;
import org.roko.erp.dto.BankAccountLedgerEntryDTO;

public interface BankAccountLedgerEntryService {
    
    public void create(BankAccountLedgerEntry bankAccountLedgerEntry);

    public List<BankAccountLedgerEntry> findFor(BankAccount bankAccount);

    public List<BankAccountLedgerEntry> findFor(BankAccount bankAccount, int page);

    public int count(BankAccount bankAccount);

    public BankAccountLedgerEntryDTO toDTO(BankAccountLedgerEntry bankAccountLedgerEntry);
}
