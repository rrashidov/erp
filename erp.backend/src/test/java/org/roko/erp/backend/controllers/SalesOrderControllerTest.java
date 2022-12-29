package org.roko.erp.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.SalesOrder;
import org.roko.erp.backend.model.SalesOrderLine;
import org.roko.erp.backend.model.jpa.SalesOrderLineId;
import org.roko.erp.backend.services.SalesCodeSeriesService;
import org.roko.erp.backend.services.SalesOrderLineService;
import org.roko.erp.backend.services.SalesOrderPostService;
import org.roko.erp.backend.services.SalesOrderService;
import org.roko.erp.backend.services.exc.PostFailedException;
import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.dto.SalesDocumentLineDTO;
import org.roko.erp.dto.list.SalesDocumentLineList;
import org.roko.erp.dto.list.SalesDocumentList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SalesOrderControllerTest {

    private static final String TEST_POST_FAILED_MSG = "test-post-failed-msg";

    private static final int TEST_COUNT = 222;

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 123;

    private static final int TEST_LINE_NO = 234;

    private static final int TEST_MAX_LINE_NO = 12;

    @Captor
    private ArgumentCaptor<SalesOrderLineId> salesOrderLineIdArgumentCaptor;

    @Mock
    private SalesOrder salesOrderMock;

    @Mock
    private SalesDocumentDTO salesOrderDtoMock;

    @Mock
    private SalesOrderService svcMock;

    @Mock
    private SalesOrderLineService salesOrderLineSvcMock;

    @Mock
    private SalesOrderLine salesOrderLineMock;

    @Mock
    private SalesDocumentLineDTO salesOrderLineDtoMock;

    @Mock
    private SalesOrderLineId salesOrderLineId;

    @Mock
    private SalesCodeSeriesService salesCodeSeriesSvcMock;

    @Mock
    private SalesOrderPostService salesOrderPostSvcMock;

    private SalesOrderController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(salesCodeSeriesSvcMock.orderCode()).thenReturn(TEST_CODE);

        when(salesOrderLineId.getSalesOrder()).thenReturn(salesOrderMock);
        when(salesOrderLineId.getLineNo()).thenReturn(TEST_LINE_NO);

        SalesOrderLineId existingSalesOrderLineId = new SalesOrderLineId();
        existingSalesOrderLineId.setSalesOrder(salesOrderMock);
        existingSalesOrderLineId.setLineNo(TEST_LINE_NO);

        when(salesOrderLineMock.getSalesOrderLineId()).thenReturn(salesOrderLineId);

        when(salesOrderLineSvcMock.list(salesOrderMock, TEST_PAGE)).thenReturn(Arrays.asList(salesOrderLineMock));
        when(salesOrderLineSvcMock.get(existingSalesOrderLineId)).thenReturn(salesOrderLineMock);
        when(salesOrderLineSvcMock.fromDTO(salesOrderLineDtoMock)).thenReturn(salesOrderLineMock);
        when(salesOrderLineSvcMock.toDTO(salesOrderLineMock)).thenReturn(salesOrderLineDtoMock);
        when(salesOrderLineSvcMock.count(salesOrderMock)).thenReturn(TEST_COUNT);
        when(salesOrderLineSvcMock.maxLineNo(salesOrderMock)).thenReturn(TEST_MAX_LINE_NO);

        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(salesOrderMock));
        when(svcMock.get(TEST_CODE)).thenReturn(salesOrderMock);
        when(svcMock.fromDTO(salesOrderDtoMock)).thenReturn(salesOrderMock);
        when(svcMock.toDTO(salesOrderMock)).thenReturn(salesOrderDtoMock);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        controller = new SalesOrderController(svcMock, salesOrderLineSvcMock, salesCodeSeriesSvcMock,
                salesOrderPostSvcMock);
    }

    @Test
    public void list_returnsProperResult() {
        SalesDocumentList list = controller.list(TEST_PAGE);

        assertEquals(salesOrderDtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void get_delegatesToService() {
        controller.get(TEST_CODE);

        verify(svcMock).get(TEST_CODE);
        verify(svcMock).toDTO(salesOrderMock);
    }

    @Test
    public void listLines_returnsProperResult() {
        SalesDocumentLineList list = controller.listLines(TEST_CODE, TEST_PAGE);

        assertEquals(salesOrderLineDtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void getLine_delegatesToService() {
        controller.getLine(TEST_CODE, TEST_LINE_NO);

        verify(salesOrderLineSvcMock).get(salesOrderLineIdArgumentCaptor.capture());

        SalesOrderLineId salesOrderLineId = salesOrderLineIdArgumentCaptor.getValue();

        assertEquals(salesOrderMock, salesOrderLineId.getSalesOrder());
        assertEquals(TEST_LINE_NO, salesOrderLineId.getLineNo());

        verify(salesOrderLineSvcMock).toDTO(salesOrderLineMock);
    }

    @Test
    public void postLine_delegatesToService() {
        controller.postLine(TEST_CODE, salesOrderLineDtoMock);

        verify(salesOrderLineMock).setSalesOrderLineId(salesOrderLineIdArgumentCaptor.capture());

        SalesOrderLineId salesOrderLineId = salesOrderLineIdArgumentCaptor.getValue();

        assertEquals(salesOrderMock, salesOrderLineId.getSalesOrder());
        assertEquals(TEST_MAX_LINE_NO + 1, salesOrderLineId.getLineNo());

        verify(salesOrderLineSvcMock).create(salesOrderLineMock);
    }

    @Test
    public void putLine_delegatesToService() {
        controller.putLine(TEST_CODE, TEST_LINE_NO, salesOrderLineDtoMock);

        verify(salesOrderLineSvcMock).update(salesOrderLineIdArgumentCaptor.capture(), eq(salesOrderLineMock));

        SalesOrderLineId salesOrderLineId = salesOrderLineIdArgumentCaptor.getValue();

        assertEquals(salesOrderMock, salesOrderLineId.getSalesOrder());
        assertEquals(TEST_LINE_NO, salesOrderLineId.getLineNo());
    }

    @Test
    public void deleteLine_delegatesToService() {
        controller.deleteLine(TEST_CODE, TEST_LINE_NO);

        verify(salesOrderLineSvcMock).delete(salesOrderLineIdArgumentCaptor.capture());

        SalesOrderLineId salesOrderLineId = salesOrderLineIdArgumentCaptor.getValue();

        assertEquals(salesOrderMock, salesOrderLineId.getSalesOrder());
        assertEquals(TEST_LINE_NO, salesOrderLineId.getLineNo());
    }

    @Test
    public void post_delegatesToService() {
        controller.post(salesOrderDtoMock);

        verify(svcMock).create(salesOrderMock);

        verify(salesOrderMock).setCode(TEST_CODE);

        verify(salesCodeSeriesSvcMock).orderCode();
    }

    @Test
    public void put_delegatesToService() {
        controller.put(TEST_CODE, salesOrderDtoMock);

        verify(svcMock).update(TEST_CODE, salesOrderMock);
    }

    @Test
    public void delete_delegatesToService() {
        controller.delete(TEST_CODE);

        verify(svcMock).delete(TEST_CODE);
    }

    @Test
    public void postOperation_delegatesToService() throws PostFailedException {
        ResponseEntity<String> response = controller.operationPost(TEST_CODE);

        verify(salesOrderPostSvcMock).post(TEST_CODE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void postOperation_returnsBadRequest_whenPostingThrowsException() throws PostFailedException {
        doThrow(new PostFailedException(TEST_POST_FAILED_MSG)).when(salesOrderPostSvcMock).post(TEST_CODE);

        ResponseEntity<String> response = controller.operationPost(TEST_CODE);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(TEST_POST_FAILED_MSG, response.getBody());
    }
}
