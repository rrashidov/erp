package org.roko.erp.frontend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.frontend.controllers.paging.PagingServiceImpl;
import org.roko.erp.frontend.model.PurchaseOrder;
import org.roko.erp.frontend.repositories.PurchaseOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PurchaseOrderServiceTest {

    private static final String NON_EXISTING_CODE = "non-existing-code";

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<PurchaseOrder> pageMock;

    @Mock
    private PurchaseOrder purchaseOrderMock;

    @Mock
    private PurchaseOrder purchaseOrderMock1;

    @Mock
    private PurchaseOrder purchaseOrderMock2;

    @Mock
    private PurchaseOrderRepository repoMock;

    private PurchaseOrderService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(purchaseOrderMock));
        when(repoMock.findAll(any(Pageable.class))).thenReturn(pageMock);
        when(repoMock.findAll()).thenReturn(Arrays.asList(purchaseOrderMock, purchaseOrderMock1, purchaseOrderMock2));

        svc = new PurchaseOrderServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(purchaseOrderMock);

        verify(repoMock).save(purchaseOrderMock);
    }

    @Test
    public void update_delegatesToRepo(){
        svc.update(TEST_CODE, purchaseOrderMock);

        verify(repoMock).findById(TEST_CODE);
        verify(repoMock).save(purchaseOrderMock);
    }

    @Test
    public void delete_delegatesToRepo(){
        svc.delete(TEST_CODE);

        verify(repoMock).delete(purchaseOrderMock);
    }

    @Test
    public void get_delegatesToRepo(){
        svc.get(TEST_CODE);

        verify(repoMock).findById(TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenCalledWithNonExistingCode(){
        PurchaseOrder purchaseOrder = svc.get(NON_EXISTING_CODE);

        assertNull(purchaseOrder);
    }

    @Test
    public void list_delegatesToRepo(){
        svc.list();

        verify(repoMock).findAll();

        verify(repoMock).amount(purchaseOrderMock);
        verify(repoMock).amount(purchaseOrderMock1);
        verify(repoMock).amount(purchaseOrderMock2);
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
