package org.roko.erp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.roko.erp.controllers.model.SalesOrderModel;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.Customer;
import org.roko.erp.model.PaymentMethod;
import org.roko.erp.model.SalesOrder;
import org.roko.erp.services.CustomerService;
import org.roko.erp.services.PaymentMethodService;
import org.roko.erp.services.SalesOrderService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

public class SalesOrderControllerTest {

    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";

    private static final String TEST_CUSTOMER_CODE = "test-customer-code";
    private static final String TEST_CUSTOMER_NAME = "test-customer-name";

    private static final Long TEST_PAGE = 123l;

    private static final long TEST_COUNT = 234;

    private List<SalesOrder> salesOrderList = new ArrayList<>();

    private List<Customer> customerList = new ArrayList<>();

    private List<PaymentMethod> paymentMethodList = new ArrayList<>();

    @Captor
    private ArgumentCaptor<SalesOrder> salesOrderArgumentCaptor;

    @Mock
    private PaymentMethod paymentMethodMock;

    @Mock
    private Customer customerMock;

    @Mock
    private SalesOrderModel salesOrderModelMock;

    @Mock
    private PagingData pagingDataMock;

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
    
    private SalesOrderController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(paymentMethodMock.getCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(customerMock.getPaymentMethod()).thenReturn(paymentMethodMock);
        when(customerMock.getName()).thenReturn(TEST_CUSTOMER_NAME);

        when(salesOrderModelMock.getCustomerCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(salesOrderModelMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(paymentMethodSvc.list()).thenReturn(paymentMethodList);
        when(paymentMethodSvc.get(TEST_PAYMENT_METHOD_CODE)).thenReturn(paymentMethodMock);

        when(customerSvcMock.list()).thenReturn(customerList);
        when(customerSvcMock.get(TEST_CUSTOMER_CODE)).thenReturn(customerMock);

        when(svcMock.list()).thenReturn(salesOrderList);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        when(pagingSvcMock.generate("salesOrder", TEST_PAGE, TEST_COUNT)).thenReturn(pagingDataMock);

        controller = new SalesOrderController(svcMock, pagingSvcMock, customerSvcMock, paymentMethodSvc);
    }

    @Test
    public void listReturnsProperTemplate(){
        String template = controller.list(TEST_PAGE, modelMock);

        assertEquals("salesOrderList.html", template);

        verify(modelMock).addAttribute("salesOrders", salesOrderList);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }

    @Test
    public void salesOrderWizardReturnsProperTemplate(){
        String template = controller.wizard(modelMock);

        assertEquals("salesOrderWizardFirstPage.html", template);

        verify(modelMock).addAttribute("customers", customerList);
    }

    @Test
    public void postSalesOrderWizardFirstPage_returnsProperTemplate(){
        String template = controller.postWizardFirstPage(salesOrderModelMock, modelMock);

        assertEquals("salesOrderWizardSecondPage.html", template);

        verify(salesOrderModelMock).setPaymentMethodCode(TEST_PAYMENT_METHOD_CODE);
        verify(salesOrderModelMock).setCustomerName(TEST_CUSTOMER_NAME);

        verify(modelMock).addAttribute("salesOrder", salesOrderModelMock);
        verify(modelMock).addAttribute("paymentMethods", paymentMethodList);
    }

    @Test
    public void postSalesOrderWizardSecondPage_createsSalesOrder(){
        Date testDate = new Date();

        when(salesOrderModelMock.getDate()).thenReturn(testDate);
        
        RedirectView redirectView = controller.postWizardSecondPage(salesOrderModelMock);

        assertEquals("/salesOrderList", redirectView.getUrl());

        verify(svcMock).create(salesOrderArgumentCaptor.capture());

        SalesOrder salesOrder = salesOrderArgumentCaptor.getValue();

        assertEquals(customerMock, salesOrder.getCustomer());
        assertEquals(paymentMethodMock, salesOrder.getPaymentMethod());
        assertEquals(testDate, salesOrder.getDate());
    }
}
