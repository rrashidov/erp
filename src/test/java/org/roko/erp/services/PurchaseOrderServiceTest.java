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
import org.roko.erp.repositories.PurchaseOrderRepository;

public class PurchaseOrderServiceTest {

    private static final String NON_EXISTING_CODE = "non-existing-code";

    private static final String TEST_CODE = "test-code";

    @Mock
    private PurchaseOrder purchaseOrderMock;

    @Mock
    private PurchaseOrderRepository repoMock;

    private PurchaseOrderService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(purchaseOrderMock));

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
    }

    @Test
    public void count_delegatesToRepo(){
        svc.count();

        verify(repoMock).count();
    }
}
