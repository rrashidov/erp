package org.roko.erp.services;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.PostedSalesCreditMemo;
import org.roko.erp.repositories.PostedSalesCreditMemoRepository;

public class PostedSalesCreditMemoServiceTest {
    
    private static final String NON_EXISTING_CODE = "non-existing-code";

    private static final String TEST_CODE = "test-code";

    @Mock
    private PostedSalesCreditMemo postedSalesCreditMemoMock;

    @Mock
    private PostedSalesCreditMemoRepository repoMock;

    private PostedSalesCreditMemoService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(postedSalesCreditMemoMock));

        svc = new PostedSalesCreditMemoServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(postedSalesCreditMemoMock);

        verify(repoMock).save(postedSalesCreditMemoMock);
    }

    @Test
    public void get_delegatesToRepo(){
        svc.get(TEST_CODE);

        verify(repoMock).findById(TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenCalledWithNonExistingCode(){
        PostedSalesCreditMemo postedSalesCreditMemo = svc.get(NON_EXISTING_CODE);

        assertNull(postedSalesCreditMemo);
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
