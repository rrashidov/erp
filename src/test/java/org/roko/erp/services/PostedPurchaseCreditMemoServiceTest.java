package org.roko.erp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.controllers.paging.PagingServiceImpl;
import org.roko.erp.model.PostedPurchaseCreditMemo;
import org.roko.erp.repositories.PostedPurchaseCreditMemoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PostedPurchaseCreditMemoServiceTest {
    
    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<PostedPurchaseCreditMemo> pageMock;

    @Mock
    private PostedPurchaseCreditMemo postedPurchaseCreditMemoMock;

    @Mock
    private PostedPurchaseCreditMemoRepository repoMock;

    private PostedPurchaseCreditMemoService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(repoMock.findAll(any(Pageable.class))).thenReturn(pageMock);

        svc = new PostedPurchaseCreditMemoServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo() {
        svc.create(postedPurchaseCreditMemoMock);

        verify(repoMock).save(postedPurchaseCreditMemoMock);
    }

    @Test
    public void get_delegatesToRepo() {
        svc.get(TEST_CODE);

        verify(repoMock).findById(TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenCalledWithNonExistingCode() {
        PostedPurchaseCreditMemo postedPurchaseCreditMemo = svc.get("non-existing-code");

        assertNull(postedPurchaseCreditMemo);
    }

    @Test
    public void list_delegatesToRepo() {
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
    public void count_delegatesToRepo() {
        svc.count();

        verify(repoMock).count();
    }
}
