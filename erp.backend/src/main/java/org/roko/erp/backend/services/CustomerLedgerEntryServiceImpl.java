package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.Customer;
import org.roko.erp.backend.model.CustomerLedgerEntry;
import org.roko.erp.backend.repositories.CustomerLedgerEntryRepository;
import org.roko.erp.dto.CustomerLedgerEntryDTO;
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
        return repo.findFor(customer, PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE)).toList();
    }

    @Override
    public long count(Customer customer) {
        return repo.count(customer);
    }

    @Override
    public CustomerLedgerEntryDTO toDTO(CustomerLedgerEntry customerLedgerEntry) {
        CustomerLedgerEntryDTO dto = new CustomerLedgerEntryDTO();
        dto.setId(customerLedgerEntry.getId());
        dto.setType(customerLedgerEntry.getType().name());
        dto.setDocumentCode(customerLedgerEntry.getDocumentCode());
        dto.setDate(customerLedgerEntry.getDate());
        dto.setAmount(customerLedgerEntry.getAmount());
        return dto;
    }
        
}
