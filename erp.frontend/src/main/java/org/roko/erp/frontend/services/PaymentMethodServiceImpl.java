package org.roko.erp.frontend.services;

import org.roko.erp.dto.PaymentMethodDTO;
import org.roko.erp.dto.list.PaymentMethodList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private RestTemplate restTemplate;

    @Autowired
    public PaymentMethodServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void create(PaymentMethodDTO paymentMethod) {
        restTemplate.postForObject("/api/v1/paymentmethods", paymentMethod, String.class);
    }

    @Override
    public void update(String code, PaymentMethodDTO paymentMethod) {
        restTemplate.put("/api/v1/paymentmethods/{code}", paymentMethod, code);
    }

    @Override
    public void delete(String code) {
        restTemplate.delete("/api/v1/paymentmethods/{code}", code);
    }

    @Override
    public PaymentMethodDTO get(String code) {
        try {
            return restTemplate.getForObject("/api/v1/paymentmethods/{code}", PaymentMethodDTO.class, code);
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    @Override
    public PaymentMethodList list() {
        return restTemplate.getForObject("/api/v1/paymentmethods", PaymentMethodList.class);
    }

    @Override
    public PaymentMethodList list(int page) {
        return restTemplate.getForObject("/api/v1/paymentmethods/page/{page}", PaymentMethodList.class, page);
    }

}
