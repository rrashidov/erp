package org.roko.erp.itests.clients;

import org.roko.erp.dto.PurchaseDocumentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class PurchaseOrderClient {

    private RestTemplate rest;

    @Autowired
    public PurchaseOrderClient(RestTemplate rest) {
        this.rest = rest;
    }

    public String create(PurchaseDocumentDTO purchaseDocument) {
        return rest.postForEntity("/api/v1/purchaseorders", purchaseDocument, String.class).getBody();
    }

    public PurchaseDocumentDTO read(String id) {
        try {
            return rest.getForEntity("/api/v1/purchaseorders/{code}", PurchaseDocumentDTO.class, id).getBody();
        } catch (RestClientException e) {
            return null;
        }
    }

    public void update(String id, PurchaseDocumentDTO purchaseDocument) {
        rest.put("/api/v1/purchaseorders/{code}", purchaseDocument, id);
    }

    public boolean delete(String id) {
        try {
            rest.delete("/api/v1/purchaseorders/{code}", id);
            return true;
        } catch (RestClientException e) {
            return false;
        }
    }
}
