package org.roko.erp.frontend.services;

import org.roko.erp.dto.PurchaseDocumentLineDTO;
import org.roko.erp.dto.list.PurchaseDocumentLineList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PurchaseCreditMemoLineServiceImpl implements PurchaseCreditMemoLineService {

    private RestTemplate restTemplate;

    @Autowired
    public PurchaseCreditMemoLineServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void create(String code, PurchaseDocumentLineDTO purchaseCreditMemoLine) {
        restTemplate.postForObject("/api/v1/purchasecreditmemos/{code}/lines", purchaseCreditMemoLine, Integer.class,
                code);
    }

    @Override
    public void update(String code, int lineNo, PurchaseDocumentLineDTO purchaseCreditMemoLine) {
        restTemplate.put("/api/v1/purchasecreditmemos/{code}/lines/{lineNo}", purchaseCreditMemoLine, code, lineNo);
    }

    @Override
    public void delete(String code, int lineNo) {
        restTemplate.delete("/api/v1/purchasecreditmemos/{code}/lines/{lineNo}", code, lineNo);
    }

    @Override
    public PurchaseDocumentLineDTO get(String code, int lineNo) {
        return restTemplate.getForObject("/api/v1/purchasecreditmemos/{code}/lines/{lineNo}",
                PurchaseDocumentLineDTO.class, code, lineNo);
    }

    @Override
    public PurchaseDocumentLineList list(String code, int page) {
        return restTemplate.getForObject("/api/v1/purchasecreditmemos/{code}/lines/page/{page}",
                PurchaseDocumentLineList.class, code, page);
    }

}
