package org.roko.erp.services;

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
import org.roko.erp.controllers.paging.PagingServiceImpl;
import org.roko.erp.model.Vendor;
import org.roko.erp.model.VendorLedgerEntry;
import org.roko.erp.repositories.VendorLedgerEntryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class VendorLedgerEntryServiceTest {

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
        assertEquals(PagingServiceImpl.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo() {
        svc.count(vendorMock);

        verify(repoMock).count(vendorMock);
    }
}
