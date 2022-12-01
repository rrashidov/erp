package org.roko.erp.backend.repositories;

import java.util.List;

import org.roko.erp.backend.model.Item;
import org.roko.erp.backend.model.ItemLedgerEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemLedgerEntryRepository extends JpaRepository<ItemLedgerEntry, Long> {

    @Query("SELECT itemLedgerEntry FROM ItemLedgerEntry itemLedgerEntry WHERE itemLedgerEntry.item = :item")
    public List<ItemLedgerEntry> findFor(@Param("item") Item item);

    @Query("SELECT itemLedgerEntry FROM ItemLedgerEntry itemLedgerEntry WHERE itemLedgerEntry.item = :item")
    public Page<ItemLedgerEntry> findFor(@Param("item") Item item, Pageable pageable);

    @Query("SELECT COUNT(itemLedgerEntry) FROM ItemLedgerEntry itemLedgerEntry WHERE itemLedgerEntry.item = :item")
    public long count(@Param("item") Item item);
}
