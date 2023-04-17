package org.roko.erp.frontend.services;

import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.dto.list.PurchaseDocumentList;

public interface PurchaseCreditMemoService {

    public String create(PurchaseDocumentDTO purchaseCreditMemo);

    public void update(String code, PurchaseDocumentDTO purchaseCreditMemo);

    public void delete(String code) throws DeleteFailedException;

    public PurchaseDocumentDTO get(String code);

    public PurchaseDocumentList list(int page);

}
