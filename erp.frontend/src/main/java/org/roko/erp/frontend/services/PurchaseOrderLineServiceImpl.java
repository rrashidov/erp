package org.roko.erp.frontend.services;

import org.roko.erp.dto.PurchaseDocumentLineDTO;
import org.roko.erp.dto.list.PurchaseDocumentLineList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PurchaseOrderLineServiceImpl implements PurchaseOrderLineService {

    private RestTemplate restTemplate;

    @Autowired
    public PurchaseOrderLineServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void create(String code, PurchaseDocumentLineDTO purchaseOrderLine) {
        restTemplate.postForObject("/api/v1/purchaseorders/{code}/lines", purchaseOrderLine, Integer.class, code);
    }

    @Override
    public void delete(String code, int lineNo) {
        restTemplate.delete("/api/v1/purchaseorders/{code}/lines/{lineNo}", code, lineNo);
    }

    @Override
    public PurchaseDocumentLineDTO get(String code, int lineNo) {
        return restTemplate.getForObject("/api/v1/purchaseorders/{code}/lines/{lineNo}", PurchaseDocumentLineDTO.class, code, lineNo);
    }

    @Override
    public PurchaseDocumentLineList list(String code, int page) {
        return restTemplate.getForObject("/api/v1/purchaseorders/{code}/lines/page/{page}", PurchaseDocumentLineList.class, code, page);
    }

    @Override
    public void update(String code, int lineNo, PurchaseDocumentLineDTO purchaseOrderLine) {
        restTemplate.put("/api/v1/purchaseorders/{code}/lines/{lineNo}", purchaseOrderLine, code, lineNo);
    }
    
}
