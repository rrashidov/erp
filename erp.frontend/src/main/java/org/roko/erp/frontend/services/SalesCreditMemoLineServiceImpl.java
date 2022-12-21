package org.roko.erp.frontend.services;

import org.roko.erp.dto.SalesDocumentLineDTO;
import org.roko.erp.dto.list.SalesDocumentLineList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SalesCreditMemoLineServiceImpl implements SalesCreditMemoLineService {

    private RestTemplate restTemplate;

    @Autowired
    public SalesCreditMemoLineServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void create(String code, SalesDocumentLineDTO salesCreditMemoLine) {
        restTemplate.postForObject("/api/v1/salescreditmemos/{code}/lines", salesCreditMemoLine, Integer.class, code);
    }

    @Override
    public void update(String code, int lineNo, SalesDocumentLineDTO salesCreditMemoLine) {
        restTemplate.put("/api/v1/salescreditmemos/{code}/lines/{lineNo}", salesCreditMemoLine, code, lineNo);
    }

    @Override
    public void delete(String code, int lineNo) {
        restTemplate.delete("/api/v1/salescreditmemos/{code}/lines/{lineNo}", code, lineNo);
    }

    @Override
    public SalesDocumentLineDTO get(String code, int lineNo) {
        return restTemplate.getForObject("/api/v1/salescreditmemos/{code}/lines/{lineNo}", SalesDocumentLineDTO.class,
                code, lineNo);
    }

    @Override
    public SalesDocumentLineList list(String code, int page) {
        return restTemplate.getForObject("/api/v1/salescreditmemos/{code}/lines/page/{page}",
                SalesDocumentLineList.class, code, page);
    }

}
