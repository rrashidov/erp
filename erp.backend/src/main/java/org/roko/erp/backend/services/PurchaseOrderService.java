package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.PurchaseOrder;
import org.roko.erp.model.dto.PurchaseDocumentDTO;

public interface PurchaseOrderService {

    public void create(PurchaseOrder purchaseOrder);

    public void update(String code, PurchaseOrder purchaseOrder);

    public void delete(String code);

    public PurchaseOrder get(String code);

    public List<PurchaseOrder> list();

    public List<PurchaseOrder> list(int page);

    public int count();

    public PurchaseDocumentDTO toDTO(PurchaseOrder purchaseOrder);

    public PurchaseOrder fromDTO(PurchaseDocumentDTO dto);
}
