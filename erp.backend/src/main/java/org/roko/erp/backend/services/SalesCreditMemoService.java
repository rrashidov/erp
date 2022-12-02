package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.SalesCreditMemo;

public interface SalesCreditMemoService {

    public void create(SalesCreditMemo salesCreditMemo);

    public void update(String code, SalesCreditMemo salesCreditMemo);

    public void delete(String code);

    public SalesCreditMemo get(String code);

    public List<SalesCreditMemo> list();

    public List<SalesCreditMemo> list(int page);

    public int count();
    
}
