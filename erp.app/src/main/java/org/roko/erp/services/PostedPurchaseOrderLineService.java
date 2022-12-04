package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.PostedPurchaseOrder;
import org.roko.erp.model.PostedPurchaseOrderLine;

public interface PostedPurchaseOrderLineService {
    
    public void create(PostedPurchaseOrderLine postedPurchaseOrderLine);

    public List<PostedPurchaseOrderLine> list(PostedPurchaseOrder postedPurchaseOrder);

    public List<PostedPurchaseOrderLine> list(PostedPurchaseOrder postedPurchaseOrder, int page);

    public int count(PostedPurchaseOrder postedPurchaseOrder);
}