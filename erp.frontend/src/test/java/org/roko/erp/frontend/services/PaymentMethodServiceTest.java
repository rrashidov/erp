package org.roko.erp.frontend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.frontend.controllers.paging.PagingServiceImpl;
import org.roko.erp.frontend.model.PaymentMethod;
import org.roko.erp.frontend.repositories.PaymentMethodRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PaymentMethodServiceTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<PaymentMethod> pageMock;

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

        when(pageMock.toList()).thenReturn(new ArrayList<>());

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(persistedPaymentMethodMock));
        when(repoMock.findAll(any(Pageable.class))).thenReturn(pageMock);

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
    public void listWithPage_delegatesToRepo(){
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
