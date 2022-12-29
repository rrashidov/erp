package org.roko.erp.frontend.services;

import org.roko.erp.dto.PostedSalesDocumentDTO;
import org.roko.erp.dto.list.PostedSalesDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PostedSalesOrderServiceImpl implements PostedSalesOrderService {

    private RestTemplate restTemplate;

    @Autowired
    public PostedSalesOrderServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public PostedSalesDocumentDTO get(String code) {
        return restTemplate.getForObject("/api/v1/postedsalesorders/{code}", PostedSalesDocumentDTO.class, code);
    }

    @Override
    public PostedSalesDocumentList list(int page) {
        return restTemplate.getForObject("/api/v1/postedsalesorders/page/{page}", PostedSalesDocumentList.class, page);
    }

}
