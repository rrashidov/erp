package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.PostedSalesOrder;
import org.roko.erp.model.PostedSalesOrderLine;

public interface PostedSalesOrderLineService {

    public void create(PostedSalesOrderLine PostedSalesOrderLine);

    public List<PostedSalesOrderLine> list(PostedSalesOrder PostedSalesOrder);

    public int count(PostedSalesOrder postedSalesOrder);
}
