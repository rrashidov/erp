package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.Customer;
import org.roko.erp.model.CustomerLedgerEntry;

public interface CustomerLedgerEntryService {
    
    public void create(CustomerLedgerEntry customerLedgerEntry);

    public List<CustomerLedgerEntry> findFor(Customer customer);

    public long count(Customer customer);
}
