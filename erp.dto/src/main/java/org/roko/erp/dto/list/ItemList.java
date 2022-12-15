package org.roko.erp.dto.list;

import java.util.List;

import org.roko.erp.dto.ItemDTO;

public class ItemList extends BaseDTOList {
    
    private List<ItemDTO> data;

    public List<ItemDTO> getData() {
        return data;
    }

    public void setData(List<ItemDTO> data) {
        this.data = data;
    }

}
