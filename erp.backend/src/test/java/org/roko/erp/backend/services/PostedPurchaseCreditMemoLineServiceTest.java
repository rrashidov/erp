package org.roko.erp.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.PostedPurchaseCreditMemo;
import org.roko.erp.backend.model.PostedPurchaseCreditMemoLine;
import org.roko.erp.backend.repositories.PostedPurchaseCreditMemoLineRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PostedPurchaseCreditMemoLineServiceTest {
    
    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private PostedPurchaseCreditMemo postedPurchaseCreditMemoMock;

    @Mock
    private PostedPurchaseCreditMemoLine postedPurchaseCreditMemoLineMock;

    @Mock
    private PostedPurchaseCreditMemoLineRepository repoMock;

    @Mock
    private Page<PostedPurchaseCreditMemoLine> pageMock;

    private PostedPurchaseCreditMemoLineService svc;

    @BeforeEach
    public void setup () {
        MockitoAnnotations.openMocks(this);

        when(repoMock.findFor(eq(postedPurchaseCreditMemoMock), any(Pageable.class))).thenReturn(pageMock);

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
    public void listWithPage_delegatesToRepo() {
        svc.list(postedPurchaseCreditMemoMock, TEST_PAGE);

        verify(repoMock).findFor(eq(postedPurchaseCreditMemoMock), pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(Constants.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo() {
        svc.count(postedPurchaseCreditMemoMock);

        verify(repoMock).count(postedPurchaseCreditMemoMock);
    }
}
