package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.PurchaseCreditMemo;
import org.roko.erp.dto.PurchaseDocumentDTO;

public interface PurchaseCreditMemoService {

    public void create(PurchaseCreditMemo purchaseCreditMemo);

    public void update(String code, PurchaseCreditMemo purchaseCreditMemo);

    public void delete(String code);

    public PurchaseCreditMemo get(String code);

    public List<PurchaseCreditMemo> list();

    public List<PurchaseCreditMemo> list(int page);

    public int count();

    public PurchaseDocumentDTO toDTO(PurchaseCreditMemo purchaseCreditMemo);

    public PurchaseCreditMemo fromDTO(PurchaseDocumentDTO dto);
}
