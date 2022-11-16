package org.roko.erp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.roko.erp.model.Item;
import org.roko.erp.model.ItemLedgerEntry;
import org.roko.erp.model.ItemLedgerEntryType;
import org.roko.erp.model.PaymentMethod;
import org.roko.erp.model.PostedPurchaseCreditMemo;
import org.roko.erp.model.PostedPurchaseCreditMemoLine;
import org.roko.erp.model.PurchaseCreditMemo;
import org.roko.erp.model.PurchaseCreditMemoLine;
import org.roko.erp.model.Vendor;
import org.roko.erp.model.VendorLedgerEntry;
import org.roko.erp.model.VendorLedgerEntryType;
import org.roko.erp.model.jpa.PurchaseCreditMemoLineId;

public class PurchaseCreditMemoPostServiceTest {

    private static final String TEST_CODE = "test-code";

    private static final Date TEST_DATE = new Date();

    private static final int TEST_LINE_NO = 2;

    private static final Double TEST_QTY = 10.00;

    private static final Double TEST_PRICE = 12.00;

    private static final Double TEST_AMOUNT = 120.00;

    @Captor
    private ArgumentCaptor<PostedPurchaseCreditMemo> posteedPurchaseCreditMemoArgumentCaptor;

    @Captor
    private ArgumentCaptor<PostedPurchaseCreditMemoLine> postedPurchaseCreditMemoLineArgumentCaptor;

    @Captor
    private ArgumentCaptor<ItemLedgerEntry> itemLedgerEntryArgumentCaptor;

    @Captor
    private ArgumentCaptor<VendorLedgerEntry> vendorLedgerEntryArgumentCaptor;

    @Captor
    private ArgumentCaptor<BankAccountLedgerEntry> bankAccountLedgerEntryArgumentCaptor;

    @Mock
    private PurchaseCreditMemo purchaseCreditMemoMock;

    @Mock
    private PurchaseCreditMemoLine purchaseCreditMemoLineMock;

    @Mock
    private Item itemMock;

    @Mock
    private Vendor vendorMock;

    @Mock
    private PaymentMethod paymentMethodMock;

    @Mock
    private BankAccount bankAccountMock;

    @Mock
    private PurchaseCreditMemoService purchaseCreditMemoSvcMock;

    @Mock
    private PurchaseCreditMemoLineService purchaseCreditMemoLineSvcMock;

    @Mock
    private PostedPurchaseCreditMemoService postedPurchaseCreditMemoSvcMock;

    @Mock
    private PostedPurchaseCreditMemoLineService postedPurchaseCreditMemoLineSvcMock;

    @Mock
    private ItemLedgerEntryService itemLedgerEntrySvcMock;

    @Mock
    private VendorLedgerEntryService vendorLedgerEntrySvcMock;

    @Mock
    private BankAccountLedgerEntryService bankAccountLedgerEntrySvcMock;

    private PurchaseCreditMemoPostService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(paymentMethodMock.getBankAccount()).thenReturn(bankAccountMock);

        when(purchaseCreditMemoMock.getCode()).thenReturn(TEST_CODE);
        when(purchaseCreditMemoMock.getVendor()).thenReturn(vendorMock);
        when(purchaseCreditMemoMock.getPaymentMethod()).thenReturn(paymentMethodMock);
        when(purchaseCreditMemoMock.getDate()).thenReturn(TEST_DATE);

        when(purchaseCreditMemoSvcMock.get(TEST_CODE)).thenReturn(purchaseCreditMemoMock);

        PurchaseCreditMemoLineId purchaseCreditMemoLineId = new PurchaseCreditMemoLineId();
        purchaseCreditMemoLineId.setPurchaseCreditMemo(purchaseCreditMemoMock);
        purchaseCreditMemoLineId.setLineNo(TEST_LINE_NO);

        when(purchaseCreditMemoLineMock.getPurchaseCreditMemoLineId()).thenReturn(purchaseCreditMemoLineId);
        when(purchaseCreditMemoLineMock.getItem()).thenReturn(itemMock);
        when(purchaseCreditMemoLineMock.getQuantity()).thenReturn(TEST_QTY);
        when(purchaseCreditMemoLineMock.getPrice()).thenReturn(TEST_PRICE);
        when(purchaseCreditMemoLineMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(purchaseCreditMemoLineSvcMock.list(purchaseCreditMemoMock))
                .thenReturn(Arrays.asList(purchaseCreditMemoLineMock, purchaseCreditMemoLineMock));

        svc = new PurchaseCreditMemoPostServiceImpl(purchaseCreditMemoSvcMock, purchaseCreditMemoLineSvcMock,
                postedPurchaseCreditMemoSvcMock, postedPurchaseCreditMemoLineSvcMock, itemLedgerEntrySvcMock,
                vendorLedgerEntrySvcMock, bankAccountLedgerEntrySvcMock);
    }

    @Test
    public void allRelatedInteractionsAreDone() {
        svc.post(TEST_CODE);

        PostedPurchaseCreditMemo postedPurchaseCreditMemo = verifyPostedPurchaseCreditMemoCreated();

        verifyPostedPurchaseCreditMemoLinesCreated(postedPurchaseCreditMemo);

        verifyItemLedgerEntriesCreated(postedPurchaseCreditMemo);

        verifyVendorLedgerEntriesCreated(postedPurchaseCreditMemo);

        verifyBankAccountLedgerEntryCreated(postedPurchaseCreditMemo);

        verifyPurchaseCreditMemoLinesDeleted();

        verifyPurchaseCreditMemoDeleted();
    }

    @Test
    public void paymentRelatedEntriesAreNotCreated_whenPaymentMethodDoesNotHaveBankAccount() {
        when(paymentMethodMock.getBankAccount()).thenReturn(null);

        svc.post(TEST_CODE);

        PostedPurchaseCreditMemo postedPurchaseCreditMemo = verifyPostedPurchaseCreditMemoCreated();

        verifyVendorLedgerEntriesCreated(postedPurchaseCreditMemo);

        verifyNoBankAccountLedgerEntriesCreated();
    }

    private void verifyNoBankAccountLedgerEntriesCreated() {
        verify(bankAccountLedgerEntrySvcMock, never()).create(any(BankAccountLedgerEntry.class));
    }

    private void verifyPurchaseCreditMemoDeleted() {
        verify(purchaseCreditMemoSvcMock).delete(TEST_CODE);
    }

    private void verifyPurchaseCreditMemoLinesDeleted() {
        PurchaseCreditMemoLineId purchaseCreditMemoLineId = new PurchaseCreditMemoLineId();
        purchaseCreditMemoLineId.setPurchaseCreditMemo(purchaseCreditMemoMock);
        purchaseCreditMemoLineId.setLineNo(TEST_LINE_NO);

        verify(purchaseCreditMemoLineSvcMock, times(2)).delete(purchaseCreditMemoLineId);
    }

    private void verifyBankAccountLedgerEntryCreated(PostedPurchaseCreditMemo postedPurchaseCreditMemo) {
        verify(bankAccountLedgerEntrySvcMock).create(bankAccountLedgerEntryArgumentCaptor.capture());

        BankAccountLedgerEntry bankAccountLedgerEntry = bankAccountLedgerEntryArgumentCaptor.getValue();

        assertEquals(bankAccountMock, bankAccountLedgerEntry.getBankAccount());
        assertEquals(BankAccountLedgerEntryType.VENDOR_REFUND, bankAccountLedgerEntry.getType());
        assertEquals(TEST_AMOUNT * 2, bankAccountLedgerEntry.getAmount());
        assertEquals(postedPurchaseCreditMemo.getCode(), bankAccountLedgerEntry.getDocumentCode());
    }

    private void verifyVendorLedgerEntriesCreated(PostedPurchaseCreditMemo postedPurchaseCreditMemo) {
        int expectedNumberOfVendorLedgerEntryes = 1;

        if (postedPurchaseCreditMemo.getPaymentMethod().getBankAccount() != null) {
            expectedNumberOfVendorLedgerEntryes++;
        }

        verify(vendorLedgerEntrySvcMock, times(expectedNumberOfVendorLedgerEntryes))
                .create(vendorLedgerEntryArgumentCaptor.capture());

        List<VendorLedgerEntry> vendorLedgerEntries = vendorLedgerEntryArgumentCaptor.getAllValues();

        VendorLedgerEntry documentVendorLedgerEntry = vendorLedgerEntries.get(0);
        verifyDocumentVendorLedgerEntry(documentVendorLedgerEntry, postedPurchaseCreditMemo);

        if (postedPurchaseCreditMemo.getPaymentMethod().getBankAccount() != null) {
            VendorLedgerEntry paymentVendorLedgerEntry = vendorLedgerEntries.get(1);
            verifyPaymentVendorLedgerEntry(paymentVendorLedgerEntry, postedPurchaseCreditMemo);
        }
    }

    private void verifyPaymentVendorLedgerEntry(VendorLedgerEntry paymentVendorLedgerEntry,
            PostedPurchaseCreditMemo postedPurchaseCreditMemo) {
        assertEquals(vendorMock, paymentVendorLedgerEntry.getVendor());
        assertEquals(VendorLedgerEntryType.REFUND, paymentVendorLedgerEntry.getType());
        assertEquals(TEST_AMOUNT * 2, paymentVendorLedgerEntry.getAmount());
        assertEquals(postedPurchaseCreditMemo.getCode(), paymentVendorLedgerEntry.getDocumentCode());
    }

    private void verifyDocumentVendorLedgerEntry(VendorLedgerEntry documentVendorLedgerEntry,
            PostedPurchaseCreditMemo postedPurchaseCreditMemo) {
        assertEquals(vendorMock, documentVendorLedgerEntry.getVendor());
        assertEquals(VendorLedgerEntryType.PURCHASE_CREDIT_MEMO, documentVendorLedgerEntry.getType());
        assertEquals(-(TEST_AMOUNT * 2), documentVendorLedgerEntry.getAmount());
        assertEquals(postedPurchaseCreditMemo.getCode(), documentVendorLedgerEntry.getDocumentCode());
    }

    private void verifyItemLedgerEntriesCreated(PostedPurchaseCreditMemo postedPurchaseCreditMemo) {
        verify(itemLedgerEntrySvcMock, times(2)).create(itemLedgerEntryArgumentCaptor.capture());

        ItemLedgerEntry itemLedgerEntry = itemLedgerEntryArgumentCaptor.getValue();

        assertEquals(itemMock, itemLedgerEntry.getItem());
        assertEquals(ItemLedgerEntryType.PURCHASE_CREDIT_MEMO, itemLedgerEntry.getType());
        assertEquals(-TEST_QTY, itemLedgerEntry.getQuantity());
        assertEquals(postedPurchaseCreditMemo.getCode(), itemLedgerEntry.getDocumentCode());
    }

    private void verifyPostedPurchaseCreditMemoLinesCreated(PostedPurchaseCreditMemo postedPurchaseCreditMemo) {
        verify(postedPurchaseCreditMemoLineSvcMock, times(2))
                .create(postedPurchaseCreditMemoLineArgumentCaptor.capture());

        PostedPurchaseCreditMemoLine postedPurchaseCreditMemoLine = postedPurchaseCreditMemoLineArgumentCaptor
                .getValue();

        assertEquals(postedPurchaseCreditMemo,
                postedPurchaseCreditMemoLine.getPostedPurchaseCreditMemoLineId().getPostedPurchaseCreditMemo());
        assertEquals(TEST_LINE_NO, postedPurchaseCreditMemoLine.getPostedPurchaseCreditMemoLineId().getLineNo());
        assertEquals(itemMock, postedPurchaseCreditMemoLine.getItem());
        assertEquals(TEST_QTY, postedPurchaseCreditMemoLine.getQuantity());
        assertEquals(TEST_PRICE, postedPurchaseCreditMemoLine.getPrice());
        assertEquals(TEST_AMOUNT, postedPurchaseCreditMemoLine.getAmount());
    }

    private PostedPurchaseCreditMemo verifyPostedPurchaseCreditMemoCreated() {
        verify(postedPurchaseCreditMemoSvcMock).create(posteedPurchaseCreditMemoArgumentCaptor.capture());

        PostedPurchaseCreditMemo postedPurchaseCreditMemo = posteedPurchaseCreditMemoArgumentCaptor.getValue();

        assertEquals(vendorMock, postedPurchaseCreditMemo.getVendor());
        assertEquals(paymentMethodMock, postedPurchaseCreditMemo.getPaymentMethod());
        assertEquals(TEST_CODE, postedPurchaseCreditMemo.getOrderCode());
        assertEquals(TEST_DATE, postedPurchaseCreditMemo.getOrderDate());

        return postedPurchaseCreditMemo;
    }
}
