package org.roko.erp.services;

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
import org.roko.erp.controllers.paging.PagingServiceImpl;
import org.roko.erp.model.Customer;
import org.roko.erp.model.PaymentMethod;
import org.roko.erp.model.SalesCreditMemo;
import org.roko.erp.repositories.SalesCreditMemoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class SalesCreditMemoServiceTest {

    private static final String NON_EXISTING_CODE = "non-existing-code";

    private static final String TEST_CODE = "test-code";

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
    private SalesCreditMemoRepository repoMock;

    private SalesCreditMemoService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(salesCreditMemoMock));
        when(repoMock.findAll()).thenReturn(Arrays.asList(salesCreditMemoMock, salesCreditMemoMock1, salesCreditMemoMock2));
        when(repoMock.findAll(any(Pageable.class))).thenReturn(pageMock);

        when(salesCreditMemoToUpdateMock.getCustomer()).thenReturn(updatedCustomerMock);
        when(salesCreditMemoToUpdateMock.getDate()).thenReturn(updatedDateMock);
        when(salesCreditMemoToUpdateMock.getPaymentMethod()).thenReturn(updatedPaymentMethodMock);

        svc = new SalesCreditMemoServiceImpl(repoMock);
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
        assertEquals(PagingServiceImpl.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo(){
        svc.count();

        verify(repoMock).count();
    }
}
