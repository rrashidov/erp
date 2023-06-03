package org.roko.erp.itests.clients;

import org.roko.erp.dto.ItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class ItemClient {

    private RestTemplate rest;

    @Autowired
    public ItemClient(RestTemplate rest) {
        this.rest = rest;
    }

    public String create(ItemDTO item) {
        return rest.postForObject("/api/v1/items", item, String.class);
    }

    public ItemDTO read(String id) {
        try {
            return rest.getForEntity("/api/v1/items/{code}", ItemDTO.class, id).getBody();
        } catch (RestClientException e) {
            return null;
        }
    }

    public void update(String id, ItemDTO item) {
        rest.put("/api/v1/items/{code}", item, id);
    }

    public void delete(String id) {
        rest.delete("/api/v1/items/{code}", id);
    }
}
