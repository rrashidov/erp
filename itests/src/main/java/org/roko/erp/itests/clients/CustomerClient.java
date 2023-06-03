package org.roko.erp.itests.clients;

import org.roko.erp.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class CustomerClient {
    
    private RestTemplate rest;

    @Autowired
    public CustomerClient(RestTemplate rest) {
        this.rest = rest;
    }

    public String create(CustomerDTO customer) {
        return rest.postForObject("/api/v1/customers", customer, String.class);
    }

    public CustomerDTO read(String id) {
        try {
            return rest.getForObject("/api/v1/customers/{code}", CustomerDTO.class, id);
        } catch (RestClientException e) {
            return null;
        }
    }

    public void update(String id, CustomerDTO customer) {
        rest.put("/api/v1/customers/{code}", customer, id);
    }

    public void delete(String id) {
        rest.delete("/api/v1/customers/{code}", id);
    }
}
