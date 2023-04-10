package org.roko.erp.backend.beans.rabbitmq;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.services.AsyncPurchaseCreditMemoPostService;

public class PurchaseCreditMemoPostReceiverTest {

    private static final String TEST_CODE = "test-code";

    @Mock
    private AsyncPurchaseCreditMemoPostService svcMock;
    
    private PurchaseCreditMemoPostReceiver purchaseCreditMemoPostReceiver;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        purchaseCreditMemoPostReceiver = new PurchaseCreditMemoPostReceiver(svcMock);
    }

    @Test
    public void svcIsUsed_whenProcessingReceivedMessage() {
        purchaseCreditMemoPostReceiver.receiveMessage(TEST_CODE);

        verify(svcMock).post(TEST_CODE);
    }
}
