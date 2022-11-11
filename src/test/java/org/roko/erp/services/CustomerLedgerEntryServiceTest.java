package org.roko.erp.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.Customer;
import org.roko.erp.model.CustomerLedgerEntry;
import org.roko.erp.repositories.CustomerLedgerEntryRepository;

public class CustomerLedgerEntryServiceTest {
    
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
    public void count_delegatesToRepo() {
        svc.count(customerMock);

        verify(repoMock).count(customerMock);
    }
}
