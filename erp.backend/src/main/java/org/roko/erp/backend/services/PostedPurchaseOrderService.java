package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.PostedPurchaseOrder;
import org.roko.erp.dto.PostedPurchaseDocumentDTO;

public interface PostedPurchaseOrderService {
    
    public void create(PostedPurchaseOrder postedPurchaseOrder);

    public PostedPurchaseOrder get(String code);

    public List<PostedPurchaseOrder> list();

    public List<PostedPurchaseOrder> list(int page);

    public int count();

    public PostedPurchaseDocumentDTO toDTO(PostedPurchaseOrder postedPurchaseOrder);
}
