package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.PostedPurchaseCreditMemo;

public interface PostedPurchaseCreditMemoService {

    public void create(PostedPurchaseCreditMemo postedPurchaseCreditMemo);

    public PostedPurchaseCreditMemo get(String code);

    public List<PostedPurchaseCreditMemo> list();

    public long count();
    
}
