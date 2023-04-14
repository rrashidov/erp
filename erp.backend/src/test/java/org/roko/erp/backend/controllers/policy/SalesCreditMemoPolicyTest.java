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
import org.roko.erp.backend.model.SalesCreditMemo;
import org.roko.erp.backend.services.SalesCreditMemoLineService;
import org.roko.erp.backend.services.SalesCreditMemoService;

public class SalesCreditMemoPolicyTest {
    
    private static final String TEST_CODE = "test-code";

    private static final String NOT_FOUND_TMPL = "Sales Credit Memo %s not found";
    private static final String HAS_LINES_TMPL = "Sales Credit Memo %s has lines";
    private static final String SCHEDULED_FOR_POSTING_TMPL = "Sales Credit Memo %s is scheduled for posting";

    @Mock
    private SalesCreditMemo salesCreditMemoMock;

    @Mock
    private SalesCreditMemoService salesCreditMemoSvcMock;

    @Mock
    private SalesCreditMemoLineService salesCreditMemoLineSvcMock;

    private EntityPolicy policy;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(salesCreditMemoMock.getPostStatus()).thenReturn(DocumentPostStatus.READY);

        when(salesCreditMemoSvcMock.get(TEST_CODE)).thenReturn(salesCreditMemoMock);

        when(salesCreditMemoLineSvcMock.count(salesCreditMemoMock)).thenReturn(0);

        policy = new SalesCreditMemoPolicy(salesCreditMemoSvcMock, salesCreditMemoLineSvcMock);
    }

    @Test
    public void resultIsFalse_whenSalesCreditMemoNotFound() {
        when(salesCreditMemoSvcMock.get(TEST_CODE)).thenReturn(null);
        
        PolicyResult result = policy.canDelete(TEST_CODE);

        assertFalse(result.getResult());
        assertEquals(String.format(NOT_FOUND_TMPL, TEST_CODE), result.getText());
    }

    @Test
    public void resultIsFalse_whenSalesCreditMemoScheduledForPost() {
        when(salesCreditMemoMock.getPostStatus()).thenReturn(DocumentPostStatus.SCHEDULED);

        PolicyResult result = policy.canDelete(TEST_CODE);

        assertFalse(result.getResult());
        assertEquals(String.format(SCHEDULED_FOR_POSTING_TMPL, TEST_CODE), result.getText());
    }

    @Test
    public void resultIsFalse_whenSalesCreditMemoLinesExist() {
        when(salesCreditMemoLineSvcMock.count(salesCreditMemoMock)).thenReturn(1);

        PolicyResult result = policy.canDelete(TEST_CODE);

        assertFalse(result.getResult());
        assertEquals(String.format(HAS_LINES_TMPL, TEST_CODE), result.getText());
    }

    @Test
    public void resultIsTrue_whenAllConditionsMet(){
        PolicyResult result = policy.canDelete(TEST_CODE);

        assertTrue(result.getResult());
        assertEquals("", result.getText());
    }
}
