package org.roko.erp.frontend.services;

import org.roko.erp.dto.PaymentMethodDTO;
import org.roko.erp.dto.list.PaymentMethodList;

public interface PaymentMethodService {
    
    public void create(PaymentMethodDTO paymentMethod);

    public void update(String code, PaymentMethodDTO paymentMethod);

    public void delete(String code);

    public PaymentMethodDTO get(String code);

    public PaymentMethodList list();

    public PaymentMethodList list(int page);

}
