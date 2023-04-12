package org.roko.erp.backend.beans.rabbitmq;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.services.AsyncGeneralJournalBatchPostService;

public class GeneralJournalBatchPostReceiverTest {
    
    private static final String TEST_CODE = "test-code";

    @Mock
    private AsyncGeneralJournalBatchPostService svcMock;

    private GeneralJournalBatchPostReceiver generalJournalBatchPostReceiver;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        
        generalJournalBatchPostReceiver = new GeneralJournalBatchPostReceiver(svcMock);
    }

    @Test
    public void svcIsUsed_whenProcessingReceivedMessage() {
        generalJournalBatchPostReceiver.receiveMessage(TEST_CODE);

        verify(svcMock).post(TEST_CODE);
    }
}
