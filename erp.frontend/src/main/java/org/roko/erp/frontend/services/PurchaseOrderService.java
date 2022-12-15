package org.roko.erp.frontend.services;

import java.util.List;

import org.roko.erp.frontend.model.PurchaseOrder;

public interface PurchaseOrderService {

    public void create(PurchaseOrder purchaseOrder);

    public void update(String code, PurchaseOrder purchaseOrder);

    public void delete(String code);

    public PurchaseOrder get(String code);

    public List<PurchaseOrder> list();

    public List<PurchaseOrder> list(int page);

    public int count();
    
}
