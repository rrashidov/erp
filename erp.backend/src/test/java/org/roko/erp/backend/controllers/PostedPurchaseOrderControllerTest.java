package org.roko.erp.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.roko.erp.dto.PostedPurchaseDocumentDTO;
import org.roko.erp.dto.PostedPurchaseDocumentLineDTO;
import org.roko.erp.dto.list.PostedPurchaseDocumentLineList;
import org.roko.erp.dto.list.PostedPurchaseDocumentList;

public class PostedPurchaseOrderControllerTest {

    private static final long TEST_COUNT = 222;

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

    @Mock
    private PostedPurchaseDocumentDTO dtoMock;

    @Mock
    private PostedPurchaseDocumentLineDTO lineDtoMock;

    private PostedPurchaseOrderController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(postedPurchaseOrderLineSvcMock.list(postedPurchaseOrder, TEST_PAGE))
                .thenReturn(Arrays.asList(postedPurchaseOrderLineMock));
        when(postedPurchaseOrderLineSvcMock.toDTO(postedPurchaseOrderLineMock)).thenReturn(lineDtoMock);
        when(postedPurchaseOrderLineSvcMock.count(postedPurchaseOrder)).thenReturn(TEST_COUNT);

        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(postedPurchaseOrder));
        when(svcMock.get(TEST_CODE)).thenReturn(postedPurchaseOrder);
        when(svcMock.toDTO(postedPurchaseOrder)).thenReturn(dtoMock);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        controller = new PostedPurchaseOrderController(svcMock, postedPurchaseOrderLineSvcMock);
    }

    @Test
    public void list_returnsProperValue() {
        PostedPurchaseDocumentList list = controller.list(TEST_PAGE);

        assertEquals(dtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void get_delegatesToService() {
        controller.get(TEST_CODE);

        verify(svcMock).get(TEST_CODE);
        verify(svcMock).toDTO(postedPurchaseOrder);
    }

    @Test
    public void listLines_returnsProperValue() {
        PostedPurchaseDocumentLineList list = controller.listLines(TEST_CODE, TEST_PAGE);

        assertEquals(lineDtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }
}
