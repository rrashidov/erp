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
import org.roko.erp.model.PostedPurchaseOrder;
import org.roko.erp.model.PostedPurchaseOrderLine;
import org.roko.erp.services.PostedPurchaseOrderLineService;
import org.roko.erp.services.PostedPurchaseOrderService;
import org.springframework.ui.Model;

public class PostedPurchaseOrderControllerTest {

    private static final Long TEST_COUNT = 123l;

    private static final String TEST_CODE = "test-code";

    private static final Long TEST_LINE_COUNT = 234l;

    private List<PostedPurchaseOrder> postedPurchaseOrders = new ArrayList<>();

    private List<PostedPurchaseOrderLine> postedPurchaseOrderLines = new ArrayList<>();

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

    @Mock
    private PostedPurchaseOrder postedPurchaseOrderMock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(svcMock.list()).thenReturn(postedPurchaseOrders);
        when(svcMock.count()).thenReturn(TEST_COUNT);
        when(svcMock.get(TEST_CODE)).thenReturn(postedPurchaseOrderMock);

        when(postedPurchaseOrderLineSvcMock.list(postedPurchaseOrderMock)).thenReturn(postedPurchaseOrderLines);
        when(postedPurchaseOrderLineSvcMock.count(postedPurchaseOrderMock)).thenReturn(TEST_LINE_COUNT);

        when(pagingSvcMock.generate("postedPurchaseOrder", null, TEST_COUNT)).thenReturn(pagingDataMock);
        when(pagingSvcMock.generate("postedPurchaseOrderLine", null, TEST_LINE_COUNT))
                .thenReturn(postedPurchaseOrderLinePagingDataMock);

        controller = new PostedPurchaseOrderController(svcMock, postedPurchaseOrderLineSvcMock, pagingSvcMock);
    }

    @Test
    public void list_returnsProperTemplate() {
        String template = controller.list(modelMock);

        assertEquals("postedPurchaseOrderList.html", template);

        verify(modelMock).addAttribute("postedPurchaseOrders", postedPurchaseOrders);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }

    @Test
    public void card_returnsProperTemplate() {
        String template = controller.card(TEST_CODE, modelMock);

        assertEquals("postedPurchaseOrderCard.html", template);

        verify(modelMock).addAttribute("postedPurchaseOrder", postedPurchaseOrderMock);
        verify(modelMock).addAttribute("postedPurchaseOrderLines", postedPurchaseOrderLines);
        verify(modelMock).addAttribute("paging", postedPurchaseOrderLinePagingDataMock);
    }
}
