package org.roko.erp.frontend.services;

import org.roko.erp.dto.SalesDocumentLineDTO;
import org.roko.erp.dto.list.SalesDocumentLineList;

public interface SalesOrderLineService {
    
    public void create(String code, SalesDocumentLineDTO salesOrderLine);

    public void update(String code, int lineNo, SalesDocumentLineDTO salesOrderLine);

    public void delete(String code, int lineNo);

    public SalesDocumentLineDTO get(String code, int lineNo);

    public SalesDocumentLineList list(String code, int page);

}
