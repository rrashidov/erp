package org.roko.erp.repositories;

import java.util.List;

import org.roko.erp.model.Customer;
import org.roko.erp.model.CustomerLedgerEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerLedgerEntryRepository extends JpaRepository<CustomerLedgerEntry, Long> {

    @Query("SELECT customerLedgerEntry FROM CustomerLedgerEntry customerLedgerEntry WHERE customerLedgerEntry.customer = :customer")
    public List<CustomerLedgerEntry> findFor(@Param("customer") Customer customer);

    @Query("SELECT customerLedgerEntry FROM CustomerLedgerEntry customerLedgerEntry WHERE customerLedgerEntry.customer = :customer")
    public Page<CustomerLedgerEntry> findFor(@Param("customer") Customer customer, Pageable pageable);

    @Query("SELECT COUNT(customerLedgerEntry) FROM CustomerLedgerEntry customerLedgerEntry WHERE customerLedgerEntry.customer = :customer")
    public long count(@Param("customer") Customer customer);

}
