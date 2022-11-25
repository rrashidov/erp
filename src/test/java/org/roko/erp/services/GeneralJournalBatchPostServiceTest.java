package org.roko.erp.services;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.GeneralJournalBatch;

public class GeneralJournalBatchPostServiceTest {
    
    private static final String TEST_CODE = "test-code";

    @Mock
    private GeneralJournalBatchService generalJournalBatchSvcMock;

    @Mock
    private GeneralJournalBatch generalJournalBatchMock;

    @Mock
    private GeneralJournalBatchLineService generalJournalBatchLineSvcMock;

    private GeneralJournalBatchPostService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(generalJournalBatchSvcMock.get(TEST_CODE)).thenReturn(generalJournalBatchMock);

        when(generalJournalBatchLineSvcMock.list(generalJournalBatchMock)).then

        svc = new GeneralJournalBatchPostServiceImpl(generalJournalBatchSvcMock, generalJournalBatchLineSvcMock);
    }

    @Test
    public void customerLedgerEntryIsCreated_whenSourceTypeCustomer() throws PostFailedException{
        svc.post(TEST_CODE);
    }
}
