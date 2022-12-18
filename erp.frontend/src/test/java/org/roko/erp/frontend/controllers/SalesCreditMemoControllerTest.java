package org.roko.erp.frontend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.frontend.controllers.model.SalesCreditMemoModel;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.model.Customer;
import org.roko.erp.frontend.model.PaymentMethod;
import org.roko.erp.frontend.model.SalesCreditMemo;
import org.roko.erp.frontend.model.SalesCreditMemoLine;
import org.roko.erp.frontend.services.CustomerService;
import org.roko.erp.frontend.services.FeedbackService;
import org.roko.erp.frontend.services.PaymentMethodService;
import org.roko.erp.frontend.services.PostFailedException;
import org.roko.erp.frontend.services.SalesCodeSeriesService;
import org.roko.erp.frontend.services.SalesCreditMemoLineService;
import org.roko.erp.frontend.services.SalesCreditMemoPostService;
import org.roko.erp.frontend.services.SalesCreditMemoService;
import org.roko.erp.frontend.services.util.Feedback;
import org.roko.erp.frontend.services.util.FeedbackType;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

public class SalesCreditMemoControllerTest {

    private static final String TEST_POST_FAILED_MSG = "test-post-failed-msg";

    private static final String TEST_NEW_SALES_CREDIT_MEMO_CODE = "test-new-sales-credit-memo-code";

    private static final String TEST_SALES_CREDIT_MEMO_CODE = "test-code";

    private static final String TEST_CUSTOMER_CODE = "test-customer-code";
    private static final String TEST_CUSTOMER_NAME = "test-customer-name";

    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";

    private static final int TEST_PAGE = 123;
    private static final int TEST_COUNT = 234;

    private static final int TEST_SALES_CREDIT_MEMO_LINE_COUNT = 123;

    private List<SalesCreditMemo> salesCreditMemos = new ArrayList<>();

    private List<Customer> customers = new ArrayList<>();

    //private List<PaymentMethod> paymentMethods = new ArrayList<>();

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

    @Mock
    private SalesCreditMemoPostService salesCreditMemoPostSvcMock;

    @Mock
    private SalesCodeSeriesService salesCodeSeriesSvcMock;

    @Mock
    private FeedbackService feedbackSvcMock;

    @Mock
    private HttpSession httpSessionMock;

    @Mock
    private Feedback feedbackMock;

    private SalesCreditMemoController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        salesCreditMemos = Arrays.asList(salesCreditMemoMock);

        when(salesCreditMemoModelMock.getCode()).thenReturn("");
        when(salesCreditMemoModelMock.getCustomerCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(salesCreditMemoModelMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(paymentMethodMock.getCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        //when(paymentMethodSvcMock.list()).thenReturn(paymentMethods);
        //when(paymentMethodSvcMock.get(TEST_PAYMENT_METHOD_CODE)).thenReturn(paymentMethodMock);

        when(customerMock.getCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(customerMock.getName()).thenReturn(TEST_CUSTOMER_NAME);
        when(customerMock.getPaymentMethod()).thenReturn(paymentMethodMock);

        //when(customerSvcMock.list()).thenReturn(customers);
        //when(customerSvcMock.get(TEST_CUSTOMER_CODE)).thenReturn(customerMock);

        when(salesCreditMemoMock.getCode()).thenReturn(TEST_SALES_CREDIT_MEMO_CODE);
        when(salesCreditMemoMock.getCustomer()).thenReturn(customerMock);
        when(salesCreditMemoMock.getDate()).thenReturn(dateMock);
        when(salesCreditMemoMock.getPaymentMethod()).thenReturn(paymentMethodMock);

        when(svcMock.list(TEST_PAGE)).thenReturn(salesCreditMemos);
        when(svcMock.count()).thenReturn(TEST_COUNT);
        when(svcMock.get(TEST_SALES_CREDIT_MEMO_CODE)).thenReturn(salesCreditMemoMock);

        when(salesCreditMemoLineSvcMock.list(salesCreditMemoMock)).thenReturn(salesCreditMemoLines);
        when(salesCreditMemoLineSvcMock.count(salesCreditMemoMock)).thenReturn(TEST_SALES_CREDIT_MEMO_LINE_COUNT);

        when(pagingSvcMock.generate("salesCreditMemo", TEST_PAGE, TEST_COUNT)).thenReturn(pagingDataMock);
        when(pagingSvcMock.generate("salesCreditMemoCard", TEST_SALES_CREDIT_MEMO_CODE, TEST_PAGE,
                TEST_SALES_CREDIT_MEMO_LINE_COUNT)).thenReturn(salesCreditMemoLinePagingMock);

        when(salesCodeSeriesSvcMock.creditMemoCode()).thenReturn(TEST_NEW_SALES_CREDIT_MEMO_CODE);

        when(feedbackSvcMock.get(httpSessionMock)).thenReturn(feedbackMock);

        controller = new SalesCreditMemoController(svcMock, pagingSvcMock, customerSvcMock, paymentMethodSvcMock,
                salesCreditMemoLineSvcMock, salesCreditMemoPostSvcMock, salesCodeSeriesSvcMock, feedbackSvcMock);
    }

    // @Test
    // public void listReturnsProperTemplate() {
    //     String template = controller.list(TEST_PAGE, modelMock, httpSessionMock);

    //     assertEquals("salesCreditMemoList.html", template);

    //     verify(modelMock).addAttribute("salesCreditMemos", salesCreditMemos);
    //     verify(modelMock).addAttribute("paging", pagingDataMock);
    //     verify(modelMock).addAttribute("feedback", feedbackMock);
    // }

    // @Test
    // public void wizardReturnsProperTemplate_whenCalledForNew() {
    //     String template = controller.wizard(null, modelMock);

    //     assertEquals("salesCreditMemoWizardFirstPage.html", template);

    //     verify(modelMock).addAttribute(eq("salesCreditMemoModel"), salesCreditMemoModelArgumentCaptor.capture());
    //     verify(modelMock).addAttribute("customers", customers);

    //     SalesCreditMemoModel salesCreditMemoModel = salesCreditMemoModelArgumentCaptor.getValue();

    //     assertEquals("", salesCreditMemoModel.getCode());
    //     assertEquals("", salesCreditMemoModel.getCustomerCode());
    //     assertEquals("", salesCreditMemoModel.getCustomerName());
    //     assertEquals("", salesCreditMemoModel.getPaymentMethodCode());
    // }

    // @Test
    // public void wizardReturnsProperTemplate_whenCalledForExisting() {
    //     String template = controller.wizard(TEST_SALES_CREDIT_MEMO_CODE, modelMock);

    //     assertEquals("salesCreditMemoWizardFirstPage.html", template);

    //     verify(modelMock).addAttribute(eq("salesCreditMemoModel"), salesCreditMemoModelArgumentCaptor.capture());
    //     verify(modelMock).addAttribute("customers", customers);

    //     SalesCreditMemoModel salesCreditMemoModel = salesCreditMemoModelArgumentCaptor.getValue();

    //     assertEquals(TEST_SALES_CREDIT_MEMO_CODE, salesCreditMemoModel.getCode());
    //     assertEquals(TEST_CUSTOMER_CODE, salesCreditMemoModel.getCustomerCode());
    //     assertEquals(TEST_CUSTOMER_NAME, salesCreditMemoModel.getCustomerName());
    //     assertEquals(dateMock, salesCreditMemoModel.getDate());
    //     assertEquals(TEST_PAYMENT_METHOD_CODE, salesCreditMemoModel.getPaymentMethodCode());
    // }

    // // @Test
    // // public void postWizardFirstPage_returnsProperTemplate() {
    // //     String template = controller.postWizardFirstPage(salesCreditMemoModelMock, modelMock);

    // //     assertEquals("salesCreditMemoWizardSecondPage.html", template);

    // //     verify(modelMock).addAttribute("paymentMethods", paymentMethods);

    // //     verify(salesCreditMemoModelMock).setCustomerName(TEST_CUSTOMER_NAME);
    // //     verify(salesCreditMemoModelMock).setPaymentMethodCode(TEST_PAYMENT_METHOD_CODE);
    // // }

    // // @Test
    // // public void postWizardSecondPage_createsSalesCreditMemoAndReturnsProperTemplate_whenCalledForNew() {
    // //     RedirectView redirectView = controller.postWizardSecondPage(salesCreditMemoModelMock, redirectAttributesMock);

    // //     assertEquals("/salesCreditMemoCard", redirectView.getUrl());

    // //     verify(svcMock).create(salesCreditMemoArgumentCaptor.capture());

    // //     SalesCreditMemo createdSalesCreditMemo = salesCreditMemoArgumentCaptor.getValue();

    // //     assertEquals(TEST_NEW_SALES_CREDIT_MEMO_CODE, createdSalesCreditMemo.getCode());
    // //     assertEquals(customerMock, createdSalesCreditMemo.getCustomer());
    // //     assertEquals(paymentMethodMock, createdSalesCreditMemo.getPaymentMethod());
    // // }

    // @Test
    // public void postWizardSecondPage_updatesSalesCreditMemoAndReturnsProperTemplate_whenCalledForExisting(){
    //     when(salesCreditMemoModelMock.getCode()).thenReturn(TEST_SALES_CREDIT_MEMO_CODE);

    //     RedirectView redirectView = controller.postWizardSecondPage(salesCreditMemoModelMock, redirectAttributesMock);

    //     assertEquals("/salesCreditMemoCard", redirectView.getUrl());

    //     verify(redirectAttributesMock).addAttribute("code", TEST_SALES_CREDIT_MEMO_CODE);

    //     verify(svcMock).update(TEST_SALES_CREDIT_MEMO_CODE, salesCreditMemoMock);
    // }

    // @Test
    // public void delete_deletesSalesCreditMemo() {
    //     RedirectView redirectView = controller.delete(TEST_SALES_CREDIT_MEMO_CODE);

    //     assertEquals("/salesCreditMemoList", redirectView.getUrl());

    //     verify(svcMock).delete(TEST_SALES_CREDIT_MEMO_CODE);
    // }

    // @Test
    // public void card_returnsProperTemplate() {
    //     String template = controller.card(TEST_SALES_CREDIT_MEMO_CODE, TEST_PAGE, modelMock);

    //     assertEquals("salesCreditMemoCard.html", template);

    //     verify(modelMock).addAttribute("salesCreditMemo", salesCreditMemoMock);
    //     verify(modelMock).addAttribute("salesCreditMemoLines", salesCreditMemoLines);
    //     verify(modelMock).addAttribute("paging", salesCreditMemoLinePagingMock);
    // }

    // @Test
    // public void post_returnsProperTemplate() throws PostFailedException {
    //     RedirectView redirectView = controller.post(TEST_SALES_CREDIT_MEMO_CODE, httpSessionMock);

    //     assertEquals("/salesCreditMemoList", redirectView.getUrl());

    //     verify(salesCreditMemoPostSvcMock).post(TEST_SALES_CREDIT_MEMO_CODE);

    //     verify(feedbackSvcMock).give(FeedbackType.INFO, "Sales credit memo " + TEST_SALES_CREDIT_MEMO_CODE + " posted.",
    //             httpSessionMock);
    // }

    // @Test
    // public void postReturnsProperFeedback_whenPostingFails() throws PostFailedException {
    //     doThrow(new PostFailedException(TEST_POST_FAILED_MSG)).when(salesCreditMemoPostSvcMock)
    //             .post(TEST_SALES_CREDIT_MEMO_CODE);

    //     RedirectView redirectView = controller.post(TEST_SALES_CREDIT_MEMO_CODE, httpSessionMock);

    //     assertEquals("/salesCreditMemoList", redirectView.getUrl());

    //     verify(salesCreditMemoPostSvcMock).post(TEST_SALES_CREDIT_MEMO_CODE);

    //     verify(feedbackSvcMock).give(FeedbackType.ERROR,
    //             "Sales credit memo " + TEST_SALES_CREDIT_MEMO_CODE + " post failed: " + TEST_POST_FAILED_MSG,
    //             httpSessionMock);
    // }
}
