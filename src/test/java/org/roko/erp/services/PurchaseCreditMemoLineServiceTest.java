package org.roko.erp.services;

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
import org.roko.erp.controllers.paging.PagingServiceImpl;
import org.roko.erp.model.PurchaseCreditMemo;
import org.roko.erp.model.PurchaseCreditMemoLine;
import org.roko.erp.model.jpa.PurchaseCreditMemoLineId;
import org.roko.erp.repositories.PurchaseCreditMemoLineRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PurchaseCreditMemoLineServiceTest {

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

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

    @Mock
    private Page<PurchaseCreditMemoLine> pageMock;

    private PurchaseCreditMemoLineService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(repoMock.findById(purchaseCreditMemoLineIdMock)).thenReturn(Optional.of(purchaseCreditMemoLineMock));
        when(repoMock.findFor(eq(purchaseCreditMemoMock), any(Pageable.class))).thenReturn(pageMock);

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
    public void listWithPage_delegatesToRepo() {
        svc.list(purchaseCreditMemoMock, TEST_PAGE);

        verify(repoMock).findFor(eq(purchaseCreditMemoMock), pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(PagingServiceImpl.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo() {
        svc.count(purchaseCreditMemoMock);

        verify(repoMock).count(purchaseCreditMemoMock);
    }
}
