package org.roko.erp.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.Customer;
import org.roko.erp.backend.model.PaymentMethod;
import org.roko.erp.backend.model.PostedSalesOrder;
import org.roko.erp.backend.repositories.PostedSalesOrderRepository;
import org.roko.erp.dto.PostedSalesDocumentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PostedSalesOrderServiceTest {
    
    private static final String TEST_CODE = "test-code";
    private static final Date TEST_DATE = new Date();
    private static final String TEST_CUSTOMER_NAME = "test-customer-name";
    private static final String TEST_PAYMENT_METHOD_NAME = "test-payment-method-name";
    private static final double TEST_AMOUNT = 11.22;

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<PostedSalesOrder> pageMock;

    @Mock
    private PostedSalesOrder postedSalesOrderMock;

    @Mock
    private PostedSalesOrder postedSalesOrderMock1;

    @Mock
    private PostedSalesOrder postedSalesOrderMock2;

    @Mock
    private PostedSalesOrderRepository repoMock;

    @Mock
    private Customer customerMock;

    @Mock
    private PaymentMethod paymentMethodMock;

    private PostedSalesOrderService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(customerMock.getName()).thenReturn(TEST_CUSTOMER_NAME);

        when(paymentMethodMock.getName()).thenReturn(TEST_PAYMENT_METHOD_NAME);

        when(postedSalesOrderMock.getCode()).thenReturn(TEST_CODE);
        when(postedSalesOrderMock.getCustomer()).thenReturn(customerMock);
        when(postedSalesOrderMock.getDate()).thenReturn(TEST_DATE);
        when(postedSalesOrderMock.getPaymentMethod()).thenReturn(paymentMethodMock);
        when(postedSalesOrderMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(postedSalesOrderMock));
        when(repoMock.findAll(any(Pageable.class))).thenReturn(pageMock);
        when(repoMock.findAll()).thenReturn(Arrays.asList(postedSalesOrderMock, postedSalesOrderMock1, postedSalesOrderMock2));

        svc = new PostedSalesOrderServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(postedSalesOrderMock);

        verify(repoMock).save(postedSalesOrderMock);
    }

    @Test
    public void get_delegatesToRepo(){
        svc.get(TEST_CODE);

        verify(repoMock).findById(TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenCalledWithNonExistingId(){
        PostedSalesOrder postedSalesOrder = svc.get("non-existing-code");

        assertNull(postedSalesOrder);
    }

    @Test
    public void list_delegatesToRepo(){
        svc.list();

        verify(repoMock).findAll();

        verify(repoMock).amount(postedSalesOrderMock);
        verify(repoMock).amount(postedSalesOrderMock1);
        verify(repoMock).amount(postedSalesOrderMock2);
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
    public void toDTO_returnsProperResult() {
        PostedSalesDocumentDTO dto = svc.toDTO(postedSalesOrderMock);

        assertEquals(TEST_CODE, dto.getCode());
        assertEquals(TEST_CUSTOMER_NAME, dto.getCustomerName());
        assertEquals(TEST_DATE, dto.getDate());
        assertEquals(TEST_PAYMENT_METHOD_NAME, dto.getPaymentMethodName());
        assertEquals(TEST_AMOUNT, dto.getAmount());
    }
}
