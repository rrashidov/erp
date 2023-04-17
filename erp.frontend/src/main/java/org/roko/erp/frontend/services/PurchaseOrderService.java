package org.roko.erp.frontend.services;

import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.dto.list.PurchaseDocumentList;

public interface PurchaseOrderService {

    public String create(PurchaseDocumentDTO purchaseOrder);

    public void update(String code, PurchaseDocumentDTO purchaseOrder);

    public void delete(String code) throws DeleteFailedException;

    public PurchaseDocumentDTO get(String code);

    public PurchaseDocumentList list(int page);

}
