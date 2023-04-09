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
import org.roko.erp.backend.model.PurchaseCreditMemo;
import org.roko.erp.backend.services.exc.PostFailedException;

public class AsyncPurchaseCreditMemoPostServiceTest {

    private static final String TEST_CODE = "test-code";

    private static final String TEST_POST_FAILED_MSG = "test-post-failed-msg";

    @Mock
    private PurchaseCreditMemo purchaseCreditMemoMock;

    @Mock
    private PurchaseCreditMemoService purchaseCreditMemoSvcMock;

    @Mock
    private PurchaseCreditMemoPostService purchaseCreditMemoPostSvcMock;

    private AsyncPurchaseCreditMemoPostService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(purchaseCreditMemoSvcMock.get(TEST_CODE)).thenReturn(purchaseCreditMemoMock);

        svc = new AsyncPurchaseCreditMemoPostServiceImpl(purchaseCreditMemoSvcMock, purchaseCreditMemoPostSvcMock);
    }

    @Test
    public void purchaseCreditMemoPostStatusIsUpdated_whenPostingFails() throws PostFailedException {
        doThrow(new PostFailedException(TEST_POST_FAILED_MSG)).when(purchaseCreditMemoPostSvcMock).post(TEST_CODE);

        svc.post(TEST_CODE);

        verify(purchaseCreditMemoMock).setPostStatus(DocumentPostStatus.FAILED);
        verify(purchaseCreditMemoMock).setPostStatusReason(TEST_POST_FAILED_MSG);

        verify(purchaseCreditMemoSvcMock).update(TEST_CODE, purchaseCreditMemoMock);
    }

    @Test
    public void purchaseCreditMemoPostStatusIsNotUpdated_whenPostingSucceeds() {
        svc.post(TEST_CODE);

        verify(purchaseCreditMemoSvcMock, never()).update(TEST_CODE, purchaseCreditMemoMock);
    }
}
