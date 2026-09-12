package org.roko.erp.backend.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.roko.erp.backend.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, String> {

    @Query("SELECT COALESCE(SUM(vendorLedgerEntry.amount), 0) FROM VendorLedgerEntry vendorLedgerEntry WHERE vendorLedgerEntry.vendor = :vendor")
    public BigDecimal balance(@Param("vendor") Vendor vendor);

    public List<Vendor> findByNameContainingIgnoreCase(String name);

    public long countByNameContainingIgnoreCase(String name);
}
