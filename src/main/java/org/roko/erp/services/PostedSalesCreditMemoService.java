package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.PostedSalesCreditMemo;

public interface PostedSalesCreditMemoService {
    
    public void create(PostedSalesCreditMemo postedSalesCreditMemo);

    public PostedSalesCreditMemo get(String code);

    public List<PostedSalesCreditMemo> list();

    public long count();
}
