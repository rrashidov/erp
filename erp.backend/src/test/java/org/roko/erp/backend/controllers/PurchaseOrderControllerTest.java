package org.roko.erp.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.PurchaseOrder;
import org.roko.erp.backend.model.PurchaseOrderLine;
import org.roko.erp.backend.model.jpa.PurchaseOrderLineId;
import org.roko.erp.backend.services.PurchaseCodeSeriesService;
import org.roko.erp.backend.services.PurchaseOrderLineService;
import org.roko.erp.backend.services.PurchaseOrderService;
import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.dto.PurchaseDocumentLineDTO;
import org.roko.erp.dto.list.PurchaseDocumentLineList;
import org.roko.erp.dto.list.PurchaseDocumentList;

public class PurchaseOrderControllerTest {
    
    private static final int TEST_MAX_LINE_NO = 345;

    private static final String NEW_CODE = "new-code";

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 123;

    private static final int TEST_LINE_NO = 1234;

    private static final int TEST_COUNT = 222;

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

    private PurchaseOrderController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(purchaseCodeSeriesSvcMock.orderCode()).thenReturn(NEW_CODE);

        PurchaseOrderLineId purchaseOrderLineId = new PurchaseOrderLineId();
        purchaseOrderLineId.setPurchaseOrder(purchaseOrderMock);
        purchaseOrderLineId.setLineNo(TEST_LINE_NO);

        when(purchaseOrderLineSvcMock.list(purchaseOrderMock, TEST_PAGE)).thenReturn(Arrays.asList(purchaseOrderLineMock));
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

        controller = new PurchaseOrderController(svcMock, purchaseOrderLineSvcMock, purchaseCodeSeriesSvcMock);
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
    public void delete_delegatesToService() {
        controller.delete(TEST_CODE);

        verify(svcMock).delete(TEST_CODE);
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
}
