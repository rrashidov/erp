package org.roko.erp.frontend.services;

import org.roko.erp.dto.PostedPurchaseDocumentDTO;
import org.roko.erp.dto.list.PostedPurchaseDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PostedPurchaseOrderServiceImpl implements PostedPurchaseOrderService {

    private RestTemplate restTemplate;

    @Autowired
    public PostedPurchaseOrderServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public PostedPurchaseDocumentDTO get(String code) {
        return restTemplate.getForObject("/api/v1/postedpurchaseorders/{code}", PostedPurchaseDocumentDTO.class, code);
    }

    @Override
    public PostedPurchaseDocumentList list(int page) {
        return restTemplate.getForObject("/api/v1/postedpurchaseorders/page/{page}", PostedPurchaseDocumentList.class,
                page);
    }

}
