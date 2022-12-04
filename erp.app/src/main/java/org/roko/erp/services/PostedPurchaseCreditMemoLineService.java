package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.PostedPurchaseCreditMemo;
import org.roko.erp.model.PostedPurchaseCreditMemoLine;

public interface PostedPurchaseCreditMemoLineService {
    
    public void create(PostedPurchaseCreditMemoLine postedPurchaseCreditMemoLine);

    public List<PostedPurchaseCreditMemoLine> list(PostedPurchaseCreditMemo postedPurchaseCreditMemo);

    public List<PostedPurchaseCreditMemoLine> list(PostedPurchaseCreditMemo postedPurchaseCreditMemo, int page);

    public int count(PostedPurchaseCreditMemo postedPurchaseCreditMemo);
}