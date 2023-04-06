package org.roko.erp.backend.beans.rabbitmq;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.services.AsyncSalesOrderPostService;

public class SalesOrderPostReceiverTest {

    private static final String TEST_MSG_CONTENT = "test-msg-content";

    @Mock
    private AsyncSalesOrderPostService svcMock;

    private SalesOrderPostReceiver salesOrderPostReceiver;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        salesOrderPostReceiver = new SalesOrderPostReceiver(svcMock);
    }

    @Test
    public void svcIsUsed_whenProcessingReceivedMessage() {
        salesOrderPostReceiver.receiveMessage(TEST_MSG_CONTENT);

        verify(svcMock).post(TEST_MSG_CONTENT);
    }
}
