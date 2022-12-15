package org.roko.erp.frontend.services;

import java.util.List;

import org.roko.erp.frontend.controllers.paging.PagingServiceImpl;
import org.roko.erp.frontend.model.BankAccount;
import org.roko.erp.frontend.model.BankAccountLedgerEntry;
import org.roko.erp.frontend.repositories.BankAccountLedgerEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    public List<BankAccountLedgerEntry> findFor(BankAccount bankAccount, int page) {
        return repo.findFor(bankAccount, PageRequest.of(page - 1, PagingServiceImpl.RECORDS_PER_PAGE)).toList();
    }

    @Override
    public int count(BankAccount bankAccount) {
        return new Long(repo.count(bankAccount)).intValue();
    }
    
}
