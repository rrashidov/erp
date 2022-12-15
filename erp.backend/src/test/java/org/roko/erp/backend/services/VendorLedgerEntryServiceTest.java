package org.roko.erp.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.Vendor;
import org.roko.erp.backend.model.VendorLedgerEntry;
import org.roko.erp.backend.model.VendorLedgerEntryType;
import org.roko.erp.backend.repositories.VendorLedgerEntryRepository;
import org.roko.erp.dto.VendorLedgerEntryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class VendorLedgerEntryServiceTest {

    private static final long TEST_ID = 123l;
    private static final VendorLedgerEntryType TEST_TYPE = VendorLedgerEntryType.PAYMENT;
    private static final String TEST_DOCUMENT_CODE = "test-document-code";
    private static final Date TEST_DATE = new Date();
    private static final double TEST_AMOUNT = 123.45;

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Vendor vendorMock;

    @Mock
    private Page<VendorLedgerEntry> pageMock;

    @Mock
    private VendorLedgerEntry vendorLedgerEntryMock;

    @Mock
    private VendorLedgerEntryRepository repoMock;

    private VendorLedgerEntryService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(vendorLedgerEntryMock.getId()).thenReturn(TEST_ID);
        when(vendorLedgerEntryMock.getType()).thenReturn(TEST_TYPE);
        when(vendorLedgerEntryMock.getDocumentCode()).thenReturn(TEST_DOCUMENT_CODE);
        when(vendorLedgerEntryMock.getDate()).thenReturn(TEST_DATE);
        when(vendorLedgerEntryMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(repoMock.findFor(eq(vendorMock), any(Pageable.class))).thenReturn(pageMock);

        svc = new VendorLedgerEntryServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo() {
        svc.create(vendorLedgerEntryMock);

        verify(repoMock).save(vendorLedgerEntryMock);
    }

    @Test
    public void list_delegatesToRepo() {
        svc.findFor(vendorMock);

        verify(repoMock).findFor(vendorMock);
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        svc.findFor(vendorMock, TEST_PAGE);

        verify(repoMock).findFor(eq(vendorMock), pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(Constants.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo() {
        svc.count(vendorMock);

        verify(repoMock).count(vendorMock);
    }

    @Test
    public void toDTO_returnsProperValue() {
        VendorLedgerEntryDTO dto = svc.toDTO(vendorLedgerEntryMock);

        assertEquals(TEST_ID, dto.getId());
        assertEquals(TEST_TYPE.name(), dto.getType());
        assertEquals(TEST_DOCUMENT_CODE, dto.getDocumentCode());
        assertEquals(TEST_DATE, dto.getDate());
        assertEquals(TEST_AMOUNT, dto.getAmount());
    }
}
