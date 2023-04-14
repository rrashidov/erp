package org.roko.erp.backend.controllers.policy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.DocumentPostStatus;
import org.roko.erp.backend.model.SalesOrder;
import org.roko.erp.backend.services.SalesOrderLineService;
import org.roko.erp.backend.services.SalesOrderService;

public class SalesOrderPolicyTest {

    private static final String NOT_FOUND_TMPL = "Sales Order %s not found";
    private static final String HAS_LINES_TMPL = "Sales Order %s has lines";
    private static final String SCHEDULED_FOR_POSTING_TMPL = "Sales Order %s is scheduled for posting";

    private static final String TEST_CODE = "test-code";

    @Mock
    private SalesOrderService svcMock;

    @Mock
    private SalesOrderLineService salesOrderLineSvcMock;

    @Mock
    private SalesOrder salesOrderMock;

    private SalesOrderPolicy policy;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(salesOrderMock.getPostStatus()).thenReturn(DocumentPostStatus.READY);

        when(svcMock.get(TEST_CODE)).thenReturn(salesOrderMock);

        when(salesOrderLineSvcMock.count(salesOrderMock)).thenReturn(0);

        policy = new SalesOrderPolicy(svcMock, salesOrderLineSvcMock);
    }

    @Test
    public void resultIsFalse_whenSalesOrderNotFound() {
        when(svcMock.get(TEST_CODE)).thenReturn(null);

        PolicyResult result = policy.canDelete(TEST_CODE);

        assertFalse(result.getResult());
        assertEquals(String.format(NOT_FOUND_TMPL, TEST_CODE), result.getText());
    }

    @Test
    public void resultIsFalse_whenSalesOrderScheduledForPosting() {
        when(salesOrderMock.getPostStatus()).thenReturn(DocumentPostStatus.SCHEDULED);

        PolicyResult result = policy.canDelete(TEST_CODE);

        assertFalse(result.getResult());
        assertEquals(String.format(SCHEDULED_FOR_POSTING_TMPL, TEST_CODE), result.getText());
    }

    @Test
    public void resultIsFalse_whenSalesOrderHasLines() {
        when(salesOrderLineSvcMock.count(salesOrderMock)).thenReturn(1);

        PolicyResult result = policy.canDelete(TEST_CODE);

        assertFalse(result.getResult());
        assertEquals(String.format(HAS_LINES_TMPL, TEST_CODE), result.getText());
    }
}
