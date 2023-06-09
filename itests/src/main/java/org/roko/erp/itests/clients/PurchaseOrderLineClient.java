package org.roko.erp.itests.clients;

import org.roko.erp.dto.PurchaseDocumentLineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class PurchaseOrderLineClient {

    private RestTemplate rest;

    @Autowired
    public PurchaseOrderLineClient(RestTemplate rest) {
        this.rest = rest;
    }

    public Integer create(String id, PurchaseDocumentLineDTO purchaseOrderLine) {
        return rest.postForEntity("/api/v1/purchaseorders/{code}/lines", purchaseOrderLine, Integer.class, id).getBody();
    }

    public PurchaseDocumentLineDTO read(String id, int lineNo) {
        try {
            return rest
                    .getForEntity("/api/v1/purchaseorders/{code}/lines/{lineNo}", PurchaseDocumentLineDTO.class, id, lineNo)
                    .getBody();
        } catch (RestClientException e) {
            return null;
        }
    }

    public void update(String id, int lineNo, PurchaseDocumentLineDTO purchaseOrderLine) {
        rest.put("/api/v1/purchaseorders/{code}/lines/{lineNo}", purchaseOrderLine, id, lineNo);
    }

    public void delete(String id, int lineNo) {
        rest.delete("/api/v1/purchaseorders/{code}/lines/{lineNo}", id, lineNo);
    }
}
