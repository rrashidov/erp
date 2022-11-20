package org.roko.erp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.CodeSerie;
import org.roko.erp.model.Setup;

public class PurchaseCodeSeriesServiceTest {

    private static final String PURCHASE_ORDER_CS = "purchase-order-cs";
    private static final String PURCHASE_CREDIT_MEMO_CS = "purchase-credit-memo-cs";
    private static final String POSTED_PURCHASE_ORDER_CS = "posted-purchase-order-cs";
    private static final String POSTED_PURCHASE_CREDIT_MEMO_CS = "posted-purchase-credit-memo-cs";

    private static final String TEST_PURCHASE_ORDER_CODE = "test-purchase-order-code";
    private static final String TEST_PURCHASE_CREDIT_MEMO_CODE = "test-purchase-credit-memo-code";
    private static final String TEST_POSTED_PURCHASE_ORDER_CODE = "test-posted-purchase-order-code";
    private static final String TEST_POSTED_PURCHASE_CREDIT_MEMO_CODE = "test-posted-purchase-credit-memo-code";

    @Mock
    private CodeSerie purchaseOrderCodeSerieMock;

    @Mock
    private CodeSerie purchaseCreditMemoCodeSerieMock;

    @Mock
    private CodeSerie postedPurchaseOrderCodeSerieMock;

    @Mock
    private CodeSerie postedPurchaseCreditMemoCodeSerieMock;

    @Mock
    private SetupService setupSvcMock;

    @Mock
    private Setup setupMock;

    @Mock
    private CodeSerieService codeSerieSvcMock;
    
    private PurchaseCodeSeriesService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(purchaseOrderCodeSerieMock.getCode()).thenReturn(PURCHASE_ORDER_CS);
        when(purchaseCreditMemoCodeSerieMock.getCode()).thenReturn(PURCHASE_CREDIT_MEMO_CS);
        when(postedPurchaseOrderCodeSerieMock.getCode()).thenReturn(POSTED_PURCHASE_ORDER_CS);
        when(postedPurchaseCreditMemoCodeSerieMock.getCode()).thenReturn(POSTED_PURCHASE_CREDIT_MEMO_CS);

        when(setupMock.getPurchaseOrderCodeSerie()).thenReturn(purchaseOrderCodeSerieMock);
        when(setupMock.getPurchaseCreditMemoCodeSerie()).thenReturn(purchaseCreditMemoCodeSerieMock);
        when(setupMock.getPostedPurchaseOrderCodeSerie()).thenReturn(postedPurchaseOrderCodeSerieMock);
        when(setupMock.getPostedPurchaseCreditMemoCodeSerie()).thenReturn(postedPurchaseCreditMemoCodeSerieMock);

        when(setupSvcMock.get()).thenReturn(setupMock);

        when(codeSerieSvcMock.generate(PURCHASE_ORDER_CS)).thenReturn(TEST_PURCHASE_ORDER_CODE);
        when(codeSerieSvcMock.generate(PURCHASE_CREDIT_MEMO_CS)).thenReturn(TEST_PURCHASE_CREDIT_MEMO_CODE);
        when(codeSerieSvcMock.generate(POSTED_PURCHASE_ORDER_CS)).thenReturn(TEST_POSTED_PURCHASE_ORDER_CODE);
        when(codeSerieSvcMock.generate(POSTED_PURCHASE_CREDIT_MEMO_CS)).thenReturn(TEST_POSTED_PURCHASE_CREDIT_MEMO_CODE);

        svc = new PurchaseCodeSeriesServiceImpl(setupSvcMock, codeSerieSvcMock);
    }

    @Test
    public void orderCode_generatesProperCode() {
        String orderCode = svc.orderCode();

        assertEquals(TEST_PURCHASE_ORDER_CODE, orderCode);
    }

    @Test
    public void creditMemoCode_generatesProperCode() {
        String creditMemoCode = svc.creditMemoCode();

        assertEquals(TEST_PURCHASE_CREDIT_MEMO_CODE, creditMemoCode);
    }

    @Test
    public void postedOrderCode_generatesProperCode() {
        String postedOrderCode = svc.postedOrderCode();

        assertEquals(TEST_POSTED_PURCHASE_ORDER_CODE, postedOrderCode);
    }

    @Test
    public void postedCreditMemoCode_generatesProperCode() {
        String postedCreditMemoCode = svc.postedCreditMemoCode();

        assertEquals(TEST_POSTED_PURCHASE_CREDIT_MEMO_CODE, postedCreditMemoCode);
    }
}
