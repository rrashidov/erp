package org.roko.erp.frontend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.CustomerDTO;
import org.roko.erp.dto.PaymentMethodDTO;
import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.dto.SalesDocumentLineDTO;
import org.roko.erp.dto.list.CustomerList;
import org.roko.erp.dto.list.PaymentMethodList;
import org.roko.erp.dto.list.SalesDocumentLineList;
import org.roko.erp.dto.list.SalesDocumentList;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.services.CustomerService;
import org.roko.erp.frontend.services.DeleteFailedException;
import org.roko.erp.frontend.services.FeedbackService;
import org.roko.erp.frontend.services.PaymentMethodService;
import org.roko.erp.frontend.services.PostFailedException;
import org.roko.erp.frontend.services.SalesCreditMemoLineService;
import org.roko.erp.frontend.services.SalesCreditMemoPostService;
import org.roko.erp.frontend.services.SalesCreditMemoService;
import org.roko.erp.frontend.services.util.Feedback;
import org.roko.erp.frontend.services.util.FeedbackType;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

public class SalesCreditMemoControllerTest {

    private static final String DELETED_MSG_TMPL = "Sales Credit Memo %s deleted";

    private static final String TEST_POST_FAILED_MSG = "test-post-failed-msg";

    private static final String NEW_SALES_CREDIT_MEMO_CODE = "new-sales-credit-memo-code";

    private static final String TEST_SALES_CREDIT_MEMO_CODE = "test-code";

    private static final String TEST_CUSTOMER_CODE = "test-customer-code";
    private static final String TEST_CUSTOMER_NAME = "test-customer-name";

    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";

    private static final int TEST_PAGE = 123;

    private static final long TEST_COUNT = 234;
    private static final long TEST_SALES_CREDIT_MEMO_LINE_COUNT = 123;

    private List<SalesDocumentDTO> salesCreditMemos = new ArrayList<>();

    private List<CustomerDTO> customers = new ArrayList<>();

    private List<PaymentMethodDTO> paymentMethods = new ArrayList<>();

    private List<SalesDocumentLineDTO> salesCreditMemoLines = new ArrayList<>();

    @Captor
    private ArgumentCaptor<SalesDocumentDTO> salesCreditMemoArgumentCaptor;

    @Captor
    private ArgumentCaptor<SalesDocumentDTO> salesCreditMemoModelArgumentCaptor;

    @Mock
    private Date dateMock;

    @Mock
    private SalesDocumentDTO salesCreditMemoMock;

    @Mock
    private CustomerDTO customerMock;

    @Mock
    private PaymentMethodDTO paymentMethodMock;

    @Mock
    private Model modelMock;

    @Mock
    private RedirectAttributes redirectAttributesMock;

    @Mock
    private SalesDocumentDTO salesCreditMemoModelMock;

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

    @Mock
    private SalesCreditMemoPostService salesCreditMemoPostSvcMock;

    @Mock
    private FeedbackService feedbackSvcMock;

    @Mock
    private HttpSession httpSessionMock;

    @Mock
    private Feedback feedbackMock;

    @Mock
    private SalesDocumentList salesCreditMemoList;

    @Mock
    private SalesDocumentLineList salesCreditMemoLineList;

    @Mock
    private CustomerList customerList;

    @Mock
    private PaymentMethodList paymentMethodList;

    private SalesCreditMemoController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        salesCreditMemos = Arrays.asList(salesCreditMemoMock);

        when(salesCreditMemoModelMock.getCode()).thenReturn("");
        when(salesCreditMemoModelMock.getCustomerCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(salesCreditMemoModelMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(paymentMethodMock.getCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(paymentMethodList.getData()).thenReturn(paymentMethods);
        when(paymentMethodSvcMock.list()).thenReturn(paymentMethodList);
        when(paymentMethodSvcMock.get(TEST_PAYMENT_METHOD_CODE)).thenReturn(paymentMethodMock);

        when(customerMock.getCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(customerMock.getName()).thenReturn(TEST_CUSTOMER_NAME);
        when(customerMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(customerList.getData()).thenReturn(customers);
        when(customerSvcMock.list()).thenReturn(customerList);
        when(customerSvcMock.get(TEST_CUSTOMER_CODE)).thenReturn(customerMock);

        when(salesCreditMemoMock.getCode()).thenReturn(TEST_SALES_CREDIT_MEMO_CODE);
        when(salesCreditMemoMock.getCustomerCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(salesCreditMemoMock.getCustomerName()).thenReturn(TEST_CUSTOMER_NAME);
        when(salesCreditMemoMock.getDate()).thenReturn(dateMock);
        when(salesCreditMemoMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(salesCreditMemoList.getData()).thenReturn(salesCreditMemos);
        when(salesCreditMemoList.getCount()).thenReturn(TEST_COUNT);

        when(svcMock.list(TEST_PAGE)).thenReturn(salesCreditMemoList);
        when(svcMock.get(TEST_SALES_CREDIT_MEMO_CODE)).thenReturn(salesCreditMemoMock);
        when(svcMock.create(any(SalesDocumentDTO.class))).thenReturn(NEW_SALES_CREDIT_MEMO_CODE);

        when(salesCreditMemoLineList.getData()).thenReturn(salesCreditMemoLines);
        when(salesCreditMemoLineList.getCount()).thenReturn(TEST_SALES_CREDIT_MEMO_LINE_COUNT);

        when(salesCreditMemoLineSvcMock.list(TEST_SALES_CREDIT_MEMO_CODE, TEST_PAGE))
                .thenReturn(salesCreditMemoLineList);

        when(pagingSvcMock.generate("salesCreditMemo", TEST_PAGE, (int) TEST_COUNT)).thenReturn(pagingDataMock);
        when(pagingSvcMock.generate("salesCreditMemoCard", TEST_SALES_CREDIT_MEMO_CODE, TEST_PAGE,
                (int) TEST_SALES_CREDIT_MEMO_LINE_COUNT)).thenReturn(salesCreditMemoLinePagingMock);

        when(feedbackSvcMock.get(httpSessionMock)).thenReturn(feedbackMock);

        controller = new SalesCreditMemoController(svcMock, pagingSvcMock, customerSvcMock, paymentMethodSvcMock,
                salesCreditMemoLineSvcMock, salesCreditMemoPostSvcMock, feedbackSvcMock);
    }

    @Test
    public void listReturnsProperTemplate() {
        String template = controller.list(TEST_PAGE, modelMock, httpSessionMock);

        assertEquals("salesCreditMemoList.html", template);

        verify(modelMock).addAttribute("salesCreditMemos", salesCreditMemos);
        verify(modelMock).addAttribute("paging", pagingDataMock);
        verify(modelMock).addAttribute("feedback", feedbackMock);
    }

    @Test
    public void wizardReturnsProperTemplate_whenCalledForNew() {
        String template = controller.wizard(null, modelMock);

        assertEquals("salesCreditMemoWizardFirstPage.html", template);

        verify(modelMock).addAttribute(eq("salesCreditMemoModel"), salesCreditMemoModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("customers", customers);

        SalesDocumentDTO salesCreditMemoModel = salesCreditMemoModelArgumentCaptor.getValue();

        assertEquals("", salesCreditMemoModel.getCode());
        assertEquals("", salesCreditMemoModel.getCustomerCode());
        assertEquals("", salesCreditMemoModel.getCustomerName());
        assertEquals("", salesCreditMemoModel.getPaymentMethodCode());
    }

    @Test
    public void wizardReturnsProperTemplate_whenCalledForExisting() {
        String template = controller.wizard(TEST_SALES_CREDIT_MEMO_CODE, modelMock);

        assertEquals("salesCreditMemoWizardFirstPage.html", template);

        verify(modelMock).addAttribute(eq("salesCreditMemoModel"), salesCreditMemoModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("customers", customers);

        SalesDocumentDTO salesCreditMemoModel = salesCreditMemoModelArgumentCaptor.getValue();

        assertEquals(TEST_SALES_CREDIT_MEMO_CODE, salesCreditMemoModel.getCode());
        assertEquals(TEST_CUSTOMER_CODE, salesCreditMemoModel.getCustomerCode());
        assertEquals(TEST_CUSTOMER_NAME, salesCreditMemoModel.getCustomerName());
        assertEquals(dateMock, salesCreditMemoModel.getDate());
        assertEquals(TEST_PAYMENT_METHOD_CODE, salesCreditMemoModel.getPaymentMethodCode());
    }

    @Test
    public void postWizardFirstPage_returnsProperTemplate() {
        String template = controller.postWizardFirstPage(salesCreditMemoModelMock, modelMock);

        assertEquals("salesCreditMemoWizardSecondPage.html", template);

        verify(modelMock).addAttribute("paymentMethods", paymentMethods);

        verify(salesCreditMemoModelMock).setCustomerName(TEST_CUSTOMER_NAME);
        verify(salesCreditMemoModelMock).setPaymentMethodCode(TEST_PAYMENT_METHOD_CODE);
    }

    @Test
    public void postWizardSecondPage_createsSalesCreditMemoAndReturnsProperTemplate_whenCalledForNew() {
        RedirectView redirectView = controller.postWizardSecondPage(salesCreditMemoModelMock, redirectAttributesMock);

        assertEquals("/salesCreditMemoCard", redirectView.getUrl());

        verify(svcMock).create(salesCreditMemoArgumentCaptor.capture());

        verify(redirectAttributesMock).addAttribute("code", NEW_SALES_CREDIT_MEMO_CODE);

        SalesDocumentDTO createdSalesCreditMemo = salesCreditMemoArgumentCaptor.getValue();

        assertEquals(TEST_CUSTOMER_CODE, createdSalesCreditMemo.getCustomerCode());
        assertEquals(TEST_PAYMENT_METHOD_CODE, createdSalesCreditMemo.getPaymentMethodCode());
    }

    @Test
    public void postWizardSecondPage_updatesSalesCreditMemoAndReturnsProperTemplate_whenCalledForExisting(){
        when(salesCreditMemoModelMock.getCode()).thenReturn(TEST_SALES_CREDIT_MEMO_CODE);

        RedirectView redirectView = controller.postWizardSecondPage(salesCreditMemoModelMock, redirectAttributesMock);

        assertEquals("/salesCreditMemoCard", redirectView.getUrl());

        verify(redirectAttributesMock).addAttribute("code", TEST_SALES_CREDIT_MEMO_CODE);

        verify(svcMock).update(TEST_SALES_CREDIT_MEMO_CODE, salesCreditMemoModelMock);
    }

    @Test
    public void delete_deletesSalesCreditMemo() throws DeleteFailedException {
        RedirectView redirectView = controller.delete(TEST_SALES_CREDIT_MEMO_CODE, TEST_PAGE, redirectAttributesMock,
                httpSessionMock);

        assertEquals("/salesCreditMemoList", redirectView.getUrl());

        verify(svcMock).delete(TEST_SALES_CREDIT_MEMO_CODE);

        verify(feedbackSvcMock).give(FeedbackType.INFO,
                String.format(DELETED_MSG_TMPL, TEST_SALES_CREDIT_MEMO_CODE), httpSessionMock);

        verify(redirectAttributesMock).addAttribute("page", TEST_PAGE);
    }

    @Test
    public void deleteGivesProperFeedback_whenDeleteFails() throws DeleteFailedException {
        doThrow(new DeleteFailedException("test-err-msg")).when(svcMock).delete(TEST_SALES_CREDIT_MEMO_CODE);

        RedirectView redirectView = controller.delete(TEST_SALES_CREDIT_MEMO_CODE, TEST_PAGE, redirectAttributesMock,
                httpSessionMock);

        assertEquals("/salesCreditMemoList", redirectView.getUrl());

        verify(svcMock).delete(TEST_SALES_CREDIT_MEMO_CODE);

        verify(feedbackSvcMock).give(FeedbackType.ERROR,
                "test-err-msg", httpSessionMock);
    }

    @Test
    public void card_returnsProperTemplate() {
        String template = controller.card(TEST_SALES_CREDIT_MEMO_CODE, TEST_PAGE, modelMock);

        assertEquals("salesCreditMemoCard.html", template);

        verify(modelMock).addAttribute("salesCreditMemo", salesCreditMemoMock);
        verify(modelMock).addAttribute("salesCreditMemoLines", salesCreditMemoLines);
        verify(modelMock).addAttribute("paging", salesCreditMemoLinePagingMock);
    }

    @Test
    public void post_returnsProperTemplate() throws PostFailedException {
        RedirectView redirectView = controller.post(TEST_SALES_CREDIT_MEMO_CODE, httpSessionMock);

        assertEquals("/salesCreditMemoList", redirectView.getUrl());

        verify(salesCreditMemoPostSvcMock).post(TEST_SALES_CREDIT_MEMO_CODE);

        verify(feedbackSvcMock).give(FeedbackType.INFO,
                "Sales credit memo " + TEST_SALES_CREDIT_MEMO_CODE + " post scheduled.",
                httpSessionMock);
    }

    @Test
    public void postReturnsProperFeedback_whenPostingFails() throws PostFailedException {
        doThrow(new PostFailedException(TEST_POST_FAILED_MSG)).when(salesCreditMemoPostSvcMock)
                .post(TEST_SALES_CREDIT_MEMO_CODE);

        RedirectView redirectView = controller.post(TEST_SALES_CREDIT_MEMO_CODE, httpSessionMock);

        assertEquals("/salesCreditMemoList", redirectView.getUrl());

        verify(salesCreditMemoPostSvcMock).post(TEST_SALES_CREDIT_MEMO_CODE);

        verify(feedbackSvcMock).give(FeedbackType.ERROR,
                "Sales credit memo " + TEST_SALES_CREDIT_MEMO_CODE + " post scheduling failed: " + TEST_POST_FAILED_MSG,
                httpSessionMock);
    }
}
