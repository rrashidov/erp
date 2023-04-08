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
import org.roko.erp.backend.model.PurchaseOrder;
import org.roko.erp.backend.services.exc.PostFailedException;

public class AsyncPurchaseOrderPostServiceTest {
    
    private static final String TEST_POST_FAILED_MSG = "test-post-failed-msg";

    private static final String TEST_CODE = "test-code";

    @Mock
    private PurchaseOrder purchaseOrderMock;

    @Mock
    private PurchaseOrderService purchaseOrderSvcMock;

    @Mock
    private PurchaseOrderPostService purchaseOrderPostSvcMock;

    private AsyncPurchaseOrderPostService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(purchaseOrderSvcMock.get(TEST_CODE)).thenReturn(purchaseOrderMock);

        svc = new AsyncPurchaseOrderPostServiceImpl(purchaseOrderSvcMock, purchaseOrderPostSvcMock);
    }

    @Test
    public void purchaseOrderPostStatusIsUpdated_whenPostingFails() throws PostFailedException {
        doThrow(new PostFailedException(TEST_POST_FAILED_MSG)).when(purchaseOrderPostSvcMock).post(TEST_CODE);

        svc.post(TEST_CODE);

        verify(purchaseOrderMock).setPostStatus(DocumentPostStatus.FAILED);
        verify(purchaseOrderMock).setPostStatusReason(TEST_POST_FAILED_MSG);

        verify(purchaseOrderSvcMock).update(TEST_CODE, purchaseOrderMock);
    }

    @Test
    public void purchaseOrderPostStatusIsNotUpdated_whenPostingSucceeds(){
        svc.post(TEST_CODE);

        verify(purchaseOrderSvcMock, never()).update(TEST_CODE, purchaseOrderMock);
    }
}
