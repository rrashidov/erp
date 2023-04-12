package org.roko.erp.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.roko.erp.backend.model.GeneralJournalBatch;
import org.roko.erp.backend.model.GeneralJournalBatchLine;
import org.roko.erp.backend.model.jpa.GeneralJournalBatchLineId;
import org.roko.erp.backend.services.GeneralJournalBatchLineService;
import org.roko.erp.backend.services.GeneralJournalBatchService;
import org.roko.erp.backend.services.exc.PostFailedException;
import org.roko.erp.dto.GeneralJournalBatchDTO;
import org.roko.erp.dto.GeneralJournalBatchLineDTO;
import org.roko.erp.dto.list.GeneralJournalBatchLineList;
import org.roko.erp.dto.list.GeneralJournalBatchList;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GeneralJournalBatchControllerTest {

    private static final String POST_OPERATION_EXCHANGE_NAME = "erp.operations.post";
    private static final String POST_OPERATION_ROUTING_KEY = "generaljournalbatch";

    private static final int TEST_MAX_LINENO = 456;

    private static final String TEST_FAILED_MSG = "test-failed-msg";

    private static final int TEST_COUNT = 222;

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 123;

    private static final int TEST_LINE_NO = 1234;

    @Captor
    private ArgumentCaptor<GeneralJournalBatchLineId> generalJournalBatchLineIdArgumentCaptor;

    @Mock
    private GeneralJournalBatch generalJournalBatchMock;

    @Mock
    private GeneralJournalBatchDTO dtoMock;

    @Mock
    private GeneralJournalBatchService svcMock;

    @Mock
    private GeneralJournalBatchLineService generalJournalBatchLineSvcMock;

    @Mock
    private GeneralJournalBatchLine generalJournalBatchLineMock;

    @Mock
    private GeneralJournalBatchLineDTO generalJournalBatchLineDtoMock;

    @Mock
    private RabbitTemplate rabbitMQClientMock;

    private GeneralJournalBatchController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        GeneralJournalBatchLineId generalJournalBatchLineId = new GeneralJournalBatchLineId();
        generalJournalBatchLineId.setGeneralJournalBatch(generalJournalBatchMock);
        generalJournalBatchLineId.setLineNo(TEST_LINE_NO);

        when(generalJournalBatchLineSvcMock.list(generalJournalBatchMock, TEST_PAGE))
                .thenReturn(Arrays.asList(generalJournalBatchLineMock));
        when(generalJournalBatchLineSvcMock.get(generalJournalBatchLineId)).thenReturn(generalJournalBatchLineMock);
        when(generalJournalBatchLineSvcMock.fromDTO(generalJournalBatchLineDtoMock))
                .thenReturn(generalJournalBatchLineMock);
        when(generalJournalBatchLineSvcMock.toDTO(generalJournalBatchLineMock))
                .thenReturn(generalJournalBatchLineDtoMock);
        when(generalJournalBatchLineSvcMock.count(generalJournalBatchMock)).thenReturn(TEST_COUNT);
        when(generalJournalBatchLineSvcMock.maxLineNo(generalJournalBatchMock)).thenReturn(TEST_MAX_LINENO);

        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(generalJournalBatchMock));
        when(svcMock.get(TEST_CODE)).thenReturn(generalJournalBatchMock);
        when(svcMock.fromDTO(dtoMock)).thenReturn(generalJournalBatchMock);
        when(svcMock.toDTO(generalJournalBatchMock)).thenReturn(dtoMock);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        controller = new GeneralJournalBatchController(svcMock, generalJournalBatchLineSvcMock, rabbitMQClientMock);
    }

    @Test
    public void list_returnsProperValue() {
        GeneralJournalBatchList list = controller.list(TEST_PAGE);

        assertEquals(dtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void get_delegatesToService() {
        controller.get(TEST_CODE);

        verify(svcMock).get(TEST_CODE);
        verify(svcMock).toDTO(generalJournalBatchMock);
    }

    @Test
    public void post_delegatesToService() {
        controller.post(dtoMock);

        verify(svcMock).create(generalJournalBatchMock);
    }

    @Test
    public void put_delegatesToService() {
        controller.put(TEST_CODE, dtoMock);

        verify(svcMock).update(TEST_CODE, generalJournalBatchMock);
    }

    @Test
    public void delete_delegatesToService() {
        controller.delete(TEST_CODE);

        verify(svcMock).delete(TEST_CODE);
    }

    @Test
    public void listLines_returnsProperValue() {
        GeneralJournalBatchLineList list = controller.listLines(TEST_CODE, TEST_PAGE);

        assertEquals(generalJournalBatchLineDtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void getLine_delegatesToService() {
        controller.getLine(TEST_CODE, TEST_LINE_NO);

        GeneralJournalBatchLineId generalJournalBatchLineId = new GeneralJournalBatchLineId();
        generalJournalBatchLineId.setGeneralJournalBatch(generalJournalBatchMock);
        generalJournalBatchLineId.setLineNo(TEST_LINE_NO);

        verify(generalJournalBatchLineSvcMock).get(generalJournalBatchLineId);
        verify(generalJournalBatchLineSvcMock).toDTO(generalJournalBatchLineMock);
    }

    @Test
    public void postLine_delegatesToService() {
        controller.postLine(TEST_CODE, generalJournalBatchLineDtoMock);

        verify(generalJournalBatchLineSvcMock).create(generalJournalBatchLineMock);

        verify(generalJournalBatchLineMock)
                .setGeneralJournalBatchLineId(generalJournalBatchLineIdArgumentCaptor.capture());

        GeneralJournalBatchLineId generalJournalBatchLineId = generalJournalBatchLineIdArgumentCaptor.getValue();

        assertEquals(generalJournalBatchMock, generalJournalBatchLineId.getGeneralJournalBatch());
        assertEquals(TEST_MAX_LINENO + 1, generalJournalBatchLineId.getLineNo());
    }

    @Test
    public void putLine_delegatesToService() {
        controller.putLine(TEST_CODE, TEST_LINE_NO, generalJournalBatchLineDtoMock);

        GeneralJournalBatchLineId generalJournalBatchLineId = new GeneralJournalBatchLineId();
        generalJournalBatchLineId.setGeneralJournalBatch(generalJournalBatchMock);
        generalJournalBatchLineId.setLineNo(TEST_LINE_NO);

        verify(generalJournalBatchLineSvcMock).update(generalJournalBatchLineId, generalJournalBatchLineMock);
    }

    @Test
    public void deleteLine_delegatesToService() {
        controller.deleteLine(TEST_CODE, TEST_LINE_NO);

        GeneralJournalBatchLineId generalJournalBatchLineId = new GeneralJournalBatchLineId();
        generalJournalBatchLineId.setGeneralJournalBatch(generalJournalBatchMock);
        generalJournalBatchLineId.setLineNo(TEST_LINE_NO);

        verify(generalJournalBatchLineSvcMock).delete(generalJournalBatchLineId);
    }

    @Test
    public void operationPost_updatesPostStatus_andSendsMessage() throws PostFailedException {
        ResponseEntity<String> response = controller.operationPost(TEST_CODE);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(generalJournalBatchMock).setPostStatus(DocumentPostStatus.SCHEDULED);
        verify(svcMock).update(TEST_CODE, generalJournalBatchMock);

        verify(rabbitMQClientMock).convertAndSend(POST_OPERATION_EXCHANGE_NAME, POST_OPERATION_ROUTING_KEY, TEST_CODE);
    }

    @Test
    public void operationPost_returnsBadRequest_whenPostingFails() throws PostFailedException {
        doThrow(new AmqpException(TEST_FAILED_MSG)).when(rabbitMQClientMock)
                .convertAndSend(POST_OPERATION_EXCHANGE_NAME, POST_OPERATION_ROUTING_KEY, TEST_CODE);

        ResponseEntity<String> response = controller.operationPost(TEST_CODE);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(TEST_FAILED_MSG, response.getBody());
    }
}
