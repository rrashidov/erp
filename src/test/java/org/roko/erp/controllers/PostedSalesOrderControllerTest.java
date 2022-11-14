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
import org.roko.erp.model.PostedSalesOrder;
import org.roko.erp.services.PostedSalesOrderService;
import org.springframework.ui.Model;

public class PostedSalesOrderControllerTest {
    
    private static final Long TEST_PAGE = 123l;

    private static final long TEST_COUNT = 234l;

    private List<PostedSalesOrder> postedSalesOrders = new ArrayList<>();

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private Model modelMock;

    @Mock
    private PostedSalesOrderService svcMock;

    @Mock
    private PagingService pagingSvcMock;

    private PostedSalesOrderController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(svcMock.list()).thenReturn(postedSalesOrders);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        when(pagingSvcMock.generate("postedSalesOrder", TEST_PAGE, TEST_COUNT)).thenReturn(pagingDataMock);

        controller = new PostedSalesOrderController(svcMock, pagingSvcMock);
    }

    @Test
    public void list_returnsProperTemplate(){
        String template = controller.list(TEST_PAGE, modelMock);

        assertEquals("postedSalesOrderList.html", template);

        verify(modelMock).addAttribute("postedSalesOrders", postedSalesOrders);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }
}
