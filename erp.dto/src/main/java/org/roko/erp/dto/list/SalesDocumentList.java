package org.roko.erp.dto.list;

import java.util.List;

import org.roko.erp.dto.SalesDocumentDTO;

public class SalesDocumentList extends BaseDTOList {
    
    private List<SalesDocumentDTO> data;

    public List<SalesDocumentDTO> getData() {
        return data;
    }

    public void setData(List<SalesDocumentDTO> data) {
        this.data = data;
    }

}
