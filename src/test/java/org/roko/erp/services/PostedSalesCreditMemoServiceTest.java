package org.roko.erp.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.PostedSalesCreditMemo;
import org.roko.erp.repositories.PostedSalesCreditMemoRepository;

public class PostedSalesCreditMemoServiceTest {
    
    @Mock
    private PostedSalesCreditMemo postedSalesCreditMemoMock;

    @Mock
    private PostedSalesCreditMemoRepository repoMock;

    private PostedSalesCreditMemoService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new PostedSalesCreditMemoServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(postedSalesCreditMemoMock);

        verify(repoMock).save(postedSalesCreditMemoMock);
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
