package org.roko.erp.frontend.services;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.CustomerDTO;
import org.roko.erp.dto.list.CustomerList;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class CustomerServiceTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 12;

    @Mock
    private CustomerDTO customerMock;

    @Mock
    private RestTemplate restTemplateMock;

    private CustomerService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new CustomerServiceImpl(restTemplateMock);
    }

    @Test
    public void create_callsBackend() {
        svc.create(customerMock);

        verify(restTemplateMock).postForObject("/api/v1/customers", customerMock, String.class);
    }

    @Test
    public void update_callsBackend() {
        svc.update(TEST_CODE, customerMock);

        verify(restTemplateMock).put("/api/v1/customers/{code}", customerMock, TEST_CODE);
    }

    @Test
    public void delete_callsBackend() {
        svc.delete(TEST_CODE);

        verify(restTemplateMock).delete("/api/v1/customers/{code}", TEST_CODE);
    }

    @Test
    public void get_callsBackend() {
        svc.get(TEST_CODE);

        verify(restTemplateMock).getForObject("/api/v1/customers/{code}", CustomerDTO.class, TEST_CODE);
    }

    @Test 
    public void getReturnsNull_whenNotFound(){
        when(restTemplateMock.getForObject("/api/v1/customers/{code}", CustomerDTO.class, "non-existing-code")).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        CustomerDTO retrievedCustomer = svc.get("non-existing-code");

        assertNull(retrievedCustomer);
    }

    @Test
    public void list_callsBackend() {
        svc.list();

        verify(restTemplateMock).getForObject("/api/v1/customers", CustomerList.class);
    }

    @Test
    public void listWithPage_callsBackend() {
        svc.list(TEST_PAGE);

        verify(restTemplateMock).getForObject("/api/v1/customers/page/{page}", CustomerList.class, TEST_PAGE);
    }

}
