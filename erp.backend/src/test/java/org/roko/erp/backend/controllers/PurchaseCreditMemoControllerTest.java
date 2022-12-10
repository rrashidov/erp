package org.roko.erp.backend.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.PurchaseCreditMemo;
import org.roko.erp.backend.services.PurchaseCreditMemoService;
import org.roko.erp.model.dto.PurchaseDocumentDTO;

public class PurchaseCreditMemoControllerTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 123;

    @Mock
    private PurchaseCreditMemo purchaseCreditMemoMock;

    @Mock
    private PurchaseCreditMemoService svcMock;

    @Mock
    private PurchaseDocumentDTO dtoMock;

    private PurchaseCreditMemoController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(purchaseCreditMemoMock));
        when(svcMock.get(TEST_CODE)).thenReturn(purchaseCreditMemoMock);
        when(svcMock.fromDTO(dtoMock)).thenReturn(purchaseCreditMemoMock);

        controller = new PurchaseCreditMemoController(svcMock);
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
}
