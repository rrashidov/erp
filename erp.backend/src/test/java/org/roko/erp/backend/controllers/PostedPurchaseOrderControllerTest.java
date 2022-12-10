package org.roko.erp.backend.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.PostedPurchaseOrder;
import org.roko.erp.backend.model.PostedPurchaseOrderLine;
import org.roko.erp.backend.services.PostedPurchaseOrderLineService;
import org.roko.erp.backend.services.PostedPurchaseOrderService;

public class PostedPurchaseOrderControllerTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 123;

    @Mock
    private PostedPurchaseOrderService svcMock;

    @Mock
    private PostedPurchaseOrder postedPurchaseOrder;

    @Mock
    private PostedPurchaseOrderLineService postedPurchaseOrderLineSvcMock;

    @Mock
    private PostedPurchaseOrderLine postedPurchaseOrderLineMock;

    private PostedPurchaseOrderController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(postedPurchaseOrderLineSvcMock.list(postedPurchaseOrder, TEST_PAGE))
                .thenReturn(Arrays.asList(postedPurchaseOrderLineMock));

        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(postedPurchaseOrder));
        when(svcMock.get(TEST_CODE)).thenReturn(postedPurchaseOrder);

        controller = new PostedPurchaseOrderController(svcMock, postedPurchaseOrderLineSvcMock);
    }

    @Test
    public void list_delegatesToService() {
        controller.list(TEST_PAGE);

        verify(svcMock).list(TEST_PAGE);
        verify(svcMock).toDTO(postedPurchaseOrder);
    }

    @Test
    public void get_delegatesToService() {
        controller.get(TEST_CODE);

        verify(svcMock).get(TEST_CODE);
        verify(svcMock).toDTO(postedPurchaseOrder);
    }

    @Test
    public void listLines_delegatesToService() {
        controller.listLines(TEST_CODE, TEST_PAGE);

        verify(postedPurchaseOrderLineSvcMock).list(postedPurchaseOrder, TEST_PAGE);
        verify(postedPurchaseOrderLineSvcMock).toDTO(postedPurchaseOrderLineMock);
    }
}
