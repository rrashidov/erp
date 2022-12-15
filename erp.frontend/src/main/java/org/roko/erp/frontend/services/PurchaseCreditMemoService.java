package org.roko.erp.frontend.services;

import java.util.List;

import org.roko.erp.frontend.model.PurchaseCreditMemo;

public interface PurchaseCreditMemoService {

    public void create(PurchaseCreditMemo purchaseCreditMemo);

    public void update(String code, PurchaseCreditMemo purchaseCreditMemo);

    public void delete(String code);

    public PurchaseCreditMemo get(String code);

    public List<PurchaseCreditMemo> list();

    public List<PurchaseCreditMemo> list(int page);

    public int count();
    
}
