package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.SalesOrder;
import org.roko.erp.model.dto.SalesOrderDTO;

public interface SalesOrderService {
    
    public void create(SalesOrder salesOrder);

    public void update(String code, SalesOrder salesOrder);

    public void delete(String code);

    public SalesOrder get(String code);

    public List<SalesOrder> list();

    public List<SalesOrder> list(int page);

    public int count();

    public SalesOrderDTO toDTO(SalesOrder salesOrder);

    public SalesOrder fromDTO(SalesOrderDTO dto);
}
