package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.PaymentMethod;
import org.roko.erp.dto.PaymentMethodDTO;

public interface PaymentMethodService {
    
    public void create(PaymentMethod paymentMethod);

    public void update(String code, PaymentMethod paymentMethod);

    public void delete(String code);

    public PaymentMethod get(String code);

    public List<PaymentMethod> list();

    public List<PaymentMethod> list(int page);

    public int count();

    public PaymentMethodDTO toDTO(PaymentMethod paymentMethod);

    public PaymentMethod fromDTO(PaymentMethodDTO paymentMethodDto);
}
