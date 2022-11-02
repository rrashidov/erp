package org.roko.erp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.controllers.model.SalesCreditMemoModel;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.Customer;
import org.roko.erp.model.PaymentMethod;
import org.roko.erp.model.SalesCreditMemo;
import org.roko.erp.model.SalesCreditMemoLine;
import org.roko.erp.services.CustomerService;
import org.roko.erp.services.PaymentMethodService;
import org.roko.erp.services.SalesCreditMemoLineService;
import org.roko.erp.services.SalesCreditMemoService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

public class SalesCreditMemoControllerTest {


    private static final String TEST_SALES_CREDIT_MEMO_CODE = "test-code";

    private static final String TEST_CUSTOMER_CODE = "test-customer-code";
    private static final String TEST_CUSTOMER_NAME = "test-customer-name";

    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";

    private static final Long TEST_PAGE = 123l;

    private static final Long TEST_COUNT = 234l;

    private static final long TEST_SALES_CREDIT_MEMO_LINE_COUNT = 123l;

    private List<SalesCreditMemo> salesCreditMemos = new ArrayList<>();

    private List<Customer> customers = new ArrayList<>();

    private List<PaymentMethod> paymentMethods = new ArrayList<>();

    private List<SalesCreditMemoLine> salesCreditMemoLines = new ArrayList<>();

    @Captor
    private ArgumentCaptor<SalesCreditMemo> salesCreditMemoArgumentCaptor;

    @Captor
    private ArgumentCaptor<SalesCreditMemoModel> salesCreditMemoModelArgumentCaptor;

    @Mock
    private Date dateMock;

    @Mock
    private SalesCreditMemo salesCreditMemoMock;

    @Mock
    private Customer customerMock;

    @Mock
    private PaymentMethod paymentMethodMock;

    @Mock
    private Model modelMock;

    @Mock
    private RedirectAttributes redirectAttributesMock;

    @Mock
    private SalesCreditMemoModel salesCreditMemoModelMock;

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private PagingData salesCreditMemoLinePagingMock;

    @Mock
    private SalesCreditMemoService svcMock;

    @Mock
    private SalesCreditMemoLineService salesCreditMemoLineSvcMock;

    @Mock
    private CustomerService customerSvcMock;

    @Mock
    private PaymentMethodService paymentMethodSvcMock;

    @Mock
    private PagingService pagingSvcMock;

    private SalesCreditMemoController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(salesCreditMemoModelMock.getCode()).thenReturn("");
        when(salesCreditMemoModelMock.getCustomerCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(salesCreditMemoModelMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(paymentMethodMock.getCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(paymentMethodSvcMock.list()).thenReturn(paymentMethods);
        when(paymentMethodSvcMock.get(TEST_PAYMENT_METHOD_CODE)).thenReturn(paymentMethodMock);

        when(customerMock.getCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(customerMock.getName()).thenReturn(TEST_CUSTOMER_NAME);
        when(customerMock.getPaymentMethod()).thenReturn(paymentMethodMock);

        when(customerSvcMock.list()).thenReturn(customers);
        when(customerSvcMock.get(TEST_CUSTOMER_CODE)).thenReturn(customerMock);

        when(salesCreditMemoMock.getCode()).thenReturn(TEST_SALES_CREDIT_MEMO_CODE);
        when(salesCreditMemoMock.getCustomer()).thenReturn(customerMock);
        when(salesCreditMemoMock.getDate()).thenReturn(dateMock);
        when(salesCreditMemoMock.getPaymentMethod()).thenReturn(paymentMethodMock);

        when(svcMock.list()).thenReturn(salesCreditMemos);
        when(svcMock.count()).thenReturn(TEST_COUNT);
        when(svcMock.get(TEST_SALES_CREDIT_MEMO_CODE)).thenReturn(salesCreditMemoMock);

        when(salesCreditMemoLineSvcMock.list(salesCreditMemoMock)).thenReturn(salesCreditMemoLines);
        when(salesCreditMemoLineSvcMock.count(salesCreditMemoMock)).thenReturn(TEST_SALES_CREDIT_MEMO_LINE_COUNT);

        when(pagingSvcMock.generate("salesCreditMemo", TEST_PAGE, TEST_COUNT)).thenReturn(pagingDataMock);
        when(pagingSvcMock.generate("salesCreditMemoLine", null, TEST_SALES_CREDIT_MEMO_LINE_COUNT)).thenReturn(salesCreditMemoLinePagingMock);

        controller = new SalesCreditMemoController(svcMock, pagingSvcMock, customerSvcMock, paymentMethodSvcMock, salesCreditMemoLineSvcMock);
    }

    @Test
    public void listReturnsProperTemplate(){
        String template = controller.list(TEST_PAGE, modelMock);

        assertEquals("salesCreditMemoList.html", template);

        verify(modelMock).addAttribute("salesCreditMemos", salesCreditMemos);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }

    @Test
    public void wizardReturnsProperTemplate_whenCalledForNew(){
        String template = controller.wizard(null, modelMock);

        assertEquals("salesCreditMemoWizardFirstPage.html", template);

        verify(modelMock).addAttribute(eq("salesCreditMemoModel"), salesCreditMemoModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("customers", customers);

        SalesCreditMemoModel salesCreditMemoModel = salesCreditMemoModelArgumentCaptor.getValue();

        assertEquals("", salesCreditMemoModel.getCode());
        assertEquals("", salesCreditMemoModel.getCustomerCode());
        assertEquals("", salesCreditMemoModel.getCustomerName());
        assertEquals("", salesCreditMemoModel.getPaymentMethodCode());
    }

    @Test
    public void wizardReturnsProperTemplate_whenCalledForExisting(){
        String template = controller.wizard(TEST_SALES_CREDIT_MEMO_CODE, modelMock);

        assertEquals("salesCreditMemoWizardFirstPage.html", template);

        verify(modelMock).addAttribute(eq("salesCreditMemoModel"), salesCreditMemoModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("customers", customers);

        SalesCreditMemoModel salesCreditMemoModel = salesCreditMemoModelArgumentCaptor.getValue();

        assertEquals(TEST_SALES_CREDIT_MEMO_CODE, salesCreditMemoModel.getCode());
        assertEquals(TEST_CUSTOMER_CODE, salesCreditMemoModel.getCustomerCode());
        assertEquals(TEST_CUSTOMER_NAME, salesCreditMemoModel.getCustomerName());
        assertEquals(dateMock, salesCreditMemoModel.getDate());
        assertEquals(TEST_PAYMENT_METHOD_CODE, salesCreditMemoModel.getPaymentMethodCode());
    }

    @Test
    public void postWizardFirstPage_returnsProperTemplate(){
        String template = controller.postWizardFirstPage(salesCreditMemoModelMock, modelMock);

        assertEquals("salesCreditMemoWizardSecondPage.html", template);

        verify(modelMock).addAttribute("paymentMethods", paymentMethods);

        verify(salesCreditMemoModelMock).setCustomerName(TEST_CUSTOMER_NAME);
        verify(salesCreditMemoModelMock).setPaymentMethodCode(TEST_PAYMENT_METHOD_CODE);
    }

    @Test
    public void postWizardSecondPage_createsSalesCreditMemoAndReturnsProperTemplate_whenCalledForNew(){
        RedirectView redirectView = controller.postWizardSecondPage(salesCreditMemoModelMock, redirectAttributesMock);

        assertEquals("/salesCreditMemoCard", redirectView.getUrl());

        verify(svcMock).create(salesCreditMemoArgumentCaptor.capture());

        SalesCreditMemo createdSalesCreditMemo = salesCreditMemoArgumentCaptor.getValue();

        assertEquals(customerMock, createdSalesCreditMemo.getCustomer());
        assertEquals(paymentMethodMock, createdSalesCreditMemo.getPaymentMethod());
    }

    @Test
    public void postWizardSecondPage_updatesSalesCreditMemoAndReturnsProperTemplate_whenCalledForExisting(){
        when(salesCreditMemoModelMock.getCode()).thenReturn(TEST_SALES_CREDIT_MEMO_CODE);

        RedirectView redirectView = controller.postWizardSecondPage(salesCreditMemoModelMock, redirectAttributesMock);

        assertEquals("/salesCreditMemoCard", redirectView.getUrl());

        verify(redirectAttributesMock).addAttribute("code", TEST_SALES_CREDIT_MEMO_CODE);

        verify(svcMock).update(TEST_SALES_CREDIT_MEMO_CODE, salesCreditMemoMock);
    }

    @Test
    public void delete_deletesSalesCreditMemo(){
        RedirectView redirectView = controller.delete(TEST_SALES_CREDIT_MEMO_CODE);

        assertEquals("/salesCreditMemoList", redirectView.getUrl());

        verify(svcMock).delete(TEST_SALES_CREDIT_MEMO_CODE);
    }

    @Test
    public void card_returnsProperTemplate(){
        String template = controller.card(TEST_SALES_CREDIT_MEMO_CODE, modelMock);

        assertEquals("salesCreditMemoCard.html", template);

        verify(modelMock).addAttribute("salesCreditMemo", salesCreditMemoMock);
        verify(modelMock).addAttribute("salesCreditMemoLines", salesCreditMemoLines);
        verify(modelMock).addAttribute("paging", salesCreditMemoLinePagingMock);
    }
}