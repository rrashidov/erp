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
import org.roko.erp.backend.model.PurchaseCreditMemo;
import org.roko.erp.backend.services.PurchaseCreditMemoLineService;
import org.roko.erp.backend.services.PurchaseCreditMemoService;

public class PurchaseCreditMemoPolicyTest {

    private static final String NOT_FOUND_TMPL = "Purchase Credit Memo %s not found";
    private static final String POST_SCHEDULED_TMPL = "Purchase Credit Memo %s is scheduled for posting";
    private static final String HAS_LINES_TMPL = "Purchase Credit Memo %s has lines";

    private static final String NON_EXISTING_CODE = "non-existing-code";

    private static final String TEST_CODE = "test-code";

    @Mock
    private PurchaseCreditMemo purchaseCreditMemoMock;

    @Mock
    private PurchaseCreditMemoService purchaseCreditMemoSvcMock;

    @Mock
    private PurchaseCreditMemoLineService purchaseCreditMemoLineSvcMock;

    private PurchaseCreditMemoPolicy policy;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(purchaseCreditMemoMock.getPostStatus()).thenReturn(DocumentPostStatus.READY);

        when(purchaseCreditMemoSvcMock.get(TEST_CODE)).thenReturn(purchaseCreditMemoMock);

        when(purchaseCreditMemoLineSvcMock.count(purchaseCreditMemoMock)).thenReturn(0);

        policy = new PurchaseCreditMemoPolicy(purchaseCreditMemoSvcMock, purchaseCreditMemoLineSvcMock);
    }

    @Test
    public void resultIsFalse_whenPurchaseCreditMemoNotFound() {
        PolicyResult result = policy.canDelete(NON_EXISTING_CODE);

        assertFalse(result.getResult());
        assertEquals(String.format(NOT_FOUND_TMPL, NON_EXISTING_CODE), result.getText());
    }

    @Test
    public void resultIsFalse_whenPurchaseCreditMemoScheduledForPosting() {
        when(purchaseCreditMemoMock.getPostStatus()).thenReturn(DocumentPostStatus.SCHEDULED);

        PolicyResult result = policy.canDelete(TEST_CODE);

        assertFalse(result.getResult());
        assertEquals(String.format(POST_SCHEDULED_TMPL, TEST_CODE), result.getText());
    }

    @Test
    public void resultIsFalse_whenLinesExist() {
        when(purchaseCreditMemoLineSvcMock.count(purchaseCreditMemoMock)).thenReturn(1);

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
