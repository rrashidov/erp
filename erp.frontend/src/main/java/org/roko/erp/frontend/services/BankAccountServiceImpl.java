package org.roko.erp.frontend.services;

import org.roko.erp.dto.BankAccountDTO;
import org.roko.erp.dto.list.BankAccountList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class BankAccountServiceImpl implements BankAccountService {

    private RestTemplate restTemplate;

    @Autowired
    public BankAccountServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
	}

	@Override
    public void create(BankAccountDTO bankAccount) {
        restTemplate.postForObject("/api/v1/bankaccounts", bankAccount, String.class);
    }

    @Override
    public void update(String code, BankAccountDTO bankAccount) {
        restTemplate.put("/api/v1/bankaccounts/{code}", bankAccount, code);
    }

    @Override
    public void delete(String code) {
        restTemplate.delete("/api/v1/bankaccounts/{code}", code);
    }

    @Override
    public BankAccountDTO get(String code) {
        try {
            return restTemplate.getForObject("/api/v1/bankaccounts/{code}", BankAccountDTO.class, code);
        } catch (HttpClientErrorException e) {
            return null;
        } 
    }

    @Override
    public BankAccountList list() {
        return restTemplate.getForObject("/api/v1/bankaccounts", BankAccountList.class);
    }

    @Override
    public BankAccountList list(int page) {
        return restTemplate.getForObject("/api/v1/bankaccounts/page/{page}", BankAccountList.class, page);
    }

}
