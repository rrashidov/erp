package org.roko.erp.frontend.services;

import org.roko.erp.dto.list.PostedPurchaseDocumentLineList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PostedPurchaseOrderLineServiceImpl implements PostedPurchaseOrderLineService {

    private RestTemplate restTemplate;

    @Autowired
    public PostedPurchaseOrderLineServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public PostedPurchaseDocumentLineList list(String code, int page) {
        return restTemplate.getForObject("/api/v1/postedpurchaseorders/{code}/lines/page/{page}",
                PostedPurchaseDocumentLineList.class, code, page);
    }

}
