package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.PostedSalesOrder;
import org.roko.erp.dto.PostedSalesDocumentDTO;

public interface PostedSalesOrderService {
    
    public void create(PostedSalesOrder postedSalesOrder);

    public PostedSalesOrder get(String code);

    public List<PostedSalesOrder> list();

    public List<PostedSalesOrder> list(int page);

    public long count();

    public PostedSalesDocumentDTO toDTO(PostedSalesOrder postedSalesOrder);
}
