package org.roko.erp.frontend.services;

import org.roko.erp.dto.PostedSalesDocumentDTO;
import org.roko.erp.dto.list.PostedSalesDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PostedSalesCreditMemoServiceImpl implements PostedSalesCreditMemoService {

    private RestTemplate restTemplate;

    @Autowired
    public PostedSalesCreditMemoServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public PostedSalesDocumentDTO get(String code) {
        return restTemplate.getForObject("/api/v1/postedsalescreditmemos/{code}", PostedSalesDocumentDTO.class, code);
    }

    @Override
    public PostedSalesDocumentList list(int page) {
        return restTemplate.getForObject("/api/v1/postedsalescreditmemos/page/{page}", PostedSalesDocumentList.class,
                page);
    }

}
