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
    public void leftNavigationHasProperState_whenNoPageSpecified(){
        PagingData pagingData = svc.generate("item", null, 120);

        assertFalse(pagingData.isFirstActive());
        assertFalse(pagingData.isPrevActive());

        assertEquals(1, pagingData.getPrevPage());
        assertEquals(1, pagingData.getCurrentPage());

        assertEquals(6, pagingData.getMaxPageCount());
    }

    @Test
    public void leftNavigationHasProperState_whenPageOneProvided(){
        PagingData pagingData = svc.generate("item", new Long(1), 120);

        assertFalse(pagingData.isFirstActive());
        assertFalse(pagingData.isPrevActive());

        assertEquals(1, pagingData.getPrevPage());
        assertEquals(1, pagingData.getCurrentPage());

        assertEquals(6, pagingData.getMaxPageCount());
    }

    @Test
    public void leftNavigationHasProperState_whenPageTwoProvided(){
        PagingData pagingData = svc.generate("item", new Long(2), 120);

        assertTrue(pagingData.isFirstActive());
        assertTrue(pagingData.isPrevActive());

        assertEquals(1, pagingData.getPrevPage());

        assertEquals(6, pagingData.getMaxPageCount());
    }

    @Test
    public void rightNavigationHasProperState_whenLastPageProvided(){
        // record count: 120
        // records per page: 20
        // max page count: 6

        PagingData pagingData = svc.generate("item", new Long(6), 120);

        assertFalse(pagingData.isNextActive());
        assertFalse(pagingData.isLastActive());

        assertEquals(6, pagingData.getNextPage());
        assertEquals(6, pagingData.getCurrentPage());
        assertEquals(6, pagingData.getLastPage());
        assertEquals(6, pagingData.getMaxPageCount());
    }

    @Test
    public void rightNavigationHasProperState_whenOneBeforeLastPageProvided(){
        // record count: 120
        // records per page: 20
        // max page count: 6

        PagingData pagingData = svc.generate("item", new Long(5), 120);

        assertTrue(pagingData.isNextActive());
        assertTrue(pagingData.isLastActive());

        assertEquals(6, pagingData.getNextPage());
        assertEquals(5, pagingData.getCurrentPage());
        assertEquals(6, pagingData.getLastPage());
        assertEquals(6, pagingData.getMaxPageCount());
    }

    @Test
    public void rightNavigationHasProperState_whenOneBeyondLastPageProvided(){
        // record count: 120
        // records per page: 20
        // max page count: 6

        PagingData pagingData = svc.generate("item", new Long(10), 120);

        assertFalse(pagingData.isNextActive());
        assertFalse(pagingData.isLastActive());

        assertEquals(6, pagingData.getNextPage());
        assertEquals(6, pagingData.getCurrentPage());
        assertEquals(6, pagingData.getLastPage());
        assertEquals(6, pagingData.getMaxPageCount());
    }

    @Test
    public void navigationHasProperState_whenNoRecords(){
        PagingData pagingData = svc.generate("item", null, 0);

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
        PagingData pagingData = svc.generate("item", new Long(12), 0);

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
