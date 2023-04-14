package org.roko.erp.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.controllers.policy.PolicyResult;
import org.roko.erp.backend.controllers.policy.PurchaseOrderPolicy;
import org.roko.erp.backend.model.DocumentPostStatus;
import org.roko.erp.backend.model.PurchaseOrder;
import org.roko.erp.backend.model.PurchaseOrderLine;
import org.roko.erp.backend.model.jpa.PurchaseOrderLineId;
import org.roko.erp.backend.services.PurchaseCodeSeriesService;
import org.roko.erp.backend.services.PurchaseOrderLineService;
import org.roko.erp.backend.services.PurchaseOrderService;
import org.roko.erp.backend.services.exc.PostFailedException;
import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.dto.PurchaseDocumentLineDTO;
import org.roko.erp.dto.list.PurchaseDocumentLineList;
import org.roko.erp.dto.list.PurchaseDocumentList;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PurchaseOrderControllerTest {

    private static final String POST_OPERATION_EXCHANGE_NAME = "erp.operations.post";
    private static final String POST_OPERATION_ROUTING_KEY = "purchase.order";

    private static final String TEST_POST_FAILED_MSG = "test-post-failed-msg";

    private static final int TEST_MAX_LINE_NO = 345;

    private static final String NEW_CODE = "new-code";

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 123;

    private static final int TEST_LINE_NO = 1234;

    private static final int TEST_COUNT = 222;

    private static final String POST_SCHEDULED_ERR_TMPL = "Purchase Order %s already scheduled for posting";

    private static final String TEST_DELETE_ERROR_MSG = "test-delete-error-msg";

    @Captor
    private ArgumentCaptor<PurchaseOrderLineId> purchaseOrderLineIdArgumentCaptor;

    @Mock
    private PurchaseOrderService svcMock;

    @Mock
    private PurchaseOrder purchaseOrderMock;

    @Mock
    private PurchaseDocumentDTO dtoMock;

    @Mock
    private PurchaseDocumentLineDTO purchaseOrderLineDtoMock;

    @Mock
    private PurchaseOrderLineService purchaseOrderLineSvcMock;

    @Mock
    private PurchaseOrderLine purchaseOrderLineMock;

    @Mock
    private PurchaseCodeSeriesService purchaseCodeSeriesSvcMock;

    @Mock
    private RabbitTemplate rabbitMQClientMock;

    @Mock
    private PurchaseOrderPolicy policyMock;

    private PurchaseOrderController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(policyMock.canDelete(TEST_CODE)).thenReturn(new PolicyResult(true, ""));

        when(purchaseOrderMock.getCode()).thenReturn(TEST_CODE);
        when(purchaseOrderMock.getPostStatus()).thenReturn(DocumentPostStatus.READY);

        when(purchaseCodeSeriesSvcMock.orderCode()).thenReturn(NEW_CODE);

        PurchaseOrderLineId purchaseOrderLineId = new PurchaseOrderLineId();
        purchaseOrderLineId.setPurchaseOrder(purchaseOrderMock);
        purchaseOrderLineId.setLineNo(TEST_LINE_NO);

        when(purchaseOrderLineSvcMock.list(purchaseOrderMock, TEST_PAGE))
                .thenReturn(Arrays.asList(purchaseOrderLineMock));
        when(purchaseOrderLineSvcMock.get(purchaseOrderLineId)).thenReturn(purchaseOrderLineMock);
        when(purchaseOrderLineSvcMock.fromDTO(purchaseOrderLineDtoMock)).thenReturn(purchaseOrderLineMock);
        when(purchaseOrderLineSvcMock.toDTO(purchaseOrderLineMock)).thenReturn(purchaseOrderLineDtoMock);
        when(purchaseOrderLineSvcMock.count(purchaseOrderMock)).thenReturn(TEST_COUNT);
        when(purchaseOrderLineSvcMock.maxLineNo(purchaseOrderMock)).thenReturn(TEST_MAX_LINE_NO);

        when(svcMock.list()).thenReturn(Arrays.asList(purchaseOrderMock));
        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(purchaseOrderMock));
        when(svcMock.get(TEST_CODE)).thenReturn(purchaseOrderMock);
        when(svcMock.toDTO(purchaseOrderMock)).thenReturn(dtoMock);
        when(svcMock.fromDTO(dtoMock)).thenReturn(purchaseOrderMock);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        controller = new PurchaseOrderController(svcMock, purchaseOrderLineSvcMock, purchaseCodeSeriesSvcMock,
                rabbitMQClientMock, policyMock);
    }

    @Test
    public void list_returnsProperValue() {
        PurchaseDocumentList list = controller.list();

        assertEquals(dtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void listWithPage_returnsProperValue() {
        PurchaseDocumentList list = controller.list(TEST_PAGE);

        assertEquals(dtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void get_delegatesToService() {
        controller.get(TEST_CODE);

        verify(svcMock).get(TEST_CODE);
        verify(svcMock).toDTO(purchaseOrderMock);
    }

    @Test
    public void post_delegatesToService() {
        controller.post(dtoMock);

        verify(svcMock).create(purchaseOrderMock);
        verify(purchaseOrderMock).setCode(NEW_CODE);
    }

    @Test
    public void put_delegatesToService() {
        controller.put(TEST_CODE, dtoMock);

        verify(svcMock).update(TEST_CODE, purchaseOrderMock);
    }

    @Test
    public void deleteSucceeds_whenPolicyAllows() {
        ResponseEntity<String> response = controller.delete(TEST_CODE);

        verify(svcMock).delete(TEST_CODE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(TEST_CODE, response.getBody());
    }

    @Test
    public void deleteFails_whenPolicyDoesNotAllowIt(){
        when(policyMock.canDelete(TEST_CODE)).thenReturn(new PolicyResult(false, TEST_DELETE_ERROR_MSG));

        ResponseEntity<String> response = controller.delete(TEST_CODE);

        verify(svcMock, never()).delete(TEST_CODE);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(TEST_DELETE_ERROR_MSG, response.getBody());
    }

    @Test
    public void listLines_returnsProperValue() {
        PurchaseDocumentLineList list = controller.listLines(TEST_CODE, TEST_PAGE);

        assertEquals(purchaseOrderLineDtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void getLine_delegatesToService() {
        controller.getLine(TEST_CODE, TEST_LINE_NO);

        PurchaseOrderLineId purchaseOrderLineId = new PurchaseOrderLineId();
        purchaseOrderLineId.setPurchaseOrder(purchaseOrderMock);
        purchaseOrderLineId.setLineNo(TEST_LINE_NO);

        verify(purchaseOrderLineSvcMock).get(purchaseOrderLineId);
        verify(purchaseOrderLineSvcMock).toDTO(purchaseOrderLineMock);
    }

    @Test
    public void postLine_delegatesToService() {
        controller.postLine(TEST_CODE, purchaseOrderLineDtoMock);

        verify(purchaseOrderLineSvcMock).create(purchaseOrderLineMock);

        verify(purchaseOrderLineMock).setPurchaseOrderLineId(purchaseOrderLineIdArgumentCaptor.capture());

        PurchaseOrderLineId purchaseOrderLineId = purchaseOrderLineIdArgumentCaptor.getValue();

        assertEquals(purchaseOrderMock, purchaseOrderLineId.getPurchaseOrder());
        assertEquals(TEST_MAX_LINE_NO + 1, purchaseOrderLineId.getLineNo());
    }

    @Test
    public void putLine_delegatesToService() {
        controller.putLine(TEST_CODE, TEST_LINE_NO, purchaseOrderLineDtoMock);

        PurchaseOrderLineId purchaseOrderLineId = new PurchaseOrderLineId();
        purchaseOrderLineId.setPurchaseOrder(purchaseOrderMock);
        purchaseOrderLineId.setLineNo(TEST_LINE_NO);

        verify(purchaseOrderLineSvcMock).update(purchaseOrderLineId, purchaseOrderLineMock);
    }

    @Test
    public void deleteLine_delegatesToService() {
        controller.deleteLine(TEST_CODE, TEST_LINE_NO);

        PurchaseOrderLineId purchaseOrderLineId = new PurchaseOrderLineId();
        purchaseOrderLineId.setPurchaseOrder(purchaseOrderMock);
        purchaseOrderLineId.setLineNo(TEST_LINE_NO);

        verify(purchaseOrderLineSvcMock).delete(purchaseOrderLineId);
    }

    @Test
    public void postOperation_updatesPurchaseOrderAndSendsMessageToRabbitMQ() throws PostFailedException {
        ResponseEntity<String> response = controller.operationPost(TEST_CODE);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(purchaseOrderMock).setPostStatus(DocumentPostStatus.SCHEDULED);
        verify(purchaseOrderMock).setPostStatusReason("");

        verify(svcMock).update(TEST_CODE, purchaseOrderMock);

        verify(rabbitMQClientMock).convertAndSend(POST_OPERATION_EXCHANGE_NAME, POST_OPERATION_ROUTING_KEY, TEST_CODE);
    }

    @Test
    public void postOperation_returnsBadRequest_whenPostingThrowsException() throws PostFailedException {
        doThrow(new AmqpException(TEST_POST_FAILED_MSG)).when(rabbitMQClientMock)
                .convertAndSend(POST_OPERATION_EXCHANGE_NAME, POST_OPERATION_ROUTING_KEY, TEST_CODE);

        ResponseEntity<String> response = controller.operationPost(TEST_CODE);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(TEST_POST_FAILED_MSG, response.getBody());
    }

    @Test
    public void postOperation_returnsBadRequest_whenPurchaseOrderAlreadyScheduledForPosting() throws PostFailedException {
        when(purchaseOrderMock.getPostStatus()).thenReturn(DocumentPostStatus.SCHEDULED);

        ResponseEntity<String> response = controller.operationPost(TEST_CODE);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(String.format(POST_SCHEDULED_ERR_TMPL, TEST_CODE), response.getBody());
    }

}
