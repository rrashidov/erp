package org.roko.erp.dto.list;

import java.util.List;

import org.roko.erp.dto.ItemLedgerEntryDTO;

public class ItemLedgerEntryList extends BaseDTOList {
    
    private List<ItemLedgerEntryDTO> data;

    public List<ItemLedgerEntryDTO> getData() {
        return data;
    }

    public void setData(List<ItemLedgerEntryDTO> data) {
        this.data = data;
    }

}
