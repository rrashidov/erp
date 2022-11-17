package org.roko.erp.services;

import java.util.List;

import org.roko.erp.controllers.paging.PagingServiceImpl;
import org.roko.erp.model.Customer;
import org.roko.erp.model.CustomerLedgerEntry;
import org.roko.erp.repositories.CustomerLedgerEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CustomerLedgerEntryServiceImpl implements CustomerLedgerEntryService {

    private CustomerLedgerEntryRepository repo;

    @Autowired
    public CustomerLedgerEntryServiceImpl(CustomerLedgerEntryRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(CustomerLedgerEntry customerLedgerEntry) {
        repo.save(customerLedgerEntry);
    }

    @Override
    public List<CustomerLedgerEntry> findFor(Customer customer) {
        return repo.findFor(customer);
    }

    @Override
    public List<CustomerLedgerEntry> findFor(Customer customer, int page) {
        return repo.findFor(customer, PageRequest.of(page - 1, PagingServiceImpl.RECORDS_PER_PAGE)).toList();
    }

    @Override
    public int count(Customer customer) {
        return new Long(repo.count(customer)).intValue();
    }
    
}
