package org.roko.erp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import org.roko.erp.controllers.model.GeneralJournalBatchLineModel;
import org.roko.erp.controllers.model.GeneralJournalBatchLineSource;
import org.roko.erp.model.BankAccount;
import org.roko.erp.model.Customer;
import org.roko.erp.model.GeneralJournalBatch;
import org.roko.erp.model.GeneralJournalBatchLine;
import org.roko.erp.model.GeneralJournalBatchLineOperationType;
import org.roko.erp.model.GeneralJournalBatchLineType;
import org.roko.erp.model.Vendor;
import org.roko.erp.model.jpa.GeneralJournalBatchLineId;
import org.roko.erp.services.BankAccountService;
import org.roko.erp.services.CustomerService;
import org.roko.erp.services.GeneralJournalBatchLineService;
import org.roko.erp.services.GeneralJournalBatchService;
import org.roko.erp.services.VendorService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

public class GeneralJournalBatchLineControllerTest {

    private static final int BANK_ACCOUNTS_COUNT = 2;

    private static final String TEST_BANK_ACCOUNT_CODE_1 = "test-bank-account-code-1";
    private static final String TEST_BANK_ACCOUNT_CODE_2 = "test-bank-account-code-2";

    private static final String TEST_BANK_ACCOUNT_NAME_1 = "test-bank-account-name-1";
    private static final String TEST_BANK_ACCOUNT_NAME_2 = "test-bank-account-name-2";

    private static final int CUSTOMERS_COUNT = 3;

    private static final String TEST_CUSTOMER_CODE_1 = "test-customer-code-1";
    private static final String TEST_CUSTOMER_CODE_2 = "test-customer-code-2";
    private static final String TEST_CUSTOMER_CODE_3 = "test-customer-code-3";

    private static final String TEST_CUSTOMER_NAME_1 = "test-customer-name-1";
    private static final String TEST_CUSTOMER_NAME_2 = "test-customer-name-2";
    private static final String TEST_CUSTOMER_NAME_3 = "test-customer-name-3";

    private static final Integer VENDORS_COUNT = 2;

    private static final String TEST_VENDOR_CODE_1 = "test-vendor-code-1";
    private static final String TEST_VENDOR_CODE_2 = "test-vendor-code-2";

    private static final String TEST_VENDOR_NAME_1 = "test-vendor-name-1";
    private static final String TEST_VENDOR_NAME_2 = "test-vendor-name-2";

    private static final String TEST_CODE = "test-code";
    private static final int TEST_LINE_NO = 123;

    private static final GeneralJournalBatchLineType TEST_SOURCE_TYPE = GeneralJournalBatchLineType.VENDOR;
    private static final String TEST_SOURCE_CODE = "test-source-code";
    private static final String TEST_SOURCE_NAME = "test-source-name";
    private static final GeneralJournalBatchLineOperationType TEST_OPERATION_TYPE = GeneralJournalBatchLineOperationType.PAYMENT;
    private static final Date TEST_DATE = new Date();
    private static final String TEST_DOCUMENT_CODE = "test-document-code";
    private static final Double TEST_AMOUNT = 12.12;

    private List<BankAccount> bankAccounts;

    @Captor
    private ArgumentCaptor<List<GeneralJournalBatchLineSource>> sourcesArgumentCaptor;

    @Captor
    private ArgumentCaptor<GeneralJournalBatchLine> generalJournalBatchLineArgumentCaptor;

    @Captor
    private ArgumentCaptor<GeneralJournalBatchLineModel> generalJournalBatchLineModelArgumentCaptor;

    private GeneralJournalBatchLineId generalJournalBatchLineId;

    @Mock
    private GeneralJournalBatchLineService svcMock;

    @Mock
    private GeneralJournalBatchService generalJournalBatchSvcMock;

    @Mock
    private GeneralJournalBatch generalJournalBatchMock;

    @Mock
    private Model modelMock;

    @Mock
    private RedirectAttributes redirectAttributesMock;

    @Mock
    private GeneralJournalBatchLineModel generalJournalBatchLineModelMock;

    @Mock
    private BankAccountService bankAccountSvcMock;

    @Mock
    private BankAccount bankAccountMock1;

    @Mock
    private BankAccount bankAccountMock2;

    @Mock
    private CustomerService customerSvcMock;

    @Mock
    private Customer customerMock1;

    @Mock
    private Customer customerMock2;

    @Mock
    private Customer customerMock3;

    @Mock
    private VendorService vendorSvcMock;

    @Mock
    private Vendor vendorMock1;

    @Mock
    private Vendor vendorMock2;

    @Mock
    private GeneralJournalBatchLine generalJournalBatchLineMock;

    private GeneralJournalBatchLineController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        bankAccounts = Arrays.asList(bankAccountMock1, bankAccountMock2);

        when(bankAccountMock1.getCode()).thenReturn(TEST_BANK_ACCOUNT_CODE_1);
        when(bankAccountMock1.getName()).thenReturn(TEST_BANK_ACCOUNT_NAME_1);

        when(bankAccountMock2.getCode()).thenReturn(TEST_BANK_ACCOUNT_CODE_2);
        when(bankAccountMock2.getName()).thenReturn(TEST_BANK_ACCOUNT_NAME_2);

        when(bankAccountSvcMock.list()).thenReturn(bankAccounts);
        when(bankAccountSvcMock.get(TEST_BANK_ACCOUNT_CODE_1)).thenReturn(bankAccountMock1);

        when(customerMock1.getCode()).thenReturn(TEST_CUSTOMER_CODE_1);
        when(customerMock1.getName()).thenReturn(TEST_CUSTOMER_NAME_1);

        when(customerMock2.getCode()).thenReturn(TEST_CUSTOMER_CODE_2);
        when(customerMock2.getName()).thenReturn(TEST_CUSTOMER_NAME_2);

        when(customerMock3.getCode()).thenReturn(TEST_CUSTOMER_CODE_3);
        when(customerMock3.getName()).thenReturn(TEST_CUSTOMER_NAME_3);

        when(customerSvcMock.list()).thenReturn(Arrays.asList(customerMock1, customerMock2, customerMock3));

        when(vendorMock1.getCode()).thenReturn(TEST_VENDOR_CODE_1);
        when(vendorMock1.getName()).thenReturn(TEST_VENDOR_NAME_1);

        when(vendorMock2.getCode()).thenReturn(TEST_VENDOR_CODE_2);
        when(vendorMock2.getName()).thenReturn(TEST_VENDOR_NAME_2);

        when(vendorSvcMock.list()).thenReturn(Arrays.asList(vendorMock1, vendorMock2));

        when(generalJournalBatchLineModelMock.getGeneralJournalBatchCode()).thenReturn(TEST_CODE);

        when(generalJournalBatchSvcMock.get(TEST_CODE)).thenReturn(generalJournalBatchMock);

        generalJournalBatchLineId = new GeneralJournalBatchLineId();
        generalJournalBatchLineId.setGeneralJournalBatch(generalJournalBatchMock);
        generalJournalBatchLineId.setLineNo(TEST_LINE_NO);

        when(generalJournalBatchLineMock.getGeneralJournalBatchLineId()).thenReturn(generalJournalBatchLineId);
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(TEST_SOURCE_TYPE);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_SOURCE_CODE);
        when(generalJournalBatchLineMock.getSourceName()).thenReturn(TEST_SOURCE_NAME);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(TEST_OPERATION_TYPE);
        when(generalJournalBatchLineMock.getDate()).thenReturn(TEST_DATE);
        when(generalJournalBatchLineMock.getDocumentCode()).thenReturn(TEST_DOCUMENT_CODE);
        when(generalJournalBatchLineMock.getAmount()).thenReturn(TEST_AMOUNT);
        when(generalJournalBatchLineMock.getTarget()).thenReturn(bankAccountMock1);

        when(svcMock.get(generalJournalBatchLineId)).thenReturn(generalJournalBatchLineMock);

        controller = new GeneralJournalBatchLineController(svcMock, generalJournalBatchSvcMock, bankAccountSvcMock,
                customerSvcMock, vendorSvcMock);
    }

    @Test
    public void wizard_returnsProperTemplate() {
        String template = controller.wizard(TEST_CODE, 0, modelMock);

        verify(modelMock).addAttribute(eq("generalJournalBatchLine"), any(GeneralJournalBatchLineModel.class));

        assertEquals("generalJournalBatchLineWizardFirstPage.html", template);
    }

    @Test
    public void wizard_returnsProperTemplate_whenCalledForExisting() {
        controller.wizard(TEST_CODE, TEST_LINE_NO, modelMock);

        verify(modelMock).addAttribute(eq("generalJournalBatchLine"),
                generalJournalBatchLineModelArgumentCaptor.capture());

        GeneralJournalBatchLineModel generalJournalBatchLineModel = generalJournalBatchLineModelArgumentCaptor
                .getValue();

        assertEquals(TEST_CODE, generalJournalBatchLineModel.getGeneralJournalBatchCode());
        assertEquals(TEST_LINE_NO, generalJournalBatchLineModel.getLineNo());
        assertEquals(TEST_SOURCE_TYPE, generalJournalBatchLineModel.getSourceType());
        assertEquals(TEST_SOURCE_CODE, generalJournalBatchLineModel.getSourceCode());
        assertEquals(TEST_SOURCE_NAME, generalJournalBatchLineModel.getSourceName());
        assertEquals(TEST_OPERATION_TYPE, generalJournalBatchLineModel.getOperationType());
        assertEquals(TEST_DATE, generalJournalBatchLineModel.getDate());
        assertEquals(TEST_DOCUMENT_CODE, generalJournalBatchLineModel.getDocumentCode());
        assertEquals(TEST_AMOUNT, generalJournalBatchLineModel.getAmount());
        assertEquals(TEST_BANK_ACCOUNT_CODE_1, generalJournalBatchLineModel.getBankAccountCode());
    }

    @Test
    public void postGeneralJournalBatchLineWizardFirstPage_returnsProperTemplate_whenSourceTypeBankAccount() {
        when(generalJournalBatchLineModelMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.BANK_ACCOUNT);
        
        String template = controller.postGeneralJournalBatchLineWizardFirstPage(generalJournalBatchLineModelMock, modelMock);

        verify(modelMock).addAttribute("generalJournalBatchLine", generalJournalBatchLineModelMock);
        verify(modelMock).addAttribute(eq("sources"), sourcesArgumentCaptor.capture());

        List<GeneralJournalBatchLineSource> sources = sourcesArgumentCaptor.getValue();

        assertBankAccounts(sources);

        assertEquals("generalJournalBatchLineWizardSecondPage.html", template);
    }

    @Test
    public void postGeneralJournalBatchLineWizardFirstPage_returnsProperTemplate_whenSourceTypeCustomer() {
        when(generalJournalBatchLineModelMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.CUSTOMER);
        
        controller.postGeneralJournalBatchLineWizardFirstPage(generalJournalBatchLineModelMock, modelMock);

        verify(modelMock).addAttribute(eq("sources"), sourcesArgumentCaptor.capture());

        List<GeneralJournalBatchLineSource> sources = sourcesArgumentCaptor.getValue();

        assertCustomers(sources);
    }

    @Test
    public void postGeneralJournalBatchLineWizardFirstPage_returnsProperTemplate_whenSourceTypeVendor() {
        when(generalJournalBatchLineModelMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.VENDOR);
        
        controller.postGeneralJournalBatchLineWizardFirstPage(generalJournalBatchLineModelMock, modelMock);

        verify(modelMock).addAttribute(eq("sources"), sourcesArgumentCaptor.capture());

        List<GeneralJournalBatchLineSource> sources = sourcesArgumentCaptor.getValue();

        assertVendors(sources);
    }

    @Test
    public void postGeneralJournalBatchLineWizardSecondPage_returnsProperTemplate_whenSourceTypeBankAccount() {
        when(generalJournalBatchLineModelMock.getSourceType()).thenReturn(GeneralJournalBatchLineType.BANK_ACCOUNT);
        when(generalJournalBatchLineModelMock.getSourceCode()).thenReturn(TEST_BANK_ACCOUNT_CODE_1);

        String template = controller.postGeneralJournalBatchLineWizardSecondPage(generalJournalBatchLineModelMock, modelMock);

        verify(modelMock).addAttribute("generalJournalBatchLine", generalJournalBatchLineModelMock);
        verify(modelMock).addAttribute("bankAccounts", bankAccounts);

        verify(generalJournalBatchLineModelMock).setSourceName(TEST_BANK_ACCOUNT_NAME_1);

        assertEquals("generalJournalBatchLineWizardThirdPage.html", template);
    }

    @Test
    public void postGeneralJournalBatchLineWizardThirdPage_createsAndReturnsProperResult_whenCalledForNew() {
        RedirectView redirectView = controller
                .postGeneralJournalBatchLineWizardThirdPage(generalJournalBatchLineModelMock, redirectAttributesMock);

        assertEquals("/generalJournalBatchCard", redirectView.getUrl());

        verify(redirectAttributesMock).addAttribute("code", TEST_CODE);

        verify(svcMock).create(generalJournalBatchLineArgumentCaptor.capture());

        GeneralJournalBatchLine generalJournalBatchLine = generalJournalBatchLineArgumentCaptor.getValue();

        assertEquals(generalJournalBatchMock,
                generalJournalBatchLine.getGeneralJournalBatchLineId().getGeneralJournalBatch());
    }

    @Test
    public void postGeneralJournalBatchLineWizardThirdPage_updatesAndReturnsProperResult_whenCalledForExisting(){
        when(generalJournalBatchLineModelMock.getLineNo()).thenReturn(TEST_LINE_NO);

        controller.postGeneralJournalBatchLineWizardThirdPage(generalJournalBatchLineModelMock, redirectAttributesMock);

        verify(svcMock).update(eq(generalJournalBatchLineId), generalJournalBatchLineArgumentCaptor.capture());

        GeneralJournalBatchLine generalJournalBatchLine = generalJournalBatchLineArgumentCaptor.getValue();

        assertEquals(generalJournalBatchMock, generalJournalBatchLine.getGeneralJournalBatchLineId().getGeneralJournalBatch());
    }

    @Test
    public void deleteGeneralJournalBatchLine_returnsProperTemplate() {
        RedirectView redirectView = controller.deleteGeneralJournalBatchLine(TEST_CODE, TEST_LINE_NO, redirectAttributesMock);

        assertEquals("/generalJournalBatchCard", redirectView.getUrl());

        verify(redirectAttributesMock).addAttribute("code", TEST_CODE);

        verify(svcMock).delete(generalJournalBatchLineId);
    }

    private void assertVendors(List<GeneralJournalBatchLineSource> sources) {
        assertEquals(VENDORS_COUNT, sources.size());

        assertEquals(TEST_VENDOR_CODE_1, sources.get(0).getCode());
        assertEquals(TEST_VENDOR_CODE_2, sources.get(1).getCode());

        assertEquals(TEST_VENDOR_NAME_1, sources.get(0).getName());
        assertEquals(TEST_VENDOR_NAME_2, sources.get(1).getName());
    }

    private void assertBankAccounts(List<GeneralJournalBatchLineSource> sources) {
        assertEquals(BANK_ACCOUNTS_COUNT, sources.size());

        assertEquals(TEST_BANK_ACCOUNT_CODE_1, sources.get(0).getCode());
        assertEquals(TEST_BANK_ACCOUNT_CODE_2, sources.get(1).getCode());

        assertEquals(TEST_BANK_ACCOUNT_NAME_1, sources.get(0).getName());
        assertEquals(TEST_BANK_ACCOUNT_NAME_2, sources.get(1).getName());
    }

    private void assertCustomers(List<GeneralJournalBatchLineSource> sources) {
        assertEquals(CUSTOMERS_COUNT, sources.size());

        assertEquals(TEST_CUSTOMER_CODE_1, sources.get(0).getCode());
        assertEquals(TEST_CUSTOMER_CODE_2, sources.get(1).getCode());
        assertEquals(TEST_CUSTOMER_CODE_3, sources.get(2).getCode());

        assertEquals(TEST_CUSTOMER_NAME_1, sources.get(0).getName());
        assertEquals(TEST_CUSTOMER_NAME_2, sources.get(1).getName());
        assertEquals(TEST_CUSTOMER_NAME_3, sources.get(2).getName());
    }
}
