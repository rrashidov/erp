package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.Item;
import org.roko.erp.model.ItemLedgerEntry;
import org.roko.erp.repositories.ItemLedgerEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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
    public int count(Item item) {
        return new Long(repo.count(item)).intValue();
    }
    
}
