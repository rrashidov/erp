package org.roko.erp.frontend.services;

import org.roko.erp.dto.list.PostedSalesDocumentLineList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PostedSalesOrderLineServiceImpl implements PostedSalesOrderLineService {

    private RestTemplate restTemplate;

    @Autowired
    public PostedSalesOrderLineServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public PostedSalesDocumentLineList list(String code, int page) {
        return restTemplate.getForObject("/api/v1/postedsalesorders/{code}/lines/page/{page}",
                PostedSalesDocumentLineList.class, code, page);
    }

}
