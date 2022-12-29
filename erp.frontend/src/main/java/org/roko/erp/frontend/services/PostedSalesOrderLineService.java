package org.roko.erp.frontend.services;

import org.roko.erp.dto.list.PostedSalesDocumentLineList;

public interface PostedSalesOrderLineService {

    public PostedSalesDocumentLineList list(String code, int page);

}
