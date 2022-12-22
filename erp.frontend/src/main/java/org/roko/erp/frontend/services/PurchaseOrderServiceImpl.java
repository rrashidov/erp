package org.roko.erp.frontend.services;

import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.dto.list.PurchaseDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private RestTemplate restTemplate;

    @Autowired
    public PurchaseOrderServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String create(PurchaseDocumentDTO purchaseOrder) {
        return restTemplate.postForObject("/api/v1/purchaseorders", purchaseOrder, String.class);
    }

    @Override
    public void update(String code, PurchaseDocumentDTO purchaseOrder) {
        restTemplate.put("/api/v1/purchaseorders/{code}", purchaseOrder, code);
    }

    @Override
    public void delete(String code) {
        restTemplate.delete("/api/v1/purchaseorders/{code}", code);
    }

    @Override
    public PurchaseDocumentDTO get(String code) {
        return restTemplate.getForObject("/api/v1/purchaseorders/{code}", PurchaseDocumentDTO.class, code);
    }

    @Override
    public PurchaseDocumentList list(int page) {
        return restTemplate.getForObject("/api/v1/purchaseorders/page/{page}", PurchaseDocumentList.class, page);
    }

}
