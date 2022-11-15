package org.roko.erp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.roko.erp.model.Item;
import org.roko.erp.model.ItemLedgerEntry;
import org.roko.erp.model.ItemLedgerEntryType;
import org.roko.erp.model.PaymentMethod;
import org.roko.erp.model.PostedPurchaseOrder;
import org.roko.erp.model.PostedPurchaseOrderLine;
import org.roko.erp.model.PurchaseOrder;
import org.roko.erp.model.PurchaseOrderLine;
import org.roko.erp.model.Vendor;
import org.roko.erp.model.VendorLedgerEntry;
import org.roko.erp.model.VendorLedterEntryType;
import org.roko.erp.model.jpa.PurchaseOrderLineId;

public class PurchaseOrderPostServiceTest {

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

        svc = new PurchaseOrderPostServiceImpl(purchaseOrderSvcMock, purchaseOrderLineSvcMock,
                postedPurchaseOrderSvcMock, postedPurchaseOrderLineSvcMock, itemLedgerEntrySvcMock,
                vendorLedgerEntrySvcMock, bankAccountLedgerEntrySvcMock);
    }

    @Test
    public void allRelatedInteractionsAreDone() {
        svc.post(TEST_CODE);

        PostedPurchaseOrder postedPurchaseOrder = verifyPostedPurchaseOrderCreated();

        verifyPostedPurchaseOrderLinesCreated(postedPurchaseOrder);

        verifyItemLedgerEntriesCreated(postedPurchaseOrder);

        verifyVendorLedgerEntriesCreated(postedPurchaseOrder);

        verifyBankAccountLedgerEntryCreated(postedPurchaseOrder);

        verifyPurchaseOrderLinesDeleted();

        verifyPurchaseOrderDeleted();
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
        verify(vendorLedgerEntrySvcMock, times(2)).create(vendorLedgerEntryArgumentCaptor.capture());

        List<VendorLedgerEntry> vendorLedgerEntries = vendorLedgerEntryArgumentCaptor.getAllValues();

        VendorLedgerEntry documentVendorLedgerEntry = vendorLedgerEntries.get(0);
        VendorLedgerEntry paymentVendorLedgerEntry = vendorLedgerEntries.get(1);

        verifyDocumentVendorLedgerEntry(documentVendorLedgerEntry, postedPurchaseOrder);
        verifyPaymentVendorLedgerEntry(paymentVendorLedgerEntry, postedPurchaseOrder);
    }

    private void verifyPaymentVendorLedgerEntry(VendorLedgerEntry paymentVendorLedgerEntry,
            PostedPurchaseOrder postedPurchaseOrder) {
        assertEquals(vendorMock, paymentVendorLedgerEntry.getVendor());
        assertEquals(VendorLedterEntryType.PAYMENT, paymentVendorLedgerEntry.getType());
        assertEquals(-(TEST_AMOUNT * 2), paymentVendorLedgerEntry.getAmount());
        assertEquals(postedPurchaseOrder.getCode(), paymentVendorLedgerEntry.getDocumentCode());
    }

    private void verifyDocumentVendorLedgerEntry(VendorLedgerEntry documentVendorLedgerEntry,
            PostedPurchaseOrder postedPurchaseOrder) {
        assertEquals(vendorMock, documentVendorLedgerEntry.getVendor());
        assertEquals(VendorLedterEntryType.PURCHASE_ORDER, documentVendorLedgerEntry.getType());
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

        assertEquals(vendorMock, postedPurchaseOrder.getVendor());
        assertEquals(paymentMethodMock, postedPurchaseOrder.getPaymentMethod());
        assertEquals(TEST_CODE, postedPurchaseOrder.getOrderCode());
        assertEquals(TEST_DATE, postedPurchaseOrder.getOrderDate());

        return postedPurchaseOrder;
    }
}