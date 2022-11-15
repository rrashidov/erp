package org.roko.erp.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.PostedSalesCreditMemo;
import org.roko.erp.model.PostedSalesCreditMemoLine;
import org.roko.erp.repositories.PostedSalesCreditMemoLineRepository;

public class PostedSalesCreditMemoLineServiceTest {
    
    @Mock
    private PostedSalesCreditMemo postedSalesCreditMemoMock;

    @Mock
    private PostedSalesCreditMemoLine postedSalesCreditMemoLineMock;
    
    @Mock
    private PostedSalesCreditMemoLineRepository repoMock;

    private PostedSalesCreditMemoLineService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        svc = new PostedSalesCreditMemoLineServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(postedSalesCreditMemoLineMock);

        verify(repoMock).save(postedSalesCreditMemoLineMock);
    }

    @Test
    public void list_delegatesToRepo(){
        svc.list(postedSalesCreditMemoMock);

        verify(repoMock).findFor(postedSalesCreditMemoMock);
    }

    @Test
    public void count_delegatesToRepo() {
        svc.count(postedSalesCreditMemoMock);

        verify(repoMock).count(postedSalesCreditMemoMock);
    }
}
