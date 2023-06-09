package org.roko.erp.itests.clients;

import org.roko.erp.dto.PurchaseDocumentLineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class PurchaseCreditMemoLineClient {

    private RestTemplate rest;

    @Autowired
    public PurchaseCreditMemoLineClient(RestTemplate rest) {
        this.rest = rest;
    }

    public Integer create(String id, PurchaseDocumentLineDTO purchaseCreditMemoLine) {
        return rest.postForEntity("/api/v1/purchasecreditmemos/{code}/lines", purchaseCreditMemoLine, Integer.class, id)
                .getBody();
    }

    public PurchaseDocumentLineDTO read(String id, int lineNo) {
        try {
            return rest
                    .getForEntity("/api/v1/purchasecreditmemos/{code}/lines/{lineNo}", PurchaseDocumentLineDTO.class,
                            id, lineNo)
                    .getBody();
        } catch (RestClientException e) {
            return null;
        }
    }

    public void update(String id, int lineNo, PurchaseDocumentLineDTO purchaseCreditMemoLine) {
        rest.put("/api/v1/purchasecreditmemos/{code}/lines/{lineNo}", purchaseCreditMemoLine, id, lineNo);
    }

    public void delete(String id, int lineNo) {
        rest.delete("/api/v1/purchasecreditmemos/{code}/lines/{lineNo}", id, lineNo);
    }
}
