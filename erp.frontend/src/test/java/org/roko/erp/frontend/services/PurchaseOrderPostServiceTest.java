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
import org.roko.erp.frontend.model.Item;
import org.roko.erp.frontend.model.ItemLedgerEntry;
import org.roko.erp.frontend.model.ItemLedgerEntryType;
import org.roko.erp.frontend.model.PaymentMethod;
import org.roko.erp.frontend.model.PostedPurchaseOrder;
import org.roko.erp.frontend.model.PostedPurchaseOrderLine;
import org.roko.erp.frontend.model.PurchaseOrder;
import org.roko.erp.frontend.model.PurchaseOrderLine;
import org.roko.erp.frontend.model.Vendor;
import org.roko.erp.frontend.model.VendorLedgerEntry;
import org.roko.erp.frontend.model.VendorLedgerEntryType;
import org.roko.erp.frontend.model.jpa.PurchaseOrderLineId;

public class PurchaseOrderPostServiceTest {

    private static final String TEST_BANK_ACCOUNT_CODE = "test-bank-account-code";
    private static final double TEST_BANK_ACCOUNT_BALANCE = 250.00;

    private static final String NEW_POSTED_ORDER_CODE = "new-posted-order-code";

    private static final String TEST_CODE = "test-code";

    private static final Date TEST_DATE = new Date();

    private static final int TEST_LINE_NO = 1;

    private static final Double TEST_QTY = 12.00;

    private static final Double TEST_PRICE = 10.00;

    private static final Double TEST_AMOUNT = 120.00;

    @Captor
    private ArgumentCaptor<PostedPurchaseOrder> postedPurchaseOrderArgumentCaptor;

    @Captor
    private ArgumentCaptor<PostedPurchaseOrderLine> postedPurchaseOrderLineArgumentCaptor;

    @Captor
    private ArgumentCaptor<ItemLedgerEntry> itemLedgerEntryArgumentCaptor;

    @Captor
    private ArgumentCaptor<VendorLedgerEntry> vendorLedgerEntryArgumentCaptor;

    @Captor
    private ArgumentCaptor<BankAccountLedgerEntry> bankAccountLedgerEntryArgumentCaptor;

    @Mock
    private Vendor vendorMock;

    @Mock
    private PaymentMethod paymentMethodMock;

    @Mock
    private BankAccount bankAccountMock;

    @Mock
    private Item itemMock;

    @Mock
    private PurchaseOrder purchaseOrderMock;

    @Mock
    private PurchaseOrderLine purchaseOrderLineMock;

    @Mock
    private PurchaseOrderService purchaseOrderSvcMock;

    @Mock
    private PurchaseOrderLineService purchaseOrderLineSvcMock;

    @Mock
    private PostedPurchaseOrderService postedPurchaseOrderSvcMock;

    @Mock
    private PostedPurchaseOrderLineService postedPurchaseOrderLineSvcMock;

    @Mock
    private ItemLedgerEntryService itemLedgerEntrySvcMock;

    @Mock
    private VendorLedgerEntryService vendorLedgerEntrySvcMock;

    @Mock
    private BankAccountLedgerEntryService bankAccountLedgerEntrySvcMock;

    @Mock
    private BankAccountService bankAccountSvcMock;

    @Mock
    private PurchaseCodeSeriesService purchaseCodeSeriesSvcMock;

    private PurchaseOrderPostService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(paymentMethodMock.getBankAccount()).thenReturn(bankAccountMock);

        when(purchaseOrderMock.getCode()).thenReturn(TEST_CODE);
        when(purchaseOrderMock.getVendor()).thenReturn(vendorMock);
        when(purchaseOrderMock.getDate()).thenReturn(TEST_DATE);
        when(purchaseOrderMock.getPaymentMethod()).thenReturn(paymentMethodMock);

        PurchaseOrderLineId purchaseOrderLineId = new PurchaseOrderLineId();
        purchaseOrderLineId.setPurchaseOrder(purchaseOrderMock);
        purchaseOrderLineId.setLineNo(TEST_LINE_NO);

        when(purchaseOrderLineMock.getPurchaseOrderLineId()).thenReturn(purchaseOrderLineId);
        when(purchaseOrderLineMock.getItem()).thenReturn(itemMock);
        when(purchaseOrderLineMock.getQuantity()).thenReturn(TEST_QTY);
        when(purchaseOrderLineMock.getPrice()).thenReturn(TEST_PRICE);
        when(purchaseOrderLineMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(purchaseOrderSvcMock.get(TEST_CODE)).thenReturn(purchaseOrderMock);

        when(purchaseOrderLineSvcMock.list(purchaseOrderMock))
                .thenReturn(Arrays.asList(purchaseOrderLineMock, purchaseOrderLineMock));

        when(purchaseCodeSeriesSvcMock.postedOrderCode()).thenReturn(NEW_POSTED_ORDER_CODE);

        when(bankAccountMock.getCode()).thenReturn(TEST_BANK_ACCOUNT_CODE);
        when(bankAccountMock.getBalance()).thenReturn(TEST_BANK_ACCOUNT_BALANCE);
        when(bankAccountSvcMock.get(TEST_BANK_ACCOUNT_CODE)).thenReturn(bankAccountMock);

        svc = new PurchaseOrderPostServiceImpl(purchaseOrderSvcMock, purchaseOrderLineSvcMock,
                postedPurchaseOrderSvcMock, postedPurchaseOrderLineSvcMock, itemLedgerEntrySvcMock,
                vendorLedgerEntrySvcMock, bankAccountLedgerEntrySvcMock, bankAccountSvcMock, 
                purchaseCodeSeriesSvcMock);
    }

    @Test
    public void allRelatedInteractionsAreDone() throws PostFailedException {
        svc.post(TEST_CODE);

        PostedPurchaseOrder postedPurchaseOrder = verifyPostedPurchaseOrderCreated();

        verifyPostedPurchaseOrderLinesCreated(postedPurchaseOrder);

        verifyItemLedgerEntriesCreated(postedPurchaseOrder);

        verifyVendorLedgerEntriesCreated(postedPurchaseOrder);

        verifyBankAccountLedgerEntryCreated(postedPurchaseOrder);

        verifyPurchaseOrderLinesDeleted();

        verifyPurchaseOrderDeleted();
    }

    @Test
    public void paymentRelatedEntriesNotCreated_whenPaymentMethodDoesNotHaveBankAccount() throws PostFailedException {
        when(paymentMethodMock.getBankAccount()).thenReturn(null);

        svc.post(TEST_CODE);

        PostedPurchaseOrder postedPurchaseOrder = verifyPostedPurchaseOrderCreated();

        verifyVendorLedgerEntriesCreated(postedPurchaseOrder);

        verifyNoBankAccountLedgerEntiresCreated();
    }

    @Test
    public void postingFails_whenBankAccountDoesNotHaveEnoughBalance () throws PostFailedException {
        when(bankAccountMock.getBalance()).thenReturn(0.0);

        assertThrows(PostFailedException.class, () -> {svc.post(TEST_CODE);});
    }

    private void verifyNoBankAccountLedgerEntiresCreated() {
        verify(bankAccountLedgerEntrySvcMock, never()).create(any(BankAccountLedgerEntry.class));
    }

    private void verifyPurchaseOrderDeleted() {
        verify(purchaseOrderSvcMock).delete(TEST_CODE);
    }

    private void verifyPurchaseOrderLinesDeleted() {
        PurchaseOrderLineId purchaseOrderLineId = new PurchaseOrderLineId();
        purchaseOrderLineId.setPurchaseOrder(purchaseOrderMock);
        purchaseOrderLineId.setLineNo(TEST_LINE_NO);

        verify(purchaseOrderLineSvcMock, times(2)).delete(purchaseOrderLineId);
    }

    private void verifyBankAccountLedgerEntryCreated(PostedPurchaseOrder postedPurchaseOrder) {
        verify(bankAccountLedgerEntrySvcMock).create(bankAccountLedgerEntryArgumentCaptor.capture());

        BankAccountLedgerEntry bankAccountLedgerEntry = bankAccountLedgerEntryArgumentCaptor.getValue();

        assertEquals(bankAccountMock, bankAccountLedgerEntry.getBankAccount());
        assertEquals(BankAccountLedgerEntryType.VENDOR_PAYMENT, bankAccountLedgerEntry.getType());
        assertEquals(-(TEST_AMOUNT * 2), bankAccountLedgerEntry.getAmount());
        assertEquals(postedPurchaseOrder.getCode(), bankAccountLedgerEntry.getDocumentCode());
    }

    private void verifyVendorLedgerEntriesCreated(PostedPurchaseOrder postedPurchaseOrder) {
        int expectedNumberOfVendorLedgerEntries = 1;

        if (postedPurchaseOrder.getPaymentMethod().getBankAccount() != null){
            expectedNumberOfVendorLedgerEntries++;
        }

        verify(vendorLedgerEntrySvcMock, times(expectedNumberOfVendorLedgerEntries)).create(vendorLedgerEntryArgumentCaptor.capture());

        List<VendorLedgerEntry> vendorLedgerEntries = vendorLedgerEntryArgumentCaptor.getAllValues();

        VendorLedgerEntry documentVendorLedgerEntry = vendorLedgerEntries.get(0);
        verifyDocumentVendorLedgerEntry(documentVendorLedgerEntry, postedPurchaseOrder);

        if (postedPurchaseOrder.getPaymentMethod().getBankAccount() != null) {
            VendorLedgerEntry paymentVendorLedgerEntry = vendorLedgerEntries.get(1);
            verifyPaymentVendorLedgerEntry(paymentVendorLedgerEntry, postedPurchaseOrder);    
        }
    }

    private void verifyPaymentVendorLedgerEntry(VendorLedgerEntry paymentVendorLedgerEntry,
            PostedPurchaseOrder postedPurchaseOrder) {
        assertEquals(vendorMock, paymentVendorLedgerEntry.getVendor());
        assertEquals(VendorLedgerEntryType.PAYMENT, paymentVendorLedgerEntry.getType());
        assertEquals(-(TEST_AMOUNT * 2), paymentVendorLedgerEntry.getAmount());
        assertEquals(postedPurchaseOrder.getCode(), paymentVendorLedgerEntry.getDocumentCode());
    }

    private void verifyDocumentVendorLedgerEntry(VendorLedgerEntry documentVendorLedgerEntry,
            PostedPurchaseOrder postedPurchaseOrder) {
        assertEquals(vendorMock, documentVendorLedgerEntry.getVendor());
        assertEquals(VendorLedgerEntryType.PURCHASE_ORDER, documentVendorLedgerEntry.getType());
        assertEquals(TEST_AMOUNT * 2, documentVendorLedgerEntry.getAmount());
        assertEquals(postedPurchaseOrder.getCode(), documentVendorLedgerEntry.getDocumentCode());
    }

    private void verifyItemLedgerEntriesCreated(PostedPurchaseOrder postedPurchaseOrder) {
        verify(itemLedgerEntrySvcMock, times(2)).create(itemLedgerEntryArgumentCaptor.capture());

        ItemLedgerEntry itemLedgerEntry = itemLedgerEntryArgumentCaptor.getValue();

        assertEquals(ItemLedgerEntryType.PURCHASE_ORDER, itemLedgerEntry.getType());
        assertEquals(TEST_QTY, itemLedgerEntry.getQuantity());
        assertEquals(postedPurchaseOrder.getCode(), itemLedgerEntry.getDocumentCode());
    }

    private void verifyPostedPurchaseOrderLinesCreated(PostedPurchaseOrder postedPurchaseOrder) {
        verify(postedPurchaseOrderLineSvcMock, times(2)).create(postedPurchaseOrderLineArgumentCaptor.capture());

        PostedPurchaseOrderLine postedPurchaseOrderLine = postedPurchaseOrderLineArgumentCaptor.getValue();

        assertEquals(postedPurchaseOrder,
                postedPurchaseOrderLine.getPostedPurchaseOrderLineId().getPostedPurchaseOrder());
        assertEquals(TEST_LINE_NO, postedPurchaseOrderLine.getPostedPurchaseOrderLineId().getLineNo());
        assertEquals(itemMock, postedPurchaseOrderLine.getItem());
        assertEquals(TEST_QTY, postedPurchaseOrderLine.getQuantity());
        assertEquals(TEST_PRICE, postedPurchaseOrderLine.getPrice());
        assertEquals(TEST_AMOUNT, postedPurchaseOrderLine.getAmount());
    }

    private PostedPurchaseOrder verifyPostedPurchaseOrderCreated() {
        verify(postedPurchaseOrderSvcMock).create(postedPurchaseOrderArgumentCaptor.capture());

        PostedPurchaseOrder postedPurchaseOrder = postedPurchaseOrderArgumentCaptor.getValue();

        assertEquals(NEW_POSTED_ORDER_CODE, postedPurchaseOrder.getCode());
        assertEquals(vendorMock, postedPurchaseOrder.getVendor());
        assertEquals(paymentMethodMock, postedPurchaseOrder.getPaymentMethod());
        assertEquals(TEST_CODE, postedPurchaseOrder.getOrderCode());
        assertEquals(TEST_DATE, postedPurchaseOrder.getOrderDate());

        return postedPurchaseOrder;
    }
}
