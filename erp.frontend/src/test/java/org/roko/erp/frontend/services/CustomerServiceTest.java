package org.roko.erp.frontend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.frontend.controllers.paging.PagingServiceImpl;
import org.roko.erp.frontend.model.Customer;
import org.roko.erp.frontend.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class CustomerServiceTest {

    private static final String TEST_CODE = "test-code";

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
    private CustomerRepository repoMock;
    
    private CustomerService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        List<Customer> customers = Arrays.asList(customerMock, customerMock1, customerMock2);

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(customerMock));
        when(repoMock.findAll()).thenReturn(customers);
        when(repoMock.findAll(any(Pageable.class))).thenReturn(pageMock);

        svc = new CustomerServiceImpl(repoMock);
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
        assertEquals(PagingServiceImpl.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo(){
        svc.count();

        verify(repoMock).count();
    }
}
