package org.roko.erp.backend.repositories;

import java.util.List;

import org.roko.erp.backend.model.Vendor;
import org.roko.erp.backend.model.VendorLedgerEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorLedgerEntryRepository extends JpaRepository<VendorLedgerEntry, Long> {

    @Query("SELECT vendorLedgerEntry FROM VendorLedgerEntry vendorLedgerEntry WHERE vendorLedgerEntry.vendor = :vendor")
    public List<VendorLedgerEntry> findFor(@Param("vendor") Vendor vendor);

    @Query("SELECT vendorLedgerEntry FROM VendorLedgerEntry vendorLedgerEntry WHERE vendorLedgerEntry.vendor = :vendor")
    public Page<VendorLedgerEntry> findFor(@Param("vendor") Vendor vendor, Pageable pageable);

    @Query("SELECT COUNT(vendorLedgerEntry) FROM VendorLedgerEntry vendorLedgerEntry WHERE vendorLedgerEntry.vendor = :vendor")
    public long count(@Param("vendor") Vendor vendor);

}
