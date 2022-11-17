package org.roko.erp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.controllers.paging.PagingServiceImpl;
import org.roko.erp.model.PostedSalesOrder;
import org.roko.erp.repositories.PostedSalesOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PostedSalesOrderServiceTest {
    
    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<PostedSalesOrder> pageMock;

    @Mock
    private PostedSalesOrder postedSalesOrderMock;

    @Mock
    private PostedSalesOrderRepository repoMock;

    private PostedSalesOrderService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(postedSalesOrderMock));
        when(repoMock.findAll(any(Pageable.class))).thenReturn(pageMock);

        svc = new PostedSalesOrderServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(postedSalesOrderMock);

        verify(repoMock).save(postedSalesOrderMock);
    }

    @Test
    public void get_delegatesToRepo(){
        svc.get(TEST_CODE);

        verify(repoMock).findById(TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenCalledWithNonExistingId(){
        PostedSalesOrder postedSalesOrder = svc.get("non-existing-code");

        assertNull(postedSalesOrder);
    }

    @Test
    public void list_delegatesToRepo(){
        svc.list();

        verify(repoMock).findAll();
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        svc.list(TEST_PAGE);

        verify(repoMock).findAll(pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(PagingServiceImpl.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo(){
        svc.count();

        verify(repoMock).count();
    }
}
