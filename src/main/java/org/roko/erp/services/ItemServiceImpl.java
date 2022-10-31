package org.roko.erp.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.model.Item;
import org.roko.erp.repositories.ItemRepository;
import org.springframework.stereotype.Component;

@Component
public class ItemServiceImpl implements ItemService {

    private ItemRepository repo;

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
        Optional<Item> item = repo.findById(id);

        if (item.isPresent()) {
            return item.get();
        }

        return null;
    }

    @Override
    public List<Item> list() {
        return repo.findAll();
    }

    @Override
    public long count() {
        return repo.count();
    }

    private void transferFields(Item item, Item itemFromDB) {
        itemFromDB.setName(item.getName());
        itemFromDB.setSalesPrice(item.getSalesPrice());
        itemFromDB.setPurchasePrice(item.getPurchasePrice());
    }

}
