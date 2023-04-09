package org.roko.erp.backend.beans.rabbitmq;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.services.AsyncPurchaseOrderPostService;

public class PurchaseOrderPostReceiverTest {

    private static final String TEST_CODE = "test-code";

    @Mock
    private AsyncPurchaseOrderPostService svcMock;

    private PurchaseOrderPostReceiver purchaseOrderPostReceiver;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        purchaseOrderPostReceiver = new PurchaseOrderPostReceiver(svcMock);
    }
    
    @Test
    public void svcIsUsed_whenProcessingReceivedMessage() {
        purchaseOrderPostReceiver.receiveMessage(TEST_CODE);

        verify(svcMock).post(TEST_CODE);
    }
}
