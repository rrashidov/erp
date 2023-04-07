package org.roko.erp.backend.beans.rabbitmq;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.services.AsyncSalesCreditMemoPostService;

public class SalesCreditMemoPostReceiverTest {

    private static final String TEST_MESSAGE = "test-message";

    @Mock
    private AsyncSalesCreditMemoPostService svcMock;
    
    private SalesCreditMemoPostReceiver salesCreditMemoPostReceiver;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        
        salesCreditMemoPostReceiver = new SalesCreditMemoPostReceiver(svcMock);
    }

    @Test
    public void svcIsUsed_whenProcessingReceivedMessage(){
        salesCreditMemoPostReceiver.receiveMessage(TEST_MESSAGE);

        verify(svcMock).post(TEST_MESSAGE);
    }
}
