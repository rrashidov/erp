package org.roko.erp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.controllers.paging.PagingServiceImpl;
import org.roko.erp.model.PostedPurchaseOrder;
import org.roko.erp.repositories.PostedPurchaseOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PostedPurchaseOrderServiceTest {
    
    private static final String NON_EXISTING_CODE = "non-existing-code";

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<PostedPurchaseOrder> pageMock;

    @Mock
    private PostedPurchaseOrder postedPurchaseOrderMock;

    @Mock
    private PostedPurchaseOrder postedPurchaseOrderMock1;

    @Mock
    private PostedPurchaseOrder postedPurchaseOrderMock2;

    @Mock
    private PostedPurchaseOrderRepository repoMock;

    private PostedPurchaseOrderService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(repoMock.findAll(any(Pageable.class))).thenReturn(pageMock);
        when(repoMock.findAll()).thenReturn(Arrays.asList(postedPurchaseOrderMock, postedPurchaseOrderMock1, postedPurchaseOrderMock2));

        svc = new PostedPurchaseOrderServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(postedPurchaseOrderMock);

        verify(repoMock).save(postedPurchaseOrderMock);
    }

    @Test
    public void get_delegatesToRepo() {
        svc.get(TEST_CODE);

        verify(repoMock).findById(TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenCalledWithNonExistingCode(){
        PostedPurchaseOrder postedPurchaseOrder = svc.get(NON_EXISTING_CODE);

        assertNull(postedPurchaseOrder);
    }

    @Test
    public void list_delegatesToRepo() {
        svc.list();

        verify(repoMock).findAll();

        verify(repoMock).amount(postedPurchaseOrderMock);
        verify(repoMock).amount(postedPurchaseOrderMock1);
        verify(repoMock).amount(postedPurchaseOrderMock2);
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
