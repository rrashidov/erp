package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.PurchaseCreditMemo;
import org.roko.erp.model.PurchaseCreditMemoLine;
import org.roko.erp.model.jpa.PurchaseCreditMemoLineId;

public interface PurchaseCreditMemoLineService {
    
    public void create(PurchaseCreditMemoLine purchaseCreditMemoLine);

    public void update(PurchaseCreditMemoLineId id, PurchaseCreditMemoLine purchaseCreditMemoLine);

    public void delete(PurchaseCreditMemoLineId id);

    public PurchaseCreditMemoLine get(PurchaseCreditMemoLineId id);

    public List<PurchaseCreditMemoLine> list(PurchaseCreditMemo purchaseCreditMemo);

    public long count(PurchaseCreditMemo purchaseCreditMemo);
}
