package org.roko.erp.repositories;

import org.roko.erp.model.VendorLedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorLedgerEntryRepository extends JpaRepository<VendorLedgerEntry, Long> {

}
