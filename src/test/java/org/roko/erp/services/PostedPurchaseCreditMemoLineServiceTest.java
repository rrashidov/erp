package org.roko.erp.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.PostedPurchaseCreditMemo;
import org.roko.erp.model.PostedPurchaseCreditMemoLine;
import org.roko.erp.repositories.PostedPurchaseCreditMemoLineRepository;

public class PostedPurchaseCreditMemoLineServiceTest {
    
    @Mock
    private PostedPurchaseCreditMemo postedPurchaseCreditMemoMock;

    @Mock
    private PostedPurchaseCreditMemoLine postedPurchaseCreditMemoLineMock;

    @Mock
    private PostedPurchaseCreditMemoLineRepository repoMock;

    private PostedPurchaseCreditMemoLineService svc;

    @BeforeEach
    public void setup () {
        MockitoAnnotations.openMocks(this);

        svc = new PostedPurchaseCreditMemoLineServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo() {
        svc.create(postedPurchaseCreditMemoLineMock);

        verify(repoMock).save(postedPurchaseCreditMemoLineMock);
    }

    @Test
    public void list_delegatesToRepo() {
        svc.list(postedPurchaseCreditMemoMock);

        verify(repoMock).findFor(postedPurchaseCreditMemoMock);
    }

    @Test
    public void count_delegatesToRepo() {
        svc.count(postedPurchaseCreditMemoMock);

        verify(repoMock).count(postedPurchaseCreditMemoMock);
    }
}
