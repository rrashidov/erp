package org.roko.erp.frontend.services;

import org.roko.erp.dto.list.CustomerLedgerEntryList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomerLedgerEntryServiceImpl implements CustomerLedgerEntryService {

    private RestTemplate restTemplate;

    @Autowired
    public CustomerLedgerEntryServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public CustomerLedgerEntryList list(String customerCode, int page) {
        return restTemplate.getForObject("/api/v1/customers/{code}/ledgerentries/page/{page}",
                CustomerLedgerEntryList.class, customerCode, page);
    }

}
