package org.roko.erp.frontend.services;

import org.roko.erp.dto.list.ItemLedgerEntryList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ItemLedgerEntryServiceImpl implements ItemLedgerEntryService {

    private RestTemplate restTemplate;

    @Autowired
    public ItemLedgerEntryServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ItemLedgerEntryList list(String itemCode, int page) {
        return restTemplate.getForObject("/api/v1/items/{code}/ledgerentries/page/{page}", ItemLedgerEntryList.class,
                itemCode, page);
    }

}
