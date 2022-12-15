package org.roko.erp.dto.list;

import java.util.List;

import org.roko.erp.dto.VendorDTO;

public class VendorList extends BaseDTOList {
    
    private List<VendorDTO> data;

    public List<VendorDTO> getData() {
        return data;
    }

    public void setData(List<VendorDTO> data) {
        this.data = data;
    }

}
