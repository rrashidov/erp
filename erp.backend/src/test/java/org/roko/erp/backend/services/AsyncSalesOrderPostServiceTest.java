package org.roko.erp.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.DocumentPostStatus;
import org.roko.erp.backend.model.SalesOrder;
import org.roko.erp.backend.services.exc.PostFailedException;

public class AsyncSalesOrderPostServiceTest {

    private static final String TEST_CODE = "test-code";

    private static final String TEST_POST_FAILED_MSG = "test-post-failed-msg";

    @Mock
    private SalesOrderPostService salesOrderPostSvcMock;

    @Mock
    private SalesOrderService salesOrderSvcMock;

    @Captor
    private ArgumentCaptor<SalesOrder> salesOrderArgumentCaptor;

    @Mock
    private SalesOrder salesOrderMock;

    private AsyncSalesOrderPostService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(salesOrderSvcMock.get(TEST_CODE)).thenReturn(salesOrderMock);

        svc = new AsyncSalesOrderPostServiceImpl(salesOrderSvcMock, salesOrderPostSvcMock);
    }

    @Test
    public void salesOrderPostStatusIsUpdated_whenPostingFails() throws PostFailedException {
        doThrow(new PostFailedException(TEST_POST_FAILED_MSG)).when(salesOrderPostSvcMock).post(TEST_CODE);

        svc.post(TEST_CODE);

        verify(salesOrderSvcMock).update(TEST_CODE, salesOrderMock);

        verify(salesOrderMock).setPostStatus(DocumentPostStatus.FAILED);
        verify(salesOrderMock).setPostStatusReason(TEST_POST_FAILED_MSG);
    }

    @Test
    public void salesOrderPostStatusIsNotUpdated_whenPostingSucceeds(){
        svc.post(TEST_CODE);

        verify(salesOrderSvcMock, never()).update(TEST_CODE, salesOrderMock);
    }
}
