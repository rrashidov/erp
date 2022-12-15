package org.roko.erp.dto.list;

import java.util.List;

import org.roko.erp.dto.BankAccountLedgerEntryDTO;

public class BankAccountLedgerEntryList extends BaseDTOList {
    
    private List<BankAccountLedgerEntryDTO> data;
    
    public List<BankAccountLedgerEntryDTO> getData() {
        return data;
    }
    public void setData(List<BankAccountLedgerEntryDTO> data) {
        this.data = data;
    }

}
