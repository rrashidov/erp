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
import org.roko.erp.dto.PostedSalesDocumentDTO;
import org.roko.erp.dto.PostedSalesDocumentLineDTO;
import org.roko.erp.dto.list.PostedSalesDocumentLineList;
import org.roko.erp.dto.list.PostedSalesDocumentList;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.services.PostedSalesOrderLineService;
import org.roko.erp.frontend.services.PostedSalesOrderService;
import org.springframework.ui.Model;

public class PostedSalesOrderControllerTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 123;

    private static final long TEST_COUNT = 234;
    private static final long TEST_LINE_COUNT = 345;

    private List<PostedSalesDocumentDTO> postedSalesOrders = new ArrayList<>();

    private List<PostedSalesDocumentLineDTO> postedSalesOrderLines = new ArrayList<>();

    @Mock
    private PostedSalesDocumentDTO postedSalesOrderMock;

    @Mock
    private PostedSalesDocumentLineDTO postedSalesOrderLineMock;

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

    @Mock
    private PostedSalesDocumentList postedSalesOrderList;

    @Mock
    private PostedSalesDocumentLineList postedSalesOrderLineList;

    private PostedSalesOrderController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        postedSalesOrderLines = Arrays.asList(postedSalesOrderLineMock);

        postedSalesOrders = Arrays.asList(postedSalesOrderMock);

        when(postedSalesOrderList.getData()).thenReturn(postedSalesOrders);
        when(postedSalesOrderList.getCount()).thenReturn(TEST_COUNT);

        when(svcMock.list(TEST_PAGE)).thenReturn(postedSalesOrderList);
        when(svcMock.get(TEST_CODE)).thenReturn(postedSalesOrderMock);

        when(postedSalesOrderLineList.getData()).thenReturn(postedSalesOrderLines);
        when(postedSalesOrderLineList.getCount()).thenReturn(TEST_LINE_COUNT);

        when(lineSvcMock.list(TEST_CODE, TEST_PAGE)).thenReturn(postedSalesOrderLineList);

        when(pagingSvcMock.generate("postedSalesOrder", TEST_PAGE, (int) TEST_COUNT)).thenReturn(pagingDataMock);
        when(pagingSvcMock.generate("postedSalesOrderCard", TEST_CODE, TEST_PAGE, (int) TEST_LINE_COUNT)).thenReturn(linePagingDataMock);

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
        String template = controller.card(TEST_CODE, TEST_PAGE, modelMock);

        assertEquals("postedSalesOrderCard.html", template);

        verify(modelMock).addAttribute("postedSalesOrder", postedSalesOrderMock);
        verify(modelMock).addAttribute("postedSalesOrderLines", postedSalesOrderLines);
        verify(modelMock).addAttribute("paging", linePagingDataMock);
    }
}
