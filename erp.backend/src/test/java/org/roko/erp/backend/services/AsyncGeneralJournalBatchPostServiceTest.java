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
import org.roko.erp.backend.model.GeneralJournalBatch;
import org.roko.erp.backend.services.exc.PostFailedException;

public class AsyncGeneralJournalBatchPostServiceTest {

    private static final String TEST_CODE = "test-code";

    private static final String TEST_POST_FAILED_MSG = "test-post-failed-msg";

    @Mock
    private GeneralJournalBatch generalJournalBatchMock;

    @Mock
    private GeneralJournalBatchService generalJournalBatchSvcMock;

    @Mock
    private GeneralJournalBatchPostService generalJournalBatchPostSvcMock;

    private AsyncGeneralJournalBatchPostService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(generalJournalBatchSvcMock.get(TEST_CODE)).thenReturn(generalJournalBatchMock);

        svc = new AsyncGeneralJournalBatchPostServiceImpl(generalJournalBatchSvcMock, generalJournalBatchPostSvcMock);
    }

    @Test
    public void generalJournalBatchPostStatusIsUpdated_whenPostingFails() throws PostFailedException {
        doThrow(new PostFailedException(TEST_POST_FAILED_MSG)).when(generalJournalBatchPostSvcMock).post(TEST_CODE);

        svc.post(TEST_CODE);

        verify(generalJournalBatchMock).setPostStatus(DocumentPostStatus.FAILED);
        verify(generalJournalBatchMock).setPostStatusReason(TEST_POST_FAILED_MSG);

        verify(generalJournalBatchSvcMock).update(TEST_CODE, generalJournalBatchMock);
    }

    @Test
    public void generalJournalBatchPostStatusIsNotUpdated_whenPostingSucceeds() {
        svc.post(TEST_CODE);

        verify(generalJournalBatchSvcMock, never()).update(TEST_CODE, generalJournalBatchMock);
    }
}
