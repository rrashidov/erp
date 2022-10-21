package org.roko.erp.controllers.paging;

import org.springframework.stereotype.Component;

@Component
public class PagingServiceImpl implements PagingService {

    private static final int RECORDS_PER_PAGE = 20;

    @Override
    public PagingData generate(String objectName, Long page, long recordCount) {
        PagingData result = new PagingData();

        result.setObjectName(objectName);

        if (page == null){
            page = new Long(1);
        }

        long maxPageCount = recordCount / RECORDS_PER_PAGE;

        if ((recordCount % RECORDS_PER_PAGE) > 0){
            maxPageCount++;
        }

        if (maxPageCount == 0){
            maxPageCount = 1;
        }

        if (page > maxPageCount) {
            page = new Long(maxPageCount);
        }

        long prevPage = page - 1;
        if (prevPage < 1) {
            prevPage = new Long(1);
        }

        long nextPage = page + 1;
        if (nextPage > maxPageCount) {
            nextPage = maxPageCount;
        }

        result.setFirstActive(page.longValue() > 1);
        result.setPrevActive(page.longValue() > 1);

        result.setPrevPage(prevPage);
        result.setCurrentPage(page);
        result.setNextPage(nextPage);
        result.setLastPage(maxPageCount);
        result.setMaxPageCount(maxPageCount);

        result.setNextActive(page.longValue() < maxPageCount);
        result.setLastActive(page.longValue() < maxPageCount);

        return result;
    }
    
}
