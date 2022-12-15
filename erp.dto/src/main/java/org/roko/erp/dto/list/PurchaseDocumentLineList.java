package org.roko.erp.dto.list;

import java.util.List;

import org.roko.erp.dto.PurchaseDocumentLineDTO;

public class PurchaseDocumentLineList extends BaseDTOList {
    
    private List<PurchaseDocumentLineDTO> data;

    public List<PurchaseDocumentLineDTO> getData() {
        return data;
    }

    public void setData(List<PurchaseDocumentLineDTO> data) {
        this.data = data;
    }

}
