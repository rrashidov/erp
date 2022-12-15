package org.roko.erp.dto.list;

import java.util.List;

import org.roko.erp.dto.PaymentMethodDTO;

public class PaymentMethodList extends BaseDTOList {
    
    private List<PaymentMethodDTO> data;

    public List<PaymentMethodDTO> getData() {
        return data;
    }

    public void setData(List<PaymentMethodDTO> data) {
        this.data = data;
    }

}
