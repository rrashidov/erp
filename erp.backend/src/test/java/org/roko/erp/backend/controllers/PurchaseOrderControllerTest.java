package org.roko.erp.backend.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.PurchaseOrder;
import org.roko.erp.backend.model.PurchaseOrderLine;
import org.roko.erp.backend.model.jpa.PurchaseOrderLineId;
import org.roko.erp.backend.services.PurchaseOrderLineService;
import org.roko.erp.backend.services.PurchaseOrderService;
import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.dto.PurchaseDocumentLineDTO;

public class PurchaseOrderControllerTest {
    
    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 123;

    private static final int TEST_LINE_NO = 1234;

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

    private PurchaseOrderController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        PurchaseOrderLineId purchaseOrderLineId = new PurchaseOrderLineId();
        purchaseOrderLineId.setPurchaseOrder(purchaseOrderMock);
        purchaseOrderLineId.setLineNo(TEST_LINE_NO);

        when(purchaseOrderLineSvcMock.list(purchaseOrderMock, TEST_PAGE)).thenReturn(Arrays.asList(purchaseOrderLineMock));
        when(purchaseOrderLineSvcMock.get(purchaseOrderLineId)).thenReturn(purchaseOrderLineMock);
        when(purchaseOrderLineSvcMock.fromDTO(purchaseOrderLineDtoMock)).thenReturn(purchaseOrderLineMock);

        when(svcMock.list()).thenReturn(Arrays.asList(purchaseOrderMock));
        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(purchaseOrderMock));
        when(svcMock.get(TEST_CODE)).thenReturn(purchaseOrderMock);
        when(svcMock.toDTO(purchaseOrderMock)).thenReturn(dtoMock);
        when(svcMock.fromDTO(dtoMock)).thenReturn(purchaseOrderMock);

        controller = new PurchaseOrderController(svcMock, purchaseOrderLineSvcMock);
    }

    @Test
    public void list_delegatesToService() {
        controller.list();

        verify(svcMock).list();
        verify(svcMock).toDTO(purchaseOrderMock);
    }

    @Test
    public void listWithPage_delegatesToService() {
        controller.list(TEST_PAGE);

        verify(svcMock).list(TEST_PAGE);
        verify(svcMock).toDTO(purchaseOrderMock);
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
    }

    @Test
    public void put_delegatesToService() {
        controller.put(TEST_CODE, dtoMock);

        verify(svcMock).update(TEST_CODE, purchaseOrderMock);
    }

    @Test
    public void delete_delegatesToService() {
        controller.delete(TEST_CODE);

        verify(svcMock).delete(TEST_CODE);
    }

    @Test
    public void listLines_delegatesToService() {
        controller.listLines(TEST_CODE, TEST_PAGE);

        verify(purchaseOrderLineSvcMock).list(purchaseOrderMock, TEST_PAGE);
        verify(purchaseOrderLineSvcMock).toDTO(purchaseOrderLineMock);
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
}
