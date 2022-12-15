package org.roko.erp.dto.list;

import java.util.List;

import org.roko.erp.dto.BankAccountDTO;

public class BankAccountList extends BaseDTOList {
    
    private List<BankAccountDTO> data;
    
    public List<BankAccountDTO> getData() {
        return data;
    }
    public void setData(List<BankAccountDTO> data) {
        this.data = data;
    }

}
