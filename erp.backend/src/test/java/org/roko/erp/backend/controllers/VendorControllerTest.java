package org.roko.erp.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.roko.erp.dto.VendorLedgerEntryDTO;
import org.roko.erp.dto.list.VendorLedgerEntryList;
import org.roko.erp.dto.list.VendorList;

public class VendorControllerTest {

    private static final int TEST_COUNT = 222;

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

    @Mock
    private VendorLedgerEntryDTO vendorLedgerEntryDtoMock;

    private VendorController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(vendorLedgerEntrySvcMock.findFor(vendorMock, TEST_PAGE)).thenReturn(Arrays.asList(vendorLedgerEntryMock));
        when(vendorLedgerEntrySvcMock.toDTO(vendorLedgerEntryMock)).thenReturn(vendorLedgerEntryDtoMock);
        when(vendorLedgerEntrySvcMock.count(vendorMock)).thenReturn(TEST_COUNT);

        when(svcMock.list()).thenReturn(Arrays.asList(vendorMock));
        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(vendorMock));
        when(svcMock.get(TESST_CODE)).thenReturn(vendorMock);
        when(svcMock.fromDTO(dtoMock)).thenReturn(vendorMock);
        when(svcMock.toDTO(vendorMock)).thenReturn(dtoMock);
        when(svcMock.count()).thenReturn(TEST_COUNT);
        
        controller = new VendorController(svcMock, vendorLedgerEntrySvcMock);
    }

    @Test
    public void list_returnsProperValue() {
        VendorList list = controller.list();

        assertEquals(dtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void listWithPage_returnsProperValue() {
        VendorList list = controller.list(TEST_PAGE);

        assertEquals(dtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
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
    public void listLedgerEntries_returnsProperValue() {
        VendorLedgerEntryList list = controller.listLedgerEntries(TESST_CODE, TEST_PAGE);

        assertEquals(vendorLedgerEntryDtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }
}
