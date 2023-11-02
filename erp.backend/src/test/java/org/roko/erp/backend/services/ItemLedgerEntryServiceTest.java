package org.roko.erp.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.Item;
import org.roko.erp.backend.model.ItemLedgerEntry;
import org.roko.erp.backend.model.ItemLedgerEntryType;
import org.roko.erp.backend.repositories.ItemLedgerEntryRepository;
import org.roko.erp.dto.ItemLedgerEntryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ItemLedgerEntryServiceTest {

    private static final long TEST_ID = 123l;
    private static final ItemLedgerEntryType TEST_TYPE = ItemLedgerEntryType.PURCHASE_ORDER;
    private static final BigDecimal TEST_QUANTITY = new BigDecimal(34.56);
    private static final String TEST_DOCUMENT_CODE = "test-document-code";
    private static final Date TEST_DATE = new Date();

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

        when(itemLedgerEntryMock.getId()).thenReturn(TEST_ID);
        when(itemLedgerEntryMock.getType()).thenReturn(TEST_TYPE);
        when(itemLedgerEntryMock.getQuantity()).thenReturn(TEST_QUANTITY);
        when(itemLedgerEntryMock.getDate()).thenReturn(TEST_DATE);
        when(itemLedgerEntryMock.getDocumentCode()).thenReturn(TEST_DOCUMENT_CODE);
        
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

    @Test
    public void toDTO_returnsProperResult() {
        ItemLedgerEntryDTO dto = svc.toDTO(itemLedgerEntryMock);

        assertEquals(TEST_ID, dto.getId());
        assertEquals(TEST_TYPE.name(), dto.getType());
        assertEquals(TEST_QUANTITY, dto.getQuantity());
        assertEquals(TEST_DATE, dto.getDate());
        assertEquals(TEST_DOCUMENT_CODE, dto.getDocumentCode());
    }
}
