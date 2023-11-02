package org.roko.erp.backend.repositories;

import java.math.BigDecimal;

import org.roko.erp.backend.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    @Query("SELECT COALESCE(SUM(customerLedgerEntry.amount), 0) FROM CustomerLedgerEntry customerLedgerEntry WHERE customerLedgerEntry.customer = :customer")
    public BigDecimal balance(@Param("customer") Customer customer);
}
