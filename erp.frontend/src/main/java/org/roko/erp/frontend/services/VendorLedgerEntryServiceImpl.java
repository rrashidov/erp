package org.roko.erp.frontend.services;

import org.roko.erp.dto.list.VendorLedgerEntryList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VendorLedgerEntryServiceImpl implements VendorLedgerEntryService {

    private RestTemplate restTemplate;

    @Autowired
    public VendorLedgerEntryServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public VendorLedgerEntryList list(String code, int page) {
        return restTemplate.getForObject("/api/v1/vendors/{code}/ledgerentries/page/{page}",
                VendorLedgerEntryList.class, code, page);
    }

}
