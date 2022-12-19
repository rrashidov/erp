package org.roko.erp.frontend.services;

import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.dto.list.SalesDocumentList;

public interface SalesOrderService {
    
    public void create(SalesDocumentDTO salesOrder);

    public void update(String code, SalesDocumentDTO salesOrder);

    public void delete(String code);

    public SalesDocumentDTO get(String code);

    public SalesDocumentList list(int page);

}
