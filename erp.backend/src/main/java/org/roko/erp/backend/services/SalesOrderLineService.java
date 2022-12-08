package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.SalesOrder;
import org.roko.erp.backend.model.SalesOrderLine;
import org.roko.erp.backend.model.jpa.SalesOrderLineId;
import org.roko.erp.model.dto.SalesOrderLineDTO;

public interface SalesOrderLineService {
    
    public void create(SalesOrderLine salesOrderLine);

    public void update(SalesOrderLineId id, SalesOrderLine salesOrderLine);

    public void delete(SalesOrderLineId id);

    public SalesOrderLine get(SalesOrderLineId id);

    public List<SalesOrderLine> list(SalesOrder salesOrder);

    public List<SalesOrderLine> list(SalesOrder salesOrder, int page);

    public int count(SalesOrder salesOrder);

    public int maxLineNo(SalesOrder salesOrder);

    public SalesOrderLineDTO toDTO(SalesOrderLine salesOrderLine);

    public SalesOrderLine fromDTO(SalesOrderLineDTO dto);
}
