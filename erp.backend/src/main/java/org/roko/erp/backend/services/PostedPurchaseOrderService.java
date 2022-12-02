package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.PostedPurchaseOrder;

public interface PostedPurchaseOrderService {
    
    public void create(PostedPurchaseOrder postedPurchaseOrder);

    public PostedPurchaseOrder get(String code);

    public List<PostedPurchaseOrder> list();

    public List<PostedPurchaseOrder> list(int page);

    public int count();
}
