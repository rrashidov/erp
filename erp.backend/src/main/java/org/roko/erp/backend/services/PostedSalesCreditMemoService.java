package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.PostedSalesCreditMemo;
import org.roko.erp.dto.PostedSalesDocumentDTO;

public interface PostedSalesCreditMemoService {
    
    public void create(PostedSalesCreditMemo postedSalesCreditMemo);

    public PostedSalesCreditMemo get(String code);

    public List<PostedSalesCreditMemo> list();

    public List<PostedSalesCreditMemo> list(int page);

    public int count();

    public PostedSalesDocumentDTO toDTO(PostedSalesCreditMemo postedSalesCreditMemo);
}
