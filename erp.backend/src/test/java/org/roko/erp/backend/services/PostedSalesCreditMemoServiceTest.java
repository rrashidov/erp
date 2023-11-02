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
import org.roko.erp.backend.model.PaymentMethod;
import org.roko.erp.backend.model.PostedSalesCreditMemo;
import org.roko.erp.backend.repositories.PostedSalesCreditMemoRepository;
import org.roko.erp.dto.PostedSalesDocumentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PostedSalesCreditMemoServiceTest {
    
    private static final String TEST_CUSTOMER_NAME = "test-customer-name";

    private static final String TEST_PAYMENT_METHOD_NAME = "test-payment-method-name";

    private static final BigDecimal TEST_AMOUNT = new BigDecimal(12.12);

    private static final String NON_EXISTING_CODE = "non-existing-code";

    private static final String TEST_CODE = "test-code";
    private static final Date TEST_DATE = new Date();

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<PostedSalesCreditMemo> pageMock;

    @Mock
    private PostedSalesCreditMemo postedSalesCreditMemoMock;

    @Mock
    private PostedSalesCreditMemo postedSalesCreditMemoMock1;

    @Mock
    private PostedSalesCreditMemo postedSalesCreditMemoMock2;

    @Mock
    private PostedSalesCreditMemoRepository repoMock;

    @Mock
    private Customer customerMock;

    @Mock
    private PaymentMethod paymentMethodMock;

    private PostedSalesCreditMemoService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(customerMock.getName()).thenReturn(TEST_CUSTOMER_NAME);

        when(paymentMethodMock.getName()).thenReturn(TEST_PAYMENT_METHOD_NAME);

        when(postedSalesCreditMemoMock.getCode()).thenReturn(TEST_CODE);
        when(postedSalesCreditMemoMock.getCustomer()).thenReturn(customerMock);
        when(postedSalesCreditMemoMock.getDate()).thenReturn(TEST_DATE);
        when(postedSalesCreditMemoMock.getPaymentMethod()).thenReturn(paymentMethodMock);
        when(postedSalesCreditMemoMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(postedSalesCreditMemoMock));
        when(repoMock.findAll(any(Pageable.class))).thenReturn(pageMock);
        when(repoMock.findAll()).thenReturn(Arrays.asList(postedSalesCreditMemoMock, postedSalesCreditMemoMock1, postedSalesCreditMemoMock2));

        svc = new PostedSalesCreditMemoServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(postedSalesCreditMemoMock);

        verify(repoMock).save(postedSalesCreditMemoMock);
    }

    @Test
    public void get_delegatesToRepo(){
        svc.get(TEST_CODE);

        verify(repoMock).findById(TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenCalledWithNonExistingCode(){
        PostedSalesCreditMemo postedSalesCreditMemo = svc.get(NON_EXISTING_CODE);

        assertNull(postedSalesCreditMemo);
    }

    @Test
    public void list_delegatesToRepo() {
        svc.list();

        verify(repoMock).findAll();

        verify(repoMock).amount(postedSalesCreditMemoMock);
        verify(repoMock).amount(postedSalesCreditMemoMock1);
        verify(repoMock).amount(postedSalesCreditMemoMock2);
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
    public void count_delegatesToRepo() {
        svc.count();

        verify(repoMock).count();
    }

    @Test
    public void toDTO_returnsProperValue() {
        PostedSalesDocumentDTO dto = svc.toDTO(postedSalesCreditMemoMock);

        assertEquals(TEST_CODE, dto.getCode());
        assertEquals(TEST_CUSTOMER_NAME, dto.getCustomerName());
        assertEquals(TEST_DATE, dto.getDate());
        assertEquals(TEST_PAYMENT_METHOD_NAME, dto.getPaymentMethodName());
        assertEquals(TEST_AMOUNT, dto.getAmount());
    }
}
