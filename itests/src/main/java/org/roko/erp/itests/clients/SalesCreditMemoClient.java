package org.roko.erp.itests.clients;

import org.roko.erp.dto.SalesDocumentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class SalesCreditMemoClient {

    private RestTemplate rest;

    @Autowired
    public SalesCreditMemoClient(RestTemplate rest) {
        this.rest = rest;
    }

    public String create(SalesDocumentDTO salesDocument) {
        return rest.postForEntity("/api/v1/salescreditmemos", salesDocument, String.class).getBody();
    }

    public SalesDocumentDTO read(String id) {
        try {
            return rest.getForEntity("/api/v1/salescreditmemos/{code}", SalesDocumentDTO.class, id).getBody();
        } catch (RestClientException e) {
            return null;
        }
    }

    public void update(String id, SalesDocumentDTO salesDocument) {
        rest.put("/api/v1/salescreditmemos/{code}", salesDocument, id);
    }

    public boolean delete(String id) {
        try {
            rest.delete("/api/v1/salescreditmemos/{code}", id);
            return true;
        } catch (RestClientException e) {
            return false;
        }
    }
}
