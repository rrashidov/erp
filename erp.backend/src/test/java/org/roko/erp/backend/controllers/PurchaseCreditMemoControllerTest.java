package org.roko.erp.backend.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.PurchaseCreditMemo;
import org.roko.erp.backend.model.PurchaseCreditMemoLine;
import org.roko.erp.backend.model.jpa.PurchaseCreditMemoLineId;
import org.roko.erp.backend.services.PurchaseCreditMemoLineService;
import org.roko.erp.backend.services.PurchaseCreditMemoService;
import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.dto.PurchaseDocumentLineDTO;

public class PurchaseCreditMemoControllerTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 123;

    private static final int TEST_LINE_NO = 23;

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

    private PurchaseCreditMemoController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        PurchaseCreditMemoLineId purchaseCreditMemoLineId = new PurchaseCreditMemoLineId();
        purchaseCreditMemoLineId.setPurchaseCreditMemo(purchaseCreditMemoMock);
        purchaseCreditMemoLineId.setLineNo(TEST_LINE_NO);

        when(purchaseCreditMemoLineSvcMock.list(purchaseCreditMemoMock, TEST_PAGE))
                .thenReturn(Arrays.asList(purchaseCreditMemoLineMock));
        when(purchaseCreditMemoLineSvcMock.get(purchaseCreditMemoLineId)).thenReturn(purchaseCreditMemoLineMock);
        when(purchaseCreditMemoLineSvcMock.fromDTO(purchaseCreditMemoLineDtoMock))
                .thenReturn(purchaseCreditMemoLineMock);

        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(purchaseCreditMemoMock));
        when(svcMock.get(TEST_CODE)).thenReturn(purchaseCreditMemoMock);
        when(svcMock.fromDTO(dtoMock)).thenReturn(purchaseCreditMemoMock);

        controller = new PurchaseCreditMemoController(svcMock, purchaseCreditMemoLineSvcMock);
    }

    @Test
    public void list_delegatesToService() {
        controller.list(TEST_PAGE);

        verify(svcMock).list(TEST_PAGE);
        verify(svcMock).toDTO(purchaseCreditMemoMock);
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
    public void listLines_delegatesToService() {
        controller.listLines(TEST_CODE, TEST_PAGE);

        verify(svcMock).get(TEST_CODE);
        verify(purchaseCreditMemoLineSvcMock).list(purchaseCreditMemoMock, TEST_PAGE);
        verify(purchaseCreditMemoLineSvcMock).toDTO(purchaseCreditMemoLineMock);
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
}
