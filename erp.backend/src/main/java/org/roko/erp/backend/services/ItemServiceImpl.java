package org.roko.erp.backend.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.backend.model.Item;
import org.roko.erp.backend.repositories.ItemRepository;
import org.roko.erp.dto.ItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class ItemServiceImpl implements ItemService {

    private ItemRepository repo;

    @Autowired
    public ItemServiceImpl(ItemRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(Item item) {
        repo.save(item);
    }

    @Override
    public void update(String id, Item item) {
        Item itemFromDB = repo.findById(id).get();

        transferFields(item, itemFromDB);

        repo.save(itemFromDB);
    }

    @Override
    public void delete(String id) {
        Item itemFromDB = repo.findById(id).get();
        repo.delete(itemFromDB);
    }

    @Override
    public Item get(String id) {
        Optional<Item> itemOptional = repo.findById(id);

        if (itemOptional.isPresent()) {
            Item item = itemOptional.get();
            item.setInventory(repo.inventory(item));
            return item;
        }

        return null;
    }

    @Override
    public List<Item> list() {
        List<Item> items = repo.findAll();

        items.stream()
                .forEach(item -> item.setInventory(repo.inventory(item)));

        return items;
    }

    @Override
    public List<Item> list(int page) {
        List<Item> items = repo.findAll(PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE)).toList();

        items.stream()
                .forEach(item -> item.setInventory(repo.inventory(item)));

        return items;
    }

    @Override
    public long count() {
        return repo.count();
    }
    
    @Override
    public Item fromDTO(ItemDTO itemDto) {
        Item item = new Item();
        item.setCode(itemDto.getCode());
        item.setName(itemDto.getName());
        item.setSalesPrice(itemDto.getSalesPrice());
        item.setPurchasePrice(itemDto.getPurchasePrice());
        return item;
    }

    @Override
    public ItemDTO toDTO(Item item) {
        ItemDTO dto = new ItemDTO();
        dto.setCode(item.getCode());
        dto.setName(item.getName());
        dto.setSalesPrice(item.getSalesPrice());
        dto.setPurchasePrice(item.getPurchasePrice());
        dto.setInventory(item.getInventory());
        return dto;
    }

    private void transferFields(Item item, Item itemFromDB) {
        itemFromDB.setName(item.getName());
        itemFromDB.setSalesPrice(item.getSalesPrice());
        itemFromDB.setPurchasePrice(item.getPurchasePrice());
    }

}
