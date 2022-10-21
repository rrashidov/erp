package org.roko.erp.controllers.paging;

import org.springframework.stereotype.Component;

@Component
public interface PagingService {

    public PagingData generate(String objectName, Long page, long recordCount);
}
