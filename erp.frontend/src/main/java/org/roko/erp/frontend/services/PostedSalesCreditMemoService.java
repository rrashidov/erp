package org.roko.erp.frontend.services;

import org.roko.erp.dto.PostedSalesDocumentDTO;
import org.roko.erp.dto.list.PostedSalesDocumentList;

public interface PostedSalesCreditMemoService {
    
    public PostedSalesDocumentDTO get(String code);

    public PostedSalesDocumentList list(int page);

}
