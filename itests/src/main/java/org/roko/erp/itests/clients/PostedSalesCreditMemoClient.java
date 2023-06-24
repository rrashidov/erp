package org.roko.erp.itests.clients;

import org.roko.erp.dto.PostedSalesDocumentDTO;
import org.roko.erp.dto.list.PostedSalesDocumentLineList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PostedSalesCreditMemoClient {

    private RestTemplate rest;

    @Autowired
    public PostedSalesCreditMemoClient(RestTemplate rest) {
        this.rest = rest;
    }

    public PostedSalesDocumentDTO read(String code) {
        return rest.getForEntity("/api/v1/postedsalescreditmemos/{code}", PostedSalesDocumentDTO.class, code).getBody();
    }

    public PostedSalesDocumentLineList readLines(String code, int page) {
        return rest.getForEntity("/api/v1/postedsalescreditmemos/{code}/lines/page/{page}",
                PostedSalesDocumentLineList.class, code, page).getBody();
    }
}
