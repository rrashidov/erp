package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.SalesOrder;
import org.roko.erp.dto.SalesDocumentDTO;

public interface SalesOrderService {
    
    public void create(SalesOrder salesOrder);

    public void update(String code, SalesOrder salesOrder);

    public void delete(String code);

    public SalesOrder get(String code);

    public List<SalesOrder> list();

    public List<SalesOrder> list(int page);

    public long count();

    public SalesDocumentDTO toDTO(SalesOrder salesOrder);

    public SalesOrder fromDTO(SalesDocumentDTO dto);
}
