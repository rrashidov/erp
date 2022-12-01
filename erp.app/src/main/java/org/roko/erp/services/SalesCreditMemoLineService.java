package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.SalesCreditMemo;
import org.roko.erp.model.SalesCreditMemoLine;
import org.roko.erp.model.jpa.SalesCreditMemoLineId;

public interface SalesCreditMemoLineService {
    
    public void create(SalesCreditMemoLine salesCreditMemoLine);

    public void update(SalesCreditMemoLineId salesCreditMemoLineId, SalesCreditMemoLine salesCreditMemoLine);

    public void delete(SalesCreditMemoLineId salesCreditMemoLineId);

    public SalesCreditMemoLine get(SalesCreditMemoLineId salesCreditMemoLineId);

    public List<SalesCreditMemoLine> list(SalesCreditMemo salesCreditMemo);

    public List<SalesCreditMemoLine> list(SalesCreditMemo salesCreditMemo, int page);

    public int count(SalesCreditMemo salesCreditMemo);

    public int maxLineNo(SalesCreditMemo salesCreditMemo);
}
