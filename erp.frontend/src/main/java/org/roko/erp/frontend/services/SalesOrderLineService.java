package org.roko.erp.frontend.services;

import java.util.List;

import org.roko.erp.frontend.model.SalesOrder;
import org.roko.erp.frontend.model.SalesOrderLine;
import org.roko.erp.frontend.model.jpa.SalesOrderLineId;

public interface SalesOrderLineService {
    
    public void create(SalesOrderLine salesOrderLine);

    public void update(SalesOrderLineId id, SalesOrderLine salesOrderLine);

    public void delete(SalesOrderLineId id);

    public SalesOrderLine get(SalesOrderLineId id);

    public List<SalesOrderLine> list(SalesOrder salesOrder);

    public List<SalesOrderLine> list(SalesOrder salesOrder, int page);

    public int count(SalesOrder salesOrder);

    public int maxLineNo(SalesOrder salesOrder);
}
