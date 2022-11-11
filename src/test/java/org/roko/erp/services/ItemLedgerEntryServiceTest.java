package org.roko.erp.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.Item;
import org.roko.erp.model.ItemLedgerEntry;
import org.roko.erp.repositories.ItemLedgerEntryRepository;

public class ItemLedgerEntryServiceTest {

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
    public void count_delegatesToRepo() {
        svc.count(itemMock);

        verify(repoMock).count(itemMock);
    }
}
