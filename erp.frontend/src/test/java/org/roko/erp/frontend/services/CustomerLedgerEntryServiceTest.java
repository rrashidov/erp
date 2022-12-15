package org.roko.erp.frontend.services;

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
import org.roko.erp.frontend.controllers.paging.PagingServiceImpl;
import org.roko.erp.frontend.model.Customer;
import org.roko.erp.frontend.model.CustomerLedgerEntry;
import org.roko.erp.frontend.repositories.CustomerLedgerEntryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class CustomerLedgerEntryServiceTest {
    
    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<CustomerLedgerEntry> pageMock;

    @Mock
    private Customer customerMock;

    @Mock
    private CustomerLedgerEntry customerLedgerEntryMock;

    @Mock
    private CustomerLedgerEntryRepository repoMock;

    private CustomerLedgerEntryService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(repoMock.findFor(eq(customerMock), any(Pageable.class))).thenReturn(pageMock);

        svc = new CustomerLedgerEntryServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(customerLedgerEntryMock);

        verify(repoMock).save(customerLedgerEntryMock);
    }

    @Test
    public void list_delegatesToRepo() {
        svc.findFor(customerMock);

        verify(repoMock).findFor(customerMock);
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        svc.findFor(customerMock, TEST_PAGE);

        verify(repoMock).findFor(eq(customerMock), pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(PagingServiceImpl.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo() {
        svc.count(customerMock);

        verify(repoMock).count(customerMock);
    }
}
