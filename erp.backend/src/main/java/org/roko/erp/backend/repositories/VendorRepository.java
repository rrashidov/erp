package org.roko.erp.backend.repositories;

import org.roko.erp.backend.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, String> {

    @Query("SELECT COALESCE(SUM(vendorLedgerEntry.amount), 0) FROM VendorLedgerEntry vendorLedgerEntry WHERE vendorLedgerEntry.vendor = :vendor")
    public double balance(@Param("vendor") Vendor vendor);
}
