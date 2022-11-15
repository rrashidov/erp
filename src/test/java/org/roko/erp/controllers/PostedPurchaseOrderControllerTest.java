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
import org.roko.erp.services.PostedPurchaseOrderService;
import org.springframework.ui.Model;

public class PostedPurchaseOrderControllerTest {
    
    private static final Long TEST_COUNT = 123l;

    private List<PostedPurchaseOrder> postedPurchaseOrders = new ArrayList<>();

    @Mock
    private Model modelMock;

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private PostedPurchaseOrderService svcMock;

    @Mock
    private PagingService pagingSvcMock;

    private PostedPurchaseOrderController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(svcMock.list()).thenReturn(postedPurchaseOrders);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        when(pagingSvcMock.generate("postedPurchaseOrder", null, TEST_COUNT)).thenReturn(pagingDataMock);

        controller = new PostedPurchaseOrderController(svcMock, pagingSvcMock);
    }

    @Test
    public void list_returnsProperTemplate() {
        String template = controller.list(modelMock);

        assertEquals("postedPurchaseOrderList.html", template);

        verify(modelMock).addAttribute("postedPurchaseOrders", postedPurchaseOrders);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }
}
