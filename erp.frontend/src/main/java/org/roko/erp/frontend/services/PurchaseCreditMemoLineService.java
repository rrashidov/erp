package org.roko.erp.frontend.services;

import java.util.List;

import org.roko.erp.frontend.model.PurchaseCreditMemo;
import org.roko.erp.frontend.model.PurchaseCreditMemoLine;
import org.roko.erp.frontend.model.jpa.PurchaseCreditMemoLineId;

public interface PurchaseCreditMemoLineService {
    
    public void create(PurchaseCreditMemoLine purchaseCreditMemoLine);

    public void update(PurchaseCreditMemoLineId id, PurchaseCreditMemoLine purchaseCreditMemoLine);

    public void delete(PurchaseCreditMemoLineId id);

    public PurchaseCreditMemoLine get(PurchaseCreditMemoLineId id);

    public List<PurchaseCreditMemoLine> list(PurchaseCreditMemo purchaseCreditMemo);

    public List<PurchaseCreditMemoLine> list(PurchaseCreditMemo purchaseCreditMemo, int page);

    public int count(PurchaseCreditMemo purchaseCreditMemo);

    public int maxLineNo(PurchaseCreditMemo purchaseCreditMemo);
}
