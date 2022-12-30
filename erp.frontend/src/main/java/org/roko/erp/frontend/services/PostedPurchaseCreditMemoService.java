package org.roko.erp.frontend.services;

import org.roko.erp.dto.PostedPurchaseDocumentDTO;
import org.roko.erp.dto.list.PostedPurchaseDocumentList;

public interface PostedPurchaseCreditMemoService {

    public PostedPurchaseDocumentDTO get(String code);

    public PostedPurchaseDocumentList list(int page);

}
