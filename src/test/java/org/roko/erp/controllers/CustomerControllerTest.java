package org.roko.erp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.Customer;
import org.roko.erp.services.CustomerService;
import org.springframework.ui.Model;

public class CustomerControllerTest {
    
    private static final String PAGING_MODEL_ATTRIBUTE_NAME = "paging";

    private static final String CUSTOMER_OBJECT_NAME = "customer";

    private static final String EXPECTED_CUSTOMER_LIST_TEMPLATE = "customerList.html";

    private static final Long TEST_PAGE = 123l;

    private static final Long TEST_CUSTOMER_COUNT = 234l;

    private List<Customer> customerList = new ArrayList<>();
    
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

        when(pagingServiceMock.generate(CUSTOMER_OBJECT_NAME, TEST_PAGE, TEST_CUSTOMER_COUNT)).thenReturn(pagingDataMock);

        when(customerSvcMock.count()).thenReturn(TEST_CUSTOMER_COUNT);
        when(customerSvcMock.list()).thenReturn(customerList);

        controller = new CustomerController(customerSvcMock, pagingServiceMock);
    }

    @Test
    public void listReturnsProperTemplate(){
        String returnedTemplate = controller.list(TEST_PAGE, modelMock);

        assertEquals(EXPECTED_CUSTOMER_LIST_TEMPLATE, returnedTemplate);

        verify(pagingServiceMock).generate(CUSTOMER_OBJECT_NAME, TEST_PAGE, TEST_CUSTOMER_COUNT);

        verify(modelMock).addAttribute(PAGING_MODEL_ATTRIBUTE_NAME, pagingDataMock);
        verify(modelMock).addAttribute("customers", customerList);
    }
}
