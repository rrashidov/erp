package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.Customer;
import org.roko.erp.backend.model.CustomerLedgerEntry;
import org.roko.erp.dto.CustomerLedgerEntryDTO;

public interface CustomerLedgerEntryService {
    
    public void create(CustomerLedgerEntry customerLedgerEntry);

    public List<CustomerLedgerEntry> findFor(Customer customer);

    public List<CustomerLedgerEntry> findFor(Customer customer, int page);

    public int count(Customer customer);

    public CustomerLedgerEntryDTO toDTO(CustomerLedgerEntry customerLedgerEntry);
}
