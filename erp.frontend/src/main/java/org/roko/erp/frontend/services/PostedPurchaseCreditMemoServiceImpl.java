package org.roko.erp.frontend.services;

import org.roko.erp.dto.PostedPurchaseDocumentDTO;
import org.roko.erp.dto.list.PostedPurchaseDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PostedPurchaseCreditMemoServiceImpl implements PostedPurchaseCreditMemoService {

    private RestTemplate restTemplate;

    @Autowired
    public PostedPurchaseCreditMemoServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public PostedPurchaseDocumentDTO get(String code) {
        return restTemplate.getForObject("/api/v1/postedpurchasecreditmemos/{code}", PostedPurchaseDocumentDTO.class,
                code);
    }

    @Override
    public PostedPurchaseDocumentList list(int page) {
        return restTemplate.getForObject("/api/v1/postedpurchasecreditmemos/page/{page}",
                PostedPurchaseDocumentList.class, page);
    }

}
