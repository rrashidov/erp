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
import org.roko.erp.repositories.PurchaseCreditMemoRepository;

public class PurchaseCreditMemoServiceTest {

    private static final String TEST_CODE = "test-code";

    @Mock
    private PurchaseCreditMemo purchaseCreditMemoMock;

    @Mock
    private PurchaseCreditMemoRepository repoMock;

    private PurchaseCreditMemoService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(purchaseCreditMemoMock));

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
    }

    @Test
    public void count_delegatesToRepo() {
        svc.count();

        verify(repoMock).count();
    }
}
