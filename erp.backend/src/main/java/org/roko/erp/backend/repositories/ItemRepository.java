package org.roko.erp.backend.repositories;

import java.math.BigDecimal;

import org.roko.erp.backend.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {

    @Query("SELECT COALESCE(SUM(itemLedgerEntry.quantity), 0) FROM ItemLedgerEntry itemLedgerEntry WHERE itemLedgerEntry.item = :item")
    public BigDecimal inventory(@Param("item") Item item);
}
