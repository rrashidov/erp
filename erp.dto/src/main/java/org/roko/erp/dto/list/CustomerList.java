package org.roko.erp.dto.list;

import java.util.List;

import org.roko.erp.dto.CustomerDTO;

public class CustomerList extends BaseDTOList {
    
    private List<CustomerDTO> data;

    public List<CustomerDTO> getData() {
        return data;
    }

    public void setData(List<CustomerDTO> data) {
        this.data = data;
    }

}
