package org.roko.erp.frontend.services;

import org.roko.erp.dto.CustomerDTO;
import org.roko.erp.dto.list.CustomerList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class CustomerServiceImpl implements CustomerService {

    private RestTemplate restTemplate;

    @Autowired
    public CustomerServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void create(CustomerDTO customer) {
        restTemplate.postForObject("/api/v1/customers", customer, String.class);
    }

    @Override
    public void update(String code, CustomerDTO customer) {
        restTemplate.put("/api/v1/customers/{code}", customer, code);
    }

    @Override
    public void delete(String code) {
        restTemplate.delete("/api/v1/customers/{code}", code);
    }

    @Override
    public CustomerDTO get(String code) {
        try {
            return restTemplate.getForObject("/api/v1/customers/{code}", CustomerDTO.class, code);
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    @Override
    public CustomerList list() {
        return restTemplate.getForObject("/api/v1/customers", CustomerList.class);
    }

    @Override
    public CustomerList list(int page) {
        return restTemplate.getForObject("/api/v1/customers/page/{page}", CustomerList.class, page);
    }

}
