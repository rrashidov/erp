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
import org.roko.erp.backend.model.SalesOrder;
import org.roko.erp.backend.repositories.SalesOrderRepository;
import org.roko.erp.model.dto.SalesOrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class SalesOrderServiceTest {

    private static final String TEST_CUSTOMER_CODE = "test-customer-code";
    private static final String TEST_CUSTOMER_NAME = "test-customer-name";
    private static final Date TEST_DATE = new Date();
    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";
    private static final String TEST_PAYMENT_METHOD_NAME = "payment-method-name";
    private static final double TEST_AMOUNT = 123.12;

    private static final String NON_EXISTING_CODE = "non-existing-code";

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<SalesOrder> pageMock;

    @Mock
	private SalesOrder salesOrderMock;

    @Mock
    private SalesOrder salesOrderMock1;

    @Mock
    private SalesOrder salesOrderMock2;

    @Mock
    private SalesOrderRepository repoMock;
    
    @Mock
    private SalesOrderDTO salesOrderDtoMock;

    @Mock
    private CustomerService customerSvcMock;

    @Mock
    private Customer customerMock;

    @Mock
    private PaymentMethodService paymentMethodSvcMock;

    @Mock
    private PaymentMethod paymentMethodMock;

    @Mock
    private SalesCodeSeriesService salesCodeSeriesSvcMock;

    private SalesOrderService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(paymentMethodMock.getCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);
        when(paymentMethodMock.getName()).thenReturn(TEST_PAYMENT_METHOD_NAME);

        when(paymentMethodSvcMock.get(TEST_PAYMENT_METHOD_CODE)).thenReturn(paymentMethodMock);

        when(customerMock.getCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(customerMock.getName()).thenReturn(TEST_CUSTOMER_NAME);

        when(customerSvcMock.get(TEST_CUSTOMER_CODE)).thenReturn(customerMock);

        when(salesCodeSeriesSvcMock.orderCode()).thenReturn(TEST_CODE);

        when(salesOrderDtoMock.getCustomerCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(salesOrderDtoMock.getDate()).thenReturn(TEST_DATE);
        when(salesOrderDtoMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(salesOrderMock.getCode()).thenReturn(TEST_CODE);
        when(salesOrderMock.getCustomer()).thenReturn(customerMock);
        when(salesOrderMock.getDate()).thenReturn(TEST_DATE);
        when(salesOrderMock.getPaymentMethod()).thenReturn(paymentMethodMock);
        when(salesOrderMock.getAmount()).thenReturn(TEST_AMOUNT);
        
        when(repoMock.findAll()).thenReturn(Arrays.asList(salesOrderMock, salesOrderMock1, salesOrderMock2));
        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(salesOrderMock));
        when(repoMock.findAll(any(Pageable.class))).thenReturn(pageMock);

        svc = new SalesOrderServiceImpl(repoMock, customerSvcMock, paymentMethodSvcMock, salesCodeSeriesSvcMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(salesOrderMock);

        verify(repoMock).save(salesOrderMock);
    }

    @Test
    public void update_delegatesToRepo(){
        svc.update(TEST_CODE, salesOrderMock);

        verify(repoMock).findById(TEST_CODE);
        verify(repoMock).save(salesOrderMock);
    }

    @Test
    public void delete_delegatesToRepo(){
        svc.delete(TEST_CODE);

        verify(repoMock).delete(salesOrderMock);
    }

    @Test
    public void get_delegatesToRepo(){
        svc.get(TEST_CODE);

        verify(repoMock).findById(TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenEntityNotFound(){
        SalesOrder salesOrder = svc.get(NON_EXISTING_CODE);

        assertNull(salesOrder);
    }

    @Test
    public void list_delegatesToRepo(){
        svc.list();

        verify(repoMock).findAll();

        verify(repoMock).amount(salesOrderMock);
        verify(repoMock).amount(salesOrderMock1);
        verify(repoMock).amount(salesOrderMock2);
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
    public void fromDTO_returnsProperValue() {
        SalesOrder salesOrder = svc.fromDTO(salesOrderDtoMock);

        assertEquals(TEST_CODE, salesOrder.getCode());
        assertEquals(customerMock, salesOrder.getCustomer());
        assertEquals(TEST_DATE, salesOrder.getDate());
        assertEquals(paymentMethodMock, salesOrder.getPaymentMethod());
    }

    @Test
    public void toDTO_returnsProperValue() {
        SalesOrderDTO dto = svc.toDTO(salesOrderMock);

        assertEquals(TEST_CODE, dto.getCode());
        assertEquals(TEST_CUSTOMER_CODE, dto.getCustomerCode());
        assertEquals(TEST_CUSTOMER_NAME, dto.getCustomerName());
        assertEquals(TEST_DATE, dto.getDate());
        assertEquals(TEST_PAYMENT_METHOD_CODE, dto.getPaymentMethodCode());
        assertEquals(TEST_PAYMENT_METHOD_NAME, dto.getPaymentMethodName());
        assertEquals(TEST_AMOUNT, dto.getAmount());
    }
}
