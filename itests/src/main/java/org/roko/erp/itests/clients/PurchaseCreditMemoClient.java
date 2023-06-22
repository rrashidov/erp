package org.roko.erp.itests.clients;

import org.roko.erp.dto.PurchaseDocumentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class PurchaseCreditMemoClient {

    private RestTemplate rest;

    @Autowired
    public PurchaseCreditMemoClient(RestTemplate rest) {
        this.rest = rest;
    }

    public String create(PurchaseDocumentDTO purchaseDocument) {
        return rest.postForEntity("/api/v1/purchasecreditmemos", purchaseDocument, String.class).getBody();
    }

    public PurchaseDocumentDTO read(String id) {
        try {
            return rest.getForEntity("/api/v1/purchasecreditmemos/{code}", PurchaseDocumentDTO.class, id).getBody();
        } catch (RestClientException e) {
            return null;
        }
    }

    public void update(String id, PurchaseDocumentDTO purchaseDocument) {
        rest.put("/api/v1/purchasecreditmemos/{code}", purchaseDocument, id);
    }

    public boolean delete(String id) {
        try {
            rest.delete("/api/v1/purchasecreditmemos/{code}", id);
            return true;
        } catch (RestClientException e) {
            return false;
        }
    }

    public void post(String id){
        rest.getForEntity("/api/v1/purchasecreditmemos/{code}/operations/post", String.class, id);
    }
}
