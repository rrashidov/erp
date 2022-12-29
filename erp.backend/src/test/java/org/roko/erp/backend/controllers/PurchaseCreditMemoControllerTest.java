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
import org.roko.erp.backend.model.PurchaseCreditMemo;
import org.roko.erp.backend.model.PurchaseCreditMemoLine;
import org.roko.erp.backend.model.jpa.PurchaseCreditMemoLineId;
import org.roko.erp.backend.services.PurchaseCodeSeriesService;
import org.roko.erp.backend.services.PurchaseCreditMemoLineService;
import org.roko.erp.backend.services.PurchaseCreditMemoPostService;
import org.roko.erp.backend.services.PurchaseCreditMemoService;
import org.roko.erp.backend.services.exc.PostFailedException;
import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.dto.PurchaseDocumentLineDTO;
import org.roko.erp.dto.list.PurchaseDocumentLineList;
import org.roko.erp.dto.list.PurchaseDocumentList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PurchaseCreditMemoControllerTest {

    private static final String TEST_POST_FAILED_EXCEPTION = "test-post-failed-exception";

    private static final int TEST_COUNT = 222;

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 123;

    private static final int TEST_LINE_NO = 23;

    private static final int TEST_MAX_LINE_NO = 234;

    private static final String TEST_NEW_CODE = "test-new-code";

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
    private PurchaseCreditMemoPostService purchaseCreditMemoPostSvcMock;

    private PurchaseCreditMemoController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

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
                purchaseCreditMemoPostSvcMock);
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
    public void delete_delegatesToService() {
        controller.delete(TEST_CODE);

        verify(svcMock).delete(TEST_CODE);
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
    public void postLine_delegatesToService() {
        controller.postLine(TEST_CODE, purchaseCreditMemoLineDtoMock);

        verify(purchaseCreditMemoLineSvcMock).create(purchaseCreditMemoLineMock);

        verify(purchaseCreditMemoLineMock)
                .setPurchaseCreditMemoLineId(purchaseCreditMemoLineIdArgumentCaptor.capture());

        PurchaseCreditMemoLineId purchaseCreditMemoLineId = purchaseCreditMemoLineIdArgumentCaptor.getValue();

        assertEquals(purchaseCreditMemoMock, purchaseCreditMemoLineId.getPurchaseCreditMemo());
        assertEquals(TEST_MAX_LINE_NO + 1, purchaseCreditMemoLineId.getLineNo());
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
    public void operationPost_delegatesToService() throws PostFailedException {
        ResponseEntity<String> response = controller.operationPost(TEST_CODE);

        verify(purchaseCreditMemoPostSvcMock).post(TEST_CODE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void operationPost_returnsBadRequest_whenPostThrowsException() throws PostFailedException {
        doThrow(new PostFailedException(TEST_POST_FAILED_EXCEPTION)).when(purchaseCreditMemoPostSvcMock).post(TEST_CODE);

        ResponseEntity<String> response = controller.operationPost(TEST_CODE);

        verify(purchaseCreditMemoPostSvcMock).post(TEST_CODE);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(TEST_POST_FAILED_EXCEPTION, response.getBody());
    }
}
