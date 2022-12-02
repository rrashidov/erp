package org.roko.erp.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.BankAccount;
import org.roko.erp.backend.model.BankAccountLedgerEntry;
import org.roko.erp.backend.repositories.BankAccountLedgerEntryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class BankAccountLedgerEntryServiceTest {

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<BankAccountLedgerEntry> pageMock;

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

        when(repoMock.findFor(eq(bankAccountMock), any(Pageable.class))).thenReturn(pageMock);
        
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
    public void listWithPage_delegatesToRepo() {
        svc.findFor(bankAccountMock, TEST_PAGE);

        verify(repoMock).findFor(eq(bankAccountMock), pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(Constants.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo() {
        svc.count(bankAccountMock);

        verify(repoMock).count(bankAccountMock);
    }
}
