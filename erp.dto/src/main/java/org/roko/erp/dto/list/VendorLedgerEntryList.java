package org.roko.erp.dto.list;

import java.util.List;

import org.roko.erp.dto.VendorLedgerEntryDTO;

public class VendorLedgerEntryList extends BaseDTOList {
    
    private List<VendorLedgerEntryDTO> data;

    public List<VendorLedgerEntryDTO> getData() {
        return data;
    }

    public void setData(List<VendorLedgerEntryDTO> data) {
        this.data = data;
    }

}
