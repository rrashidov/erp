package org.roko.erp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.Customer;
import org.roko.erp.repositories.CustomerRepository;

public class CustomerServiceTest {

    private static final String TEST_CODE = "test-code";

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
    public void count_delegatesToRepo(){
        svc.count();

        verify(repoMock).count();
    }
}
