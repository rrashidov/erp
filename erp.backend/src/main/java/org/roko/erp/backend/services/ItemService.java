package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.Item;

public interface ItemService {
    
    public void create(Item item);

    public void update(String id, Item item);

    public void delete(String id);

    public Item get(String id);

    public List<Item> list();

    public List<Item> list(int page);

    public int count();
}
