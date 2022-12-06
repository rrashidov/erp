package org.roko.erp.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.Customer;
import org.roko.erp.backend.model.PaymentMethod;
import org.roko.erp.backend.repositories.CustomerRepository;
import org.roko.erp.model.dto.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class CustomerServiceTest {

    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";
    private static final String TEST_PAYMENT_METHOD_NAME = "test-payment-method-name";

    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";
    private static final String TEST_ADDRESS = "test-address";
    private static final double TEST_BALANCE = 12.12;

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<Customer> pageMock;

    @Mock
    private Customer customerMock1;

    @Mock
    private Customer customerMock2;

    @Mock
    private Customer customerMock;
    
    @Mock
    private CustomerDTO customerDtoMock;

    @Mock
    private CustomerRepository repoMock;

    @Mock
    private PaymentMethodService paymentMethodSvcMock;
    
    @Mock
    private PaymentMethod paymentMethodMock;

    private CustomerService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(customerMock.getCode()).thenReturn(TEST_CODE);
        when(customerMock.getName()).thenReturn(TEST_NAME);
        when(customerMock.getAddress()).thenReturn(TEST_ADDRESS);
        when(customerMock.getPaymentMethod()).thenReturn(paymentMethodMock);
        when(customerMock.getBalance()).thenReturn(TEST_BALANCE);

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(customerMock));
        when(repoMock.findAll()).thenReturn(Arrays.asList(customerMock, customerMock1, customerMock2));
        when(repoMock.findAll(any(Pageable.class))).thenReturn(pageMock);

        when(customerDtoMock.getCode()).thenReturn(TEST_CODE);
        when(customerDtoMock.getName()).thenReturn(TEST_NAME);
        when(customerDtoMock.getAddress()).thenReturn(TEST_ADDRESS);
        when(customerDtoMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(paymentMethodMock.getCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);
        when(paymentMethodMock.getName()).thenReturn(TEST_PAYMENT_METHOD_NAME);

        when(paymentMethodSvcMock.get(TEST_PAYMENT_METHOD_CODE)).thenReturn(paymentMethodMock);

        svc = new CustomerServiceImpl(repoMock, paymentMethodSvcMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(customerMock);

        verify(repoMock).save(customerMock);
    }

    @Test
    public void update_delegatesToRepo(){
        svc.update(TEST_CODE, customerMock);

        verify(repoMock).save(customerMock);
    }

    @Test
    public void delete_delegatesToRepo(){
        svc.delete(TEST_CODE);

        verify(repoMock).findById(TEST_CODE);
        verify(repoMock).delete(customerMock);
    }

    @Test
    public void get_delegatesToRepo(){
        Customer retrievedCustomer = svc.get(TEST_CODE);

        assertEquals(customerMock, retrievedCustomer);

        verify(repoMock).balance(customerMock);
    }

    @Test 
    public void getReturnsNull_whenNotFound(){
        Customer retrievedCustomer = svc.get("non-existing-code");

        assertNull(retrievedCustomer);
    }

    @Test
    public void list_delegatesToRepo(){
        svc.list();

        verify(repoMock).findAll();

        verify(repoMock).balance(customerMock);
        verify(repoMock).balance(customerMock1);
        verify(repoMock).balance(customerMock2);
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        svc.list(TEST_PAGE);

        verify(repoMock).findAll(pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(Constants.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo(){
        svc.count();

        verify(repoMock).count();
    }

    @Test
    public void fromDTO_returnsProperValue(){
        Customer customer = svc.fromDTO(customerDtoMock);

        assertEquals(TEST_CODE, customer.getCode());
        assertEquals(TEST_NAME, customer.getName());
        assertEquals(TEST_ADDRESS, customer.getAddress());
        assertEquals(paymentMethodMock, customer.getPaymentMethod());
    }

    @Test
    public void toDTO_returnsProperValue() {
        CustomerDTO dto = svc.toDTO(customerMock);

        assertEquals(TEST_CODE, dto.getCode());
        assertEquals(TEST_NAME, dto.getName());
        assertEquals(TEST_ADDRESS, dto.getAddress());
        assertEquals(TEST_BALANCE, dto.getBalance());
        assertEquals(TEST_PAYMENT_METHOD_CODE, dto.getPaymentMethodCode());
        assertEquals(TEST_PAYMENT_METHOD_NAME, dto.getPaymentMethodName());
    }
}
