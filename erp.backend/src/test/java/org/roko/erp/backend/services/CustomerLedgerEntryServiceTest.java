package org.roko.erp.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.Customer;
import org.roko.erp.backend.model.CustomerLedgerEntry;
import org.roko.erp.backend.model.CustomerLedgerEntryType;
import org.roko.erp.backend.repositories.CustomerLedgerEntryRepository;
import org.roko.erp.dto.CustomerLedgerEntryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class CustomerLedgerEntryServiceTest {
    
    private static final long TEST_ID = 123l;
    private static final CustomerLedgerEntryType TEST_TYPE = CustomerLedgerEntryType.PAYMENT;
    private static final String TEST_DOCUMENT_CODE = "test-document-code";
    private static final Date TEST_DATE = new Date();
    private static final BigDecimal TEST_AMOUNT = new BigDecimal(12.12);

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

        when(customerLedgerEntryMock.getId()).thenReturn(TEST_ID);
        when(customerLedgerEntryMock.getType()).thenReturn(TEST_TYPE);
        when(customerLedgerEntryMock.getDocumentCode()).thenReturn(TEST_DOCUMENT_CODE);
        when(customerLedgerEntryMock.getDate()).thenReturn(TEST_DATE);
        when(customerLedgerEntryMock.getAmount()).thenReturn(TEST_AMOUNT);

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
        assertEquals(Constants.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo() {
        svc.count(customerMock);

        verify(repoMock).count(customerMock);
    }

    @Test
    public void toDTO_returnsProperResult(){
        CustomerLedgerEntryDTO dto = svc.toDTO(customerLedgerEntryMock);

        assertEquals(TEST_ID, dto.getId());
        assertEquals(TEST_TYPE.name(), dto.getType());
        assertEquals(TEST_DOCUMENT_CODE, dto.getDocumentCode());
        assertEquals(TEST_DATE, dto.getDate());
        assertEquals(TEST_AMOUNT, dto.getAmount());
    }
}
