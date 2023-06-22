package org.roko.erp.itests.clients;

import org.roko.erp.dto.PostedPurchaseDocumentDTO;
import org.roko.erp.dto.list.PostedPurchaseDocumentLineList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PostedPurchaseCreditMemoClient {

    private RestTemplate rest;

    @Autowired
    public PostedPurchaseCreditMemoClient(RestTemplate rest) {
        this.rest = rest;
    }

    public PostedPurchaseDocumentDTO read(String id) {
        return rest.getForEntity("/api/v1/postedpurchasecreditmemos/{code}", PostedPurchaseDocumentDTO.class, id)
                .getBody();
    }

    public PostedPurchaseDocumentLineList readLines(String id, int page) {
        return rest.getForEntity("/api/v1/postedpurchasecreditmemos/{code}/lines/page/{page}",
                PostedPurchaseDocumentLineList.class, id, page).getBody();
    }
}
