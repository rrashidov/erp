package org.roko.erp.controllers.paging;

import org.springframework.stereotype.Component;

@Component
public class PagingServiceImpl implements PagingService {

    public static final int RECORDS_PER_PAGE = 10;

    @Override
    public PagingData generate(String objectName, int page, int recordCount) {
        PagingData result = new PagingData();

        result.setObjectName(objectName);

        if (page == 0){
            page = 1;
        }

        int maxPageCount = recordCount / RECORDS_PER_PAGE;

        if ((recordCount % RECORDS_PER_PAGE) > 0){
            maxPageCount++;
        }

        if (maxPageCount == 0){
            maxPageCount = 1;
        }

        if (page > maxPageCount) {
            page = maxPageCount;
        }

        int prevPage = page - 1;
        if (prevPage < 1) {
            prevPage = 1;
        }

        int nextPage = page + 1;
        if (nextPage > maxPageCount) {
            nextPage = maxPageCount;
        }

        result.setFirstActive(page > 1);
        result.setPrevActive(page > 1);

        result.setPrevPage(prevPage);
        result.setCurrentPage(page);
        result.setNextPage(nextPage);
        result.setLastPage(maxPageCount);
        result.setMaxPageCount(maxPageCount);

        result.setNextActive(page < maxPageCount);
        result.setLastActive(page < maxPageCount);

        return result;
    }
    
}
