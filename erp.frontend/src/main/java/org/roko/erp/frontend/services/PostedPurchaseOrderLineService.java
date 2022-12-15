package org.roko.erp.frontend.services;

import java.util.List;

import org.roko.erp.frontend.model.PostedPurchaseOrder;
import org.roko.erp.frontend.model.PostedPurchaseOrderLine;

public interface PostedPurchaseOrderLineService {
    
    public void create(PostedPurchaseOrderLine postedPurchaseOrderLine);

    public List<PostedPurchaseOrderLine> list(PostedPurchaseOrder postedPurchaseOrder);

    public List<PostedPurchaseOrderLine> list(PostedPurchaseOrder postedPurchaseOrder, int page);

    public int count(PostedPurchaseOrder postedPurchaseOrder);
}
