package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.PostedPurchaseCreditMemo;
import org.roko.erp.backend.model.PostedPurchaseCreditMemoLine;
import org.roko.erp.dto.PostedPurchaseDocumentLineDTO;

public interface PostedPurchaseCreditMemoLineService {
    
    public void create(PostedPurchaseCreditMemoLine postedPurchaseCreditMemoLine);

    public List<PostedPurchaseCreditMemoLine> list(PostedPurchaseCreditMemo postedPurchaseCreditMemo);

    public List<PostedPurchaseCreditMemoLine> list(PostedPurchaseCreditMemo postedPurchaseCreditMemo, int page);

    public long count(PostedPurchaseCreditMemo postedPurchaseCreditMemo);

    public PostedPurchaseDocumentLineDTO toDTO(PostedPurchaseCreditMemoLine postedPurchaseCreditMemoLine);
}
