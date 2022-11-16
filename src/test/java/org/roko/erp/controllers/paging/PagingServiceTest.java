package org.roko.erp.controllers.paging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PagingServiceTest {
    
    private PagingService svc;

    @BeforeEach
    public void setup(){
        svc = new PagingServiceImpl();
    }

    @Test
    public void leftNavigationHasProperState_whenPageOneProvided(){
        PagingData pagingData = svc.generate("item", 1, 120);

        assertFalse(pagingData.isFirstActive());
        assertFalse(pagingData.isPrevActive());

        assertEquals(1, pagingData.getPrevPage());
        assertEquals(1, pagingData.getCurrentPage());

        assertEquals(12, pagingData.getMaxPageCount());
    }

    @Test
    public void leftNavigationHasProperState_whenPageTwoProvided(){
        PagingData pagingData = svc.generate("item", 2, 120);

        assertTrue(pagingData.isFirstActive());
        assertTrue(pagingData.isPrevActive());

        assertEquals(1, pagingData.getPrevPage());

        assertEquals(12, pagingData.getMaxPageCount());
    }

    @Test
    public void rightNavigationHasProperState_whenLastPageProvided(){
        // record count: 120
        // records per page: 20
        // max page count: 6

        PagingData pagingData = svc.generate("item", 12, 120);

        assertFalse(pagingData.isNextActive());
        assertFalse(pagingData.isLastActive());

        assertEquals(12, pagingData.getNextPage());
        assertEquals(12, pagingData.getCurrentPage());
        assertEquals(12, pagingData.getLastPage());
        assertEquals(12, pagingData.getMaxPageCount());
    }

    @Test
    public void rightNavigationHasProperState_whenOneBeforeLastPageProvided(){
        // record count: 120
        // records per page: 20
        // max page count: 6

        PagingData pagingData = svc.generate("item", 11, 120);

        assertTrue(pagingData.isNextActive());
        assertTrue(pagingData.isLastActive());

        assertEquals(12, pagingData.getNextPage());
        assertEquals(11, pagingData.getCurrentPage());
        assertEquals(12, pagingData.getLastPage());
        assertEquals(12, pagingData.getMaxPageCount());
    }

    @Test
    public void rightNavigationHasProperState_whenOneBeyondLastPageProvided(){
        // record count: 120
        // records per page: 20
        // max page count: 6

        PagingData pagingData = svc.generate("item", 15, 120);

        assertFalse(pagingData.isNextActive());
        assertFalse(pagingData.isLastActive());

        assertEquals(12, pagingData.getNextPage());
        assertEquals(12, pagingData.getCurrentPage());
        assertEquals(12, pagingData.getLastPage());
        assertEquals(12, pagingData.getMaxPageCount());
    }

    @Test
    public void pagesAreProperlyCalculated(){
        // record count: 121
        // records per page: 20
        // max page count: 7

        PagingData pagingData = svc.generate("item", 1, 121);

        assertEquals(13, pagingData.getMaxPageCount());
    }


    @Test
    public void navigationHasProperState_whenNoRecords(){
        PagingData pagingData = svc.generate("item", 1, 0);

        assertFalse(pagingData.isFirstActive());
        assertFalse(pagingData.isPrevActive());

        assertEquals(1, pagingData.getPrevPage());
        assertEquals(1, pagingData.getCurrentPage());
        assertEquals(1, pagingData.getNextPage());
        assertEquals(1, pagingData.getLastPage());
        assertEquals(1, pagingData.getMaxPageCount());

        assertFalse(pagingData.isNextActive());
        assertFalse(pagingData.isLastActive());
    }

    @Test
    public void navigationHasProperState_whenNoRecordsAndPageProvided(){
        PagingData pagingData = svc.generate("item", 12, 0);

        assertFalse(pagingData.isFirstActive());
        assertFalse(pagingData.isPrevActive());

        assertEquals(1, pagingData.getPrevPage());
        assertEquals(1, pagingData.getCurrentPage());
        assertEquals(1, pagingData.getNextPage());
        assertEquals(1, pagingData.getLastPage());
        assertEquals(1, pagingData.getMaxPageCount());

        assertFalse(pagingData.isNextActive());
        assertFalse(pagingData.isLastActive());
    }

}
