package org.roko.erp.frontend.services;

import org.roko.erp.dto.list.PostedPurchaseDocumentLineList;

public interface PostedPurchaseCreditMemoLineService {
    
    public PostedPurchaseDocumentLineList list(String code, int page);

}
