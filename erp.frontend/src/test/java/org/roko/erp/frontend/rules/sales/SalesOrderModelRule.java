package org.roko.erp.frontend.rules.sales;

import static org.mockito.Mockito.when;

import java.util.Date;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.SalesDocumentDTO;

public class SalesOrderModelRule {
    
    public static final String TEST_SALES_ORDER_CODE = "test-sales-order-code";
    public static final String TEST_CUSTOMER_CODE = "test-customer-code";
    public static final String TEST_CUSTOMER_NAME = "test-customer-name";
    public static final Date TEST_DATE = new Date();
    public static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";

    @Mock
    public SalesDocumentDTO mock;

    public SalesOrderModelRule() {
        MockitoAnnotations.openMocks(this);

        when(mock.getCode()).thenReturn(TEST_SALES_ORDER_CODE);
        when(mock.getCustomerCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(mock.getCustomerName()).thenReturn(TEST_CUSTOMER_NAME);
        when(mock.getDate()).thenReturn(TEST_DATE);
        when(mock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);
    }

    public void stubSalesOrderModelForNewSalesOrder(){
        when(mock.getCode()).thenReturn("");
    }
}
