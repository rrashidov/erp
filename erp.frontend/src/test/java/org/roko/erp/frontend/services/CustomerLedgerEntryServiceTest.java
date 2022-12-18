package org.roko.erp.frontend.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.list.CustomerLedgerEntryList;
import org.roko.erp.frontend.repositories.CustomerLedgerEntryRepository;
import org.springframework.web.client.RestTemplate;

public class CustomerLedgerEntryServiceTest {

    private static final String TEST_CUSTOMER_CODE = "test-customer-code";

    private static final int TEST_PAGE = 12;

    @Mock
    private CustomerLedgerEntryRepository repoMock;

    @Mock
    private RestTemplate restTemplateMock;

    private CustomerLedgerEntryService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new CustomerLedgerEntryServiceImpl(restTemplateMock);
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        svc.list(TEST_CUSTOMER_CODE, TEST_PAGE);

        verify(restTemplateMock).getForObject("/api/v1/customers/{code}/ledgerentries/page/{page}",
                CustomerLedgerEntryList.class, TEST_CUSTOMER_CODE, TEST_PAGE);
    }

}
