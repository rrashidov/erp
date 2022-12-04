package org.roko.erp.backend.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.BankAccount;
import org.roko.erp.backend.services.BankAccountLedgerEntryService;
import org.roko.erp.backend.services.BankAccountService;

public class BankAccountControllerTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 123;

    @Mock
    private BankAccount bankAccountMock;

    @Mock
    private BankAccountService svcMock;

    @Mock
    private BankAccountLedgerEntryService bankAccountLedgerEntrySvcMock;

    private BankAccountController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(svcMock.get(TEST_CODE)).thenReturn(bankAccountMock);

        controller = new BankAccountController(svcMock, bankAccountLedgerEntrySvcMock);
    }

    @Test
    public void list_delegatesToSvc(){
        controller.list();

        verify(svcMock).list();
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        controller.list(TEST_PAGE);

        verify(svcMock).list(TEST_PAGE);
    }

    @Test
    public void get_delegatesToRepo() {
        controller.get(TEST_CODE);

        verify(svcMock).get(TEST_CODE);
    }

    @Test
    public void getLedgerEntries_delegatesToRepo() {
        controller.getLedgerEntries(TEST_CODE, TEST_PAGE);

        verify(svcMock).get(TEST_CODE);
        verify(bankAccountLedgerEntrySvcMock).findFor(bankAccountMock, TEST_PAGE);
    }

    @Test
    public void post_delegatesToRepo() {
        controller.post(bankAccountMock);

        verify(svcMock).create(bankAccountMock);
    }

    @Test
    public void put_delegatesToRepo(){
        controller.put(TEST_CODE, bankAccountMock);

        verify(svcMock).update(TEST_CODE, bankAccountMock);
    }

    @Test
    public void delete_delegatesToRepo(){
        controller.delete(TEST_CODE);

        verify(svcMock).delete(TEST_CODE);
    }
}
