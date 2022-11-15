package org.roko.erp.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.Vendor;
import org.roko.erp.model.VendorLedgerEntry;
import org.roko.erp.repositories.VendorLedgerEntryRepository;

public class VendorLedgerEntryServiceTest {

    @Mock
    private Vendor vendorMock;

    @Mock
    private VendorLedgerEntry vendorLedgerEntryMock;

    @Mock
    private VendorLedgerEntryRepository repoMock;

    private VendorLedgerEntryService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

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
    public void count_delegatesToRepo() {
        svc.count(vendorMock);

        verify(repoMock).count(vendorMock);
    }
}
