package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.Item;
import org.roko.erp.backend.model.ItemLedgerEntry;
import org.roko.erp.dto.ItemLedgerEntryDTO;

public interface ItemLedgerEntryService {

    public void create(ItemLedgerEntry itemLedgerEntry);

    public List<ItemLedgerEntry> list(Item item);

    public List<ItemLedgerEntry> list(Item item, int page);

    public long count(Item item);

    public ItemLedgerEntryDTO toDTO(ItemLedgerEntry itemLedgerEntry);
}
