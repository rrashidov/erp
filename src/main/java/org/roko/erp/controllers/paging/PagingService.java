package org.roko.erp.controllers.paging;

public interface PagingService {

    public PagingData generate(String objectName, int page, int recordCount);
}
