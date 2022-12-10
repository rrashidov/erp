package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.PostedPurchaseCreditMemo;
import org.roko.erp.model.dto.PostedPurchaseDocumentDTO;

public interface PostedPurchaseCreditMemoService {

    public void create(PostedPurchaseCreditMemo postedPurchaseCreditMemo);

    public PostedPurchaseCreditMemo get(String code);

    public List<PostedPurchaseCreditMemo> list();

    public List<PostedPurchaseCreditMemo> list(int page);

    public int count();

    public PostedPurchaseDocumentDTO toDTO(PostedPurchaseCreditMemo postedPurchaseCreditMemo);
}
