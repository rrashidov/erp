package org.roko.erp.rules.sales;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.SalesOrder;
import org.roko.erp.services.SalesOrderService;

public class SalesOrderServiceRule {

    public static final long TEST_SALES_ORDER_COUNT = 123l;

    private static final String TEST_SALES_ORDER_CODE = "test-sales-order-code";

    private List<SalesOrder> salesOrders = new ArrayList<>();

    @Mock
    public static SalesOrderService mock;

    @Mock
    public SalesOrder salesOrderMock;

    public SalesOrderServiceRule() {
        MockitoAnnotations.openMocks(this);

        when(mock.list()).thenReturn(salesOrders);
        when(mock.count()).thenReturn(TEST_SALES_ORDER_COUNT);
        when(mock.get(TEST_SALES_ORDER_CODE)).thenReturn(salesOrderMock);
    }

}
