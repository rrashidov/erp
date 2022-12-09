package org.roko.erp.backend.controllers;

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

public class PostedSalesCreditMemoControllerTest {

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

    private PostedSalesCreditMemoController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(postedSalesCreditMemoLineSvcMock.list(postedSalesCreditMemoMock, TEST_PAGE)).thenReturn(Arrays.asList(postedSalesCreditMemoLineMock));

        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(postedSalesCreditMemoMock));
        when(svcMock.get(TEST_CODE)).thenReturn(postedSalesCreditMemoMock);
        
        controller = new PostedSalesCreditMemoController(svcMock, postedSalesCreditMemoLineSvcMock);
    }

    @Test
    public void list_delegatesToService() {
        controller.list(TEST_PAGE);

        verify(svcMock).list(TEST_PAGE);
        verify(svcMock).toDTO(postedSalesCreditMemoMock);
    }

    @Test
    public void get_delegatesToService() {
        controller.get(TEST_CODE);

        verify(svcMock).get(TEST_CODE);
        verify(svcMock).toDTO(postedSalesCreditMemoMock);
    }

    @Test
    public void listLines_delegatesToService() {
        controller.listLines(TEST_CODE, TEST_PAGE);

        verify(postedSalesCreditMemoLineSvcMock).list(postedSalesCreditMemoMock, TEST_PAGE);
        verify(postedSalesCreditMemoLineSvcMock).toDTO(postedSalesCreditMemoLineMock);
    }
}
