package org.roko.erp.dto.list;

import java.util.List;

import org.roko.erp.dto.CustomerLedgerEntryDTO;

public class CustomerLedgerEntryList extends BaseDTOList {
    
    private List<CustomerLedgerEntryDTO> data;

    public List<CustomerLedgerEntryDTO> getData() {
        return data;
    }

    public void setData(List<CustomerLedgerEntryDTO> data) {
        this.data = data;
    }

}
