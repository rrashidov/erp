package org.roko.erp.backend.controllers.policy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.DocumentPostStatus;
import org.roko.erp.backend.model.GeneralJournalBatch;
import org.roko.erp.backend.services.GeneralJournalBatchLineService;
import org.roko.erp.backend.services.GeneralJournalBatchService;

public class GeneralJournalBatchPolicyTest {

    private static final String NOT_FOUND_TMPL = "General Journal Batch %s not found";
    private static final String SCHEDULED_FOR_POST_TMPL = "General Journal Batch %s is scheduled for posting";
    private static final String HAS_LINES_TMPL = "General Journal Batch %s has lines";

    private static final String NON_EXISTING_CODE = "non-existing-code";

    private static final String TEST_CODE = "test-code";

    @Mock
    private GeneralJournalBatch generalJournalBatchMock;

    @Mock
    private GeneralJournalBatchService generalJournalBatchSvcMock;

    @Mock
    private GeneralJournalBatchLineService generalJournalBatchLineSvcMock;

    private GeneralJournalBatchPolicy policy;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(generalJournalBatchMock.getPostStatus()).thenReturn(DocumentPostStatus.READY);

        when(generalJournalBatchSvcMock.get(TEST_CODE)).thenReturn(generalJournalBatchMock);

        policy = new GeneralJournalBatchPolicy(generalJournalBatchSvcMock, generalJournalBatchLineSvcMock);
    }

    @Test
    public void resultIsFalse_whenGeneralJournalBatchNotFound() {
        PolicyResult result = policy.canDelete(NON_EXISTING_CODE);

        assertFalse(result.getResult());
        assertEquals(String.format(NOT_FOUND_TMPL, NON_EXISTING_CODE), result.getText());
    }

    @Test
    public void resultIsFalse_whenGeneralJournalBatchScheduledForPosting() {
        when(generalJournalBatchMock.getPostStatus()).thenReturn(DocumentPostStatus.SCHEDULED);

        PolicyResult result = policy.canDelete(TEST_CODE);

        assertFalse(result.getResult());
        assertEquals(String.format(SCHEDULED_FOR_POST_TMPL, TEST_CODE), result.getText());
    }

    @Test
    public void resultIsFalse_whenGeneralJournalBatchHasLines() {
        when(generalJournalBatchLineSvcMock.count(generalJournalBatchMock)).thenReturn(1l);

        PolicyResult result = policy.canDelete(TEST_CODE);

        assertFalse(result.getResult());
        assertEquals(String.format(HAS_LINES_TMPL, TEST_CODE), result.getText());
    }

    @Test
    public void resultIsTrue_whenEverythingIsOK() {
        PolicyResult result = policy.canDelete(TEST_CODE);

        assertTrue(result.getResult());
        assertEquals("", result.getText());
    }
}
