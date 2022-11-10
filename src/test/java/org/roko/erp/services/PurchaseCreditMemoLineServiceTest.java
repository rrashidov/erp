package org.roko.erp.services;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.PurchaseCreditMemo;
import org.roko.erp.model.PurchaseCreditMemoLine;
import org.roko.erp.model.jpa.PurchaseCreditMemoLineId;
import org.roko.erp.repositories.PurchaseCreditMemoLineRepository;

public class PurchaseCreditMemoLineServiceTest {

    @Mock
    private PurchaseCreditMemo purchaseCreditMemoMock;

    @Mock
    private PurchaseCreditMemoLineId nonExistingPurchaseCreditMemoLineIdMock;

    @Mock
    private PurchaseCreditMemoLineId purchaseCreditMemoLineIdMock;

    @Mock
    private PurchaseCreditMemoLine purchaseCreditMemoLineMock;

    @Mock
    private PurchaseCreditMemoLineRepository repoMock;

    private PurchaseCreditMemoLineService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(repoMock.findById(purchaseCreditMemoLineIdMock)).thenReturn(Optional.of(purchaseCreditMemoLineMock));

        svc = new PurchaseCreditMemoLineServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo() {
        svc.create(purchaseCreditMemoLineMock);

        verify(repoMock).save(purchaseCreditMemoLineMock);
    }

    @Test
    public void update_delegatesToRepo() {
        svc.update(purchaseCreditMemoLineIdMock, purchaseCreditMemoLineMock);

        verify(repoMock).findById(purchaseCreditMemoLineIdMock);
        verify(repoMock).save(purchaseCreditMemoLineMock);
    }

    @Test
    public void delete_delegatesToRepo() {
        svc.delete(purchaseCreditMemoLineIdMock);

        verify(repoMock).delete(purchaseCreditMemoLineMock);
    }

    @Test
    public void get_delegatesToRepo() {
        svc.get(purchaseCreditMemoLineIdMock);

        verify(repoMock).findById(purchaseCreditMemoLineIdMock);
    }

    @Test
    public void getReturnsNull_whenCalledWithNonExistingId() {
        PurchaseCreditMemoLine purchaseCreditMemoLine = svc.get(nonExistingPurchaseCreditMemoLineIdMock);

        assertNull(purchaseCreditMemoLine);
    }

    @Test
    public void list_delegatesToRepo() {
        svc.list(purchaseCreditMemoMock);

        verify(repoMock).findFor(purchaseCreditMemoMock);
    }

    @Test
    public void count_delegatesToRepo() {
        svc.count(purchaseCreditMemoMock);

        verify(repoMock).count(purchaseCreditMemoMock);
    }
}
