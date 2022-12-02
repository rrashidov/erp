package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.PostedSalesOrder;
import org.roko.erp.backend.model.PostedSalesOrderLine;

public interface PostedSalesOrderLineService {

    public void create(PostedSalesOrderLine PostedSalesOrderLine);

    public List<PostedSalesOrderLine> list(PostedSalesOrder PostedSalesOrder);

    public List<PostedSalesOrderLine> list(PostedSalesOrder PostedSalesOrder, int page);

    public int count(PostedSalesOrder postedSalesOrder);
}
