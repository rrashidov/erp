package org.roko.erp.frontend.services;

import org.roko.erp.dto.ItemDTO;
import org.roko.erp.dto.list.ItemList;

public interface ItemService {
    
    public void create(ItemDTO item);

    public void update(String id, ItemDTO item);

    public void delete(String id);

    public ItemDTO get(String id);

    public ItemList list();

    public ItemList list(int page);

}
