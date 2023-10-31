package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.SalesOrder;
import org.roko.erp.backend.model.SalesOrderLine;
import org.roko.erp.backend.model.jpa.SalesOrderLineId;
import org.roko.erp.dto.SalesDocumentLineDTO;

public interface SalesOrderLineService {
    
    public void create(SalesOrderLine salesOrderLine);

    public void update(SalesOrderLineId id, SalesOrderLine salesOrderLine);

    public void delete(SalesOrderLineId id);

    public SalesOrderLine get(SalesOrderLineId id);

    public List<SalesOrderLine> list(SalesOrder salesOrder);

    public List<SalesOrderLine> list(SalesOrder salesOrder, int page);

    public long count(SalesOrder salesOrder);

    public int maxLineNo(SalesOrder salesOrder);

    public SalesDocumentLineDTO toDTO(SalesOrderLine salesOrderLine);

    public SalesOrderLine fromDTO(SalesDocumentLineDTO dto);
}
