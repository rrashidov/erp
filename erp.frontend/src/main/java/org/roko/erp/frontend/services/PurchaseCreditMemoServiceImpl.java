package org.roko.erp.frontend.services;

import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.dto.list.PurchaseDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class PurchaseCreditMemoServiceImpl implements PurchaseCreditMemoService {

    private RestTemplate restTemplate;

    @Autowired
    public PurchaseCreditMemoServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String create(PurchaseDocumentDTO purchaseCreditMemo) {
        return restTemplate.postForObject("/api/v1/purchasecreditmemos", purchaseCreditMemo, String.class);
    }

    @Override
    public void update(String code, PurchaseDocumentDTO purchaseCreditMemo) {
        restTemplate.put("/api/v1/purchasecreditmemos/{code}", purchaseCreditMemo, code);
    }

    @Override
    public void delete(String code) throws DeleteFailedException {
        try {
            restTemplate.delete("/api/v1/purchasecreditmemos/{code}", code);
        } catch (HttpClientErrorException e) {
            throw new DeleteFailedException(e.getMessage());
        }
    }

    @Override
    public PurchaseDocumentDTO get(String code) {
        return restTemplate.getForObject("/api/v1/purchasecreditmemos/{code}", PurchaseDocumentDTO.class, code);
    }

    @Override
    public PurchaseDocumentList list(int page) {
        return restTemplate.getForObject("/api/v1/purchasecreditmemos/page/{page}", PurchaseDocumentList.class, page);
    }

}
