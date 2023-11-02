package org.roko.erp.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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
import org.roko.erp.backend.model.DocumentPostStatus;
import org.roko.erp.backend.model.PaymentMethod;
import org.roko.erp.backend.model.SalesCreditMemo;
import org.roko.erp.backend.repositories.SalesCreditMemoRepository;
import org.roko.erp.dto.SalesDocumentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class SalesCreditMemoServiceTest {

    private static final Date TEST_DATE = new Date();

    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";
    private static final String TEST_PAYMENT_METHOD_NAME = "test-payment-method-name";

    private static final String TEST_CUSTOMER_CODE = "test-customer-code";
    private static final String TEST_CUSTOMER_NAME = "test-customer-name";

    private static final String TEST_SALES_CREDIT_MEMO_CODE = "test-sales-credit-memo-code";

    private static final DocumentPostStatus TEST_POST_STATUS = DocumentPostStatus.FAILED;
    private static final String TEST_POST_STATUS_REASON = "test-post-status-reason";

    private static final String NON_EXISTING_CODE = "non-existing-code";

    private static final String TEST_CODE = "test-code";

    private static final BigDecimal TEST_AMOUNT = new BigDecimal(123.12);

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<SalesCreditMemo> pageMock;

    @Mock
    private PaymentMethod updatedPaymentMethodMock;

    @Mock
    private Date updatedDateMock;

    @Mock
    private Customer updatedCustomerMock;

    @Mock
    private SalesCreditMemo salesCreditMemoMock;

    @Mock
    private SalesCreditMemo salesCreditMemoMock1;

    @Mock
    private SalesCreditMemo salesCreditMemoMock2;

    @Mock
    private SalesCreditMemo salesCreditMemoToUpdateMock;

    @Mock
    private SalesDocumentDTO salesDocumentDtoMock;

    @Mock
    private SalesCreditMemoRepository repoMock;

    @Mock
    private SalesCodeSeriesService salesCodeSeriesSvcMock;

    @Mock
    private CustomerService customerSvcMock;

    @Mock
    private Customer customerMock;

    @Mock
    private PaymentMethodService paymentMethodSvcMock;

    @Mock
    private PaymentMethod paymentMethodMock;

    private SalesCreditMemoService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(customerMock.getCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(customerMock.getName()).thenReturn(TEST_CUSTOMER_NAME);

        when(paymentMethodMock.getCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);
        when(paymentMethodMock.getName()).thenReturn(TEST_PAYMENT_METHOD_NAME);

        when(salesCreditMemoMock.getCode()).thenReturn(TEST_SALES_CREDIT_MEMO_CODE);
        when(salesCreditMemoMock.getCustomer()).thenReturn(customerMock);
        when(salesCreditMemoMock.getDate()).thenReturn(TEST_DATE);
        when(salesCreditMemoMock.getPaymentMethod()).thenReturn(paymentMethodMock);
        when(salesCreditMemoMock.getPostStatus()).thenReturn(TEST_POST_STATUS);
        when(salesCreditMemoMock.getPostStatusReason()).thenReturn(TEST_POST_STATUS_REASON);
        when(salesCreditMemoMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(customerSvcMock.get(TEST_CUSTOMER_CODE)).thenReturn(customerMock);

        when(paymentMethodSvcMock.get(TEST_PAYMENT_METHOD_CODE)).thenReturn(paymentMethodMock);

        when(salesDocumentDtoMock.getCustomerCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(salesDocumentDtoMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);
        when(salesDocumentDtoMock.getDate()).thenReturn(TEST_DATE);

        when(salesCodeSeriesSvcMock.creditMemoCode()).thenReturn(TEST_SALES_CREDIT_MEMO_CODE);

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(salesCreditMemoMock));
        when(repoMock.findAll()).thenReturn(Arrays.asList(salesCreditMemoMock, salesCreditMemoMock1, salesCreditMemoMock2));
        when(repoMock.findAll(any(Pageable.class))).thenReturn(pageMock);

        when(salesCreditMemoToUpdateMock.getCustomer()).thenReturn(updatedCustomerMock);
        when(salesCreditMemoToUpdateMock.getDate()).thenReturn(updatedDateMock);
        when(salesCreditMemoToUpdateMock.getPaymentMethod()).thenReturn(updatedPaymentMethodMock);
        when(salesCreditMemoToUpdateMock.getPostStatus()).thenReturn(TEST_POST_STATUS);
        when(salesCreditMemoToUpdateMock.getPostStatusReason()).thenReturn(TEST_POST_STATUS_REASON);

        svc = new SalesCreditMemoServiceImpl(repoMock, customerSvcMock, paymentMethodSvcMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(salesCreditMemoMock);

        verify(repoMock).save(salesCreditMemoMock);
    }

    @Test
    public void update_delegatesToRepo(){
        svc.update(TEST_CODE, salesCreditMemoToUpdateMock);

        verify(repoMock).findById(TEST_CODE);
        verify(repoMock).save(salesCreditMemoMock);

        verify(salesCreditMemoMock).setCustomer(updatedCustomerMock);
        verify(salesCreditMemoMock).setDate(updatedDateMock);
        verify(salesCreditMemoMock).setPaymentMethod(updatedPaymentMethodMock);
        verify(salesCreditMemoMock).setPostStatus(TEST_POST_STATUS);
        verify(salesCreditMemoMock).setPostStatusReason(TEST_POST_STATUS_REASON);
    }

    @Test
    public void delete_delegatesToRepo(){
        svc.delete(TEST_CODE);

        verify(repoMock).delete(salesCreditMemoMock);
    }

    @Test
    public void get_delegatesToRepo(){
        SalesCreditMemo salesCreditMemo = svc.get(TEST_CODE);

        assertEquals(salesCreditMemoMock, salesCreditMemo);
    }

    @Test
    public void getReturnsNull_whenCalledWithNonExistingCode(){
        SalesCreditMemo salesCreditMemo = svc.get(NON_EXISTING_CODE);

        assertNull(salesCreditMemo);
    }

    @Test
    public void list_delegatesToRepo(){
        svc.list();

        verify(repoMock).findAll();

        verify(repoMock).amount(salesCreditMemoMock);
        verify(repoMock).amount(salesCreditMemoMock1);
        verify(repoMock).amount(salesCreditMemoMock2);
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
        SalesCreditMemo salesCreditMemo = svc.fromDTO(salesDocumentDtoMock);

        assertNull(salesCreditMemo.getCode());
        assertEquals(customerMock, salesCreditMemo.getCustomer());
        assertEquals(TEST_DATE, salesCreditMemo.getDate());
        assertEquals(paymentMethodMock, salesCreditMemo.getPaymentMethod());
    }

    @Test
    public void toDTO_returnsProperValue(){
        SalesDocumentDTO dto = svc.toDTO(salesCreditMemoMock);

        assertEquals(TEST_SALES_CREDIT_MEMO_CODE, dto.getCode());
        assertEquals(TEST_CUSTOMER_CODE, dto.getCustomerCode());
        assertEquals(TEST_CUSTOMER_NAME, dto.getCustomerName());
        assertEquals(TEST_DATE, dto.getDate());
        assertEquals(TEST_PAYMENT_METHOD_CODE, dto.getPaymentMethodCode());
        assertEquals(TEST_PAYMENT_METHOD_NAME, dto.getPaymentMethodName());
        assertEquals(TEST_AMOUNT, dto.getAmount());
        assertEquals(TEST_POST_STATUS.name(), dto.getPostStatus());
        assertEquals(TEST_POST_STATUS_REASON, dto.getPostStatusReason());
    }
}
