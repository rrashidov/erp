package org.roko.erp.services;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.PurchaseOrder;
import org.roko.erp.model.PurchaseOrderLine;
import org.roko.erp.model.jpa.PurchaseOrderLineId;
import org.roko.erp.repositories.PurchaseOrderLineRepository;

public class PurchaseOrderLineServiceTest {

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
    public void count_delegatesToRepo(){
        svc.count(purchaseOrderMock);

        verify(repoMock).countForPurchaseOrder(purchaseOrderMock);
    }
}
