package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.BankAccount;
import org.roko.erp.model.BankAccountLedgerEntry;
import org.roko.erp.repositories.BankAccountLedgerEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountLedgerEntryServiceImpl implements BankAccountLedgerEntryService {

    private BankAccountLedgerEntryRepository repo;

    @Autowired
    public BankAccountLedgerEntryServiceImpl(BankAccountLedgerEntryRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(BankAccountLedgerEntry bankAccountLedgerEntry) {
        repo.save(bankAccountLedgerEntry);
    }

    @Override
    public List<BankAccountLedgerEntry> findFor(BankAccount bankAccount) {
        return repo.findFor(bankAccount);
    }

    @Override
    public int count(BankAccount bankAccount) {
        return new Long(repo.count(bankAccount)).intValue();
    }
    
}
