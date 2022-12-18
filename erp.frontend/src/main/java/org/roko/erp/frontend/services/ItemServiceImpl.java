package org.roko.erp.frontend.services;

import org.roko.erp.dto.ItemDTO;
import org.roko.erp.dto.list.ItemList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ItemServiceImpl implements ItemService {

    private RestTemplate restTemplate;

    @Autowired
    public ItemServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void create(ItemDTO item) {
        restTemplate.postForObject("/api/v1/items", item, String.class);
    }

    @Override
    public void update(String id, ItemDTO item) {
        restTemplate.put("/api/v1/items/{code}", item, id);
    }

    @Override
    public void delete(String id) {
        restTemplate.delete("/api/v1/items/{code}", id);
    }

    @Override
    public ItemDTO get(String id) {
        try {
            return restTemplate.getForObject("/api/v1/items/{code}", ItemDTO.class, id);
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    @Override
    public ItemList list() {
        return restTemplate.getForObject("/api/v1/items", ItemList.class);
    }

    @Override
    public ItemList list(int page) {
        return restTemplate.getForObject("/api/v1/items/page/{page}", ItemList.class, page);
    }

}
