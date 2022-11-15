package org.roko.erp.services;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.PostedPurchaseCreditMemo;
import org.roko.erp.repositories.PostedPurchaseCreditMemoRepository;

public class PostedPurchaseCreditMemoServiceTest {
    
    private static final String TEST_CODE = "test-code";

    @Mock
    private PostedPurchaseCreditMemo postedPurchaseCreditMemoMock;

    @Mock
    private PostedPurchaseCreditMemoRepository repoMock;

    private PostedPurchaseCreditMemoService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new PostedPurchaseCreditMemoServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo() {
        svc.create(postedPurchaseCreditMemoMock);

        verify(repoMock).save(postedPurchaseCreditMemoMock);
    }

    @Test
    public void get_delegatesToRepo() {
        svc.get(TEST_CODE);

        verify(repoMock).findById(TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenCalledWithNonExistingCode() {
        PostedPurchaseCreditMemo postedPurchaseCreditMemo = svc.get("non-existing-code");

        assertNull(postedPurchaseCreditMemo);
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
