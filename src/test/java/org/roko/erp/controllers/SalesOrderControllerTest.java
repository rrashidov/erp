package org.roko.erp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.SalesOrder;
import org.roko.erp.services.SalesOrderService;
import org.springframework.ui.Model;

public class SalesOrderControllerTest {

    private static final Long TEST_PAGE = 123l;

    private static final long TEST_COUNT = 234;

    private List<SalesOrder> salesOrderList = new ArrayList<>();

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private Model modelMock;

    @Mock
    private SalesOrderService svcMock;

    @Mock
    private PagingService pagingSvcMock;
    
    private SalesOrderController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(svcMock.list()).thenReturn(salesOrderList);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        when(pagingSvcMock.generate("salesOrder", TEST_PAGE, TEST_COUNT)).thenReturn(pagingDataMock);

        controller = new SalesOrderController(svcMock, pagingSvcMock);
    }

    @Test
    public void listReturnsProperTemplate(){
        String template = controller.list(TEST_PAGE, modelMock);

        assertEquals("salesOrderList.html", template);

        verify(modelMock).addAttribute("salesOrders", salesOrderList);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }
}
