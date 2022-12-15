package org.roko.erp.dto.list;

import java.util.List;

import org.roko.erp.dto.PurchaseDocumentDTO;

public class PurchaseDocumentList extends BaseDTOList {
    
    private List<PurchaseDocumentDTO> data;

    public List<PurchaseDocumentDTO> getData() {
        return data;
    }

    public void setData(List<PurchaseDocumentDTO> data) {
        this.data = data;
    }

}
