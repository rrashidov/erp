package org.roko.erp.backend.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.Vendor;
import org.roko.erp.backend.model.VendorLedgerEntry;
import org.roko.erp.backend.services.VendorLedgerEntryService;
import org.roko.erp.backend.services.VendorService;
import org.roko.erp.dto.VendorDTO;

public class VendorControllerTest {

    private static final String TESST_CODE = "tesst-code";

    private static final int TEST_PAGE = 123;

    @Mock
    private VendorService svcMock;
    
    @Mock
    private Vendor vendorMock;

    @Mock
    private VendorDTO dtoMock;

    @Mock
    private VendorLedgerEntryService vendorLedgerEntrySvcMock;

    @Mock
    private VendorLedgerEntry vendorLedgerEntryMock;

    private VendorController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(vendorLedgerEntrySvcMock.findFor(vendorMock, TEST_PAGE)).thenReturn(Arrays.asList(vendorLedgerEntryMock));

        when(svcMock.list()).thenReturn(Arrays.asList(vendorMock));
        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(vendorMock));
        when(svcMock.get(TESST_CODE)).thenReturn(vendorMock);
        when(svcMock.fromDTO(dtoMock)).thenReturn(vendorMock);
        
        controller = new VendorController(svcMock, vendorLedgerEntrySvcMock);
    }

    @Test
    public void list_delegatesToService() {
        controller.list();

        verify(svcMock).list();
        verify(svcMock).toDTO(vendorMock);
    }

    @Test
    public void listWithPage_delegatesToService() {
        controller.list(TEST_PAGE);

        verify(svcMock).list(TEST_PAGE);
        verify(svcMock).toDTO(vendorMock);
    }

    @Test
    public void get_delegatesToService() {
        controller.get(TESST_CODE);

        verify(svcMock).get(TESST_CODE);
        verify(svcMock).toDTO(vendorMock);
    }

    @Test
    public void post_delegatesToService() {
        controller.post(dtoMock);

        verify(svcMock).create(vendorMock);
    }

    @Test
    public void put_delegatesToService() {
        controller.put(TESST_CODE, dtoMock);

        verify(svcMock).update(TESST_CODE, vendorMock);
    }

    @Test
    public void delete_delegatesToService() {
        controller.delete(TESST_CODE);

        verify(svcMock).delete(TESST_CODE);
    }

    @Test
    public void listLedgerEntries_delegatesToService() {
        controller.listLedgerEntries(TESST_CODE, TEST_PAGE);

        verify(vendorLedgerEntrySvcMock).findFor(vendorMock, TEST_PAGE);
        verify(vendorLedgerEntrySvcMock).toDTO(vendorLedgerEntryMock);
    }
}
