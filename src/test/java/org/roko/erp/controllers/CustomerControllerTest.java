package org.roko.erp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.controllers.model.CustomerModel;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.Customer;
import org.roko.erp.model.CustomerLedgerEntry;
import org.roko.erp.model.PaymentMethod;
import org.roko.erp.services.CustomerLedgerEntryService;
import org.roko.erp.services.CustomerService;
import org.roko.erp.services.PaymentMethodService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

public class CustomerControllerTest {
    
    private static final String PAGING_MODEL_ATTRIBUTE_NAME = "paging";

    private static final String CUSTOMER_OBJECT_NAME = "customer";

    private static final String EXPECTED_CUSTOMER_LIST_TEMPLATE = "customerList.html";

    private static final int TEST_PAGE = 123;
    private static final int TEST_CUSTOMER_COUNT = 234;

    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";
    private static final String TEST_ADDRESS = "test-address";
    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";

    private List<PaymentMethod> paymentMethodList = new ArrayList<>();

    private List<Customer> customerList;

    private List<CustomerLedgerEntry> customerLedgerEntries = new ArrayList<>();

    @Mock
    private Customer customerMock;

    @Mock
    private PaymentMethod paymentMethodMock;

    @Mock
    private CustomerModel customerModelMock;

    @Mock
    private PaymentMethodService paymentMethodSvcMock;

    @Mock
    private CustomerLedgerEntryService customerLedgerEntrySvcMock;

    @Captor 
    private ArgumentCaptor<Customer> customerArgumentCaptor;
    
    @Captor
    private ArgumentCaptor<CustomerModel> customerModelArgumentCaptor;

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private Model modelMock;

    @Mock
    private CustomerService customerSvcMock;

    @Mock
    private PagingService pagingServiceMock;
    
    private CustomerController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        customerList = Arrays.asList(customerMock);

        when(paymentMethodMock.getCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(customerMock.getCode()).thenReturn(TEST_CODE);
        when(customerMock.getName()).thenReturn(TEST_NAME);
        when(customerMock.getAddress()).thenReturn(TEST_ADDRESS);
        when(customerMock.getPaymentMethod()).thenReturn(paymentMethodMock);

        when(customerModelMock.getCode()).thenReturn(TEST_CODE);
        when(customerModelMock.getName()).thenReturn(TEST_NAME);
        when(customerModelMock.getAddress()).thenReturn(TEST_ADDRESS);
        when(customerModelMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(paymentMethodSvcMock.list()).thenReturn(paymentMethodList);
        when(paymentMethodSvcMock.get(TEST_PAYMENT_METHOD_CODE)).thenReturn(paymentMethodMock);

        when(pagingServiceMock.generate(CUSTOMER_OBJECT_NAME, TEST_PAGE, TEST_CUSTOMER_COUNT)).thenReturn(pagingDataMock);

        when(customerSvcMock.count()).thenReturn(TEST_CUSTOMER_COUNT);
        when(customerSvcMock.list(TEST_PAGE)).thenReturn(customerList);
        when(customerSvcMock.get(TEST_CODE)).thenReturn(customerMock);

        controller = new CustomerController(customerSvcMock, pagingServiceMock, paymentMethodSvcMock,
                customerLedgerEntrySvcMock);
    }

    @Test
    public void listReturnsProperTemplate(){
        String returnedTemplate = controller.list(TEST_PAGE, modelMock);

        assertEquals(EXPECTED_CUSTOMER_LIST_TEMPLATE, returnedTemplate);

        verify(pagingServiceMock).generate(CUSTOMER_OBJECT_NAME, TEST_PAGE, TEST_CUSTOMER_COUNT);

        when(customerLedgerEntrySvcMock.findFor(customerMock)).thenReturn(customerLedgerEntries);

        verify(modelMock).addAttribute(PAGING_MODEL_ATTRIBUTE_NAME, pagingDataMock);
        verify(modelMock).addAttribute("customers", customerList);
    }

    @Test
    public void cardReturnsProperTemplate(){
        String template = controller.card(null, modelMock);

        assertEquals("customerCard.html", template);

        verify(modelMock).addAttribute(eq("customer"), customerModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("paymentMethods", paymentMethodList);

        CustomerModel customerModel = customerModelArgumentCaptor.getValue();

        assertEquals("", customerModel.getCode());
        assertEquals("", customerModel.getName());
        assertEquals("", customerModel.getAddress());
        assertEquals("", customerModel.getPaymentMethodCode());
    }

    @Test
    public void cardReturnsProperTemplate_whenCalledWithExistingCustomer(){
        String template = controller.card(TEST_CODE, modelMock);

        assertEquals("customerCard.html", template);

        verify(modelMock).addAttribute(eq("customer"), customerModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("paymentMethods", paymentMethodList);
        verify(modelMock).addAttribute("customerLedgerEntries", customerLedgerEntries);

        CustomerModel customerModel = customerModelArgumentCaptor.getValue();

        assertEquals(TEST_CODE, customerModel.getCode());
        assertEquals(TEST_NAME, customerModel.getName());
        assertEquals(TEST_ADDRESS, customerModel.getAddress());
        assertEquals(TEST_PAYMENT_METHOD_CODE, customerModel.getPaymentMethodCode());
    }


    @Test
    public void postingCustomerCard_createsNewCustomerIfCalledForNonExisting(){
        RedirectView redirectView = controller.post(customerModelMock);

        assertEquals("/customerList", redirectView.getUrl());

        verify(customerSvcMock).create(customerArgumentCaptor.capture());

        Customer customer = customerArgumentCaptor.getValue();

        assertEquals(TEST_CODE, customer.getCode());
        assertEquals(TEST_NAME, customer.getName());
        assertEquals(TEST_ADDRESS, customer.getAddress());
        assertEquals(paymentMethodMock, customer.getPaymentMethod());
    }

    @Test
    public void deletingCustomer_deletesCustomer(){
        RedirectView redirect = controller.delete(TEST_CODE);

        assertEquals("/customerList", redirect.getUrl());

        verify(customerSvcMock).delete(TEST_CODE);
    }
}
