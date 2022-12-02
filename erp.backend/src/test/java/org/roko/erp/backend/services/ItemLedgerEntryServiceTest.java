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
import org.roko.erp.backend.model.Item;
import org.roko.erp.backend.model.ItemLedgerEntry;
import org.roko.erp.backend.repositories.ItemLedgerEntryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ItemLedgerEntryServiceTest {

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<ItemLedgerEntry> pageMock;

    @Mock
    private Item itemMock;

    @Mock
    private ItemLedgerEntry itemLedgerEntryMock;

    @Mock
    private ItemLedgerEntryRepository repoMock;

    private ItemLedgerEntryService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(repoMock.findFor(eq(itemMock), any(Pageable.class))).thenReturn(pageMock);
        
        svc = new ItemLedgerEntryServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo() {
        svc.create(itemLedgerEntryMock);

        verify(repoMock).save(itemLedgerEntryMock);
    }

    @Test
    public void list_delegatesToRepo() {
        svc.list(itemMock);

        verify(repoMock).findFor(itemMock);
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        svc.list(itemMock, TEST_PAGE);

        verify(repoMock).findFor(eq(itemMock), pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(Constants.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo() {
        svc.count(itemMock);

        verify(repoMock).count(itemMock);
    }
}
