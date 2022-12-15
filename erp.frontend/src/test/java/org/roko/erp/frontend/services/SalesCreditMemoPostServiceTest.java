package org.roko.erp.frontend.services;

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
import org.roko.erp.frontend.model.BankAccount;
import org.roko.erp.frontend.model.BankAccountLedgerEntry;
import org.roko.erp.frontend.model.BankAccountLedgerEntryType;
import org.roko.erp.frontend.model.Customer;
import org.roko.erp.frontend.model.CustomerLedgerEntry;
import org.roko.erp.frontend.model.CustomerLedgerEntryType;
import org.roko.erp.frontend.model.Item;
import org.roko.erp.frontend.model.ItemLedgerEntry;
import org.roko.erp.frontend.model.ItemLedgerEntryType;
import org.roko.erp.frontend.model.PaymentMethod;
import org.roko.erp.frontend.model.PostedSalesCreditMemo;
import org.roko.erp.frontend.model.PostedSalesCreditMemoLine;
import org.roko.erp.frontend.model.SalesCreditMemo;
import org.roko.erp.frontend.model.SalesCreditMemoLine;
import org.roko.erp.frontend.model.jpa.SalesCreditMemoLineId;

public class SalesCreditMemoPostServiceTest {

    private static final String TEST_POSTED_SALES_CREDIT_MEMO_CODE = "test-posted-sales-credit-memo-code";

    private static final String TEST_CODE = "test-code";

    private static final Date TEST_DATE = new Date();

    private static final Double TEST_QTY = 12.00;
    private static final Double TEST_PRICE = 10.00;
    private static final Double TEST_AMOUNT = 120.00;

    private static final int TEST_LINE_NO = 123;

    private static final String TEST_BANK_ACCOUNT_CODE = "test-bank-account-code";
    private static final Double TEST_BANK_ACCOUNT_BALANCE = 250.00;

    @Captor
    private ArgumentCaptor<PostedSalesCreditMemo> postedSalesCreditMemoArgumentCaptor;

    @Captor
    private ArgumentCaptor<PostedSalesCreditMemoLine> postedSalesCreditMemoLineArgumentCaptor;

    @Captor
    private ArgumentCaptor<ItemLedgerEntry> itemLedgerEntryArgumentCaptor;

    @Captor
    private ArgumentCaptor<CustomerLedgerEntry> customerLedgerEntryArgumentCaptor;

    @Captor
    private ArgumentCaptor<BankAccountLedgerEntry> bankAccountLedgerEntryArgumentCaptor;

    @Mock
    private SalesCreditMemo salesCreditMemoMock;

    @Mock
    private SalesCreditMemoLine salesCreditMemoLineMock;

    @Mock
    private Customer customerMock;

    @Mock
    private PaymentMethod paymentMethodMock;

    @Mock
    private Item itemMock;

    @Mock
    private BankAccount bankAccountMock;

    @Mock
    private SalesCreditMemoService salesCreditMemoSvcMock;

    @Mock
    private SalesCreditMemoLineService salesCreditMemoLineSvcMock;

    @Mock
    private PostedSalesCreditMemoService postedSalesCreditMemoSvcMock;

    @Mock
    private PostedSalesCreditMemoLineService postedSalesCreditMemoLineSvcMock;

    @Mock
    private ItemLedgerEntryService itemLedgerEntrySvcMock;

    @Mock
    private CustomerLedgerEntryService customerLedgerEntrySvcMock;

    @Mock
    private BankAccountLedgerEntryService bankAccountLedgerEntrySvcMock;

    @Mock
    private BankAccountService bankAccountSvcMock;

    @Mock
    private SalesCodeSeriesService salesCodeSeriesSvcMock;

    private SalesCreditMemoPostService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(paymentMethodMock.getBankAccount()).thenReturn(bankAccountMock);

        when(salesCreditMemoMock.getCode()).thenReturn(TEST_CODE);
        when(salesCreditMemoMock.getDate()).thenReturn(TEST_DATE);
        when(salesCreditMemoMock.getCustomer()).thenReturn(customerMock);
        when(salesCreditMemoMock.getPaymentMethod()).thenReturn(paymentMethodMock);

        SalesCreditMemoLineId salesCreditMemoLineId = new SalesCreditMemoLineId();
        salesCreditMemoLineId.setSalesCreditMemo(salesCreditMemoMock);
        salesCreditMemoLineId.setLineNo(TEST_LINE_NO);

        when(salesCreditMemoLineMock.getSalesCreditMemoLineId()).thenReturn(salesCreditMemoLineId);
        when(salesCreditMemoLineMock.getSalesCreditMemo()).thenReturn(salesCreditMemoMock);
        when(salesCreditMemoLineMock.getItem()).thenReturn(itemMock);
        when(salesCreditMemoLineMock.getQuantity()).thenReturn(TEST_QTY);
        when(salesCreditMemoLineMock.getPrice()).thenReturn(TEST_PRICE);
        when(salesCreditMemoLineMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(salesCreditMemoSvcMock.get(TEST_CODE)).thenReturn(salesCreditMemoMock);

        when(salesCreditMemoLineSvcMock.list(salesCreditMemoMock))
                .thenReturn(Arrays.asList(salesCreditMemoLineMock, salesCreditMemoLineMock));

        when(salesCodeSeriesSvcMock.postedCreditMemoCode()).thenReturn(TEST_POSTED_SALES_CREDIT_MEMO_CODE);

        when(bankAccountMock.getCode()).thenReturn(TEST_BANK_ACCOUNT_CODE);
        when(bankAccountMock.getBalance()).thenReturn(TEST_BANK_ACCOUNT_BALANCE);

        when(bankAccountSvcMock.get(TEST_BANK_ACCOUNT_CODE)).thenReturn(bankAccountMock);

        svc = new SalesCreditMemoPostServiceImpl(salesCreditMemoSvcMock, salesCreditMemoLineSvcMock,
                postedSalesCreditMemoSvcMock, postedSalesCreditMemoLineSvcMock, itemLedgerEntrySvcMock,
                customerLedgerEntrySvcMock, bankAccountLedgerEntrySvcMock, bankAccountSvcMock, 
                salesCodeSeriesSvcMock);
    }

    @Test
    public void allRelatedInteractionsAreDone() throws PostFailedException {
        svc.post(TEST_CODE);

        PostedSalesCreditMemo postedSalesCreditMemo = verifyPostedSalesCreditMemoCreated();

        verifyPostedSalesCreditMemoLinesCreated(postedSalesCreditMemo);

        verifyItemLedgerEntriesCreated(postedSalesCreditMemo);

        verifyCustomerLedgerEntriesCreated(postedSalesCreditMemo);

        verifyBankAccountLedgerEntriesCreated(postedSalesCreditMemo);

        verifySalesCreditMemoLinesDeleted();

        verifySalesCreditMemoDeleted();
    }

    @Test
    public void allRelatednoPaymentRelatedEntriesAreCreated_whenPaymentMethodDoesNotHaveCorrespondingBankAccountInteractionsAreDone() throws PostFailedException {
        when(paymentMethodMock.getBankAccount()).thenReturn(null);

        svc.post(TEST_CODE);

        PostedSalesCreditMemo postedSalesCreditMemo = verifyPostedSalesCreditMemoCreated();

        verifyCustomerLedgerEntriesCreated(postedSalesCreditMemo);

        verifyNoBankAccountLedgerEntryCreated();
    }

    @Test
    public void postFails_whenBankAccountBalanceIsNotEnough() throws PostFailedException {
        when(bankAccountMock.getBalance()).thenReturn(0.0);

        assertThrows(PostFailedException.class, () -> {svc.post(TEST_CODE);});
    }

    private void verifyNoBankAccountLedgerEntryCreated() {
        verify(bankAccountLedgerEntrySvcMock, never()).create(any(BankAccountLedgerEntry.class));
    }

    private void verifySalesCreditMemoDeleted() {
        verify(salesCreditMemoSvcMock).delete(TEST_CODE);
    }

    private void verifySalesCreditMemoLinesDeleted() {
        SalesCreditMemoLineId salesCreditMemoLineId = new SalesCreditMemoLineId();
        salesCreditMemoLineId.setSalesCreditMemo(salesCreditMemoMock);
        salesCreditMemoLineId.setLineNo(TEST_LINE_NO);

        verify(salesCreditMemoLineSvcMock, times(2)).delete(salesCreditMemoLineId);
    }

    private void verifyBankAccountLedgerEntriesCreated(PostedSalesCreditMemo postedSalesCreditMemo) {
        verify(bankAccountLedgerEntrySvcMock).create(bankAccountLedgerEntryArgumentCaptor.capture());

        BankAccountLedgerEntry bankAccountLedgerEntry = bankAccountLedgerEntryArgumentCaptor.getValue();

        assertEquals(bankAccountMock, bankAccountLedgerEntry.getBankAccount());
        assertEquals(BankAccountLedgerEntryType.CUSTOMER_REFUND, bankAccountLedgerEntry.getType());
        assertEquals(-(TEST_AMOUNT * 2), bankAccountLedgerEntry.getAmount());
        assertEquals(postedSalesCreditMemo.getCode(), bankAccountLedgerEntry.getDocumentCode());
    }

    private void verifyCustomerLedgerEntriesCreated(PostedSalesCreditMemo postedSalesCreditMemo) {
        int expectedNumberOfCustomerLedgerEntries = 1;
        if (postedSalesCreditMemo.getPaymentMethod().getBankAccount() != null) {
            expectedNumberOfCustomerLedgerEntries++;
        }
        verify(customerLedgerEntrySvcMock, times(expectedNumberOfCustomerLedgerEntries))
                .create(customerLedgerEntryArgumentCaptor.capture());

        List<CustomerLedgerEntry> customerLedgerEntries = customerLedgerEntryArgumentCaptor.getAllValues();

        verifyFirstCustomerLedgerEntry(customerLedgerEntries.get(0), postedSalesCreditMemo);
        if (expectedNumberOfCustomerLedgerEntries > 1) {
            verifySecondCustomerLedgerEntry(customerLedgerEntries.get(1), postedSalesCreditMemo);
        }
    }

    private void verifySecondCustomerLedgerEntry(CustomerLedgerEntry customerLedgerEntry,
            PostedSalesCreditMemo postedSalesCreditMemo) {
        assertEquals(customerMock, customerLedgerEntry.getCustomer());
        assertEquals(CustomerLedgerEntryType.REFUND, customerLedgerEntry.getType());
        assertEquals(TEST_AMOUNT * 2, customerLedgerEntry.getAmount());
        assertEquals(postedSalesCreditMemo.getCode(), customerLedgerEntry.getDocumentCode());
    }

    private void verifyFirstCustomerLedgerEntry(CustomerLedgerEntry customerLedgerEntry,
            PostedSalesCreditMemo postedSalesCreditMemo) {
        assertEquals(customerMock, customerLedgerEntry.getCustomer());
        assertEquals(CustomerLedgerEntryType.SALES_CREDIT_MEMO, customerLedgerEntry.getType());
        assertEquals(-(TEST_AMOUNT * 2), customerLedgerEntry.getAmount());
        assertEquals(postedSalesCreditMemo.getCode(), customerLedgerEntry.getDocumentCode());
    }

    private void verifyItemLedgerEntriesCreated(PostedSalesCreditMemo postedSalesCreditMemo) {
        verify(itemLedgerEntrySvcMock, times(2)).create(itemLedgerEntryArgumentCaptor.capture());

        ItemLedgerEntry itemLedgerEntry = itemLedgerEntryArgumentCaptor.getValue();

        assertEquals(itemMock, itemLedgerEntry.getItem());
        assertEquals(ItemLedgerEntryType.SALES_CREDIT_MEMO, itemLedgerEntry.getType());
        assertEquals(TEST_QTY, itemLedgerEntry.getQuantity());
        assertEquals(postedSalesCreditMemo.getCode(), itemLedgerEntry.getDocumentCode());
    }

    private void verifyPostedSalesCreditMemoLinesCreated(PostedSalesCreditMemo postedSalesCreditMemo) {
        verify(postedSalesCreditMemoLineSvcMock, times(2)).create(postedSalesCreditMemoLineArgumentCaptor.capture());

        PostedSalesCreditMemoLine postedSalesCreditMemoLine = postedSalesCreditMemoLineArgumentCaptor.getValue();

        assertEquals(postedSalesCreditMemo,
                postedSalesCreditMemoLine.getPostedSalesCreditMemoLineId().getPostedSalesCreditMemo());
        assertEquals(TEST_LINE_NO, postedSalesCreditMemoLine.getPostedSalesCreditMemoLineId().getLineNo());
        assertEquals(itemMock, postedSalesCreditMemoLine.getItem());
        assertEquals(TEST_QTY, postedSalesCreditMemoLine.getQuantity());
        assertEquals(TEST_PRICE, postedSalesCreditMemoLine.getPrice());
        assertEquals(TEST_AMOUNT, postedSalesCreditMemoLine.getAmount());
    }

    private PostedSalesCreditMemo verifyPostedSalesCreditMemoCreated() {
        verify(postedSalesCreditMemoSvcMock).create(postedSalesCreditMemoArgumentCaptor.capture());

        PostedSalesCreditMemo postedSalesCreditMemo = postedSalesCreditMemoArgumentCaptor.getValue();

        assertEquals(TEST_POSTED_SALES_CREDIT_MEMO_CODE, postedSalesCreditMemo.getCode());
        assertEquals(customerMock, postedSalesCreditMemo.getCustomer());
        assertEquals(paymentMethodMock, postedSalesCreditMemo.getPaymentMethod());
        assertEquals(TEST_CODE, postedSalesCreditMemo.getOrderCode());
        assertEquals(TEST_DATE, postedSalesCreditMemo.getOrderDate());

        return postedSalesCreditMemo;
    }
}
