package org.roko.erp.backend.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.PostedPurchaseCreditMemo;
import org.roko.erp.backend.services.PostedPurchaseCreditMemoService;

public class PostedPurchaseCreditMemoControllerTest {
    
    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 123;

    @Mock
    private PostedPurchaseCreditMemoService svcMock;

    @Mock
    private PostedPurchaseCreditMemo postedPurchaseCreditMemoMock;

    private PostedPurchaseCreditMemoController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(postedPurchaseCreditMemoMock));
        when(svcMock.get(TEST_CODE)).thenReturn(postedPurchaseCreditMemoMock);

        controller = new PostedPurchaseCreditMemoController(svcMock);
    }

    @Test
    public void list_delegatesToService() {
        controller.list(TEST_PAGE);

        verify(svcMock).list(TEST_PAGE);
        verify(svcMock).toDTO(postedPurchaseCreditMemoMock);
    }

    @Test
    public void get_delegatesToService() {
        controller.get(TEST_CODE);

        verify(svcMock).get(TEST_CODE);
        verify(svcMock).toDTO(postedPurchaseCreditMemoMock);
    }
}
