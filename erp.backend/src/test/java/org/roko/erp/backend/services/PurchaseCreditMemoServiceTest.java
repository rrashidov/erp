package org.roko.erp.backend.services;

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
import org.roko.erp.backend.model.PurchaseCreditMemo;
import org.roko.erp.backend.repositories.PurchaseCreditMemoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PurchaseCreditMemoServiceTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<PurchaseCreditMemo> pageMock;

    @Mock
    private PurchaseCreditMemo purchaseCreditMemoMock;

    @Mock
    private PurchaseCreditMemo purchaseCreditMemoMock1;

    @Mock
    private PurchaseCreditMemo purchaseCreditMemoMock2;

    @Mock
    private PurchaseCreditMemoRepository repoMock;

    private PurchaseCreditMemoService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(purchaseCreditMemoMock));
        when(repoMock.findAll(any(Pageable.class))).thenReturn(pageMock);
        when(repoMock.findAll()).thenReturn(Arrays.asList(purchaseCreditMemoMock, purchaseCreditMemoMock1, purchaseCreditMemoMock2));

        svc = new PurchaseCreditMemoServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo() {
        svc.create(purchaseCreditMemoMock);

        verify(repoMock).save(purchaseCreditMemoMock);
    }

    @Test
    public void update_delegatesToRepo() {
        svc.update(TEST_CODE, purchaseCreditMemoMock);

        verify(repoMock).findById(TEST_CODE);
        verify(repoMock).save(purchaseCreditMemoMock);
    }

    @Test
    public void delete_delegatesToRepo() {
        svc.delete(TEST_CODE);

        verify(repoMock).delete(purchaseCreditMemoMock);
    }

    @Test
    public void get_delegatesToRepo() {
        svc.get(TEST_CODE);

        verify(repoMock).findById(TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenCalledWithNonExistingCode() {
        PurchaseCreditMemo purchaseCreditMemo = svc.get("non-existing-code");

        assertNull(purchaseCreditMemo);
    }

    @Test
    public void list_delegatesToRepo() {
        svc.list();

        verify(repoMock).findAll();

        verify(repoMock).amount(purchaseCreditMemoMock);
        verify(repoMock).amount(purchaseCreditMemoMock1);
        verify(repoMock).amount(purchaseCreditMemoMock2);
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        svc.list(TEST_PAGE);

        verify(repoMock).findAll(pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(Constants.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo() {
        svc.count();

        verify(repoMock).count();
    }
}
