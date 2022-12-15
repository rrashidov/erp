package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.SalesCreditMemo;
import org.roko.erp.backend.model.SalesCreditMemoLine;
import org.roko.erp.backend.model.jpa.SalesCreditMemoLineId;
import org.roko.erp.dto.SalesDocumentLineDTO;

public interface SalesCreditMemoLineService {
    
    public void create(SalesCreditMemoLine salesCreditMemoLine);

    public void update(SalesCreditMemoLineId salesCreditMemoLineId, SalesCreditMemoLine salesCreditMemoLine);

    public void delete(SalesCreditMemoLineId salesCreditMemoLineId);

    public SalesCreditMemoLine get(SalesCreditMemoLineId salesCreditMemoLineId);

    public List<SalesCreditMemoLine> list(SalesCreditMemo salesCreditMemo);

    public List<SalesCreditMemoLine> list(SalesCreditMemo salesCreditMemo, int page);

    public int count(SalesCreditMemo salesCreditMemo);

    public int maxLineNo(SalesCreditMemo salesCreditMemo);

    public SalesDocumentLineDTO toDTO(SalesCreditMemoLine salesCreditMemoLine);

    public SalesCreditMemoLine fromDTO(SalesDocumentLineDTO dto);
}
