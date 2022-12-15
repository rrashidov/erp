package org.roko.erp.dto.list;

import java.util.List;

import org.roko.erp.dto.PostedPurchaseDocumentDTO;

public class PostedPurchaseDocumentList extends BaseDTOList {
    
    private List<PostedPurchaseDocumentDTO> data;

    public List<PostedPurchaseDocumentDTO> getData() {
        return data;
    }

    public void setData(List<PostedPurchaseDocumentDTO> data) {
        this.data = data;
    }

}
