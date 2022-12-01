package org.roko.erp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.CodeSerie;
import org.roko.erp.model.Setup;

public class SalesCodeSeriesServiceTest {

    private static final String SALES_ORDER_CODE_SERIE_CODE = "sales-order";
    private static final String SALES_CREDIT_MEMO_CODE_SERIE_CODE = "sales-credit-memo";
    private static final String POSTED_SALES_ORDER_CODE_SERIE_CODE = "posted-sales-order";
    private static final String POSTED_SALES_CREDIT_MEMO_CODE_SERIE_CODE = "posted-sales-credit-memo";

    private static final String TEST_SALES_ORDER_CODE = "test-sales-order-code";
    private static final String TEST_POSTED_SALES_ORDER_CODE = "test-posted-sales-order-code";
    private static final String TEST_SALES_CREDIT_MEMO_CODE = "test-sales-credit-memo-code";
    private static final String TEST_POSTED_SALES_CREDIT_MEMO_CODE = "test-posted-sales-credit-memo-code";

    @Mock
    private CodeSerie salesOrderCodeSerieMock;

    @Mock
    private CodeSerie salesCreditMemoCodeSerieMock;

    @Mock
    private CodeSerie postedSalesOrderCodeSerieMock;

    @Mock
    private CodeSerie postedSalesCreditMemoCodeSerieMock;

    @Mock
    private Setup setupMock;

    @Mock
    private SetupService setupSvcMock;

    @Mock
    private CodeSerieService codeSerieSvcMock;
    
    private SalesCodeSeriesService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(salesOrderCodeSerieMock.getCode()).thenReturn(SALES_ORDER_CODE_SERIE_CODE);
        when(postedSalesOrderCodeSerieMock.getCode()).thenReturn(POSTED_SALES_ORDER_CODE_SERIE_CODE);
        when(salesCreditMemoCodeSerieMock.getCode()).thenReturn(SALES_CREDIT_MEMO_CODE_SERIE_CODE);
        when(postedSalesCreditMemoCodeSerieMock.getCode()).thenReturn(POSTED_SALES_CREDIT_MEMO_CODE_SERIE_CODE);

        when(setupMock.getSalesOrderCodeSerie()).thenReturn(salesOrderCodeSerieMock);
        when(setupMock.getSalesCreditMemoCodeSerie()).thenReturn(salesCreditMemoCodeSerieMock);
        when(setupMock.getPostedSalesOrderCodeSerie()).thenReturn(postedSalesOrderCodeSerieMock);
        when(setupMock.getPostedSalesCreditMemoCodeSerie()).thenReturn(postedSalesCreditMemoCodeSerieMock);

        when(codeSerieSvcMock.generate(SALES_ORDER_CODE_SERIE_CODE)).thenReturn(TEST_SALES_ORDER_CODE);
        when(codeSerieSvcMock.generate(POSTED_SALES_ORDER_CODE_SERIE_CODE)).thenReturn(TEST_POSTED_SALES_ORDER_CODE);
        when(codeSerieSvcMock.generate(SALES_CREDIT_MEMO_CODE_SERIE_CODE)).thenReturn(TEST_SALES_CREDIT_MEMO_CODE);
        when(codeSerieSvcMock.generate(POSTED_SALES_CREDIT_MEMO_CODE_SERIE_CODE)).thenReturn(TEST_POSTED_SALES_CREDIT_MEMO_CODE);

        when(setupSvcMock.get()).thenReturn(setupMock);

        svc = new SalesCodeSeriesServiceImpl(setupSvcMock, codeSerieSvcMock);
    }

    @Test
    public void properCodeIsGeneratedForSalesOrder(){
        String orderCode = svc.orderCode();

        assertEquals(TEST_SALES_ORDER_CODE, orderCode);
    }

    @Test
    public void properCodeIsGeneratedForPostedSalesOrder() {
        String postedOrderCode = svc.postedOrderCode();

        assertEquals(TEST_POSTED_SALES_ORDER_CODE, postedOrderCode);
    }

    @Test
    public void properCodeIsGeneratedForSalesCreditMemo() {
        String creditMemoCode = svc.creditMemoCode();

        assertEquals(TEST_SALES_CREDIT_MEMO_CODE, creditMemoCode);
    }

    @Test
    public void properCodeIsGeneratedForPostedSalesCreditMemo() {
        String postedCreditMemoCode = svc.postedCreditMemoCode();

        assertEquals(TEST_POSTED_SALES_CREDIT_MEMO_CODE, postedCreditMemoCode);
    }
}
