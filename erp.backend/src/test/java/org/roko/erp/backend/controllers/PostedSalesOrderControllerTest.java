package org.roko.erp.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.PostedSalesOrder;
import org.roko.erp.backend.model.PostedSalesOrderLine;
import org.roko.erp.backend.services.PostedSalesOrderLineService;
import org.roko.erp.backend.services.PostedSalesOrderService;
import org.roko.erp.dto.PostedSalesDocumentDTO;
import org.roko.erp.dto.PostedSalesDocumentLineDTO;
import org.roko.erp.dto.list.PostedSalesDocumentLineList;
import org.roko.erp.dto.list.PostedSalesDocumentList;

public class PostedSalesOrderControllerTest {
    
    private static final int TEST_COUNT = 222;

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 123;

    @Mock
    private PostedSalesOrderService svcMock;

    @Mock
    private PostedSalesOrder postedSalesOrderMock;

    @Mock
    private PostedSalesOrderLineService postedSalesOrderLineSvcMock;

    @Mock
    private PostedSalesOrderLine postedSalesOrderLineMock;

    @Mock
    private PostedSalesDocumentDTO dtoMock;

    @Mock
    private PostedSalesDocumentLineDTO lineDtoMock;

    private PostedSalesOrderController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(postedSalesOrderLineSvcMock.list(postedSalesOrderMock, TEST_PAGE)).thenReturn(Arrays.asList(postedSalesOrderLineMock));
        when(postedSalesOrderLineSvcMock.toDTO(postedSalesOrderLineMock)).thenReturn(lineDtoMock);
        when(postedSalesOrderLineSvcMock.count(postedSalesOrderMock)).thenReturn(TEST_COUNT);

        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(postedSalesOrderMock));
        when(svcMock.get(TEST_CODE)).thenReturn(postedSalesOrderMock);
        when(svcMock.toDTO(postedSalesOrderMock)).thenReturn(dtoMock);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        controller = new PostedSalesOrderController(svcMock, postedSalesOrderLineSvcMock);
    }

    @Test
    public void list_returnsProperResult() {
        PostedSalesDocumentList list = controller.list(TEST_PAGE);

        assertEquals(dtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void get_delegatesToService() {
        controller.get(TEST_CODE);

        verify(svcMock).get(TEST_CODE);
        verify(svcMock).toDTO(postedSalesOrderMock);
    }

    @Test
    public void listLines_returnsProperResult() {
        PostedSalesDocumentLineList list = controller.listLines(TEST_CODE, TEST_PAGE);

        assertEquals(lineDtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

}
