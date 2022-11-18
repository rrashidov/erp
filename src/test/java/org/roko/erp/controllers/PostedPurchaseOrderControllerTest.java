package org.roko.erp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.PostedPurchaseOrder;
import org.roko.erp.model.PostedPurchaseOrderLine;
import org.roko.erp.services.PostedPurchaseOrderLineService;
import org.roko.erp.services.PostedPurchaseOrderService;
import org.springframework.ui.Model;

public class PostedPurchaseOrderControllerTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 23;
    private static final int TEST_COUNT = 123;
    private static final int TEST_LINE_COUNT = 234;

    private List<PostedPurchaseOrder> postedPurchaseOrders = new ArrayList<>();

    private List<PostedPurchaseOrderLine> postedPurchaseOrderLines = new ArrayList<>();

    @Mock
    private PostedPurchaseOrder postedPurchaseOrderMock;

    @Mock
    private PostedPurchaseOrderLine postedPurchaseOrderLineMock;

    @Mock
    private Model modelMock;

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private PagingData postedPurchaseOrderLinePagingDataMock;

    @Mock
    private PostedPurchaseOrderService svcMock;

    @Mock
    private PostedPurchaseOrderLineService postedPurchaseOrderLineSvcMock;

    @Mock
    private PagingService pagingSvcMock;

    private PostedPurchaseOrderController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        postedPurchaseOrderLines = Arrays.asList(postedPurchaseOrderLineMock);

        when(svcMock.list(TEST_PAGE)).thenReturn(postedPurchaseOrders);
        when(svcMock.count()).thenReturn(TEST_COUNT);
        when(svcMock.get(TEST_CODE)).thenReturn(postedPurchaseOrderMock);

        when(postedPurchaseOrderLineSvcMock.list(postedPurchaseOrderMock, TEST_PAGE)).thenReturn(postedPurchaseOrderLines);
        when(postedPurchaseOrderLineSvcMock.count(postedPurchaseOrderMock)).thenReturn(TEST_LINE_COUNT);

        when(pagingSvcMock.generate("postedPurchaseOrder", TEST_PAGE, TEST_COUNT)).thenReturn(pagingDataMock);
        when(pagingSvcMock.generate("postedPurchaseOrderCard", TEST_CODE, TEST_PAGE, TEST_LINE_COUNT))
                .thenReturn(postedPurchaseOrderLinePagingDataMock);

        controller = new PostedPurchaseOrderController(svcMock, postedPurchaseOrderLineSvcMock, pagingSvcMock);
    }

    @Test
    public void list_returnsProperTemplate() {
        String template = controller.list(TEST_PAGE, modelMock);

        assertEquals("postedPurchaseOrderList.html", template);

        verify(modelMock).addAttribute("postedPurchaseOrders", postedPurchaseOrders);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }

    @Test
    public void card_returnsProperTemplate() {
        String template = controller.card(TEST_CODE, TEST_PAGE, modelMock);

        assertEquals("postedPurchaseOrderCard.html", template);

        verify(modelMock).addAttribute("postedPurchaseOrder", postedPurchaseOrderMock);
        verify(modelMock).addAttribute("postedPurchaseOrderLines", postedPurchaseOrderLines);
        verify(modelMock).addAttribute("paging", postedPurchaseOrderLinePagingDataMock);
    }
}
