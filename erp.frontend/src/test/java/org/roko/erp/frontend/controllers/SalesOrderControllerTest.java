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

import javax.servlet.http.HttpSession;

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
import org.roko.erp.frontend.controllers.model.SalesOrderLineModel;
import org.roko.erp.frontend.controllers.model.SalesOrderModel;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.model.SalesOrderLine;
import org.roko.erp.frontend.model.jpa.SalesOrderLineId;
import org.roko.erp.frontend.rules.sales.SalesOrderModelRule;
import org.roko.erp.frontend.services.CustomerService;
import org.roko.erp.frontend.services.FeedbackService;
import org.roko.erp.frontend.services.PaymentMethodService;
import org.roko.erp.frontend.services.PostFailedException;
import org.roko.erp.frontend.services.SalesCodeSeriesService;
import org.roko.erp.frontend.services.SalesOrderLineService;
import org.roko.erp.frontend.services.SalesOrderPostService;
import org.roko.erp.frontend.services.SalesOrderService;
import org.roko.erp.frontend.services.util.Feedback;
import org.roko.erp.frontend.services.util.FeedbackType;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

public class SalesOrderControllerTest {

    private static final String TEST_POST_FAILED_EXCEPTION_MSG = "test-post-failed-exception-msg";

    private static final String NEW_SALES_ORDER_CODE = "new-sales-order-code";

    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";

    private static final String TEST_CODE = "test-code";
    private static final String TEST_CUSTOMER_CODE = "test-customer-code";
    private static final String TEST_CUSTOMER_NAME = "test-customer-name";

    private static final String TEST_NEW_SALES_ORDER_CODE = "test-new-sales-order-code";

    private static final int TEST_PAGE = 123;

    private static final long TEST_COUNT = 234;
    private static final long TEST_SALES_ORDER_LINE_COUNT = 789;

    private List<SalesDocumentDTO> salesOrders = new ArrayList<>();

    private List<SalesDocumentLineDTO> salesOrderLines = new ArrayList<>();

    private List<CustomerDTO> customers = new ArrayList<>();

    private List<PaymentMethodDTO> paymentMethods = new ArrayList<>();

    @Captor
    private ArgumentCaptor<SalesDocumentDTO> salesOrderArgumentCaptor;

    @Captor
    private ArgumentCaptor<SalesOrderModel> salesOrderModelArgumentCaptor;

    @Captor
    private ArgumentCaptor<SalesOrderLine> salesOrderLineArgumentCaptor;

    @Captor
    private ArgumentCaptor<SalesOrderLineModel> salesOrderLineModelArgumentCaptor;

    @Captor
    private ArgumentCaptor<SalesOrderLineId> salesOrderLineIdArgumentCaptor;

    @Mock
    private Date dateMock;

    @Mock
    private SalesDocumentDTO salesOrderMock;

    @Mock
    private PaymentMethodDTO paymentMethodMock;

    @Mock
    private CustomerDTO customerMock;

    @Mock
    private SalesOrderModel salesOrderModelMock;

    @Mock
    private SalesOrderLine salesOrderLineMock;

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private PagingData salesOrderLinePagingMock;

    @Mock
    private Model modelMock;

    @Mock
    private SalesOrderService svcMock;

    @Mock
    private PagingService pagingSvcMock;

    @Mock
    private CustomerService customerSvcMock;

    @Mock
    private CustomerList customerList;

    @Mock
    private PaymentMethodService paymentMethodSvc;

    @Mock
    private PaymentMethodList paymentMethodList;

    @Mock
    private SalesOrderLineService salesOrderLineSvcMock;

    @Mock
    private RedirectAttributes redirectAttributesMock;

    @Mock
    private SalesOrderPostService salesOrderPostSvcMock;

    @Mock
    private SalesCodeSeriesService salesCodeSeriesSvcMock;

    @Mock
    private HttpSession httpSessionMock;

    @Mock
    private Feedback feedbackMock;

    @Mock
    private FeedbackService feedbackSvcMock;

    // private SalesOrderModelRule salesOrderModelRule = new SalesOrderModelRule();

    @Mock
    private SalesDocumentList salesOrderList;

    @Mock
    private SalesDocumentLineList salesOrderLineList;

    private SalesOrderController controller;

    private SalesOrderModelRule salesOrderModelRule = new SalesOrderModelRule();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        salesOrders = Arrays.asList(salesOrderMock);

        when(salesOrderMock.getCode()).thenReturn(TEST_CODE);
        when(salesOrderMock.getCustomerCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(salesOrderMock.getDate()).thenReturn(dateMock);
        when(salesOrderMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(paymentMethodMock.getCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(customerMock.getCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(customerMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);
        when(customerMock.getName()).thenReturn(TEST_CUSTOMER_NAME);

        when(salesOrderModelMock.getCode()).thenReturn("");
        when(salesOrderModelMock.getCustomerCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(salesOrderModelMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(paymentMethodList.getData()).thenReturn(paymentMethods);

        when(paymentMethodSvc.list()).thenReturn(paymentMethodList);
        // when(paymentMethodSvc.get(TEST_PAYMENT_METHOD_CODE)).thenReturn(paymentMethodMock);

        when(customerList.getData()).thenReturn(customers);

        when(customerSvcMock.list()).thenReturn(customerList);
        when(customerSvcMock.get(TEST_CUSTOMER_CODE)).thenReturn(customerMock);

        when(salesOrderLineList.getData()).thenReturn(salesOrderLines);
        when(salesOrderLineList.getCount()).thenReturn(TEST_SALES_ORDER_LINE_COUNT);

        when(salesOrderLineSvcMock.list(TEST_CODE, TEST_PAGE)).thenReturn(salesOrderLineList);

        when(salesOrderList.getData()).thenReturn(salesOrders);
        when(salesOrderList.getCount()).thenReturn(TEST_COUNT);

        when(svcMock.list(TEST_PAGE)).thenReturn(salesOrderList);
        when(svcMock.get(TEST_CODE)).thenReturn(salesOrderMock);
        when(svcMock.get(SalesOrderModelRule.TEST_SALES_ORDER_CODE)).thenReturn(salesOrderMock);
        when(svcMock.create(any(SalesDocumentDTO.class))).thenReturn(NEW_SALES_ORDER_CODE);

        when(pagingSvcMock.generate("salesOrder", TEST_PAGE, (int) TEST_COUNT)).thenReturn(pagingDataMock);
        when(pagingSvcMock.generate("salesOrderCard", TEST_CODE, TEST_PAGE, (int) TEST_SALES_ORDER_LINE_COUNT))
                .thenReturn(salesOrderLinePagingMock);

        when(salesCodeSeriesSvcMock.orderCode()).thenReturn(TEST_NEW_SALES_ORDER_CODE);

        when(feedbackSvcMock.get(httpSessionMock)).thenReturn(feedbackMock);

        controller = new SalesOrderController(svcMock, pagingSvcMock, customerSvcMock, paymentMethodSvc,
                salesOrderLineSvcMock, salesOrderPostSvcMock, feedbackSvcMock);
    }

    @Test
    public void listReturnsProperTemplate() {
        String template = controller.list(TEST_PAGE, modelMock, httpSessionMock);

        assertEquals("salesOrderList.html", template);

        verify(modelMock).addAttribute("salesOrders", salesOrders);
        verify(modelMock).addAttribute("paging", pagingDataMock);
        verify(modelMock).addAttribute("feedback", feedbackMock);
    }

    @Test
    public void salesOrderWizardReturnsProperTemplate_whenCalledForNewSalesOrder() {
        String template = controller.wizard(null, modelMock);

        assertEquals("salesOrderWizardFirstPage.html", template);

        verify(modelMock).addAttribute(eq("salesOrderModel"), salesOrderModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("customers", customers);

        SalesOrderModel salesOrderModel = salesOrderModelArgumentCaptor.getValue();

        assertEquals("", salesOrderModel.getCode());
        assertEquals("", salesOrderModel.getCustomerCode());
    }

    @Test
    public void salesOrderWizardReturnsProperTemplate_whenCalledForExistingSalesOrder() {
        String template = controller.wizard(TEST_CODE, modelMock);

        assertEquals("salesOrderWizardFirstPage.html", template);

        verify(modelMock).addAttribute(eq("salesOrderModel"), salesOrderModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("customers", customers);

        SalesOrderModel salesOrderModel = salesOrderModelArgumentCaptor.getValue();

        assertEquals(TEST_CODE, salesOrderModel.getCode());
        assertEquals(TEST_CUSTOMER_CODE, salesOrderModel.getCustomerCode());
    }

    @Test
    public void postSalesOrderWizardFirstPage_returnsProperTemplate() {
        String template = controller.postWizardFirstPage(salesOrderModelMock, modelMock);

        assertEquals("salesOrderWizardSecondPage.html", template);

        verify(salesOrderModelMock).setPaymentMethodCode(TEST_PAYMENT_METHOD_CODE);
        verify(salesOrderModelMock).setCustomerName(TEST_CUSTOMER_NAME);

        verify(modelMock).addAttribute("salesOrderModel", salesOrderModelMock);
        verify(modelMock).addAttribute("paymentMethods", paymentMethods);
    }

    @Test
    public void postSalesOrderWizardSecondPage_createsSalesOrder() {
        salesOrderModelRule.stubSalesOrderModelForNewSalesOrder();

        RedirectView redirectView = controller.postWizardSecondPage(salesOrderModelRule.mock, redirectAttributesMock);

        assertEquals("/salesOrderCard", redirectView.getUrl());

        verify(redirectAttributesMock).addAttribute("code", NEW_SALES_ORDER_CODE);

        verify(svcMock).create(salesOrderArgumentCaptor.capture());

        SalesDocumentDTO createdSalesOrder = salesOrderArgumentCaptor.getValue();

        assertEquals(SalesOrderModelRule.TEST_CUSTOMER_CODE, createdSalesOrder.getCustomerCode());
        assertEquals(SalesOrderModelRule.TEST_PAYMENT_METHOD_CODE, createdSalesOrder.getPaymentMethodCode());
        assertEquals(SalesOrderModelRule.TEST_DATE, createdSalesOrder.getDate());
    }

    @Test
    public void postSalesOrderWizardSecondPage_updatesSalesOrder_whenCalledWithExistingCode() {
        RedirectView redirectView = controller.postWizardSecondPage(salesOrderModelRule.mock, redirectAttributesMock);

        assertEquals("/salesOrderCard", redirectView.getUrl());

        verify(svcMock).update(eq(SalesOrderModelRule.TEST_SALES_ORDER_CODE), salesOrderArgumentCaptor.capture());

        verify(redirectAttributesMock).addAttribute("code", SalesOrderModelRule.TEST_SALES_ORDER_CODE);

        SalesDocumentDTO updatedSalesOrder = salesOrderArgumentCaptor.getValue();

        assertEquals(SalesOrderModelRule.TEST_CUSTOMER_CODE, updatedSalesOrder.getCustomerCode());
        assertEquals(SalesOrderModelRule.TEST_PAYMENT_METHOD_CODE, updatedSalesOrder.getPaymentMethodCode());
        assertEquals(dateMock, updatedSalesOrder.getDate());
    }

    @Test
    public void deleteSalesOrder_deletesSalesOrder() {
        RedirectView redirectView = controller.delete(TEST_CODE);

        assertEquals("/salesOrderList", redirectView.getUrl());

        verify(svcMock).delete(TEST_CODE);
    }

    @Test
    public void salesOrderCard_returnsProperTemplate() {
        String template = controller.card(TEST_CODE, TEST_PAGE, modelMock);

        assertEquals("salesOrderCard.html", template);

        verify(modelMock).addAttribute("salesOrder", salesOrderMock);
        verify(modelMock).addAttribute("salesOrderLines", salesOrderLines);
        verify(modelMock).addAttribute("paging", salesOrderLinePagingMock);
    }

    @Test
    public void post_callsRespectiveService() throws PostFailedException {
        RedirectView redirectView = controller.post(TEST_CODE, httpSessionMock);

        assertEquals("/salesOrderList", redirectView.getUrl());

        verify(salesOrderPostSvcMock).post(TEST_CODE);

        verify(feedbackSvcMock).give(FeedbackType.INFO, "Sales Order " + TEST_CODE +
                " posted.", httpSessionMock);
    }

    @Test
    public void postReturnsErrorFeedback_whenPostingFails() throws PostFailedException {
        doThrow(new PostFailedException(TEST_POST_FAILED_EXCEPTION_MSG)).when(salesOrderPostSvcMock).post(TEST_CODE);

        RedirectView redirectView = controller.post(TEST_CODE, httpSessionMock);

        assertEquals("/salesOrderList", redirectView.getUrl());

        verify(salesOrderPostSvcMock).post(TEST_CODE);

        verify(feedbackSvcMock).give(FeedbackType.ERROR,
                "Sales Order " + TEST_CODE + " post failed: " +
                        TEST_POST_FAILED_EXCEPTION_MSG,
                httpSessionMock);
    }
}
