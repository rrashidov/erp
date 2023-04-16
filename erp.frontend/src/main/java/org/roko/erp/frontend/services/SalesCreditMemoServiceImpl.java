package org.roko.erp.frontend.services;

import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.dto.list.SalesDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class SalesCreditMemoServiceImpl implements SalesCreditMemoService {

    private RestTemplate restTemplate;

    @Autowired
    public SalesCreditMemoServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String create(SalesDocumentDTO salesCreditMemo) {
        return restTemplate.postForObject("/api/v1/salescreditmemos", salesCreditMemo, String.class);
    }

    @Override
    public void update(String code, SalesDocumentDTO salesCreditMemo) {
        restTemplate.put("/api/v1/salescreditmemos/{code}", salesCreditMemo, code);
    }

    @Override
    public void delete(String code) throws DeleteFailedException {
        try {
            restTemplate.delete("/api/v1/salescreditmemos/{code}", code);
        } catch (HttpClientErrorException e) {
            throw new DeleteFailedException(e.getMessage());
        }
    }

    @Override
    public SalesDocumentDTO get(String code) {
        return restTemplate.getForObject("/api/v1/salescreditmemos/{code}", SalesDocumentDTO.class, code);
    }

    @Override
    public SalesDocumentList list(int page) {
        return restTemplate.getForObject("/api/v1/salescreditmemos/page/{page}", SalesDocumentList.class, page);
    }

}
