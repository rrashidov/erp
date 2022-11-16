package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.Item;
import org.roko.erp.model.ItemLedgerEntry;

public interface ItemLedgerEntryService {

    public void create(ItemLedgerEntry itemLedgerEntry);

    public List<ItemLedgerEntry> list(Item item);

    public int count(Item item);
    
}
