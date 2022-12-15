package org.roko.erp.frontend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.frontend.controllers.paging.PagingServiceImpl;
import org.roko.erp.frontend.model.GeneralJournalBatch;
import org.roko.erp.frontend.model.GeneralJournalBatchLine;
import org.roko.erp.frontend.model.jpa.GeneralJournalBatchLineId;
import org.roko.erp.frontend.repositories.GeneralJournalBatchLineRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class GeneralJournalBatchLineServiceTest {
    
    private static final int TEST_PAGE = 123;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private GeneralJournalBatch generalJournalBatchMock;

    @Mock
    private GeneralJournalBatchLineId idMock;

    @Mock
    private GeneralJournalBatchLineId nonExistingId;

    @Mock
    private GeneralJournalBatchLine generalJournalBatchLineMock;

    @Mock
    private GeneralJournalBatchLineRepository repoMock;

    @Mock
    private Page<GeneralJournalBatchLine> pageMock;

    private GeneralJournalBatchLineService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(repoMock.findById(idMock)).thenReturn(Optional.of(generalJournalBatchLineMock));
        when(repoMock.findFor(eq(generalJournalBatchMock), any(Pageable.class))).thenReturn(pageMock);

        svc = new GeneralJournalBatchLineServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo() {
        svc.create(generalJournalBatchLineMock);

        verify(repoMock).save(generalJournalBatchLineMock);
    }

    @Test
    public void update_delegatesToRepo() {
        svc.update(idMock, generalJournalBatchLineMock);

        verify(repoMock).findById(idMock);
        verify(repoMock).save(generalJournalBatchLineMock);
    }

    @Test
    public void get_delegatesToRepo() {
        svc.get(idMock);

        verify(repoMock).findById(idMock);
    }

    @Test
    public void getReturnsNull_whenCalledWithNonExistingId() {
        GeneralJournalBatchLine generalJournalBatchLine = svc.get(nonExistingId);

        assertNull(generalJournalBatchLine);
    }

    @Test
    public void list_delegatesToRepo() {
        svc.list(generalJournalBatchMock);

        verify(repoMock).findFor(generalJournalBatchMock);
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        svc.list(generalJournalBatchMock, TEST_PAGE);

        verify(repoMock).findFor(eq(generalJournalBatchMock), pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(PagingServiceImpl.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void delete_delegatesToRepo() {
        svc.delete(idMock);

        verify(repoMock).delete(generalJournalBatchLineMock);
    }

    @Test
    public void count_delegatesToRepo() {
        svc.count(generalJournalBatchMock);

        verify(repoMock).count(generalJournalBatchMock);
    }
}
