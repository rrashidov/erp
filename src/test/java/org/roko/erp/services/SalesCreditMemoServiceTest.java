package org.roko.erp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.Customer;
import org.roko.erp.model.PaymentMethod;
import org.roko.erp.model.SalesCreditMemo;
import org.roko.erp.repositories.SalesCreditMemoRepository;

public class SalesCreditMemoServiceTest {

    private static final String NON_EXISTING_CODE = "non-existing-code";

    private static final String TEST_CODE = "test-code";

    @Mock
    private PaymentMethod updatedPaymentMethodMock;

    @Mock
    private Date updatedDateMock;

    @Mock
    private Customer updatedCustomerMock;

    @Mock
    private SalesCreditMemo salesCreditMemoMock;

    @Mock
    private SalesCreditMemo salesCreditMemoToUpdateMock;

    @Mock
    private SalesCreditMemoRepository repoMock;

    private SalesCreditMemoService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(salesCreditMemoMock));

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
    }

    @Test
    public void count_delegatesToRepo(){
        svc.count();

        verify(repoMock).count();
    }
}
