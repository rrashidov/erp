package org.roko.erp.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.PostedSalesCreditMemo;
import org.roko.erp.backend.model.PostedSalesCreditMemoLine;
import org.roko.erp.backend.services.PostedSalesCreditMemoLineService;
import org.roko.erp.backend.services.PostedSalesCreditMemoService;
import org.roko.erp.dto.PostedSalesDocumentDTO;
import org.roko.erp.dto.PostedSalesDocumentLineDTO;
import org.roko.erp.dto.list.PostedSalesDocumentLineList;
import org.roko.erp.dto.list.PostedSalesDocumentList;

public class PostedSalesCreditMemoControllerTest {

    private static final long TEST_COUNT = 222;

    private static final int TEST_PAGE = 123;

    private static final String TEST_CODE = "test-code";

    @Mock
    private PostedSalesCreditMemoService svcMock;

    @Mock
    private PostedSalesCreditMemo postedSalesCreditMemoMock;

    @Mock
    private PostedSalesCreditMemoLineService postedSalesCreditMemoLineSvcMock;

    @Mock
    private PostedSalesCreditMemoLine postedSalesCreditMemoLineMock;

    @Mock
    private PostedSalesDocumentDTO dtoMock;

    @Mock
    private PostedSalesDocumentLineDTO lineDtoMock;

    private PostedSalesCreditMemoController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(postedSalesCreditMemoLineSvcMock.list(postedSalesCreditMemoMock, TEST_PAGE)).thenReturn(Arrays.asList(postedSalesCreditMemoLineMock));
        when(postedSalesCreditMemoLineSvcMock.toDTO(postedSalesCreditMemoLineMock)).thenReturn(lineDtoMock);
        when(postedSalesCreditMemoLineSvcMock.count(postedSalesCreditMemoMock)).thenReturn(TEST_COUNT);

        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(postedSalesCreditMemoMock));
        when(svcMock.get(TEST_CODE)).thenReturn(postedSalesCreditMemoMock);
        when(svcMock.toDTO(postedSalesCreditMemoMock)).thenReturn(dtoMock);
        when(svcMock.count()).thenReturn(TEST_COUNT);
        
        controller = new PostedSalesCreditMemoController(svcMock, postedSalesCreditMemoLineSvcMock);
    }

    @Test
    public void list_returnsProperValue() {
        PostedSalesDocumentList list = controller.list(TEST_PAGE);

        assertEquals(dtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void get_delegatesToService() {
        controller.get(TEST_CODE);

        verify(svcMock).get(TEST_CODE);
        verify(svcMock).toDTO(postedSalesCreditMemoMock);
    }

    @Test
    public void listLines_returnsProperValue() {
        PostedSalesDocumentLineList list = controller.listLines(TEST_CODE, TEST_PAGE);

        assertEquals(lineDtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }
}
