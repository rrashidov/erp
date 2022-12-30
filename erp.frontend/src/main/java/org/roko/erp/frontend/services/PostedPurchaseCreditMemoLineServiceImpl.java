package org.roko.erp.frontend.services;

import org.roko.erp.dto.list.PostedPurchaseDocumentLineList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PostedPurchaseCreditMemoLineServiceImpl implements PostedPurchaseCreditMemoLineService {

    private RestTemplate restTemplate;

    @Autowired
    public PostedPurchaseCreditMemoLineServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public PostedPurchaseDocumentLineList list(String code, int page) {
        return restTemplate.getForObject("/api/v1/postedpurchasecreditmemos/{code}/lines/page/{page}",
                PostedPurchaseDocumentLineList.class, code, page);
    }

}
