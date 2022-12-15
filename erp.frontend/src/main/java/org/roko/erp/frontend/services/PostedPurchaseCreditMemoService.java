package org.roko.erp.frontend.services;

import java.util.List;

import org.roko.erp.frontend.model.PostedPurchaseCreditMemo;

public interface PostedPurchaseCreditMemoService {

    public void create(PostedPurchaseCreditMemo postedPurchaseCreditMemo);

    public PostedPurchaseCreditMemo get(String code);

    public List<PostedPurchaseCreditMemo> list();

    public List<PostedPurchaseCreditMemo> list(int page);

    public int count();
    
}
