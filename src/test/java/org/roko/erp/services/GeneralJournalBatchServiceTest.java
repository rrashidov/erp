package org.roko.erp.services;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.GeneralJournalBatch;
import org.roko.erp.repositories.GeneralJournalBatchRepository;

public class GeneralJournalBatchServiceTest {

    private static final String TEST_CODE = "test-code";
    private static final String NON_EXISTING_CODE = "non-existing-code";

    @Mock
    private GeneralJournalBatch generalJournalBatchMock;

    @Mock
    private GeneralJournalBatchRepository generalJournalBatchRepoMock;

    private GeneralJournalBatchService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(generalJournalBatchRepoMock.findById(TEST_CODE)).thenReturn(Optional.of(generalJournalBatchMock));

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
        GeneralJournalBatch generalJournalBatch = svc.get(NON_EXISTING_CODE);

        assertNull(generalJournalBatch);
    }

    @Test
    public void list_delegatesToRepo(){
        svc.list();

        verify(generalJournalBatchRepoMock).findAll();
    }

    @Test
    public void count_delegatesToRepo(){
        svc.count();

        verify(generalJournalBatchRepoMock).count();
    }
}
