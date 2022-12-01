package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.PostedSalesCreditMemo;
import org.roko.erp.model.PostedSalesCreditMemoLine;

public interface PostedSalesCreditMemoLineService {
    
    public void create(PostedSalesCreditMemoLine postedSalesCreditMemoLine);

    public List<PostedSalesCreditMemoLine> list(PostedSalesCreditMemo postedSalesCreditMemo);

    public List<PostedSalesCreditMemoLine> list(PostedSalesCreditMemo postedSalesCreditMemo, int page);

    public int count(PostedSalesCreditMemo postedSalesCreditMemo);
}
