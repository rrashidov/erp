package org.roko.erp.frontend.services;

import java.util.List;

import org.roko.erp.frontend.model.PostedPurchaseOrder;

public interface PostedPurchaseOrderService {
    
    public void create(PostedPurchaseOrder postedPurchaseOrder);

    public PostedPurchaseOrder get(String code);

    public List<PostedPurchaseOrder> list();

    public List<PostedPurchaseOrder> list(int page);

    public int count();
}
