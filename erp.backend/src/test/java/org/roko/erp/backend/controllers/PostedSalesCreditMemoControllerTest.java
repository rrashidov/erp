package org.roko.erp.backend.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.PostedSalesCreditMemo;
import org.roko.erp.backend.services.PostedSalesCreditMemoService;

public class PostedSalesCreditMemoControllerTest {

    private static final int TEST_PAGE = 123;

    private static final String TEST_CODE = "test-code";

    @Mock
    private PostedSalesCreditMemoService svcMock;

    @Mock
    private PostedSalesCreditMemo postedSalesCreditMemoMock;

    private PostedSalesCreditMemoController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(postedSalesCreditMemoMock));
        when(svcMock.get(TEST_CODE)).thenReturn(postedSalesCreditMemoMock);
        
        controller = new PostedSalesCreditMemoController(svcMock);
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
}
