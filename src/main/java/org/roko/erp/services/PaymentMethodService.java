package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.PaymentMethod;

public interface PaymentMethodService {
    
    public void create(PaymentMethod paymentMethod);

    public void update(String code, PaymentMethod paymentMethod);

    public void delete(String code);

    public PaymentMethod get(String code);

    public List<PaymentMethod> list();

    public long count();
}
