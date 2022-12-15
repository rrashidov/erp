package org.roko.erp.frontend.services;

import java.util.List;

import org.roko.erp.frontend.model.Item;
import org.roko.erp.frontend.model.ItemLedgerEntry;

public interface ItemLedgerEntryService {

    public void create(ItemLedgerEntry itemLedgerEntry);

    public List<ItemLedgerEntry> list(Item item);

    public List<ItemLedgerEntry> list(Item item, int page);

    public int count(Item item);
    
}
