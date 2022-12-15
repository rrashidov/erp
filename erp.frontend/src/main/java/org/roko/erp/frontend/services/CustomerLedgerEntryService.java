package org.roko.erp.frontend.services;

import java.util.List;

import org.roko.erp.frontend.model.Customer;
import org.roko.erp.frontend.model.CustomerLedgerEntry;

public interface CustomerLedgerEntryService {
    
    public void create(CustomerLedgerEntry customerLedgerEntry);

    public List<CustomerLedgerEntry> findFor(Customer customer);

    public List<CustomerLedgerEntry> findFor(Customer customer, int page);

    public int count(Customer customer);
}
