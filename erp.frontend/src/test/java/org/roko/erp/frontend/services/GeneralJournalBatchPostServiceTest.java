package org.roko.erp.frontend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;

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
import org.roko.erp.frontend.model.GeneralJournalBatch;
import org.roko.erp.frontend.model.GeneralJournalBatchLine;
import org.roko.erp.frontend.model.GeneralJournalBatchLineOperationType;
import org.roko.erp.frontend.model.GeneralJournalBatchLineType;
import org.roko.erp.frontend.model.Vendor;
import org.roko.erp.frontend.model.VendorLedgerEntry;
import org.roko.erp.frontend.model.VendorLedgerEntryType;
import org.roko.erp.frontend.model.jpa.GeneralJournalBatchLineId;

public class GeneralJournalBatchPostServiceTest {

    private static final String TEST_CODE = "test-code";
    private static final int TEST_LINE_NO = 234;
    private static final String TEST_DOCUMENT_CODE = "test-document-code";
    private static final Date TEST_DATE = new Date();
    private static final Double TEST_AMOUNT = 123.45;

    private static final String TEST_CUSTOMER_CODE = "test-customer-code";
    private static final String TEST_VENDOR_CODE = "test-vendor-code";
    private static final String TEST_BANK_ACCOUNT_CODE = "test-bank-account-code";


    @Captor
    private ArgumentCaptor<CustomerLedgerEntry> customerLedgerEntryArgumentCaptor;

    @Captor
    private ArgumentCaptor<VendorLedgerEntry> vendorLedgerEntryArgumentCaptor;

    @Captor
    private ArgumentCaptor<BankAccountLedgerEntry> bankAccountLedgerEntryArgumentCaptor;

    private GeneralJournalBatchLineId generalJournalBatchLineId;

    @Mock
    private GeneralJournalBatchService generalJournalBatchSvcMock;

    @Mock
    private GeneralJournalBatch generalJournalBatchMock;

    @Mock
    private GeneralJournalBatchLineService generalJournalBatchLineSvcMock;

    @Mock
    private GeneralJournalBatchLine generalJournalBatchLineMock;

    @Mock
    private CustomerLedgerEntryService customerLedgerEntrySvcMock;

    @Mock
    private CustomerService customerSvcMock;

    @Mock
    private Customer customerMock;

    @Mock
    private VendorLedgerEntryService vendorLedgerEntrySvcMock;

    @Mock
    private VendorService vendorSvcMock;

    @Mock
    private Vendor vendorMock;

    @Mock
    private BankAccountLedgerEntryService bankAccountLedgerEntrySvcMock;

    @Mock
    private BankAccountService bankAccountSvcMock;

    @Mock
    private BankAccount bankAccountMock;

    private GeneralJournalBatchPostService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        generalJournalBatchLineId = new GeneralJournalBatchLineId();
        generalJournalBatchLineId.setGeneralJournalBatch(generalJournalBatchMock);
        generalJournalBatchLineId.setLineNo(TEST_LINE_NO);
        
        when(generalJournalBatchLineMock.getGeneralJournalBatchLineId()).thenReturn(generalJournalBatchLineId);
        when(generalJournalBatchLineMock.getDocumentCode()).thenReturn(TEST_DOCUMENT_CODE);
        when(generalJournalBatchLineMock.getDate()).thenReturn(TEST_DATE);
        when(generalJournalBatchLineMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(generalJournalBatchSvcMock.get(TEST_CODE)).thenReturn(generalJournalBatchMock);

        when(generalJournalBatchLineSvcMock.list(generalJournalBatchMock))
                .thenReturn(Arrays.asList(generalJournalBatchLineMock));

        when(customerSvcMock.get(TEST_CUSTOMER_CODE)).thenReturn(customerMock);

        when(vendorSvcMock.get(TEST_VENDOR_CODE)).thenReturn(vendorMock);

        when(bankAccountSvcMock.get(TEST_BANK_ACCOUNT_CODE)).thenReturn(bankAccountMock);

        svc = new GeneralJournalBatchPostServiceImpl(generalJournalBatchSvcMock, generalJournalBatchLineSvcMock,
                customerLedgerEntrySvcMock, customerSvcMock, vendorLedgerEntrySvcMock, vendorSvcMock,
                bankAccountLedgerEntrySvcMock, bankAccountSvcMock);
    }

    @Test
    public void customerLedgerEntryIsCreated_whenSourceTypeCustomer_opTypeOrder() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.CUSTOMER);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.ORDER);

        svc.post(TEST_CODE);

        verify(customerLedgerEntrySvcMock).create(customerLedgerEntryArgumentCaptor.capture());

        CustomerLedgerEntry customerLedgerEntry = customerLedgerEntryArgumentCaptor.getValue();

        assertEquals(customerMock, customerLedgerEntry.getCustomer());
        assertEquals(CustomerLedgerEntryType.SALES_ORDER, customerLedgerEntry.getType());
        assertEquals(TEST_AMOUNT, customerLedgerEntry.getAmount());
        assertEquals(TEST_DATE, customerLedgerEntry.getDate());
        assertEquals(TEST_DOCUMENT_CODE, customerLedgerEntry.getDocumentCode());
    }

    @Test
    public void noBankAccountLedgerEntryCreated_whenSourceTypeCustomer_opTypeOrder_noBankAccountSpecified() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.CUSTOMER);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.ORDER);

        svc.post(TEST_CODE);

        verify(bankAccountLedgerEntrySvcMock, never()).create(any(BankAccountLedgerEntry.class));
    }

    @Test
    public void noBankAccountLedgerEntryCreated_whenSourceTypeCustomer_opTypeOrder_bankAccountSpecified() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.CUSTOMER);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.ORDER);
        when(generalJournalBatchLineMock.getTarget()).thenReturn(bankAccountMock);

        svc.post(TEST_CODE);

        verify(bankAccountLedgerEntrySvcMock, never()).create(any(BankAccountLedgerEntry.class));
    }

    @Test
    public void customerLedgerEntryIsCreated_whenSourceTypeCustomer_opTypeCreditMemo() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.CUSTOMER);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.CREDIT_MEMO);

        svc.post(TEST_CODE);

        verify(customerLedgerEntrySvcMock).create(customerLedgerEntryArgumentCaptor.capture());

        CustomerLedgerEntry customerLedgerEntry = customerLedgerEntryArgumentCaptor.getValue();

        assertEquals(customerMock, customerLedgerEntry.getCustomer());
        assertEquals(CustomerLedgerEntryType.SALES_CREDIT_MEMO, customerLedgerEntry.getType());
        assertEquals(-TEST_AMOUNT, customerLedgerEntry.getAmount());
        assertEquals(TEST_DATE, customerLedgerEntry.getDate());
        assertEquals(TEST_DOCUMENT_CODE, customerLedgerEntry.getDocumentCode());
    }

    @Test
    public void noBankAccountLedgerEntryCreated_whenSourceTypeCustomer_opTypeCreditMemo_noBankAccountSpecified() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.CUSTOMER);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.CREDIT_MEMO);

        svc.post(TEST_CODE);

        verify(bankAccountLedgerEntrySvcMock, never()).create(any(BankAccountLedgerEntry.class));
    }

    @Test
    public void noBankAccountLedgerEntryCreated_whenSourceTypeCustomer_opTypeCreditMemo_bankAccountSpecified() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.CUSTOMER);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.CREDIT_MEMO);
        when(generalJournalBatchLineMock.getTarget()).thenReturn(bankAccountMock);

        svc.post(TEST_CODE);

        verify(bankAccountLedgerEntrySvcMock, never()).create(any(BankAccountLedgerEntry.class));
    }

    @Test
    public void customerLedgerEntryIsCreated_whenSourceTypeCustomer_opTypePayment() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.CUSTOMER);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.PAYMENT);

        svc.post(TEST_CODE);

        verify(customerLedgerEntrySvcMock).create(customerLedgerEntryArgumentCaptor.capture());

        CustomerLedgerEntry customerLedgerEntry = customerLedgerEntryArgumentCaptor.getValue();

        assertEquals(customerMock, customerLedgerEntry.getCustomer());
        assertEquals(CustomerLedgerEntryType.PAYMENT, customerLedgerEntry.getType());
        assertEquals(-TEST_AMOUNT, customerLedgerEntry.getAmount());
        assertEquals(TEST_DATE, customerLedgerEntry.getDate());
        assertEquals(TEST_DOCUMENT_CODE, customerLedgerEntry.getDocumentCode());
    }

    @Test
    public void noBankAccountLedgerEntryCreated_whenSourceTypeCustomer_opTypePayment_noBankAccountSpecified() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.CUSTOMER);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.PAYMENT);

        svc.post(TEST_CODE);

        verify(bankAccountLedgerEntrySvcMock, never()).create(any(BankAccountLedgerEntry.class));
    }

    @Test
    public void bankAccountLedgerEntryCreated_whenSourceTypeCustomer_opTypePayment_bankAccountSpecified() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.CUSTOMER);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.PAYMENT);
        when(generalJournalBatchLineMock.getTarget()).thenReturn(bankAccountMock);

        svc.post(TEST_CODE);

        verify(bankAccountLedgerEntrySvcMock).create(bankAccountLedgerEntryArgumentCaptor.capture());

        BankAccountLedgerEntry bankAccountLedgerEntry = bankAccountLedgerEntryArgumentCaptor.getValue();
        assertEquals(bankAccountMock, bankAccountLedgerEntry.getBankAccount());
        assertEquals(BankAccountLedgerEntryType.CUSTOMER_PAYMENT, bankAccountLedgerEntry.getType());
        assertEquals(TEST_AMOUNT, bankAccountLedgerEntry.getAmount());
        assertEquals(TEST_DOCUMENT_CODE, bankAccountLedgerEntry.getDocumentCode());
        assertEquals(TEST_DATE, bankAccountLedgerEntry.getDate());
    }

    @Test
    public void customerLedgerEntryIsCreated_whenSourceTypeCustomer_opTypeRefund() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.CUSTOMER);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.REFUND);

        svc.post(TEST_CODE);

        verify(customerLedgerEntrySvcMock).create(customerLedgerEntryArgumentCaptor.capture());

        CustomerLedgerEntry customerLedgerEntry = customerLedgerEntryArgumentCaptor.getValue();

        assertEquals(customerMock, customerLedgerEntry.getCustomer());
        assertEquals(CustomerLedgerEntryType.REFUND, customerLedgerEntry.getType());
        assertEquals(TEST_AMOUNT, customerLedgerEntry.getAmount());
        assertEquals(TEST_DATE, customerLedgerEntry.getDate());
        assertEquals(TEST_DOCUMENT_CODE, customerLedgerEntry.getDocumentCode());
    }

    @Test
    public void noBankAccountLedgerEntryCreated_whenSourceTypeCustomer_opTypeRefund_noBankAccountSpecified() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.CUSTOMER);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.REFUND);

        svc.post(TEST_CODE);

        verify(bankAccountLedgerEntrySvcMock, never()).create(any(BankAccountLedgerEntry.class));
    }

    @Test
    public void bankAccountLedgerEntryCreated_whenSourceTypeCustomer_opTypeRefund_bankAccountSpecified() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.CUSTOMER);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.REFUND);
        when(generalJournalBatchLineMock.getTarget()).thenReturn(bankAccountMock);

        svc.post(TEST_CODE);

        verify(bankAccountLedgerEntrySvcMock).create(bankAccountLedgerEntryArgumentCaptor.capture());

        BankAccountLedgerEntry bankAccountLedgerEntry = bankAccountLedgerEntryArgumentCaptor.getValue();
        assertEquals(bankAccountMock, bankAccountLedgerEntry.getBankAccount());
        assertEquals(BankAccountLedgerEntryType.CUSTOMER_REFUND, bankAccountLedgerEntry.getType());
        assertEquals(-TEST_AMOUNT, bankAccountLedgerEntry.getAmount());
        assertEquals(TEST_DOCUMENT_CODE, bankAccountLedgerEntry.getDocumentCode());
        assertEquals(TEST_DATE, bankAccountLedgerEntry.getDate());
    }


    @Test
    public void vendorLedgerEntryIsCreated_whenSourceTypeVendor_opTypeOrder() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.VENDOR);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_VENDOR_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.ORDER);

        svc.post(TEST_CODE);

        verify(vendorLedgerEntrySvcMock).create(vendorLedgerEntryArgumentCaptor.capture());

        VendorLedgerEntry vendorLedgerEntry = vendorLedgerEntryArgumentCaptor.getValue();

        assertEquals(vendorMock, vendorLedgerEntry.getVendor());
        assertEquals(VendorLedgerEntryType.PURCHASE_ORDER, vendorLedgerEntry.getType());
        assertEquals(TEST_AMOUNT, vendorLedgerEntry.getAmount());
        assertEquals(TEST_DATE, vendorLedgerEntry.getDate());
        assertEquals(TEST_DOCUMENT_CODE, vendorLedgerEntry.getDocumentCode());
    }

    @Test
    public void noBankAccountLedgerEntryCreated_whenSourceTypeVendor_opTypeOrder_noBankAccountSpecified() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.VENDOR);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_VENDOR_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.ORDER);

        svc.post(TEST_CODE);

        verify(bankAccountLedgerEntrySvcMock, never()).create(any(BankAccountLedgerEntry.class));
    }

    @Test
    public void noBankAccountLedgerEntryCreated_whenSourceTypeVendor_opTypeOrder_bankAccountSpecified() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.VENDOR);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_VENDOR_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.ORDER);
        when(generalJournalBatchLineMock.getTarget()).thenReturn(bankAccountMock);

        svc.post(TEST_CODE);

        verify(bankAccountLedgerEntrySvcMock, never()).create(any(BankAccountLedgerEntry.class));
    }

    @Test
    public void vendorLedgerEntryIsCreated_whenSourceTypeVendor_opTypeCreditMemo() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.VENDOR);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_VENDOR_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.CREDIT_MEMO);

        svc.post(TEST_CODE);

        verify(vendorLedgerEntrySvcMock).create(vendorLedgerEntryArgumentCaptor.capture());

        VendorLedgerEntry vendorLedgerEntry = vendorLedgerEntryArgumentCaptor.getValue();

        assertEquals(vendorMock, vendorLedgerEntry.getVendor());
        assertEquals(VendorLedgerEntryType.PURCHASE_CREDIT_MEMO, vendorLedgerEntry.getType());
        assertEquals(-TEST_AMOUNT, vendorLedgerEntry.getAmount());
        assertEquals(TEST_DATE, vendorLedgerEntry.getDate());
        assertEquals(TEST_DOCUMENT_CODE, vendorLedgerEntry.getDocumentCode());
    }

    @Test
    public void noBankAccountLedgerEntryCreated_whenSourceTypeVendor_opTypeCreditMemo_noBankAccountSpecified() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.VENDOR);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_VENDOR_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.CREDIT_MEMO);

        svc.post(TEST_CODE);

        verify(bankAccountLedgerEntrySvcMock, never()).create(any(BankAccountLedgerEntry.class));
    }

    @Test
    public void noBankAccountLedgerEntryCreated_whenSourceTypeVendor_opTypeCreditMemo_bankAccountSpecified() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.VENDOR);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_VENDOR_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.CREDIT_MEMO);
        when(generalJournalBatchLineMock.getTarget()).thenReturn(bankAccountMock);

        svc.post(TEST_CODE);

        verify(bankAccountLedgerEntrySvcMock, never()).create(any(BankAccountLedgerEntry.class));
    }

    @Test
    public void vendorLedgerEntryIsCreated_whenSourceTypeVendor_opTypePayment() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.VENDOR);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_VENDOR_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.PAYMENT);

        svc.post(TEST_CODE);

        verify(vendorLedgerEntrySvcMock).create(vendorLedgerEntryArgumentCaptor.capture());

        VendorLedgerEntry vendorLedgerEntry = vendorLedgerEntryArgumentCaptor.getValue();

        assertEquals(vendorMock, vendorLedgerEntry.getVendor());
        assertEquals(VendorLedgerEntryType.PAYMENT, vendorLedgerEntry.getType());
        assertEquals(-TEST_AMOUNT, vendorLedgerEntry.getAmount());
        assertEquals(TEST_DATE, vendorLedgerEntry.getDate());
        assertEquals(TEST_DOCUMENT_CODE, vendorLedgerEntry.getDocumentCode());
    }

    @Test
    public void noBankAccountLedgerEntryCreated_whenSourceTypeVendor_opTypePayment_noBankAccountSpecified() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.VENDOR);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_VENDOR_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.PAYMENT);

        svc.post(TEST_CODE);

        verify(bankAccountLedgerEntrySvcMock, never()).create(any(BankAccountLedgerEntry.class));
    }

    @Test
    public void bankAccountLedgerEntryCreated_whenSourceTypeVendor_opTypePayment_bankAccountSpecified() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.VENDOR);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_VENDOR_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.PAYMENT);
        when(generalJournalBatchLineMock.getTarget()).thenReturn(bankAccountMock);

        svc.post(TEST_CODE);

        verify(bankAccountLedgerEntrySvcMock).create(bankAccountLedgerEntryArgumentCaptor.capture());

        BankAccountLedgerEntry bankAccountLedgerEntry = bankAccountLedgerEntryArgumentCaptor.getValue();
        assertEquals(bankAccountMock, bankAccountLedgerEntry.getBankAccount());
        assertEquals(BankAccountLedgerEntryType.VENDOR_PAYMENT, bankAccountLedgerEntry.getType());
        assertEquals(-TEST_AMOUNT, bankAccountLedgerEntry.getAmount());
        assertEquals(TEST_DOCUMENT_CODE, bankAccountLedgerEntry.getDocumentCode());
        assertEquals(TEST_DATE, bankAccountLedgerEntry.getDate());
    }

    @Test
    public void vendorLedgerEntryIsCreated_whenSourceTypeVendor_opTypeRefund() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.VENDOR);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_VENDOR_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.REFUND);

        svc.post(TEST_CODE);

        verify(vendorLedgerEntrySvcMock).create(vendorLedgerEntryArgumentCaptor.capture());

        VendorLedgerEntry vendorLedgerEntry = vendorLedgerEntryArgumentCaptor.getValue();

        assertEquals(vendorMock, vendorLedgerEntry.getVendor());
        assertEquals(VendorLedgerEntryType.REFUND, vendorLedgerEntry.getType());
        assertEquals(TEST_AMOUNT, vendorLedgerEntry.getAmount());
        assertEquals(TEST_DATE, vendorLedgerEntry.getDate());
        assertEquals(TEST_DOCUMENT_CODE, vendorLedgerEntry.getDocumentCode());
    }

    @Test
    public void noBankAccountLedgerEntryCreated_whenSourceTypeVendor_opTypeRefund_noBankAccountSpecified() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.VENDOR);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_VENDOR_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.REFUND);

        svc.post(TEST_CODE);

        verify(bankAccountLedgerEntrySvcMock, never()).create(any(BankAccountLedgerEntry.class));
    }

    @Test
    public void bankAccountLedgerEntryCreated_whenSourceTypeVendor_opTypeRefund_bankAccountSpecified() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.VENDOR);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_VENDOR_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.REFUND);
        when(generalJournalBatchLineMock.getTarget()).thenReturn(bankAccountMock);

        svc.post(TEST_CODE);

        verify(bankAccountLedgerEntrySvcMock).create(bankAccountLedgerEntryArgumentCaptor.capture());

        BankAccountLedgerEntry bankAccountLedgerEntry = bankAccountLedgerEntryArgumentCaptor.getValue();
        assertEquals(bankAccountMock, bankAccountLedgerEntry.getBankAccount());
        assertEquals(BankAccountLedgerEntryType.VENDOR_REFUND, bankAccountLedgerEntry.getType());
        assertEquals(TEST_AMOUNT, bankAccountLedgerEntry.getAmount());
        assertEquals(TEST_DOCUMENT_CODE, bankAccountLedgerEntry.getDocumentCode());
        assertEquals(TEST_DATE, bankAccountLedgerEntry.getDate());
    }

    @Test
    public void bankAccountLedgerEntryIsCreated_whenSourceTypeBankAccount() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.BANK_ACCOUNT);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_BANK_ACCOUNT_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.EMPTY);

        svc.post(TEST_CODE);

        verify(bankAccountLedgerEntrySvcMock, times(1)).create(bankAccountLedgerEntryArgumentCaptor.capture());

        BankAccountLedgerEntry bankAccountLedgerEntry = bankAccountLedgerEntryArgumentCaptor.getValue();

        assertEquals(bankAccountMock, bankAccountLedgerEntry.getBankAccount());
        assertEquals(BankAccountLedgerEntryType.FREE_ENTRY, bankAccountLedgerEntry.getType());
        assertEquals(TEST_AMOUNT, bankAccountLedgerEntry.getAmount());
        assertEquals(TEST_DATE, bankAccountLedgerEntry.getDate());
        assertEquals(TEST_DOCUMENT_CODE, bankAccountLedgerEntry.getDocumentCode());
    }

    @Test
    public void twoBankAccountLedgerEntriesCreated_whenSourceTypeBankAccount_targetBankAccountSpecified() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.BANK_ACCOUNT);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_BANK_ACCOUNT_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.EMPTY);
        when(generalJournalBatchLineMock.getTarget()).thenReturn(bankAccountMock);

        svc.post(TEST_CODE);

        verify(bankAccountLedgerEntrySvcMock, times(2)).create(bankAccountLedgerEntryArgumentCaptor.capture());

        BankAccountLedgerEntry bankAccountLedgerEntry = bankAccountLedgerEntryArgumentCaptor.getAllValues().get(0);

        assertEquals(bankAccountMock, bankAccountLedgerEntry.getBankAccount());
        assertEquals(BankAccountLedgerEntryType.FREE_ENTRY, bankAccountLedgerEntry.getType());
        assertEquals(TEST_AMOUNT, bankAccountLedgerEntry.getAmount());
        assertEquals(TEST_DATE, bankAccountLedgerEntry.getDate());
        assertEquals(TEST_DOCUMENT_CODE, bankAccountLedgerEntry.getDocumentCode());

        bankAccountLedgerEntry = bankAccountLedgerEntryArgumentCaptor.getAllValues().get(1);

        assertEquals(bankAccountMock, bankAccountLedgerEntry.getBankAccount());
        assertEquals(BankAccountLedgerEntryType.FREE_ENTRY, bankAccountLedgerEntry.getType());
        assertEquals(-TEST_AMOUNT, bankAccountLedgerEntry.getAmount());
        assertEquals(TEST_DATE, bankAccountLedgerEntry.getDate());
        assertEquals(TEST_DOCUMENT_CODE, bankAccountLedgerEntry.getDocumentCode());
    }

    @Test
    public void generalJournalBatchLinesAreDeleted_whenPost() throws PostFailedException{
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.CUSTOMER);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.ORDER);

        svc.post(TEST_CODE);

        verify(generalJournalBatchLineSvcMock).delete(generalJournalBatchLineId);
    }
}
