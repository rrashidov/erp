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
import org.roko.erp.model.PostedSalesOrderLine;
import org.roko.erp.services.PostedSalesOrderLineService;
import org.roko.erp.services.PostedSalesOrderService;
import org.springframework.ui.Model;

public class PostedSalesOrderControllerTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 123;
    private static final int TEST_COUNT = 234;
    private static final int TEST_LINE_COUNT = 345;

    private List<PostedSalesOrder> postedSalesOrders = new ArrayList<>();

    private List<PostedSalesOrderLine> postedSalesOrderLines = new ArrayList<>();

    @Mock
    private PostedSalesOrder postedSalesOrderMock;

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private PagingData linePagingDataMock;

    @Mock
    private Model modelMock;

    @Mock
    private PostedSalesOrderService svcMock;

    @Mock
    private PostedSalesOrderLineService lineSvcMock;

    @Mock
    private PagingService pagingSvcMock;

    private PostedSalesOrderController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(svcMock.list()).thenReturn(postedSalesOrders);
        when(svcMock.count()).thenReturn(TEST_COUNT);
        when(svcMock.get(TEST_CODE)).thenReturn(postedSalesOrderMock);

        when(lineSvcMock.list(postedSalesOrderMock)).thenReturn(postedSalesOrderLines);
        when(lineSvcMock.count(postedSalesOrderMock)).thenReturn(TEST_LINE_COUNT);

        when(pagingSvcMock.generate("postedSalesOrder", TEST_PAGE, TEST_COUNT)).thenReturn(pagingDataMock);
        when(pagingSvcMock.generate("postedSalesOrderLine", 1, TEST_LINE_COUNT)).thenReturn(linePagingDataMock);

        controller = new PostedSalesOrderController(svcMock, lineSvcMock, pagingSvcMock);
    }

    @Test
    public void list_returnsProperTemplate() {
        String template = controller.list(TEST_PAGE, modelMock);

        assertEquals("postedSalesOrderList.html", template);

        verify(modelMock).addAttribute("postedSalesOrders", postedSalesOrders);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }

    @Test
    public void card_returnsProperTemplate() {
        String template = controller.card(TEST_CODE, modelMock);

        assertEquals("postedSalesOrderCard.html", template);

        verify(modelMock).addAttribute("postedSalesOrder", postedSalesOrderMock);
        verify(modelMock).addAttribute("postedSalesOrderLines", postedSalesOrderLines);
        verify(modelMock).addAttribute("paging", linePagingDataMock);
    }
}
