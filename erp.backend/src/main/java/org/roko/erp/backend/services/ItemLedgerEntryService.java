package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.Item;
import org.roko.erp.backend.model.ItemLedgerEntry;

public interface ItemLedgerEntryService {

    public void create(ItemLedgerEntry itemLedgerEntry);

    public List<ItemLedgerEntry> list(Item item);

    public List<ItemLedgerEntry> list(Item item, int page);

    public int count(Item item);
    
}
