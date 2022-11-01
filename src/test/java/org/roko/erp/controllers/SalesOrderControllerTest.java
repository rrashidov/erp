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
import org.roko.erp.controllers.model.SalesOrderLineModel;
import org.roko.erp.controllers.model.SalesOrderModel;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.Customer;
import org.roko.erp.model.PaymentMethod;
import org.roko.erp.model.SalesOrder;
import org.roko.erp.model.SalesOrderLine;
import org.roko.erp.model.jpa.SalesOrderLineId;
import org.roko.erp.rules.sales.SalesOrderModelRule;
import org.roko.erp.services.CustomerService;
import org.roko.erp.services.PaymentMethodService;
import org.roko.erp.services.SalesOrderLineService;
import org.roko.erp.services.SalesOrderService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

public class SalesOrderControllerTest {

    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";

    private static final String TEST_CODE = "test-code";
    private static final String TEST_CUSTOMER_CODE = "test-customer-code";
    private static final String TEST_CUSTOMER_NAME = "test-customer-name";

    private static final Long TEST_PAGE = 123l;

    private static final long TEST_COUNT = 234;
    private static final long TEST_SALES_ORDER_LINE_COUNT = 789l;

    private List<SalesOrder> salesOrderList = new ArrayList<>();

    private List<Customer> customerList = new ArrayList<>();

    private List<PaymentMethod> paymentMethodList = new ArrayList<>();

    private List<SalesOrderLine> salesOrderLineList = new ArrayList<>();

    @Captor
    private ArgumentCaptor<SalesOrder> salesOrderArgumentCaptor;

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
    private SalesOrder salesOrderMock;

    @Mock
    private PaymentMethod paymentMethodMock;

    @Mock
    private Customer customerMock;

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
    private PaymentMethodService paymentMethodSvc;

    @Mock
    private SalesOrderLineService salesOrderLineSvcMock;

    @Mock
    private RedirectAttributes redirectAttributesMock;

    private SalesOrderModelRule salesOrderModelRule = new SalesOrderModelRule();

    private SalesOrderController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(salesOrderMock.getCode()).thenReturn(TEST_CODE);
        when(salesOrderMock.getCustomer()).thenReturn(customerMock);
        when(salesOrderMock.getDate()).thenReturn(dateMock);
        when(salesOrderMock.getPaymentMethod()).thenReturn(paymentMethodMock);

        when(paymentMethodMock.getCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(customerMock.getCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(customerMock.getPaymentMethod()).thenReturn(paymentMethodMock);
        when(customerMock.getName()).thenReturn(TEST_CUSTOMER_NAME);

        when(salesOrderModelMock.getCode()).thenReturn("");
        when(salesOrderModelMock.getCustomerCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(salesOrderModelMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(paymentMethodSvc.list()).thenReturn(paymentMethodList);
        when(paymentMethodSvc.get(TEST_PAYMENT_METHOD_CODE)).thenReturn(paymentMethodMock);

        when(customerSvcMock.list()).thenReturn(customerList);
        when(customerSvcMock.get(TEST_CUSTOMER_CODE)).thenReturn(customerMock);

        when(salesOrderLineSvcMock.list(salesOrderMock)).thenReturn(salesOrderLineList);
        when(salesOrderLineSvcMock.count(salesOrderMock)).thenReturn(TEST_SALES_ORDER_LINE_COUNT);

        when(svcMock.list()).thenReturn(salesOrderList);
        when(svcMock.count()).thenReturn(TEST_COUNT);
        when(svcMock.get(TEST_CODE)).thenReturn(salesOrderMock);
        when(svcMock.get(SalesOrderModelRule.TEST_SALES_ORDER_CODE)).thenReturn(salesOrderMock);

        when(pagingSvcMock.generate("salesOrder", TEST_PAGE, TEST_COUNT)).thenReturn(pagingDataMock);
        when(pagingSvcMock.generate("salesOrderLine", null, TEST_SALES_ORDER_LINE_COUNT)).thenReturn(salesOrderLinePagingMock);

        controller = new SalesOrderController(svcMock, pagingSvcMock, customerSvcMock, paymentMethodSvc, salesOrderLineSvcMock);
    }

    @Test
    public void listReturnsProperTemplate(){
        String template = controller.list(TEST_PAGE, modelMock);

        assertEquals("salesOrderList.html", template);

        verify(modelMock).addAttribute("salesOrders", salesOrderList);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }

    @Test
    public void salesOrderWizardReturnsProperTemplate_whenCalledForNewSalesOrder(){
        String template = controller.wizard(null, modelMock);

        assertEquals("salesOrderWizardFirstPage.html", template);

        verify(modelMock).addAttribute(eq("salesOrderModel"), salesOrderModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("customers", customerList);

        SalesOrderModel salesOrderModel = salesOrderModelArgumentCaptor.getValue();

        assertEquals("", salesOrderModel.getCode());
        assertEquals("", salesOrderModel.getCustomerCode());
    }

    @Test
    public void salesOrderWizardReturnsProperTemplate_whenCalledForExistingSalesOrder(){
        String template = controller.wizard(TEST_CODE, modelMock);

        assertEquals("salesOrderWizardFirstPage.html", template);

        verify(modelMock).addAttribute(eq("salesOrderModel"), salesOrderModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("customers", customerList);

        SalesOrderModel salesOrderModel = salesOrderModelArgumentCaptor.getValue();

        assertEquals(TEST_CODE, salesOrderModel.getCode());
        assertEquals(TEST_CUSTOMER_CODE, salesOrderModel.getCustomerCode());
    }

    @Test
    public void postSalesOrderWizardFirstPage_returnsProperTemplate(){
        String template = controller.postWizardFirstPage(salesOrderModelMock, modelMock);

        assertEquals("salesOrderWizardSecondPage.html", template);

        verify(salesOrderModelMock).setPaymentMethodCode(TEST_PAYMENT_METHOD_CODE);
        verify(salesOrderModelMock).setCustomerName(TEST_CUSTOMER_NAME);

        verify(modelMock).addAttribute("salesOrderModel", salesOrderModelMock);
        verify(modelMock).addAttribute("paymentMethods", paymentMethodList);
    }

    @Test
    public void postSalesOrderWizardSecondPage_createsSalesOrder(){
        salesOrderModelRule.stubSalesOrderModelForNewSalesOrder();
        
        RedirectView redirectView = controller.postWizardSecondPage(salesOrderModelRule.mock, redirectAttributesMock);

        assertEquals("/salesOrderCard", redirectView.getUrl());

        verify(svcMock).create(salesOrderArgumentCaptor.capture());

        SalesOrder createdSalesOrder = salesOrderArgumentCaptor.getValue();

        assertEquals(SalesOrderModelRule.TEST_CUSTOMER_CODE, createdSalesOrder.getCustomer().getCode());
        assertEquals(SalesOrderModelRule.TEST_PAYMENT_METHOD_CODE, createdSalesOrder.getPaymentMethod().getCode());
        assertEquals(SalesOrderModelRule.TEST_DATE, createdSalesOrder.getDate());
    }

    @Test
    public void postSalesOrderWizardSecondPage_updatesSalesOrder_whenCalledWithExistingCode(){
        RedirectView redirectView = controller.postWizardSecondPage(salesOrderModelRule.mock, redirectAttributesMock);

        assertEquals("/salesOrderCard", redirectView.getUrl());

        verify(svcMock).update(eq(SalesOrderModelRule.TEST_SALES_ORDER_CODE), salesOrderArgumentCaptor.capture());

        verify(redirectAttributesMock).addAttribute("code", SalesOrderModelRule.TEST_SALES_ORDER_CODE);

        SalesOrder updatedSalesOrder = salesOrderArgumentCaptor.getValue();

        assertEquals(SalesOrderModelRule.TEST_CUSTOMER_CODE, updatedSalesOrder.getCustomer().getCode());
        assertEquals(SalesOrderModelRule.TEST_PAYMENT_METHOD_CODE, updatedSalesOrder.getPaymentMethod().getCode());
        assertEquals(dateMock, updatedSalesOrder.getDate());
    }

    @Test
    public void deleteSalesOrder_deletesSalesOrder(){
        RedirectView redirectView = controller.delete(TEST_CODE);

        assertEquals("/salesOrderList", redirectView.getUrl());

        verify(svcMock).delete(TEST_CODE);
    }

    @Test
    public void salesOrderCard_returnsProperTemplate(){
        String template = controller.card(TEST_CODE, modelMock);

        assertEquals("salesOrderCard.html", template);

        verify(modelMock).addAttribute("salesOrder", salesOrderMock);
        verify(modelMock).addAttribute("salesOrderLines", salesOrderLineList);
        verify(modelMock).addAttribute("paging", salesOrderLinePagingMock);
    }

}
