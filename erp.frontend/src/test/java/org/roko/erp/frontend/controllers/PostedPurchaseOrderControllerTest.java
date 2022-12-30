package org.roko.erp.frontend.controllers;

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
import org.roko.erp.dto.PostedPurchaseDocumentDTO;
import org.roko.erp.dto.PostedPurchaseDocumentLineDTO;
import org.roko.erp.dto.list.PostedPurchaseDocumentLineList;
import org.roko.erp.dto.list.PostedPurchaseDocumentList;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.services.PostedPurchaseOrderLineService;
import org.roko.erp.frontend.services.PostedPurchaseOrderService;
import org.springframework.ui.Model;

public class PostedPurchaseOrderControllerTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 23;
    private static final long TEST_COUNT = 123;
    private static final long TEST_LINE_COUNT = 234;

    private List<PostedPurchaseDocumentDTO> postedPurchaseOrders = new ArrayList<>();

    private List<PostedPurchaseDocumentLineDTO> postedPurchaseOrderLines = new ArrayList<>();

    @Mock
    private PostedPurchaseDocumentDTO postedPurchaseOrderMock;

    @Mock
    private PostedPurchaseDocumentLineDTO postedPurchaseOrderLineMock;

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

    @Mock
    private PostedPurchaseDocumentList postedPurchaseOrderList;

    @Mock
    private PostedPurchaseDocumentLineList postedPurchaseOrderLineList;

    private PostedPurchaseOrderController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        postedPurchaseOrderLines = Arrays.asList(postedPurchaseOrderLineMock);

        when(postedPurchaseOrderList.getData()).thenReturn(postedPurchaseOrders);
        when(postedPurchaseOrderList.getCount()).thenReturn(TEST_COUNT);

        when(svcMock.list(TEST_PAGE)).thenReturn(postedPurchaseOrderList);
        when(svcMock.get(TEST_CODE)).thenReturn(postedPurchaseOrderMock);

        when(postedPurchaseOrderLineList.getData()).thenReturn(postedPurchaseOrderLines);
        when(postedPurchaseOrderLineList.getCount()).thenReturn(TEST_LINE_COUNT);

        when(postedPurchaseOrderLineSvcMock.list(TEST_CODE, TEST_PAGE)).thenReturn(postedPurchaseOrderLineList);

        when(pagingSvcMock.generate("postedPurchaseOrder", TEST_PAGE, (int) TEST_COUNT)).thenReturn(pagingDataMock);
        when(pagingSvcMock.generate("postedPurchaseOrderCard", TEST_CODE, TEST_PAGE, (int) TEST_LINE_COUNT))
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
