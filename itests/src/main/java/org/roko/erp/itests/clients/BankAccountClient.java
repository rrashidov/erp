package org.roko.erp.itests.clients;

import org.roko.erp.dto.BankAccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class BankAccountClient {

    private RestTemplate rest;

    @Autowired
    public BankAccountClient(RestTemplate rest) {
        this.rest = rest;
    }

    public String create(BankAccountDTO bankAccount) {
        return rest.postForObject("/api/v1/bankaccounts", bankAccount, String.class);
    }

    public BankAccountDTO read(String id) {
        try {
            return rest.getForEntity("/api/v1/bankaccounts/{code}", BankAccountDTO.class, id).getBody();
        } catch (RestClientException e) {
            return null;
        }
    }

    public void update(String id, BankAccountDTO bankAccount) {
        rest.put("/api/v1/bankaccounts/{code}", bankAccount, id);
    }

    public void delete(String id) {
        rest.delete("/api/v1/bankaccounts/{code}", id);
    }
}
