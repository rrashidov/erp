package org.roko.erp.repositories;

import org.roko.erp.model.ItemLedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemLedgerEntryRepository extends JpaRepository<ItemLedgerEntry, Long> {

}
