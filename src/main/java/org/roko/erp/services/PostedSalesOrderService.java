package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.PostedSalesOrder;

public interface PostedSalesOrderService {
    
    public void create(PostedSalesOrder postedSalesOrder);

    public PostedSalesOrder get(String code);

    public List<PostedSalesOrder> list();

    public List<PostedSalesOrder> list(int page);

    public int count();
}
