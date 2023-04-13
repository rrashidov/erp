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
import org.roko.erp.backend.model.BankAccount;
import org.roko.erp.backend.model.BankAccountLedgerEntry;
import org.roko.erp.backend.services.BankAccountLedgerEntryService;
import org.roko.erp.backend.services.BankAccountService;
import org.roko.erp.dto.BankAccountDTO;
import org.roko.erp.dto.BankAccountLedgerEntryDTO;
import org.roko.erp.dto.list.BankAccountLedgerEntryList;
import org.roko.erp.dto.list.BankAccountList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

public class BankAccountControllerTest {

    private static final String NON_EXISTING_CODE = "non-existing-code";

    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";

    private static final int TEST_PAGE = 123;

    private static final int TEST_COUNT = 222;
    private static final int TEST_LEDGER_ENTRY_COUNT = 333;

    @Mock
    private BankAccount bankAccountMock;

    @Mock
    private BankAccountDTO bankAccountDtoMock;

    @Mock
    private BankAccountService svcMock;

    @Mock
    private BankAccountLedgerEntryService bankAccountLedgerEntrySvcMock;

    @Mock
    private BankAccountLedgerEntry bankAccountLedgerEntryMock;

    @Mock
    private BankAccountLedgerEntryDTO bankAccountLedgerEntryDtoMock;

    private BankAccountController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(svcMock.get(TEST_CODE)).thenReturn(bankAccountMock);
        when(svcMock.fromDTO(bankAccountDtoMock)).thenReturn(bankAccountMock);
        when(svcMock.list()).thenReturn(Arrays.asList(bankAccountMock));
        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(bankAccountMock));
        when(svcMock.toDTO(bankAccountMock)).thenReturn(bankAccountDtoMock);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        when(bankAccountDtoMock.getCode()).thenReturn(TEST_CODE);
        when(bankAccountDtoMock.getName()).thenReturn(TEST_NAME);

        when(bankAccountLedgerEntrySvcMock.count(bankAccountMock)).thenReturn(TEST_LEDGER_ENTRY_COUNT);
        when(bankAccountLedgerEntrySvcMock.findFor(bankAccountMock, TEST_PAGE))
                .thenReturn(Arrays.asList(bankAccountLedgerEntryMock));
        when(bankAccountLedgerEntrySvcMock.toDTO(bankAccountLedgerEntryMock)).thenReturn(bankAccountLedgerEntryDtoMock);

        controller = new BankAccountController(svcMock, bankAccountLedgerEntrySvcMock);
    }

    @Test
    public void list_delegatesToSvc() {
        BankAccountList list = controller.list();

        assertEquals(TEST_COUNT, list.getCount());
        assertEquals(bankAccountDtoMock, list.getData().get(0));
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        BankAccountList list = controller.list(TEST_PAGE);

        assertEquals(TEST_COUNT, list.getCount());
        assertEquals(bankAccountDtoMock, list.getData().get(0));
    }

    @Test
    public void get_delegatesToRepo() {
        controller.get(TEST_CODE);

        verify(svcMock).toDTO(bankAccountMock);
    }

    @Test
    public void get_returnsNotFound_whenCalledWithNonExistingCode() {
        assertThrows(ResponseStatusException.class, () -> {
            controller.get(NON_EXISTING_CODE);
        });
    }

    @Test
    public void post_delegatesToRepo() {
        controller.post(bankAccountDtoMock);

        verify(svcMock).create(bankAccountMock);
    }

    @Test
    public void put_delegatesToRepo() {
        controller.put(TEST_CODE, bankAccountDtoMock);

        verify(svcMock).update(TEST_CODE, bankAccountMock);
    }

    @Test
    public void delete_delegatesToRepo() {
        ResponseEntity<String> response = controller.delete(TEST_CODE);

        verify(svcMock).delete(TEST_CODE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(TEST_CODE, response.getBody());
    }

    @Test
    public void listLedgerEntries_delegatesToSvc() {
        BankAccountLedgerEntryList list = controller.listLedgerEntries(TEST_CODE, TEST_PAGE);

        assertEquals(TEST_LEDGER_ENTRY_COUNT, list.getCount());
        assertEquals(bankAccountLedgerEntryDtoMock, list.getData().get(0));
    }
}
