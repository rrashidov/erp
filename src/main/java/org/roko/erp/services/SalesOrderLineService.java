package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.SalesOrder;
import org.roko.erp.model.SalesOrderLine;
import org.roko.erp.model.jpa.SalesOrderLineId;

public interface SalesOrderLineService {
    
    public void create(SalesOrderLine salesOrderLine);

    public void update(SalesOrderLineId id, SalesOrderLine salesOrderLine);

    public void delete(SalesOrderLineId id);

    public SalesOrderLine get(SalesOrderLineId id);

    public List<SalesOrderLine> list(SalesOrder salesOrder);

    public long count(SalesOrder salesOrder);
}
