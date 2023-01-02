package org.roko.erp.frontend.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.list.BankAccountLedgerEntryList;
import org.springframework.web.client.RestTemplate;

public class BankAccountLedgerEntryServiceTest {

    private static final int TEST_PAGE = 12;

    private static final String TEST_CODE = "test-code";

    @Mock
    private RestTemplate restTemplateMock;

    private BankAccountLedgerEntryService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new BankAccountLedgerEntryServiceImpl(restTemplateMock);
    }

    @Test
    public void list_callsBackend() {
        svc.list(TEST_CODE, TEST_PAGE);

        verify(restTemplateMock).getForObject("/api/v1/bankaccounts/{code}/ledgerentries/page/{page}",
                BankAccountLedgerEntryList.class, TEST_CODE, TEST_PAGE);
    }

}
