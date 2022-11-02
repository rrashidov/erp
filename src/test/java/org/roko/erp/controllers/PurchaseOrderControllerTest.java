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
import org.roko.erp.model.PurchaseOrder;
import org.roko.erp.services.PurchaseOrderService;
import org.springframework.ui.Model;

public class PurchaseOrderControllerTest {
    
    private static final Long TEST_PAGE = 123l;

    private static final long TEST_COUNT = 234l;

    private List<PurchaseOrder> purchaseOrders = new ArrayList<>();

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private Model modelMock;

    @Mock
    private PurchaseOrderService svcMock;

    @Mock
    private PagingService pagingSvcMock;

    private PurchaseOrderController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(svcMock.list()).thenReturn(purchaseOrders);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        when(pagingSvcMock.generate("purchaseOrder", TEST_PAGE, TEST_COUNT)).thenReturn(pagingDataMock);

        controller = new PurchaseOrderController(svcMock, pagingSvcMock);
    }

    @Test
    public void list_returnsProperTemplate(){
        String template = controller.list(TEST_PAGE, modelMock);

        assertEquals("purchaseOrderList.html", template);

        verify(modelMock).addAttribute("purchaseOrders", purchaseOrders);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }
}
