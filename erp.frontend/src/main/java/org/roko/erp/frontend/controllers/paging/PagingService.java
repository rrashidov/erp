package org.roko.erp.frontend.controllers.paging;

public interface PagingService {

    public PagingData generate(String objectName, int page, int recordCount);

    public PagingData generate(String objectName, String code, int page, int recordCount);

}
