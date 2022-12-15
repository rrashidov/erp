package org.roko.erp.backend.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.roko.erp.backend.model.BankAccount;
import org.roko.erp.backend.services.BankAccountLedgerEntryService;
import org.roko.erp.backend.services.BankAccountService;
import org.roko.erp.dto.BankAccountDTO;

public class BankAccountControllerTest {

    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";

    private static final int TEST_PAGE = 123;

    @Mock
    private BankAccount bankAccountMock;

    @Mock
    private BankAccountDTO bankAccountDtoMock;

    @Mock
    private BankAccountService svcMock;

    @Mock
    private BankAccountLedgerEntryService bankAccountLedgerEntrySvcMock;

    private BankAccountController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(svcMock.get(TEST_CODE)).thenReturn(bankAccountMock);
        doAnswer(new Answer<BankAccount>() {
            @Override
            public BankAccount answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                BankAccount bankAccount = (BankAccount) args[0];
                bankAccount.setCode(TEST_CODE);
                return bankAccount;
            }
        }).when(svcMock).create(any(BankAccount.class));
        when(svcMock.fromDTO(bankAccountDtoMock)).thenReturn(bankAccountMock);

        when(bankAccountDtoMock.getCode()).thenReturn(TEST_CODE);
        when(bankAccountDtoMock.getName()).thenReturn(TEST_NAME);

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

        verify(svcMock).getDTO(TEST_CODE);
    }

    @Test
    public void getLedgerEntries_delegatesToRepo() {
        controller.listLedgerEntries(TEST_CODE, TEST_PAGE);

        verify(svcMock).get(TEST_CODE);
        verify(bankAccountLedgerEntrySvcMock).list(bankAccountMock, TEST_PAGE);
    }

    @Test
    public void post_delegatesToRepo() {
        controller.post(bankAccountDtoMock);

        verify(svcMock).create(bankAccountMock);
    }

    @Test
    public void put_delegatesToRepo(){
        controller.put(TEST_CODE, bankAccountDtoMock);

        verify(svcMock).update(TEST_CODE, bankAccountMock);
    }

    @Test
    public void delete_delegatesToRepo(){
        controller.delete(TEST_CODE);

        verify(svcMock).delete(TEST_CODE);
    }

    @Test
    public void listLedgerEntries_delegatesToSvc(){
        controller.listLedgerEntries(TEST_CODE, TEST_PAGE);

        verify(bankAccountLedgerEntrySvcMock).list(bankAccountMock, TEST_PAGE);
    }
}
