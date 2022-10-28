package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.SalesOrder;

public interface SalesOrderService {
    
    public void create(SalesOrder salesOrder);

    public void update(String code, SalesOrder salesOrder);

    public void delete(String code);

    public SalesOrder get(String code);

    public List<SalesOrder> list();

    public long count();
}
