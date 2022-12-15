package org.roko.erp.dto.list;

import java.util.List;

import org.roko.erp.dto.PostedSalesDocumentLineDTO;

public class PostedSalesDocumentLineList extends BaseDTOList {
    
    private List<PostedSalesDocumentLineDTO> data;

    public List<PostedSalesDocumentLineDTO> getData() {
        return data;
    }

    public void setData(List<PostedSalesDocumentLineDTO> data) {
        this.data = data;
    }

}
