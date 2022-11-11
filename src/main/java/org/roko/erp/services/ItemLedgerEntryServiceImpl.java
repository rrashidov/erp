package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.Item;
import org.roko.erp.model.ItemLedgerEntry;
import org.roko.erp.repositories.ItemLedgerEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ItemLedgerEntryServiceImpl implements ItemLedgerEntryService {

    private ItemLedgerEntryRepository repo;

    @Autowired
    public ItemLedgerEntryServiceImpl(ItemLedgerEntryRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(ItemLedgerEntry itemLedgerEntry) {
        repo.save(itemLedgerEntry);
    }

    @Override
    public List<ItemLedgerEntry> list(Item item) {
        return repo.findFor(item);
    }

    @Override
    public long count(Item item) {
        return repo.count(item);
    }
    
}
