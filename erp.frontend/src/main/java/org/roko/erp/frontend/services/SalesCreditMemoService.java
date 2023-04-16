package org.roko.erp.frontend.services;

import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.dto.list.SalesDocumentList;

public interface SalesCreditMemoService {

    public String create(SalesDocumentDTO salesCreditMemo);

    public void update(String code, SalesDocumentDTO salesCreditMemo);

    public void delete(String code) throws DeleteFailedException;

    public SalesDocumentDTO get(String code);

    public SalesDocumentList list(int page);

}
