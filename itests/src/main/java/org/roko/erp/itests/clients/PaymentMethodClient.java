package org.roko.erp.itests.clients;

import org.roko.erp.dto.PaymentMethodDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class PaymentMethodClient {
    
    private RestTemplate rest;

    @Autowired
    public PaymentMethodClient(RestTemplate rest) {
        this.rest = rest;
    }

    public String create(PaymentMethodDTO paymentMethod) {
        return rest.postForEntity("/api/v1/paymentmethods", paymentMethod, String.class).getBody();
    }

    public PaymentMethodDTO read(String id) {
        try {
            return rest.getForEntity("/api/v1/paymentmethods/{code}", PaymentMethodDTO.class, id).getBody();
        } catch (RestClientException e) {
            return null;
        }
    }

    public void update(String id, PaymentMethodDTO paymentMethod){
        rest.put("/api/v1/paymentmethods/{code}", paymentMethod, id);
    }

    public void delete(String id) {
        rest.delete("/api/v1/paymentmethods/{code}", id);
    }
}
