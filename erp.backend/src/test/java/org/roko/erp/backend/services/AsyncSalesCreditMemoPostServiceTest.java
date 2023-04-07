package org.roko.erp.backend.services;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.DocumentPostStatus;
import org.roko.erp.backend.model.SalesCreditMemo;
import org.roko.erp.backend.services.exc.PostFailedException;

public class AsyncSalesCreditMemoPostServiceTest {

    private static final String TEST_CODE = "test-code";

    private static final String TEST_MSG = "test-msg";

    @Mock
    private SalesCreditMemo salesCreditMemoMock;

    @Mock
    private SalesCreditMemoService salesCreditMemoSvcMock;

    @Mock
    private SalesCreditMemoPostService salesCreditMemoPostSvcMock;
    
    private AsyncSalesCreditMemoPostService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(salesCreditMemoSvcMock.get(TEST_CODE)).thenReturn(salesCreditMemoMock);

        svc = new AsyncSalesCreditMemoPostServiceImpl(salesCreditMemoSvcMock, salesCreditMemoPostSvcMock);
    }

    @Test
    public void salesCreditMemoPostStatusIsUpdated_whenPostingFails() throws PostFailedException {
        doThrow(new PostFailedException(TEST_MSG)).when(salesCreditMemoPostSvcMock).post(TEST_CODE);

        svc.post(TEST_CODE);

        verify(salesCreditMemoSvcMock).update(TEST_CODE, salesCreditMemoMock);

        verify(salesCreditMemoMock).setPostStatus(DocumentPostStatus.FAILED);
        verify(salesCreditMemoMock).setPostStatusReason(TEST_MSG);
    }

    @Test
    public void salesCreditMemoPostStatusIsNotUpdated_whenPostingSucceeds() {
        svc.post(TEST_CODE);

        verify(salesCreditMemoSvcMock, never()).update(TEST_CODE, salesCreditMemoMock);

        verify(salesCreditMemoMock, never()).setPostStatus(DocumentPostStatus.FAILED);
        verify(salesCreditMemoMock, never()).setPostStatusReason(TEST_MSG);
    }
}
