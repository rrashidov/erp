package org.roko.erp.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.BankAccount;
import org.roko.erp.model.BankAccountLedgerEntry;
import org.roko.erp.repositories.BankAccountLedgerEntryRepository;

public class BankAccountLedgerEntryServiceTest {

    @Mock
    private BankAccount bankAccountMock;

    @Mock
    private BankAccountLedgerEntry bankAccountLedgerEntryMock;

    @Mock
    private BankAccountLedgerEntryRepository repoMock;

    private BankAccountLedgerEntryService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        
        svc = new BankAccountLedgerEntryServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo() {
        svc.create(bankAccountLedgerEntryMock);

        verify(repoMock).save(bankAccountLedgerEntryMock);
    }

    @Test
    public void list_delegatesToRepo() {
        svc.findFor(bankAccountMock);

        verify(repoMock).findFor(bankAccountMock);
    }

    @Test
    public void count_delegatesToRepo() {
        svc.count(bankAccountMock);

        verify(repoMock).count(bankAccountMock);
    }
}
