package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.PostedPurchaseOrder;
import org.roko.erp.backend.model.PostedPurchaseOrderLine;
import org.roko.erp.dto.PostedPurchaseDocumentLineDTO;

public interface PostedPurchaseOrderLineService {
    
    public void create(PostedPurchaseOrderLine postedPurchaseOrderLine);

    public List<PostedPurchaseOrderLine> list(PostedPurchaseOrder postedPurchaseOrder);

    public List<PostedPurchaseOrderLine> list(PostedPurchaseOrder postedPurchaseOrder, int page);

    public long count(PostedPurchaseOrder postedPurchaseOrder);

    public PostedPurchaseDocumentLineDTO toDTO(PostedPurchaseOrderLine postedPurchaseOrderLine);
}
