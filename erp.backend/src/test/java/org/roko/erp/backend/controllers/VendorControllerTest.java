package org.roko.erp.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

public class VendorControllerTest {

    private static final String NON_EXISTING_CODE = "non-existing-code";

    private static final long TEST_COUNT = 222;

    private static final String TEST_CODE = "tesst-code";

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
        when(svcMock.get(TEST_CODE)).thenReturn(vendorMock);
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
        controller.get(TEST_CODE);

        verify(svcMock).get(TEST_CODE);
        verify(svcMock).toDTO(vendorMock);
    }

    @Test
    public void get_throwsException_whenCalledWithNonExistinCode() {
        assertThrows(ResponseStatusException.class, () -> {
            controller.get(NON_EXISTING_CODE);
        });
    }

    @Test
    public void post_delegatesToService() {
        controller.post(dtoMock);

        verify(svcMock).create(vendorMock);
    }

    @Test
    public void put_delegatesToService() {
        controller.put(TEST_CODE, dtoMock);

        verify(svcMock).update(TEST_CODE, vendorMock);
    }

    @Test
    public void delete_delegatesToService() {
        ResponseEntity<String> response = controller.delete(TEST_CODE);

        verify(svcMock).delete(TEST_CODE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(TEST_CODE, response.getBody());
    }

    @Test
    public void listLedgerEntries_returnsProperValue() {
        VendorLedgerEntryList list = controller.listLedgerEntries(TEST_CODE, TEST_PAGE);

        assertEquals(vendorLedgerEntryDtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }
}
