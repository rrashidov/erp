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
import org.roko.erp.backend.model.DocumentPostStatus;
import org.roko.erp.backend.model.SalesCreditMemo;
import org.roko.erp.backend.model.SalesCreditMemoLine;
import org.roko.erp.backend.model.jpa.SalesCreditMemoLineId;
import org.roko.erp.backend.services.SalesCodeSeriesService;
import org.roko.erp.backend.services.SalesCreditMemoLineService;
import org.roko.erp.backend.services.SalesCreditMemoService;
import org.roko.erp.backend.services.exc.PostFailedException;
import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.dto.SalesDocumentLineDTO;
import org.roko.erp.dto.list.SalesDocumentLineList;
import org.roko.erp.dto.list.SalesDocumentList;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SalesCreditMemoControllerTest {

    private static final String SALES_CREDIT_MEMO_MSG_ROUTING_KEY = "sales.creditmemo";
    private static final String SALES_CREDIT_MEMO_MSG_EXCHANGE_NAME = "erp.operations.post";

    private static final String TEST_POST_FAILED_EXCEPTION_MSG = "test-post-failed-exception-msg";

    private static final String GENERATED_CODE = "generated-code";

    private static final int TEST_COUNT = 222;

    private static final int TEST_LINE_NO = 1234;

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 123;

    private static final int TEST_MAX_LINE_NO = 345;

    @Captor
    private ArgumentCaptor<SalesCreditMemoLineId> salesCreditMemoLineIdArgumentCaptor;

    @Mock
    private SalesCreditMemoService svcMock;

    @Mock
    private SalesCreditMemo salesCreditMemoMock;

    @Mock
    private SalesDocumentDTO dtoMock;

    @Mock
    private SalesCreditMemoLineService salesCreditMemoLineSvcMock;

    @Mock
    private SalesCreditMemoLine salesCreditMemoLineMock;

    @Mock
    private SalesDocumentLineDTO salesDocumentLineDtoMock;

    @Mock
    private SalesCodeSeriesService salesCodeSeriesSvcMock;

    @Mock
    private RabbitTemplate rabbitMQClientMock;

    private SalesCreditMemoController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(salesCodeSeriesSvcMock.creditMemoCode()).thenReturn(GENERATED_CODE);

        when(salesCreditMemoLineSvcMock.list(salesCreditMemoMock, TEST_PAGE))
                .thenReturn(Arrays.asList(salesCreditMemoLineMock));
        when(salesCreditMemoLineSvcMock.fromDTO(salesDocumentLineDtoMock)).thenReturn(salesCreditMemoLineMock);
        when(salesCreditMemoLineSvcMock.toDTO(salesCreditMemoLineMock)).thenReturn(salesDocumentLineDtoMock);
        when(salesCreditMemoLineSvcMock.count(salesCreditMemoMock)).thenReturn(TEST_COUNT);
        when(salesCreditMemoLineSvcMock.maxLineNo(salesCreditMemoMock)).thenReturn(TEST_MAX_LINE_NO);

        when(svcMock.fromDTO(dtoMock)).thenReturn(salesCreditMemoMock);

        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(salesCreditMemoMock));
        when(svcMock.get(TEST_CODE)).thenReturn(salesCreditMemoMock);
        when(svcMock.toDTO(salesCreditMemoMock)).thenReturn(dtoMock);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        controller = new SalesCreditMemoController(svcMock, salesCreditMemoLineSvcMock, salesCodeSeriesSvcMock,
                rabbitMQClientMock);
    }

    @Test
    public void list_returnsProperResult() {
        SalesDocumentList list = controller.list(TEST_PAGE);

        assertEquals(dtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void get_delegatesToService() {
        controller.get(TEST_CODE);

        verify(svcMock).get(TEST_CODE);
        verify(svcMock).toDTO(salesCreditMemoMock);
    }

    @Test
    public void listLines_returnsProperResult() {
        SalesDocumentLineList list = controller.listLines(TEST_CODE, TEST_PAGE);

        assertEquals(salesDocumentLineDtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void getLine_delegatesToService() {
        controller.getLine(TEST_CODE, TEST_LINE_NO);

        verify(salesCreditMemoLineSvcMock).get(salesCreditMemoLineIdArgumentCaptor.capture());

        SalesCreditMemoLineId salesCreditMemoLineId = salesCreditMemoLineIdArgumentCaptor.getValue();

        assertEquals(salesCreditMemoMock, salesCreditMemoLineId.getSalesCreditMemo());
        assertEquals(TEST_LINE_NO, salesCreditMemoLineId.getLineNo());
    }

    @Test
    public void postLine_delegatesToService() {
        controller.postLine(TEST_CODE, salesDocumentLineDtoMock);

        verify(salesCreditMemoLineSvcMock).create(salesCreditMemoLineMock);

        verify(salesCreditMemoLineMock).setSalesCreditMemoLineId(salesCreditMemoLineIdArgumentCaptor.capture());

        SalesCreditMemoLineId salesCreditMemoLineId = salesCreditMemoLineIdArgumentCaptor.getValue();

        assertEquals(salesCreditMemoMock, salesCreditMemoLineId.getSalesCreditMemo());
        assertEquals(TEST_MAX_LINE_NO + 1, salesCreditMemoLineId.getLineNo());
    }

    @Test
    public void putLine_delegatesToService() {
        controller.putLine(TEST_CODE, TEST_LINE_NO, salesDocumentLineDtoMock);

        verify(salesCreditMemoLineSvcMock).update(salesCreditMemoLineIdArgumentCaptor.capture(),
                eq(salesCreditMemoLineMock));

        SalesCreditMemoLineId salesCreditMemoLineId = salesCreditMemoLineIdArgumentCaptor.getValue();

        assertEquals(salesCreditMemoMock, salesCreditMemoLineId.getSalesCreditMemo());
        assertEquals(TEST_LINE_NO, salesCreditMemoLineId.getLineNo());
    }

    @Test
    public void deleteLine_delegatesToService() {
        controller.deleteLine(TEST_CODE, TEST_LINE_NO);

        verify(salesCreditMemoLineSvcMock).delete(salesCreditMemoLineIdArgumentCaptor.capture());

        SalesCreditMemoLineId salesCreditMemoLineId = salesCreditMemoLineIdArgumentCaptor.getValue();

        assertEquals(salesCreditMemoMock, salesCreditMemoLineId.getSalesCreditMemo());
        assertEquals(TEST_LINE_NO, salesCreditMemoLineId.getLineNo());
    }

    @Test
    public void post_delegatesToService() {
        controller.post(dtoMock);

        verify(salesCreditMemoMock).setCode(GENERATED_CODE);

        verify(svcMock).create(salesCreditMemoMock);
    }

    @Test
    public void put_delegatesToService() {
        controller.put(TEST_CODE, dtoMock);

        verify(salesCreditMemoMock).setCode(TEST_CODE);

        verify(svcMock).update(TEST_CODE, salesCreditMemoMock);
    }

    @Test
    public void delete_delegatesToService() {
        controller.delete(TEST_CODE);

        verify(svcMock).delete(TEST_CODE);
    }

    @Test
    public void operationPost_delegatesToService() throws PostFailedException {
        ResponseEntity<String> response = controller.operationPost(TEST_CODE);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(rabbitMQClientMock).convertAndSend(SALES_CREDIT_MEMO_MSG_EXCHANGE_NAME, SALES_CREDIT_MEMO_MSG_ROUTING_KEY, TEST_CODE);

        verify(salesCreditMemoMock).setPostStatus(DocumentPostStatus.SCHEDULED);
        verify(salesCreditMemoMock).setPostStatusReason("");
        verify(svcMock).update(TEST_CODE, salesCreditMemoMock);
    }

    @Test
    public void operationPost_returnsBadRequest_whenPostingThrowsException() throws PostFailedException {
        doThrow(new AmqpException(TEST_POST_FAILED_EXCEPTION_MSG)).when(rabbitMQClientMock)
                .convertAndSend(SALES_CREDIT_MEMO_MSG_EXCHANGE_NAME, SALES_CREDIT_MEMO_MSG_ROUTING_KEY, TEST_CODE);

        ResponseEntity<String> response = controller.operationPost(TEST_CODE);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(TEST_POST_FAILED_EXCEPTION_MSG, response.getBody());
    }
}
