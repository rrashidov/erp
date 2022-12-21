package org.roko.erp.frontend.services;

import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.dto.list.SalesDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class SalesOrderServiceImpl implements SalesOrderService {

    private RestTemplate restTemplate;

    @Autowired
    public SalesOrderServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String create(SalesDocumentDTO salesOrder) {
        return restTemplate.postForObject("/api/v1/salesorders", salesOrder, String.class);
    }

    @Override
    public void update(String code, SalesDocumentDTO salesOrder) {
        restTemplate.put("/api/v1/salesorders/{code}", salesOrder, code);
    }

    @Override
    public void delete(String code) {
        restTemplate.delete("/api/v1/salesorders/{code}", code);
    }

    @Override
    public SalesDocumentDTO get(String code) {
        try {
            return restTemplate.getForObject("/api/v1/salesorders/{code}", SalesDocumentDTO.class, code);
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    @Override
    public SalesDocumentList list(int page) {
        return restTemplate.getForObject("/api/v1/salesorders/page/{page}", SalesDocumentList.class, page);
    }

}
