package org.roko.erp.itests.clients;

import org.roko.erp.dto.SalesDocumentLineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class SalesOrderLineClient {

    private RestTemplate rest;

    @Autowired
    public SalesOrderLineClient(RestTemplate rest) {
        this.rest = rest;
    }

    public Integer create(String id, SalesDocumentLineDTO salesOrderLine) {
        return rest.postForEntity("/api/v1/salesorders/{code}/lines", salesOrderLine, Integer.class, id).getBody();
    }

    public SalesDocumentLineDTO read(String id, int lineNo) {
        try {
            return rest
                    .getForEntity("/api/v1/salesorders/{code}/lines/{lineNo}", SalesDocumentLineDTO.class, id, lineNo)
                    .getBody();
        } catch (RestClientException e) {
            return null;
        }
    }

    public void update(String id, int lineNo, SalesDocumentLineDTO salesOrderLine) {
        rest.put("/api/v1/salesorders/{code}/lines/{lineNo}", salesOrderLine, id, lineNo);
    }

    public void delete(String id, int lineNo) {
        rest.delete("/api/v1/salesorders/{code}/lines/{lineNo}", id, lineNo);
    }
}
