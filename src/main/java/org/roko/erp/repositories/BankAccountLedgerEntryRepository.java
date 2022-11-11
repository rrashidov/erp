package org.roko.erp.repositories;

import org.roko.erp.model.BankAccountLedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountLedgerEntryRepository extends JpaRepository<BankAccountLedgerEntry, Long> {
    
}
