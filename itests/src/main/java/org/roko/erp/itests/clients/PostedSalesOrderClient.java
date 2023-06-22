package org.roko.erp.itests.clients;

import org.roko.erp.dto.PostedSalesDocumentDTO;
import org.roko.erp.dto.list.PostedSalesDocumentLineList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PostedSalesOrderClient {

    private RestTemplate rest;

    @Autowired
    public PostedSalesOrderClient(RestTemplate rest) {
        this.rest = rest;
    }

    public PostedSalesDocumentDTO read(String id) {
        return rest.getForEntity("/api/v1/postedsalesorders/{code}", PostedSalesDocumentDTO.class, id).getBody();
    }

    public PostedSalesDocumentLineList readLines(String id, int page) {
        return rest.getForEntity("/api/v1/postedsalesorders/{code}/lines/page/{page}",
                PostedSalesDocumentLineList.class, id, page).getBody();
    }
}
