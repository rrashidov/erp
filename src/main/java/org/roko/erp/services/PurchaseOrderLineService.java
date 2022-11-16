package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.PurchaseOrder;
import org.roko.erp.model.PurchaseOrderLine;
import org.roko.erp.model.jpa.PurchaseOrderLineId;

public interface PurchaseOrderLineService {

    public void create(PurchaseOrderLine PurchaseOrderLine);

    public void update(PurchaseOrderLineId id, PurchaseOrderLine purchaseOrderLine);

    public void delete(PurchaseOrderLineId id);

    public PurchaseOrderLine get(PurchaseOrderLineId id);

    public List<PurchaseOrderLine> list(PurchaseOrder purchaseOrder);
    
    public int count(PurchaseOrder purchaseOrder);
}
