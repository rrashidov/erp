package org.roko.erp.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.roko.erp.backend.controllers.policy.PolicyResult;
import org.roko.erp.backend.controllers.policy.PurchaseCreditMemoPolicy;
import org.roko.erp.backend.model.DocumentPostStatus;
import org.roko.erp.backend.model.PurchaseCreditMemo;
import org.roko.erp.backend.model.PurchaseCreditMemoLine;
import org.roko.erp.backend.model.jpa.PurchaseCreditMemoLineId;
import org.roko.erp.backend.services.PurchaseCodeSeriesService;
import org.roko.erp.backend.services.PurchaseCreditMemoLineService;
import org.roko.erp.backend.services.PurchaseCreditMemoService;
import org.roko.erp.backend.services.exc.PostFailedException;
import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.dto.PurchaseDocumentLineDTO;
import org.roko.erp.dto.list.PurchaseDocumentLineList;
import org.roko.erp.dto.list.PurchaseDocumentList;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

public class PurchaseCreditMemoControllerTest {

    private static final String TEST_DELETE_ERR_MSG = "test-delete-err-msg";

    private static final String POST_OPERATION_EXCHANGE_NAME = "erp.operations.post";
    private static final String POST_OPERATION_ROUTING_KEY = "purchase.creditmemo";

    private static final String TEST_POST_FAILED_EXCEPTION = "test-post-failed-exception";

    private static final int TEST_COUNT = 222;

    private static final String TEST_CODE = "test-code";

    private static final String NON_EXISTING_CODE = "non-existing-code";

    private static final int TEST_PAGE = 123;

    private static final int TEST_LINE_NO = 23;

    private static final int TEST_MAX_LINE_NO = 234;

    private static final String TEST_NEW_CODE = "test-new-code";

    private static final String POST_SCHEDULED_ERR_TMPL = "Purchase Credit Memo %s already scheduled for posting";
    
    @Captor
    private ArgumentCaptor<PurchaseCreditMemoLineId> purchaseCreditMemoLineIdArgumentCaptor;

    @Mock
    private PurchaseCreditMemo purchaseCreditMemoMock;

    @Mock
    private PurchaseCreditMemoService svcMock;

    @Mock
    private PurchaseDocumentDTO dtoMock;

    @Mock
    private PurchaseCreditMemoLineService purchaseCreditMemoLineSvcMock;

    @Mock
    private PurchaseCreditMemoLine purchaseCreditMemoLineMock;

    @Mock
    private PurchaseDocumentLineDTO purchaseCreditMemoLineDtoMock;

    @Mock
    private PurchaseCodeSeriesService purchaseCodeSeriesSvcMock;

    @Mock
    private RabbitTemplate rabbitMQClientMock;

    @Mock
    private PurchaseCreditMemoPolicy policyMock;

    private PurchaseCreditMemoController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(policyMock.canDelete(TEST_CODE)).thenReturn(new PolicyResult(true, ""));

        when(purchaseCreditMemoMock.getCode()).thenReturn(TEST_CODE);
        when(purchaseCreditMemoMock.getPostStatus()).thenReturn(DocumentPostStatus.READY);

        when(purchaseCodeSeriesSvcMock.creditMemoCode()).thenReturn(TEST_NEW_CODE);

        PurchaseCreditMemoLineId purchaseCreditMemoLineId = new PurchaseCreditMemoLineId();
        purchaseCreditMemoLineId.setPurchaseCreditMemo(purchaseCreditMemoMock);
        purchaseCreditMemoLineId.setLineNo(TEST_LINE_NO);

        when(purchaseCreditMemoLineSvcMock.list(purchaseCreditMemoMock, TEST_PAGE))
                .thenReturn(Arrays.asList(purchaseCreditMemoLineMock));
        when(purchaseCreditMemoLineSvcMock.get(purchaseCreditMemoLineId)).thenReturn(purchaseCreditMemoLineMock);
        when(purchaseCreditMemoLineSvcMock.fromDTO(purchaseCreditMemoLineDtoMock))
                .thenReturn(purchaseCreditMemoLineMock);
        when(purchaseCreditMemoLineSvcMock.toDTO(purchaseCreditMemoLineMock)).thenReturn(purchaseCreditMemoLineDtoMock);
        when(purchaseCreditMemoLineSvcMock.count(purchaseCreditMemoMock)).thenReturn(TEST_COUNT);
        when(purchaseCreditMemoLineSvcMock.maxLineNo(purchaseCreditMemoMock)).thenReturn(TEST_MAX_LINE_NO);

        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(purchaseCreditMemoMock));
        when(svcMock.get(TEST_CODE)).thenReturn(purchaseCreditMemoMock);
        when(svcMock.fromDTO(dtoMock)).thenReturn(purchaseCreditMemoMock);
        when(svcMock.toDTO(purchaseCreditMemoMock)).thenReturn(dtoMock);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        controller = new PurchaseCreditMemoController(svcMock, purchaseCreditMemoLineSvcMock, purchaseCodeSeriesSvcMock,
                rabbitMQClientMock, policyMock);
    }

    @Test
    public void list_returnsProperValue() {
        PurchaseDocumentList list = controller.list(TEST_PAGE);

        assertEquals(dtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void get_delegatesToService() {
        controller.get(TEST_CODE);

        verify(svcMock).get(TEST_CODE);
        verify(svcMock).toDTO(purchaseCreditMemoMock);
    }

    @Test
    public void getThrowsException_whenCalledWithNonExistingCode() {
        assertThrows(ResponseStatusException.class, () -> {
            controller.get(NON_EXISTING_CODE);
        });
    }

    @Test
    public void post_delegatesToService() {
        controller.post(dtoMock);

        verify(svcMock).create(purchaseCreditMemoMock);

        verify(purchaseCreditMemoMock).setCode(TEST_NEW_CODE);
    }

    @Test
    public void put_delegatesToService() {
        controller.put(TEST_CODE, dtoMock);

        verify(svcMock).update(TEST_CODE, purchaseCreditMemoMock);
    }

    @Test
    public void deleteSucceeds_whenPolicyAllows() {
        ResponseEntity<String> response = controller.delete(TEST_CODE);

        verify(svcMock).delete(TEST_CODE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(TEST_CODE, response.getBody());
    }

    @Test
    public void deleteFails_whenPolicyDoesNotAllowIt() {
        when(policyMock.canDelete(TEST_CODE)).thenReturn(new PolicyResult(false, TEST_DELETE_ERR_MSG));

        ResponseEntity<String> response = controller.delete(TEST_CODE);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(TEST_DELETE_ERR_MSG, response.getBody());
    }

    @Test
    public void listLines_returnsProperValue() {
        PurchaseDocumentLineList list = controller.listLines(TEST_CODE, TEST_PAGE);

        assertEquals(purchaseCreditMemoLineDtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void getLine_delegatesToService() {
        controller.getLine(TEST_CODE, TEST_LINE_NO);

        PurchaseCreditMemoLineId purchaseCreditMemoLineId = new PurchaseCreditMemoLineId();
        purchaseCreditMemoLineId.setPurchaseCreditMemo(purchaseCreditMemoMock);
        purchaseCreditMemoLineId.setLineNo(TEST_LINE_NO);

        verify(svcMock).get(TEST_CODE);
        verify(purchaseCreditMemoLineSvcMock).get(purchaseCreditMemoLineId);
        verify(purchaseCreditMemoLineSvcMock).toDTO(purchaseCreditMemoLineMock);
    }

    @Test
    public void getLineThrowsException_whenCalledWithNonExistingLine() {
        assertThrows(ResponseStatusException.class, () -> {
            controller.getLine(NON_EXISTING_CODE, TEST_LINE_NO);
        });
    }

    @Test
    public void postLine_delegatesToService() {
        int lineNo = controller.postLine(TEST_CODE, purchaseCreditMemoLineDtoMock);

        verify(purchaseCreditMemoLineSvcMock).create(purchaseCreditMemoLineMock);

        verify(purchaseCreditMemoLineMock)
                .setPurchaseCreditMemoLineId(purchaseCreditMemoLineIdArgumentCaptor.capture());

        PurchaseCreditMemoLineId purchaseCreditMemoLineId = purchaseCreditMemoLineIdArgumentCaptor.getValue();

        assertEquals(purchaseCreditMemoMock, purchaseCreditMemoLineId.getPurchaseCreditMemo());
        assertEquals(TEST_MAX_LINE_NO + 1, purchaseCreditMemoLineId.getLineNo());

        assertEquals(TEST_MAX_LINE_NO + 1, lineNo);
    }

    @Test
    public void putLine_delegatesToService() {
        controller.putLine(TEST_CODE, TEST_LINE_NO, purchaseCreditMemoLineDtoMock);

        PurchaseCreditMemoLineId purchaseCreditMemoLineId = new PurchaseCreditMemoLineId();
        purchaseCreditMemoLineId.setPurchaseCreditMemo(purchaseCreditMemoMock);
        purchaseCreditMemoLineId.setLineNo(TEST_LINE_NO);

        verify(purchaseCreditMemoLineSvcMock).update(purchaseCreditMemoLineId, purchaseCreditMemoLineMock);
    }

    @Test
    public void deleteLine_delegatesToService() {
        controller.deleteLine(TEST_CODE, TEST_LINE_NO);

        PurchaseCreditMemoLineId purchaseCreditMemoLineId = new PurchaseCreditMemoLineId();
        purchaseCreditMemoLineId.setPurchaseCreditMemo(purchaseCreditMemoMock);
        purchaseCreditMemoLineId.setLineNo(TEST_LINE_NO);

        verify(purchaseCreditMemoLineSvcMock).delete(purchaseCreditMemoLineId);
    }

    @Test
    public void operationPost_updatesPurchaseCreditMemoAndSendsMessageToRabbitMQ() throws PostFailedException {
        ResponseEntity<String> response = controller.operationPost(TEST_CODE);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(purchaseCreditMemoMock).setPostStatus(DocumentPostStatus.SCHEDULED);
        verify(purchaseCreditMemoMock).setPostStatusReason("");

        verify(svcMock).update(TEST_CODE, purchaseCreditMemoMock);

        verify(rabbitMQClientMock).convertAndSend(POST_OPERATION_EXCHANGE_NAME, POST_OPERATION_ROUTING_KEY, TEST_CODE);
    }

    @Test
    public void operationPost_returnsBadRequest_whenPostThrowsException() throws PostFailedException {
        doThrow(new AmqpException(TEST_POST_FAILED_EXCEPTION)).when(rabbitMQClientMock)
                .convertAndSend(POST_OPERATION_EXCHANGE_NAME, POST_OPERATION_ROUTING_KEY, TEST_CODE);

        ResponseEntity<String> response = controller.operationPost(TEST_CODE);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(TEST_POST_FAILED_EXCEPTION, response.getBody());
    }

    @Test
    public void operationPost_returnsBadRequest_whenPurchaseCreditMemoAlreadyScheduledForPosting() throws PostFailedException {
        when(purchaseCreditMemoMock.getPostStatus()).thenReturn(DocumentPostStatus.SCHEDULED);

        ResponseEntity<String> response = controller.operationPost(TEST_CODE);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(String.format(POST_SCHEDULED_ERR_TMPL, TEST_CODE), response.getBody());
    }
    
}
