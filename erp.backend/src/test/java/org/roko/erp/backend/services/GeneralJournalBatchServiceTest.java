package org.roko.erp.backend.services;

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
import org.roko.erp.backend.model.DocumentPostStatus;
import org.roko.erp.backend.model.GeneralJournalBatch;
import org.roko.erp.backend.repositories.GeneralJournalBatchRepository;
import org.roko.erp.dto.GeneralJournalBatchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class GeneralJournalBatchServiceTest {

    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";

    private static final DocumentPostStatus TEST_POST_STATUS = DocumentPostStatus.SCHEDULED;
    private static final String TEST_POST_STATUS_REASON_MSG = "non-existing-code";

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<GeneralJournalBatch> pageMock;

    @Mock
    private GeneralJournalBatch generalJournalBatchMock;

    @Mock
    private GeneralJournalBatchRepository generalJournalBatchRepoMock;

    @Mock
    private GeneralJournalBatchDTO dtoMock;

    private GeneralJournalBatchService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(dtoMock.getCode()).thenReturn(TEST_CODE);
        when(dtoMock.getName()).thenReturn(TEST_NAME);

        when(generalJournalBatchMock.getCode()).thenReturn(TEST_CODE);
        when(generalJournalBatchMock.getName()).thenReturn(TEST_NAME);
        when(generalJournalBatchMock.getPostStatus()).thenReturn(TEST_POST_STATUS);
        when(generalJournalBatchMock.getPostStatusReason()).thenReturn(TEST_POST_STATUS_REASON_MSG);

        when(generalJournalBatchRepoMock.findById(TEST_CODE)).thenReturn(Optional.of(generalJournalBatchMock));
        when(generalJournalBatchRepoMock.findAll(any(Pageable.class))).thenReturn(pageMock);

        svc = new GeneralJournalBatchServiceImpl(generalJournalBatchRepoMock);
    }

    @Test
    public void create_delegatesToRepo() {
        svc.create(generalJournalBatchMock);

        verify(generalJournalBatchRepoMock).save(generalJournalBatchMock);
    }

    @Test
    public void update_delegatesToRepo() {
        svc.update(TEST_CODE, generalJournalBatchMock);

        verify(generalJournalBatchRepoMock).findById(TEST_CODE);
        verify(generalJournalBatchRepoMock).save(generalJournalBatchMock);
    }

    @Test
    public void delete_delegatesToRepo(){
        svc.delete(TEST_CODE);

        verify(generalJournalBatchRepoMock).delete(generalJournalBatchMock);
    }

    @Test
    public void get_delegatesToRepo(){
        svc.get(TEST_CODE);

        verify(generalJournalBatchRepoMock).findById(TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenCalledWithNonExistingCode(){
        GeneralJournalBatch generalJournalBatch = svc.get(TEST_POST_STATUS_REASON_MSG);

        assertNull(generalJournalBatch);
    }

    @Test
    public void list_delegatesToRepo(){
        svc.list();

        verify(generalJournalBatchRepoMock).findAll();
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        svc.list(TEST_PAGE);

        verify(generalJournalBatchRepoMock).findAll(pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(Constants.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo(){
        svc.count();

        verify(generalJournalBatchRepoMock).count();
    }

    @Test
    public void toDTO_returnsProperResult() {
        GeneralJournalBatchDTO dto = svc.toDTO(generalJournalBatchMock);

        assertEquals(TEST_CODE, dto.getCode());
        assertEquals(TEST_NAME, dto.getName());
        assertEquals(TEST_POST_STATUS.name(), dto.getPostStatus());
        assertEquals(TEST_POST_STATUS_REASON_MSG, dto.getPostStatusReason());
    }

    @Test
    public void fromDTO_returnsProperValue() {
        GeneralJournalBatch generalJournalBatch = svc.fromDTO(dtoMock);

        assertEquals(TEST_CODE, generalJournalBatch.getCode());
        assertEquals(TEST_NAME, generalJournalBatch.getName());
    }
}
