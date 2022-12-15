package org.roko.erp.frontend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.frontend.controllers.paging.PagingServiceImpl;
import org.roko.erp.frontend.model.PurchaseOrder;
import org.roko.erp.frontend.model.PurchaseOrderLine;
import org.roko.erp.frontend.model.jpa.PurchaseOrderLineId;
import org.roko.erp.frontend.repositories.PurchaseOrderLineRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PurchaseOrderLineServiceTest {

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<PurchaseOrderLine> pageMock;

    @Mock
    private PurchaseOrder purchaseOrderMock;

    @Mock
    private PurchaseOrderLineId nonExistingPurchaseOrderLineId;

    @Mock
    private PurchaseOrderLineId purchaseOrderLineIdMock;

    @Mock
    private PurchaseOrderLine purchaseOrderLineMock;

    @Mock
    private PurchaseOrderLineRepository repoMock;

    private PurchaseOrderLineService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(repoMock.findById(purchaseOrderLineIdMock)).thenReturn(Optional.of(purchaseOrderLineMock));
        when(repoMock.listForPurchaseOrder(eq(purchaseOrderMock), any(Pageable.class))).thenReturn(pageMock);

        svc = new PurchaseOrderLineServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo() {
        svc.create(purchaseOrderLineMock);

        verify(repoMock).save(purchaseOrderLineMock);
    }

    @Test
    public void update_delegatesToRepo() {
        svc.update(purchaseOrderLineIdMock, purchaseOrderLineMock);

        verify(repoMock).findById(purchaseOrderLineIdMock);
        verify(repoMock).save(purchaseOrderLineMock);
    }

    @Test
    public void delete_delegatesToRepo() {
        svc.delete(purchaseOrderLineIdMock);

        verify(repoMock).delete(purchaseOrderLineMock);
    }

    @Test
    public void get_delegatesToRepo() {
        svc.get(purchaseOrderLineIdMock);

        verify(repoMock).findById(purchaseOrderLineIdMock);
    }

    @Test
    public void getReturnsNull_whenCalledForNonExistingId() {
        PurchaseOrderLine purchaseOrderLine = svc.get(nonExistingPurchaseOrderLineId);

        assertNull(purchaseOrderLine);
    }

    @Test
    public void list_delegatesToRepo(){
        svc.list(purchaseOrderMock);

        verify(repoMock).listForPurchaseOrder(purchaseOrderMock);
    }

    @Test
    public void listWithPage_delegatesToRepo(){
        svc.list(purchaseOrderMock, TEST_PAGE);

        verify(repoMock).listForPurchaseOrder(eq(purchaseOrderMock), pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(PagingServiceImpl.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo(){
        svc.count(purchaseOrderMock);

        verify(repoMock).countForPurchaseOrder(purchaseOrderMock);
    }

    @Test
    public void maxLineNo_delegatesToRepo() {
        svc.maxLineNo(purchaseOrderMock);

        verify(repoMock).maxLineNo(purchaseOrderMock);
    }
}
