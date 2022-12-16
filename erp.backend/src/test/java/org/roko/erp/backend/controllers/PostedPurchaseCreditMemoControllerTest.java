package org.roko.erp.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.PostedPurchaseCreditMemo;
import org.roko.erp.backend.model.PostedPurchaseCreditMemoLine;
import org.roko.erp.backend.services.PostedPurchaseCreditMemoLineService;
import org.roko.erp.backend.services.PostedPurchaseCreditMemoService;
import org.roko.erp.dto.PostedPurchaseDocumentDTO;
import org.roko.erp.dto.PostedPurchaseDocumentLineDTO;
import org.roko.erp.dto.list.PostedPurchaseDocumentLineList;
import org.roko.erp.dto.list.PostedPurchaseDocumentList;

public class PostedPurchaseCreditMemoControllerTest {

    private static final int TEST_COUNT = 222;

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 123;

    @Mock
    private PostedPurchaseCreditMemoService svcMock;

    @Mock
    private PostedPurchaseCreditMemo postedPurchaseCreditMemoMock;

    @Mock
    private PostedPurchaseCreditMemoLineService postedPurchaseCreditMemoLineSvcMock;

    @Mock
    private PostedPurchaseCreditMemoLine postedPurchaseCreditMemoLineMock;

    @Mock
    private PostedPurchaseDocumentDTO dtoMock;

    @Mock
    private PostedPurchaseDocumentLineDTO lineDtoMock;

    private PostedPurchaseCreditMemoController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(postedPurchaseCreditMemoLineSvcMock.list(postedPurchaseCreditMemoMock, TEST_PAGE))
                .thenReturn(Arrays.asList(postedPurchaseCreditMemoLineMock));
        when(postedPurchaseCreditMemoLineSvcMock.toDTO(postedPurchaseCreditMemoLineMock)).thenReturn(lineDtoMock);
        when(postedPurchaseCreditMemoLineSvcMock.count(postedPurchaseCreditMemoMock)).thenReturn(TEST_COUNT);

        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(postedPurchaseCreditMemoMock));
        when(svcMock.get(TEST_CODE)).thenReturn(postedPurchaseCreditMemoMock);
        when(svcMock.toDTO(postedPurchaseCreditMemoMock)).thenReturn(dtoMock);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        controller = new PostedPurchaseCreditMemoController(svcMock, postedPurchaseCreditMemoLineSvcMock);
    }

    @Test
    public void list_delegatesToService() {
        PostedPurchaseDocumentList list = controller.list(TEST_PAGE);

        assertEquals(dtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void get_delegatesToService() {
        controller.get(TEST_CODE);

        verify(svcMock).get(TEST_CODE);
        verify(svcMock).toDTO(postedPurchaseCreditMemoMock);
    }

    @Test
    public void listLines_delegatesToService() {
        PostedPurchaseDocumentLineList list = controller.listLines(TEST_CODE, TEST_PAGE);

        assertEquals(lineDtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }
}
