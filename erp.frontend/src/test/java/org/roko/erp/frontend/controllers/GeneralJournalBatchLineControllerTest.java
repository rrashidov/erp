package org.roko.erp.frontend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.BankAccountDTO;
import org.roko.erp.dto.CustomerDTO;
import org.roko.erp.dto.GeneralJournalBatchDTO;
import org.roko.erp.dto.GeneralJournalBatchLineDTO;
import org.roko.erp.dto.GeneralJournalBatchLineOperationType;
import org.roko.erp.dto.GeneralJournalBatchLineType;
import org.roko.erp.dto.VendorDTO;
import org.roko.erp.dto.list.BankAccountList;
import org.roko.erp.dto.list.CustomerList;
import org.roko.erp.dto.list.VendorList;
import org.roko.erp.frontend.controllers.model.GeneralJournalBatchLineSource;
import org.roko.erp.frontend.services.BankAccountService;
import org.roko.erp.frontend.services.CustomerService;
import org.roko.erp.frontend.services.GeneralJournalBatchLineService;
import org.roko.erp.frontend.services.GeneralJournalBatchService;
import org.roko.erp.frontend.services.VendorService;
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
    private static final int TEST_PAGE = 12;

    private static final GeneralJournalBatchLineType TEST_SOURCE_TYPE = GeneralJournalBatchLineType.VENDOR;
    private static final String TEST_SOURCE_CODE = "test-source-code";
    private static final String TEST_SOURCE_NAME = "test-source-name";
    private static final GeneralJournalBatchLineOperationType TEST_OPERATION_TYPE = GeneralJournalBatchLineOperationType.PAYMENT;
    private static final Date TEST_DATE = new Date();
    private static final String TEST_DOCUMENT_CODE = "test-document-code";
    private static final BigDecimal TEST_AMOUNT = new BigDecimal(12.12);

    private static final org.roko.erp.dto.GeneralJournalBatchLineType TEST_SOURCE_TYPE_DTO = org.roko.erp.dto.GeneralJournalBatchLineType.VENDOR;

    private static final org.roko.erp.dto.GeneralJournalBatchLineOperationType TEST_OPERATION_TYPE_DTO = org.roko.erp.dto.GeneralJournalBatchLineOperationType.PAYMENT;

    private List<BankAccountDTO> bankAccounts;

    @Captor
    private ArgumentCaptor<List<GeneralJournalBatchLineSource>> sourcesArgumentCaptor;

    @Captor
    private ArgumentCaptor<GeneralJournalBatchLineDTO> generalJournalBatchLineArgumentCaptor;

    @Captor
    private ArgumentCaptor<GeneralJournalBatchLineDTO> generalJournalBatchLineModelArgumentCaptor;

    @Mock
    private GeneralJournalBatchLineService svcMock;

    @Mock
    private GeneralJournalBatchService generalJournalBatchSvcMock;

    @Mock
    private GeneralJournalBatchDTO generalJournalBatchMock;

    @Mock
    private Model modelMock;

    @Mock
    private RedirectAttributes redirectAttributesMock;

    @Mock
    private GeneralJournalBatchLineDTO generalJournalBatchLineModelMock;

    @Mock
    private BankAccountService bankAccountSvcMock;

    @Mock
    private BankAccountDTO bankAccountMock1;

    @Mock
    private BankAccountDTO bankAccountMock2;

    @Mock
    private CustomerService customerSvcMock;

    @Mock
    private CustomerDTO customerMock1;

    @Mock
    private CustomerDTO customerMock2;

    @Mock
    private CustomerDTO customerMock3;

    @Mock
    private VendorService vendorSvcMock;

    @Mock
    private VendorDTO vendorMock1;

    @Mock
    private VendorDTO vendorMock2;

    @Mock
    private GeneralJournalBatchLineDTO generalJournalBatchLineMock;

    @Mock
    private CustomerList customerList;

    @Mock
    private VendorList vendorList;

    @Mock
    private BankAccountList bankAccountList;

    private GeneralJournalBatchLineController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        bankAccounts = Arrays.asList(bankAccountMock1, bankAccountMock2);

        when(bankAccountMock1.getCode()).thenReturn(TEST_BANK_ACCOUNT_CODE_1);
        when(bankAccountMock1.getName()).thenReturn(TEST_BANK_ACCOUNT_NAME_1);

        when(bankAccountMock2.getCode()).thenReturn(TEST_BANK_ACCOUNT_CODE_2);
        when(bankAccountMock2.getName()).thenReturn(TEST_BANK_ACCOUNT_NAME_2);

        when(bankAccountList.getData()).thenReturn(bankAccounts);

        when(bankAccountSvcMock.list()).thenReturn(bankAccountList);
        when(bankAccountSvcMock.get(TEST_BANK_ACCOUNT_CODE_1)).thenReturn(bankAccountMock1);

        when(customerMock1.getCode()).thenReturn(TEST_CUSTOMER_CODE_1);
        when(customerMock1.getName()).thenReturn(TEST_CUSTOMER_NAME_1);

        when(customerMock2.getCode()).thenReturn(TEST_CUSTOMER_CODE_2);
        when(customerMock2.getName()).thenReturn(TEST_CUSTOMER_NAME_2);

        when(customerMock3.getCode()).thenReturn(TEST_CUSTOMER_CODE_3);
        when(customerMock3.getName()).thenReturn(TEST_CUSTOMER_NAME_3);

        when(customerList.getData()).thenReturn(Arrays.asList(customerMock1, customerMock2, customerMock3));

        when(customerSvcMock.list()).thenReturn(customerList);

        when(vendorMock1.getCode()).thenReturn(TEST_VENDOR_CODE_1);
        when(vendorMock1.getName()).thenReturn(TEST_VENDOR_NAME_1);

        when(vendorMock2.getCode()).thenReturn(TEST_VENDOR_CODE_2);
        when(vendorMock2.getName()).thenReturn(TEST_VENDOR_NAME_2);

        when(vendorList.getData()).thenReturn(Arrays.asList(vendorMock1, vendorMock2));

        when(vendorSvcMock.list()).thenReturn(vendorList);

        when(generalJournalBatchSvcMock.get(TEST_CODE)).thenReturn(generalJournalBatchMock);

        when(generalJournalBatchLineMock.getGeneralJournalBatchCode()).thenReturn(TEST_CODE);
        when(generalJournalBatchLineMock.getLineNo()).thenReturn(TEST_LINE_NO);
        when(generalJournalBatchLineMock.getType()).thenReturn(TEST_SOURCE_TYPE_DTO);
        when(generalJournalBatchLineMock.getCode()).thenReturn(TEST_SOURCE_CODE);
        when(generalJournalBatchLineMock.getName()).thenReturn(TEST_SOURCE_NAME);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(TEST_OPERATION_TYPE_DTO);
        when(generalJournalBatchLineMock.getDate()).thenReturn(TEST_DATE);
        when(generalJournalBatchLineMock.getDocumentCode()).thenReturn(TEST_DOCUMENT_CODE);
        when(generalJournalBatchLineMock.getAmount()).thenReturn(TEST_AMOUNT);
        when(generalJournalBatchLineMock.getBankAccountCode()).thenReturn(TEST_BANK_ACCOUNT_CODE_1);

        when(generalJournalBatchLineModelMock.getGeneralJournalBatchCode()).thenReturn(TEST_CODE);
        when(generalJournalBatchLineModelMock.getType()).thenReturn(GeneralJournalBatchLineType.BANK_ACCOUNT);
        when(generalJournalBatchLineModelMock.getOperationType()).thenReturn(GeneralJournalBatchLineOperationType.EMPTY);
        when(generalJournalBatchLineModelMock.getPage()).thenReturn(TEST_PAGE);

        when(svcMock.get(TEST_CODE, TEST_LINE_NO)).thenReturn(generalJournalBatchLineMock);

        controller = new GeneralJournalBatchLineController(svcMock, generalJournalBatchSvcMock, bankAccountSvcMock,
                customerSvcMock, vendorSvcMock);
    }

    @Test
    public void wizard_returnsProperTemplate() {
        String template = controller.wizard(TEST_CODE, 0, TEST_PAGE, modelMock);

        verify(modelMock).addAttribute(eq("generalJournalBatchLine"), generalJournalBatchLineModelArgumentCaptor.capture());

        assertEquals("generalJournalBatchLineWizardFirstPage.html", template);

        GeneralJournalBatchLineDTO generalJournalBatchLineModel = generalJournalBatchLineModelArgumentCaptor.getValue();
        assertEquals(TEST_CODE, generalJournalBatchLineModel.getGeneralJournalBatchCode());
        assertEquals(TEST_PAGE, generalJournalBatchLineModel.getPage());
    }

    @Test
    public void wizard_returnsProperTemplate_whenCalledForExisting() {
        controller.wizard(TEST_CODE, TEST_LINE_NO, TEST_PAGE, modelMock);

        verify(modelMock).addAttribute(eq("generalJournalBatchLine"),
                generalJournalBatchLineModelArgumentCaptor.capture());

        GeneralJournalBatchLineDTO generalJournalBatchLineModel = generalJournalBatchLineModelArgumentCaptor.getValue();

        assertEquals(TEST_CODE, generalJournalBatchLineModel.getGeneralJournalBatchCode());
        assertEquals(TEST_LINE_NO, generalJournalBatchLineModel.getLineNo());
        assertEquals(TEST_PAGE, generalJournalBatchLineModel.getPage());
        assertEquals(TEST_SOURCE_TYPE, generalJournalBatchLineModel.getType());
        assertEquals(TEST_SOURCE_CODE, generalJournalBatchLineModel.getCode());
        assertEquals(TEST_SOURCE_NAME, generalJournalBatchLineModel.getName());
        assertEquals(TEST_OPERATION_TYPE, generalJournalBatchLineModel.getOperationType());
        assertEquals(TEST_DATE, generalJournalBatchLineModel.getDate());
        assertEquals(TEST_DOCUMENT_CODE, generalJournalBatchLineModel.getDocumentCode());
        assertEquals(TEST_AMOUNT, generalJournalBatchLineModel.getAmount());
        assertEquals(TEST_BANK_ACCOUNT_CODE_1, generalJournalBatchLineModel.getBankAccountCode());
    }

    @Test
    public void postGeneralJournalBatchLineWizardFirstPage_returnsProperTemplate_whenSourceTypeBankAccount() {
        when(generalJournalBatchLineModelMock.getType()).thenReturn(GeneralJournalBatchLineType.BANK_ACCOUNT);
        
        String template = controller.postGeneralJournalBatchLineWizardFirstPage(generalJournalBatchLineModelMock, modelMock);

        verify(modelMock).addAttribute("generalJournalBatchLine", generalJournalBatchLineModelMock);
        verify(modelMock).addAttribute(eq("sources"), sourcesArgumentCaptor.capture());

        List<GeneralJournalBatchLineSource> sources = sourcesArgumentCaptor.getValue();

        assertBankAccounts(sources);

        assertEquals("generalJournalBatchLineWizardSecondPage.html", template);
    }

    @Test
    public void postGeneralJournalBatchLineWizardFirstPage_returnsProperTemplate_whenSourceTypeCustomer() {
        when(generalJournalBatchLineModelMock.getType()).thenReturn(GeneralJournalBatchLineType.CUSTOMER);
        
        controller.postGeneralJournalBatchLineWizardFirstPage(generalJournalBatchLineModelMock, modelMock);

        verify(modelMock).addAttribute(eq("sources"), sourcesArgumentCaptor.capture());

        List<GeneralJournalBatchLineSource> sources = sourcesArgumentCaptor.getValue();

        assertCustomers(sources);
    }

    @Test
    public void postGeneralJournalBatchLineWizardFirstPage_returnsProperTemplate_whenSourceTypeVendor() {
        when(generalJournalBatchLineModelMock.getType()).thenReturn(GeneralJournalBatchLineType.VENDOR);
        
        controller.postGeneralJournalBatchLineWizardFirstPage(generalJournalBatchLineModelMock, modelMock);

        verify(modelMock).addAttribute(eq("sources"), sourcesArgumentCaptor.capture());

        List<GeneralJournalBatchLineSource> sources = sourcesArgumentCaptor.getValue();

        assertVendors(sources);
    }

    @Test
    public void postGeneralJournalBatchLineWizardSecondPage_returnsProperTemplate_whenSourceTypeBankAccount() {
        when(generalJournalBatchLineModelMock.getType()).thenReturn(GeneralJournalBatchLineType.BANK_ACCOUNT);
        when(generalJournalBatchLineModelMock.getCode()).thenReturn(TEST_BANK_ACCOUNT_CODE_1);

        String template = controller.postGeneralJournalBatchLineWizardSecondPage(generalJournalBatchLineModelMock, modelMock);

        verify(modelMock).addAttribute("generalJournalBatchLine", generalJournalBatchLineModelMock);
        verify(modelMock).addAttribute("bankAccounts", bankAccounts);

        verify(generalJournalBatchLineModelMock).setName(TEST_BANK_ACCOUNT_NAME_1);

        assertEquals("generalJournalBatchLineWizardThirdPage.html", template);
    }

    @Test
    public void postGeneralJournalBatchLineWizardThirdPage_createsAndReturnsProperResult_whenCalledForNew() {
        RedirectView redirectView = controller
                .postGeneralJournalBatchLineWizardThirdPage(generalJournalBatchLineModelMock, redirectAttributesMock);

        assertEquals("/generalJournalBatchCard", redirectView.getUrl());

        verify(redirectAttributesMock).addAttribute("code", TEST_CODE);
        verify(redirectAttributesMock).addAttribute("page", TEST_PAGE);

        verify(svcMock).create(eq(TEST_CODE), generalJournalBatchLineArgumentCaptor.capture());
    }

    @Test
    public void postGeneralJournalBatchLineWizardThirdPage_updatesAndReturnsProperResult_whenCalledForExisting(){
        when(generalJournalBatchLineModelMock.getLineNo()).thenReturn(TEST_LINE_NO);

        controller.postGeneralJournalBatchLineWizardThirdPage(generalJournalBatchLineModelMock, redirectAttributesMock);

        verify(redirectAttributesMock).addAttribute("code", TEST_CODE);
        verify(redirectAttributesMock).addAttribute("page", TEST_PAGE);

        verify(svcMock).update(eq(TEST_CODE), eq(TEST_LINE_NO), generalJournalBatchLineArgumentCaptor.capture());
    }

    @Test
    public void deleteGeneralJournalBatchLine_returnsProperTemplate() {
        RedirectView redirectView = controller.deleteGeneralJournalBatchLine(TEST_CODE, TEST_LINE_NO, TEST_PAGE,
                redirectAttributesMock);

        assertEquals("/generalJournalBatchCard", redirectView.getUrl());

        verify(redirectAttributesMock).addAttribute("code", TEST_CODE);
        verify(redirectAttributesMock).addAttribute("page", TEST_PAGE);

        verify(svcMock).delete(TEST_CODE, TEST_LINE_NO);
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
