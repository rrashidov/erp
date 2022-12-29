package org.roko.erp.frontend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class SalesOrderPostServiceImpl implements SalesOrderPostService {

    private RestTemplate restTemplate;

    @Autowired
    public SalesOrderPostServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void post(String code) throws PostFailedException {
        try {
            restTemplate.getForObject("/api/v1/salesorders/{code}/operations/post", String.class, code);
        } catch (HttpClientErrorException e){
            throw new PostFailedException(e.getMessage());
        }
    }

}
