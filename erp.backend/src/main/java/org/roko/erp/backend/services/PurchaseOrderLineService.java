package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.PurchaseOrder;
import org.roko.erp.backend.model.PurchaseOrderLine;
import org.roko.erp.backend.model.jpa.PurchaseOrderLineId;
import org.roko.erp.model.dto.PurchaseDocumentLineDTO;

public interface PurchaseOrderLineService {

    public void create(PurchaseOrderLine PurchaseOrderLine);

    public void update(PurchaseOrderLineId id, PurchaseOrderLine purchaseOrderLine);

    public void delete(PurchaseOrderLineId id);

    public PurchaseOrderLine get(PurchaseOrderLineId id);

    public List<PurchaseOrderLine> list(PurchaseOrder purchaseOrder);

    public List<PurchaseOrderLine> list(PurchaseOrder purchaseOrder, int page);

    public int count(PurchaseOrder purchaseOrder);

    public int maxLineNo(PurchaseOrder purchaseOrder);

    public PurchaseDocumentLineDTO toDTO(PurchaseOrderLine purchaseOrderLine);

    public PurchaseOrderLine fromDTO(PurchaseDocumentLineDTO dto);
}
