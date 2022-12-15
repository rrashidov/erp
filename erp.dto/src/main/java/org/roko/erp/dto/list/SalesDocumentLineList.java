package org.roko.erp.dto.list;

import java.util.List;

import org.roko.erp.dto.SalesDocumentLineDTO;

public class SalesDocumentLineList extends BaseDTOList {
    
    private List<SalesDocumentLineDTO> data;

    public List<SalesDocumentLineDTO> getData() {
        return data;
    }

    public void setData(List<SalesDocumentLineDTO> data) {
        this.data = data;
    }

}
