package org.roko.erp.frontend.services;

import java.util.List;

import org.roko.erp.frontend.model.PostedPurchaseCreditMemo;
import org.roko.erp.frontend.model.PostedPurchaseCreditMemoLine;

public interface PostedPurchaseCreditMemoLineService {
    
    public void create(PostedPurchaseCreditMemoLine postedPurchaseCreditMemoLine);

    public List<PostedPurchaseCreditMemoLine> list(PostedPurchaseCreditMemo postedPurchaseCreditMemo);

    public List<PostedPurchaseCreditMemoLine> list(PostedPurchaseCreditMemo postedPurchaseCreditMemo, int page);

    public int count(PostedPurchaseCreditMemo postedPurchaseCreditMemo);
}
