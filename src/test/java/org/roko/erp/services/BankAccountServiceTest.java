package org.roko.erp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.BankAccount;
import org.roko.erp.repositories.BankAccountRepository;

public class BankAccountServiceTest {

    private static final String TEST_CODE = "test-code";

    @Mock
    private BankAccount bankAccountMock;

    @Mock
    private BankAccount bankAccountMock1;

    @Mock
    private BankAccount bankAccountMock2;

    @Mock
    private BankAccountRepository repoMock;

    private BankAccountService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(bankAccountMock));
        when(repoMock.findAll()).thenReturn(Arrays.asList(bankAccountMock, bankAccountMock1, bankAccountMock2));

        svc = new BankAccountServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo() {
        svc.create(bankAccountMock);

        verify(repoMock).save(bankAccountMock);
    }

    @Test
    public void update_delegatesToRepo() {
        svc.update(TEST_CODE, bankAccountMock);

        verify(repoMock).save(bankAccountMock);
    }

    @Test
    public void delete_delegatesToRepo(){
        svc.delete(TEST_CODE);

        verify(repoMock).delete(bankAccountMock);
    }

    @Test
    public void get_delegatesToRepo(){
        BankAccount retrievedBankAccount = svc.get(TEST_CODE);

        assertEquals(bankAccountMock, retrievedBankAccount);
    }

    @Test
    public void getReturnsNull_whenItemNotFound(){
        BankAccount retrievedBankAccount = svc.get("non-existing-code");

        assertNull(retrievedBankAccount);
    }

    @Test
    public void list_delegatesToRepo(){
        svc.list();

        verify(repoMock).findAll();

        verify(repoMock).balance(bankAccountMock);
        verify(repoMock).balance(bankAccountMock1);
        verify(repoMock).balance(bankAccountMock2);
    }

    @Test
    public void count_delegatesToRepo(){
        svc.count();

        verify(repoMock).count();
    }
}
