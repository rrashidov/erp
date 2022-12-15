package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.PurchaseCreditMemo;
import org.roko.erp.backend.model.PurchaseCreditMemoLine;
import org.roko.erp.backend.model.jpa.PurchaseCreditMemoLineId;
import org.roko.erp.dto.PurchaseDocumentLineDTO;

public interface PurchaseCreditMemoLineService {
    
    public void create(PurchaseCreditMemoLine purchaseCreditMemoLine);

    public void update(PurchaseCreditMemoLineId id, PurchaseCreditMemoLine purchaseCreditMemoLine);

    public void delete(PurchaseCreditMemoLineId id);

    public PurchaseCreditMemoLine get(PurchaseCreditMemoLineId id);

    public List<PurchaseCreditMemoLine> list(PurchaseCreditMemo purchaseCreditMemo);

    public List<PurchaseCreditMemoLine> list(PurchaseCreditMemo purchaseCreditMemo, int page);

    public int count(PurchaseCreditMemo purchaseCreditMemo);

    public int maxLineNo(PurchaseCreditMemo purchaseCreditMemo);

    public PurchaseDocumentLineDTO toDTO(PurchaseCreditMemoLine purchaseCreditMemoLine);

    public PurchaseCreditMemoLine fromDTO(PurchaseDocumentLineDTO dto);
}
