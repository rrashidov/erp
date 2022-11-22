package org.roko.erp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.BankAccount;
import org.roko.erp.model.BankAccountLedgerEntry;
import org.roko.erp.model.BankAccountLedgerEntryType;
import org.roko.erp.model.Customer;
import org.roko.erp.model.CustomerLedgerEntry;
import org.roko.erp.model.CustomerLedgerEntryType;
import org.roko.erp.model.Item;
import org.roko.erp.model.ItemLedgerEntry;
import org.roko.erp.model.ItemLedgerEntryType;
import org.roko.erp.model.PaymentMethod;
import org.roko.erp.model.PostedSalesOrder;
import org.roko.erp.model.PostedSalesOrderLine;
import org.roko.erp.model.SalesOrder;
import org.roko.erp.model.SalesOrderLine;
import org.roko.erp.model.jpa.PostedSalesOrderLineId;
import org.roko.erp.model.jpa.SalesOrderLineId;

public class SalesOrderPostServiceTest {

    private static final String TEST_POSTED_SALES_ORDER_CODE = "test-posted-sales-order-code";

    private static final String TEST_CODE = "test-code";

    private static final String ITEM_CODE = "item-code";

    private static final Date TEST_DATE = new Date();

    private static final Double TEST_QTY = 10.00;
    private static final Double TEST_PRICE = 12.00;
    private static final Double TEST_AMOUNT = 120.00;

    private static final Integer TEST_LINE_NO = 123;

    private static final Double TEST_ITEM_INVENTORY = 12.00;

    @Captor
    private ArgumentCaptor<PostedSalesOrder> postedSalesOrderArgumentCaptor;

    @Captor
    private ArgumentCaptor<PostedSalesOrderLine> postedSalesOrderLineArgumentCaptor;

    @Captor
    private ArgumentCaptor<ItemLedgerEntry> itemLedgerEntryArgumentCaptor;

    @Captor
    private ArgumentCaptor<CustomerLedgerEntry> customerLedgerEntryArgumentCaptor;

    @Captor
    private ArgumentCaptor<BankAccountLedgerEntry> bankAccountLedgerEntryArgumentCaptor;

    @Mock
    private SalesOrder salesOrderMock;

    @Mock
    private SalesOrderLine salesOrderLineMock;

    @Mock
    private PaymentMethod paymentMethodMock;

    @Mock
    private Customer customerMock;

    @Mock
    private Item itemMock;

    @Mock
    private BankAccount bankAccountMock;

    @Mock
    private SalesOrderService salesOrderSvcMock;

    @Mock
    private SalesOrderLineService salesOrderLineSvcMock;

    @Mock
    private PostedSalesOrderService postedSalesOrderSvcMock;

    @Mock
    private PostedSalesOrderLineService postedSalesOrderLineSvcMock;

    @Mock
    private ItemService itemSvcMock;

    @Mock
    private ItemLedgerEntryService itemLedgerEntrySvcMock;

    @Mock
    private CustomerLedgerEntryService customerLedgerEntrySvcMock;

    @Mock
    private BankAccountLedgerEntryService bankAccountLedgerEntrySvcMock;

    @Mock
    private SalesCodeSeriesService salesCodeSeriesSvcMock;

    private SalesOrderPostService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(paymentMethodMock.getBankAccount()).thenReturn(bankAccountMock);

        when(itemMock.getCode()).thenReturn(ITEM_CODE);
        when(itemMock.getInventory()).thenReturn(TEST_ITEM_INVENTORY);

        when(salesOrderMock.getCode()).thenReturn(TEST_CODE);
        when(salesOrderMock.getCustomer()).thenReturn(customerMock);
        when(salesOrderMock.getDate()).thenReturn(TEST_DATE);
        when(salesOrderMock.getPaymentMethod()).thenReturn(paymentMethodMock);

        when(salesOrderSvcMock.get(TEST_CODE)).thenReturn(salesOrderMock);

        SalesOrderLineId salesOrderLineId = new SalesOrderLineId();
        salesOrderLineId.setSalesOrder(salesOrderMock);
        salesOrderLineId.setLineNo(TEST_LINE_NO);

        when(salesOrderLineMock.getSalesOrderLineId()).thenReturn(salesOrderLineId);
        when(salesOrderLineMock.getItem()).thenReturn(itemMock);
        when(salesOrderLineMock.getQuantity()).thenReturn(TEST_QTY);
        when(salesOrderLineMock.getPrice()).thenReturn(TEST_PRICE);
        when(salesOrderLineMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(salesOrderLineSvcMock.list(salesOrderMock))
                .thenReturn(Arrays.asList(salesOrderLineMock, salesOrderLineMock));

        when(salesCodeSeriesSvcMock.postedOrderCode()).thenReturn(TEST_POSTED_SALES_ORDER_CODE);

        when(itemSvcMock.get(ITEM_CODE)).thenReturn(itemMock);

        svc = new SalesOrderPostServiceImpl(salesOrderSvcMock, salesOrderLineSvcMock, postedSalesOrderSvcMock,
                postedSalesOrderLineSvcMock, itemSvcMock, itemLedgerEntrySvcMock, customerLedgerEntrySvcMock,
                bankAccountLedgerEntrySvcMock, salesCodeSeriesSvcMock);
    }

    @Test
    public void allRelatedInteractionsAreDone() throws PostFailedException {
        svc.post(TEST_CODE);

        PostedSalesOrder postedSalesOrder = verifyPostedSalesOrderCreated();

        verifyPostedSalesOrderLinesCreated(postedSalesOrder);

        verifyItemLedgerEntriesCreated(postedSalesOrder);

        verifyCustomerLedgerEntryCreated(postedSalesOrder);

        verifyBankAccountLedgerEntryCreated(postedSalesOrder);

        verifySalesOrderLinesDeleted();

        verifySalesOrderDeleted();
    }

    @Test
    public void noPaymentRelatedEntriesAreCreated_whenPaymentMethodDoesNotHaveCorrespondingBankAccount() throws PostFailedException {
        when(paymentMethodMock.getBankAccount()).thenReturn(null);
        
        svc.post(TEST_CODE);

        PostedSalesOrder postedSalesOrder = verifyPostedSalesOrderCreated();

        verifyCustomerLedgerEntryCreated(postedSalesOrder);

        verifyNoBankAccountLedgerEntryCreated();
    }

    @Test
    public void postingFails_whenItemDoesNotHaveEnoughInventory() {
        when(itemMock.getInventory()).thenReturn(0.0);

        assertThrows(PostFailedException.class, () -> {svc.post(TEST_CODE);});
    }

    private void verifyNoBankAccountLedgerEntryCreated() {
        verify(bankAccountLedgerEntrySvcMock, never()).create(any(BankAccountLedgerEntry.class));
    }

    private void verifySalesOrderDeleted() {
        verify(salesOrderSvcMock).delete(TEST_CODE);
    }

    private void verifySalesOrderLinesDeleted() {
        SalesOrderLineId salesOrderLineId = new SalesOrderLineId();
        salesOrderLineId.setSalesOrder(salesOrderMock);
        salesOrderLineId.setLineNo(TEST_LINE_NO);

        verify(salesOrderLineSvcMock, times(2)).delete(salesOrderLineId);
    }

    private void verifyBankAccountLedgerEntryCreated(PostedSalesOrder postedSalesOrder) {
        verify(bankAccountLedgerEntrySvcMock).create(bankAccountLedgerEntryArgumentCaptor.capture());

        BankAccountLedgerEntry bankAccountLedgerEntry = bankAccountLedgerEntryArgumentCaptor.getValue();

        assertEquals(bankAccountMock, bankAccountLedgerEntry.getBankAccount());
        assertEquals(BankAccountLedgerEntryType.CUSTOMER_PAYMENT, bankAccountLedgerEntry.getType());
        assertEquals(TEST_AMOUNT * 2, bankAccountLedgerEntry.getAmount());
        assertEquals(postedSalesOrder.getCode(), bankAccountLedgerEntry.getDocumentCode());
    }

    private void verifyCustomerLedgerEntryCreated(PostedSalesOrder postedSalesOrder) {
        boolean paymentShouldBeDone = postedSalesOrder.getPaymentMethod().getBankAccount() != null;

        int expectedNumberOfCustomerLedgerEntries = 1;
        if (paymentShouldBeDone) {
            expectedNumberOfCustomerLedgerEntries++;
        }

        verify(customerLedgerEntrySvcMock, times(expectedNumberOfCustomerLedgerEntries))
                .create(customerLedgerEntryArgumentCaptor.capture());

        List<CustomerLedgerEntry> customerLedgerEntries = customerLedgerEntryArgumentCaptor.getAllValues();

        verifyFirstCustomerLedgerEntry(customerLedgerEntries.get(0), postedSalesOrder.getCode());
        if (paymentShouldBeDone) {
            verifySecondCustomerLedgerEntry(customerLedgerEntries.get(1), postedSalesOrder.getCode());
        }
    }

    private void verifyFirstCustomerLedgerEntry(CustomerLedgerEntry customerLedgerEntry, String documentCode) {
        assertEquals(customerMock, customerLedgerEntry.getCustomer());
        assertEquals(CustomerLedgerEntryType.SALES_ORDER, customerLedgerEntry.getType());
        assertEquals(TEST_AMOUNT * 2, customerLedgerEntry.getAmount());
        assertEquals(documentCode, customerLedgerEntry.getDocumentCode());
    }

    private void verifySecondCustomerLedgerEntry(CustomerLedgerEntry customerLedgerEntry, String documentCode) {
        assertEquals(customerMock, customerLedgerEntry.getCustomer());
        assertEquals(CustomerLedgerEntryType.PAYMENT, customerLedgerEntry.getType());
        assertEquals(-(TEST_AMOUNT * 2), customerLedgerEntry.getAmount());
        assertEquals(documentCode, customerLedgerEntry.getDocumentCode());
    }

    private void verifyItemLedgerEntriesCreated(PostedSalesOrder postedSalesOrder) {
        verify(itemLedgerEntrySvcMock, times(2)).create(itemLedgerEntryArgumentCaptor.capture());

        ItemLedgerEntry itemLedgerEntry = itemLedgerEntryArgumentCaptor.getValue();

        assertEquals(itemMock, itemLedgerEntry.getItem());
        assertEquals(ItemLedgerEntryType.SALES_ORDER, itemLedgerEntry.getType());
        assertEquals(-TEST_QTY, itemLedgerEntry.getQuantity());
        assertEquals(postedSalesOrder.getCode(), itemLedgerEntry.getDocumentCode());
    }

    private void verifyPostedSalesOrderLinesCreated(PostedSalesOrder postedSalesOrder) {
        verify(postedSalesOrderLineSvcMock, times(2)).create(postedSalesOrderLineArgumentCaptor.capture());

        PostedSalesOrderLine postedSalesOrderLine = postedSalesOrderLineArgumentCaptor.getValue();

        assertEquals(itemMock, postedSalesOrderLine.getItem());
        assertEquals(TEST_QTY, postedSalesOrderLine.getQuantity());
        assertEquals(TEST_PRICE, postedSalesOrderLine.getPrice());
        assertEquals(TEST_AMOUNT, postedSalesOrderLine.getAmount());

        PostedSalesOrderLineId postedSalesOrderLineId = postedSalesOrderLine.getPostedSalesOrderLineId();
        assertEquals(postedSalesOrder, postedSalesOrderLineId.getPostedSalesOrder());
        assertEquals(TEST_LINE_NO, postedSalesOrderLineId.getLineNo());
    }

    private PostedSalesOrder verifyPostedSalesOrderCreated() {
        verify(postedSalesOrderSvcMock).create(postedSalesOrderArgumentCaptor.capture());

        PostedSalesOrder postedSalesOrder = postedSalesOrderArgumentCaptor.getValue();

        assertEquals(TEST_POSTED_SALES_ORDER_CODE, postedSalesOrder.getCode());
        assertEquals(customerMock, postedSalesOrder.getCustomer());
        assertEquals(paymentMethodMock, postedSalesOrder.getPaymentMethod());
        assertEquals(TEST_CODE, postedSalesOrder.getOrderCode());
        assertEquals(TEST_DATE, postedSalesOrder.getOrderDate());

        return postedSalesOrder;
    }
}
