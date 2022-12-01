package org.roko.erp.services;

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
import org.roko.erp.controllers.paging.PagingServiceImpl;
import org.roko.erp.model.PostedSalesOrder;
import org.roko.erp.model.PostedSalesOrderLine;
import org.roko.erp.repositories.PostedSalesOrderLineRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PostedSalesOrderLineServiceTest {

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<PostedSalesOrderLine> pageMock;

    @Mock
    private PostedSalesOrder postedSalesOrderMock;

    @Mock
    private PostedSalesOrderLine postedSalesOrderLineMock;

    @Mock
    private PostedSalesOrderLineRepository repoMock;

    private PostedSalesOrderLineService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(repoMock.findFor(eq(postedSalesOrderMock), any(Pageable.class))).thenReturn(pageMock);

        svc = new PostedSalesOrderLineServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo() {
        svc.create(postedSalesOrderLineMock);

        verify(repoMock).save(postedSalesOrderLineMock);
    }

    @Test
    public void list_delegatesToRepo() {
        svc.list(postedSalesOrderMock);

        verify(repoMock).findFor(postedSalesOrderMock);
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        svc.list(postedSalesOrderMock, TEST_PAGE);

        verify(repoMock).findFor(eq(postedSalesOrderMock), pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(PagingServiceImpl.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo() {
        svc.count(postedSalesOrderMock);

        verify(repoMock).count(postedSalesOrderMock);
    }
}
