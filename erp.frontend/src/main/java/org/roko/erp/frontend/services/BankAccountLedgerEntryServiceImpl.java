package org.roko.erp.frontend.services;

import org.roko.erp.dto.list.BankAccountLedgerEntryList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BankAccountLedgerEntryServiceImpl implements BankAccountLedgerEntryService {

    private RestTemplate restTemplate;

    @Autowired
    public BankAccountLedgerEntryServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public BankAccountLedgerEntryList list(String bankAccountCode, int page) {
        return null;
    }

}
