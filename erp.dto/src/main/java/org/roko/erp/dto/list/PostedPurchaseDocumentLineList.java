package org.roko.erp.dto.list;

import java.util.List;

import org.roko.erp.dto.PostedPurchaseDocumentLineDTO;

public class PostedPurchaseDocumentLineList extends BaseDTOList {
    
    private List<PostedPurchaseDocumentLineDTO> data;

    public List<PostedPurchaseDocumentLineDTO> getData() {
        return data;
    }

    public void setData(List<PostedPurchaseDocumentLineDTO> data) {
        this.data = data;
    }

}
