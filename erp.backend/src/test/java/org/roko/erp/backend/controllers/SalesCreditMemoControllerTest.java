package org.roko.erp.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.SalesCreditMemo;
import org.roko.erp.backend.model.SalesCreditMemoLine;
import org.roko.erp.backend.model.jpa.SalesCreditMemoLineId;
import org.roko.erp.backend.services.SalesCreditMemoLineService;
import org.roko.erp.backend.services.SalesCreditMemoService;
import org.roko.erp.model.dto.SalesDocumentDTO;
import org.roko.erp.model.dto.SalesDocumentLineDTO;

public class SalesCreditMemoControllerTest {
    
    private static final int TEST_LINE_NO = 1234;

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 123;

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

    private SalesCreditMemoController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(salesCreditMemoLineSvcMock.list(salesCreditMemoMock, TEST_PAGE)).thenReturn(Arrays.asList(salesCreditMemoLineMock));
        when(salesCreditMemoLineSvcMock.fromDTO(salesDocumentLineDtoMock)).thenReturn(salesCreditMemoLineMock);

        when(svcMock.fromDTO(dtoMock)).thenReturn(salesCreditMemoMock);

        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(salesCreditMemoMock));
        when(svcMock.get(TEST_CODE)).thenReturn(salesCreditMemoMock);

        controller = new SalesCreditMemoController(svcMock, salesCreditMemoLineSvcMock);
    }

    @Test
    public void list_delegatesToService() {
        controller.list(TEST_PAGE);

        verify(svcMock).list(TEST_PAGE);
        verify(svcMock).toDTO(salesCreditMemoMock);
    }

    @Test
    public void get_delegatesToService() {
        controller.get(TEST_CODE);

        verify(svcMock).get(TEST_CODE);
        verify(svcMock).toDTO(salesCreditMemoMock);
    }

    @Test
    public void listLines_delegatesToService() {
        controller.listLines(TEST_CODE, TEST_PAGE);

        verify(salesCreditMemoLineSvcMock).list(salesCreditMemoMock, TEST_PAGE);
        verify(salesCreditMemoLineSvcMock).toDTO(salesCreditMemoLineMock);
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
    }

    @Test
    public void putLine_delegatesToService() {
        controller.putLine(TEST_CODE, TEST_LINE_NO, salesDocumentLineDtoMock);

        verify(salesCreditMemoLineSvcMock).update(salesCreditMemoLineIdArgumentCaptor.capture(), eq(salesCreditMemoLineMock));

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

        verify(svcMock).create(salesCreditMemoMock);
    }

    @Test
    public void put_delegatesToService() {
        controller.put(TEST_CODE, dtoMock);

        verify(svcMock).update(TEST_CODE, salesCreditMemoMock);
    }

    @Test
    public void delete_delegatesToService() {
        controller.delete(TEST_CODE);

        verify(svcMock).delete(TEST_CODE);
    }
}
