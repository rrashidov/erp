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
import org.roko.erp.backend.model.PurchaseOrder;
import org.roko.erp.backend.services.PurchaseOrderLineService;
import org.roko.erp.backend.services.PurchaseOrderService;

public class PurchaseOrderPolicyTest {
    
    private static final String TEST_CODE = "test-code";

    private static final String NON_EXISTING_CODE = "non-existing-code";

    private static final String NOT_FOUND_TMPL = "Purchase Order %s not found";
    private static final String SCHEDULED_FOR_POSTING_TMPL = "Purchase Order %s is scheduled for posting";
    private static final String HAS_LINES_TMPL = "Purchase Order %s has lines";

    @Mock
    private PurchaseOrder purchaseOrderMock;

    @Mock
    private PurchaseOrderService purchaseOrderSvcMock;

    @Mock
    private PurchaseOrderLineService purchaseOrderLineSvcMock;

    private PurchaseOrderPolicy policy;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(purchaseOrderMock.getPostStatus()).thenReturn(DocumentPostStatus.READY);

        when(purchaseOrderSvcMock.get(TEST_CODE)).thenReturn(purchaseOrderMock);

        when(purchaseOrderLineSvcMock.count(purchaseOrderMock)).thenReturn(0l);

        policy = new PurchaseOrderPolicy(purchaseOrderSvcMock, purchaseOrderLineSvcMock);
    }

    @Test
    public void resultIsFalse_whenPurchaseOrderNotFound() {
        PolicyResult result = policy.canDelete(NON_EXISTING_CODE);

        assertFalse(result.getResult());
        assertEquals(String.format(NOT_FOUND_TMPL, NON_EXISTING_CODE), result.getText());
    }

    @Test
    public void resultIsFalse_whenPurchaseOrderScheduledForPosting() {
        when(purchaseOrderMock.getPostStatus()).thenReturn(DocumentPostStatus.SCHEDULED);

        PolicyResult result = policy.canDelete(TEST_CODE);

        assertFalse(result.getResult());
        assertEquals(String.format(SCHEDULED_FOR_POSTING_TMPL, TEST_CODE), result.getText());
    }

    @Test
    public void resultIsFalse_whenPurchaseOrderLinesExist() {
        when(purchaseOrderLineSvcMock.count(purchaseOrderMock)).thenReturn(1l);

        PolicyResult result = policy.canDelete(TEST_CODE);

        assertFalse(result.getResult());
        assertEquals(String.format(HAS_LINES_TMPL, TEST_CODE), result.getText());
    }

    @Test
    public void resultIsTrue_whenEverythingIsFine() {
        PolicyResult result = policy.canDelete(TEST_CODE);

        assertTrue(result.getResult());
        assertEquals("", result.getText());
    }
}
