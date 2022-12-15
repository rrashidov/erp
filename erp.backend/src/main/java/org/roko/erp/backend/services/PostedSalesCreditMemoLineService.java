package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.PostedSalesCreditMemo;
import org.roko.erp.backend.model.PostedSalesCreditMemoLine;
import org.roko.erp.dto.PostedSalesDocumentLineDTO;

public interface PostedSalesCreditMemoLineService {
    
    public void create(PostedSalesCreditMemoLine postedSalesCreditMemoLine);

    public List<PostedSalesCreditMemoLine> list(PostedSalesCreditMemo postedSalesCreditMemo);

    public List<PostedSalesCreditMemoLine> list(PostedSalesCreditMemo postedSalesCreditMemo, int page);

    public int count(PostedSalesCreditMemo postedSalesCreditMemo);

    public PostedSalesDocumentLineDTO toDTO(PostedSalesCreditMemoLine postedSalesCreditMemoLine);
}
