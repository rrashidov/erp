package org.roko.erp.dto.list;

import java.util.List;

import org.roko.erp.dto.PostedSalesDocumentDTO;

public class PostedSalesDocumentList extends BaseDTOList {
    
    private List<PostedSalesDocumentDTO> data;

    public List<PostedSalesDocumentDTO> getData() {
        return data;
    }

    public void setData(List<PostedSalesDocumentDTO> data) {
        this.data = data;
    }

}
