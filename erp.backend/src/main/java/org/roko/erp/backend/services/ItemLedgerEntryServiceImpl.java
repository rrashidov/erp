package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.Item;
import org.roko.erp.backend.model.ItemLedgerEntry;
import org.roko.erp.backend.repositories.ItemLedgerEntryRepository;
import org.roko.erp.dto.ItemLedgerEntryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    public List<ItemLedgerEntry> list(Item item, int page) {
        return repo.findFor(item, PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE)).toList();
    }

    @Override
    public long count(Item item) {
        return repo.count(item);
    }

    @Override
    public ItemLedgerEntryDTO toDTO(ItemLedgerEntry itemLedgerEntry) {
        ItemLedgerEntryDTO dto = new ItemLedgerEntryDTO();
        dto.setId(itemLedgerEntry.getId());
        dto.setType(itemLedgerEntry.getType().name());
        dto.setQuantity(itemLedgerEntry.getQuantity());
        dto.setDate(itemLedgerEntry.getDate());
        dto.setDocumentCode(itemLedgerEntry.getDocumentCode());
        return dto;
    }
    
}
