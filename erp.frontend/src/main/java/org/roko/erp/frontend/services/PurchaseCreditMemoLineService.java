package org.roko.erp.frontend.services;

import org.roko.erp.dto.PurchaseDocumentLineDTO;
import org.roko.erp.dto.list.PurchaseDocumentLineList;

public interface PurchaseCreditMemoLineService {
    
    public void create(String code, PurchaseDocumentLineDTO purchaseCreditMemoLine);

    public void update(String code, int lineNo, PurchaseDocumentLineDTO purchaseCreditMemoLine);

    public void delete(String code, int lineNo);

    public PurchaseDocumentLineDTO get(String code, int lineNo);

    public PurchaseDocumentLineList list(String code, int page);

}
