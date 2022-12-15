package org.roko.erp.frontend.services;

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
import org.roko.erp.frontend.controllers.paging.PagingServiceImpl;
import org.roko.erp.frontend.model.PostedSalesCreditMemo;
import org.roko.erp.frontend.model.PostedSalesCreditMemoLine;
import org.roko.erp.frontend.repositories.PostedSalesCreditMemoLineRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PostedSalesCreditMemoLineServiceTest {
    
    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<PostedSalesCreditMemoLine> pageMock;

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

        when(repoMock.findFor(eq(postedSalesCreditMemoMock), any(Pageable.class))).thenReturn(pageMock);

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
    public void listWithPage_delegatesToRepo() {
        svc.list(postedSalesCreditMemoMock, TEST_PAGE);

        verify(repoMock).findFor(eq(postedSalesCreditMemoMock), pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(PagingServiceImpl.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo() {
        svc.count(postedSalesCreditMemoMock);

        verify(repoMock).count(postedSalesCreditMemoMock);
    }
}
