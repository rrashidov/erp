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
import org.roko.erp.backend.model.PostedPurchaseOrder;
import org.roko.erp.backend.model.PostedPurchaseOrderLine;
import org.roko.erp.backend.repositories.PostedPurchaseOrderLineRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PostedPurchaseOrderLineServiceTest {
    
    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private PostedPurchaseOrder postedPurchaseOrderMock;

    @Mock
    private PostedPurchaseOrderLine postedPurchaseOrderLineMock;

    @Mock
    private Page<PostedPurchaseOrderLine> pageMock;

    @Mock
    private PostedPurchaseOrderLineRepository repoMock;

    private PostedPurchaseOrderLineService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(repoMock.findFor(eq(postedPurchaseOrderMock), any(Pageable.class))).thenReturn(pageMock);

        svc = new PostedPurchaseOrderLineServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(postedPurchaseOrderLineMock);

        verify(repoMock).save(postedPurchaseOrderLineMock);
    }

    @Test
    public void list_delegatesToRepo(){
        svc.list(postedPurchaseOrderMock);

        verify(repoMock).findFor(postedPurchaseOrderMock);
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        svc.list(postedPurchaseOrderMock, TEST_PAGE);

        verify(repoMock).findFor(eq(postedPurchaseOrderMock), pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(Constants.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo(){
        svc.count(postedPurchaseOrderMock);

        verify(repoMock).count(postedPurchaseOrderMock);
    }
}
