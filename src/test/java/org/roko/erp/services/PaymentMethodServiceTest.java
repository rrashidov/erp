package org.roko.erp.services;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.PaymentMethod;
import org.roko.erp.repositories.PaymentMethodRepository;

public class PaymentMethodServiceTest {

    private static final String TEST_CODE = "test-code";

    @Mock
    private PaymentMethod persistedPaymentMethodMock;

    @Mock
    private PaymentMethod paymentMethodMock;
    
    @Mock
    private PaymentMethodRepository repoMock;

    private PaymentMethodService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(persistedPaymentMethodMock));

        svc = new PaymentMethodServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(paymentMethodMock);

        verify(repoMock).save(paymentMethodMock);
    }

    @Test
    public void update_delegatesToRepo(){
        svc.update(TEST_CODE, paymentMethodMock);

        verify(repoMock).save(persistedPaymentMethodMock);
    }

    @Test
    public void delete_delegatesToRepo(){
        svc.delete(TEST_CODE);

        verify(repoMock).findById(TEST_CODE);
        verify(repoMock).delete(persistedPaymentMethodMock);
    }

    @Test
    public void get_delegatesToRepo(){
        svc.get(TEST_CODE);

        verify(repoMock).findById(TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenNotFound(){
        PaymentMethod paymentMethod = svc.get("non-existing-code");

        assertNull(paymentMethod);
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
