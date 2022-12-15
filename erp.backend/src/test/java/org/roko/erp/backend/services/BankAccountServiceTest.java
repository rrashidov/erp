package org.roko.erp.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.BankAccount;
import org.roko.erp.backend.repositories.BankAccountRepository;
import org.roko.erp.dto.BankAccountDTO;
import org.roko.erp.dto.list.BankAccountList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class BankAccountServiceTest {

    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";

    private static final long TEST_COUNT = 123123l;
    
    private static final int TEST_PAGE = 123;

    private List<BankAccount> bankAccounts;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<BankAccount> pageMock;

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

        bankAccounts = Arrays.asList(bankAccountMock, bankAccountMock1, bankAccountMock2);

        when(bankAccountMock.getCode()).thenReturn(TEST_CODE);
        when(bankAccountMock.getName()).thenReturn(TEST_NAME);

        when(pageMock.toList()).thenReturn(bankAccounts);

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(bankAccountMock));
        when(repoMock.findAll()).thenReturn(bankAccounts);
        when(repoMock.findAll(any(Pageable.class))).thenReturn(pageMock);
        when(repoMock.count()).thenReturn(TEST_COUNT);

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

        verify(repoMock).balance(bankAccountMock);
    }

    @Test
    public void getReturnsNull_whenItemNotFound(){
        BankAccount retrievedBankAccount = svc.get("non-existing-code");

        assertNull(retrievedBankAccount);
    }

    @Test
    public void getDTOReturnsProperResult(){
        BankAccountDTO dto = svc.getDTO(TEST_CODE);
        
        assertEquals(TEST_CODE, dto.getCode());
        assertEquals(TEST_NAME, dto.getName());
    }

    @Test
    public void list_delegatesToRepo(){
        BankAccountList list = svc.list();

        assertEquals(bankAccounts.size(), list.getData().size());
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void listWithPage_delegatesToRepo(){
        BankAccountList list = svc.list(TEST_PAGE);

        assertEquals(bankAccounts.size(), list.getData().size());
        assertEquals(TEST_COUNT, list.getCount());
    }

}
