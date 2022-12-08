package org.roko.erp.backend.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.SalesCreditMemo;
import org.roko.erp.backend.services.SalesCreditMemoService;
import org.roko.erp.model.dto.SalesDocumentDTO;

public class SalesCreditMemoControllerTest {
    
    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 123;

    @Mock
    private SalesCreditMemoService svcMock;

    @Mock
    private SalesCreditMemo salesCreditMemoMock;

    @Mock
    private SalesDocumentDTO dtoMock;

    private SalesCreditMemoController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(svcMock.fromDTO(dtoMock)).thenReturn(salesCreditMemoMock);

        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(salesCreditMemoMock));
        when(svcMock.get(TEST_CODE)).thenReturn(salesCreditMemoMock);

        controller = new SalesCreditMemoController(svcMock);
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
