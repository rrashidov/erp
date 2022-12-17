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
import org.roko.erp.backend.model.Customer;
import org.roko.erp.backend.model.CustomerLedgerEntry;
import org.roko.erp.backend.services.CustomerLedgerEntryService;
import org.roko.erp.backend.services.CustomerService;
import org.roko.erp.dto.CustomerDTO;
import org.roko.erp.dto.CustomerLedgerEntryDTO;
import org.roko.erp.dto.list.CustomerLedgerEntryList;
import org.roko.erp.dto.list.CustomerList;
import org.springframework.web.server.ResponseStatusException;

public class CustomerControllerTest {

    private static final String NON_EXISTING_CODE = "non-existing-code";
    
    private static final int TEST_COUNT = 222;
    private static final int TEST_PAGE = 12;
    private static final String TEST_CODE = "test-code";

    @Mock
    private Customer customerMock;

    @Mock
    private CustomerDTO customerDtoMock;

    @Mock
    private CustomerService svcMock;

    @Mock
    private CustomerLedgerEntryService customerLedgerEntrySvcMock;

    @Mock
    private CustomerLedgerEntry customerLedgerEntryMock;

    @Mock
    private CustomerLedgerEntryDTO customerLedgerEntryDtoMock;

    private CustomerController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(svcMock.list()).thenReturn(Arrays.asList(customerMock));
        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(customerMock));
        when(svcMock.get(TEST_CODE)).thenReturn(customerMock);
        when(svcMock.fromDTO(customerDtoMock)).thenReturn(customerMock);
        when(svcMock.toDTO(customerMock)).thenReturn(customerDtoMock);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        when(customerLedgerEntrySvcMock.findFor(customerMock, TEST_PAGE)).thenReturn(Arrays.asList(customerLedgerEntryMock));
        when(customerLedgerEntrySvcMock.toDTO(customerLedgerEntryMock)).thenReturn(customerLedgerEntryDtoMock);
        when(customerLedgerEntrySvcMock.count(customerMock)).thenReturn(TEST_COUNT);

        controller = new CustomerController(svcMock, customerLedgerEntrySvcMock);
    }

    @Test
    public void list_returnsProperValue() {
        CustomerList list = controller.list();

        assertEquals(customerDtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void listWithPage_returnsProperValue() {
        CustomerList list = controller.list(TEST_PAGE);

        assertEquals(customerDtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void listLedgerEntries_returnsProperValue() {
        CustomerLedgerEntryList list = controller.listLedgerEntries(TEST_CODE, TEST_PAGE);

        assertEquals(TEST_COUNT, list.getCount());
        assertEquals(customerLedgerEntryDtoMock, list.getData().get(0));
    }

    @Test
    public void get_delegatesToService() {
        controller.get(TEST_CODE);

        verify(svcMock).get(TEST_CODE);
        verify(svcMock).toDTO(customerMock);
    }

    @Test
    public void get_throwsException_whenCalledWithNonExistingCode() {
        assertThrows(ResponseStatusException.class, () -> {
            controller.get(NON_EXISTING_CODE);
        });
    }

    @Test
    public void post_delegatesToService() {
        controller.post(customerDtoMock);

        verify(svcMock).fromDTO(customerDtoMock);
        verify(svcMock).create(customerMock);
    }

    @Test
    public void put_delegatesToService() {
        controller.put(TEST_CODE, customerDtoMock);

        verify(svcMock).fromDTO(customerDtoMock);
        verify(svcMock).update(TEST_CODE, customerMock);
    }

    @Test
    public void delete_delegatesToService() {
        controller.delete(TEST_CODE);

        verify(svcMock).delete(TEST_CODE);
    }
}
