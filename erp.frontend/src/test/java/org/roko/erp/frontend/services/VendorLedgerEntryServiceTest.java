package org.roko.erp.frontend.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.list.VendorLedgerEntryList;
import org.springframework.web.client.RestTemplate;

public class VendorLedgerEntryServiceTest {

    private static final String TEST_VENDOR_CODE = "test-vendor-code";

    private static final int TEST_PAGE = 12;

    @Mock
    private RestTemplate restTemplateMock;

    private VendorLedgerEntryService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new VendorLedgerEntryServiceImpl(restTemplateMock);
    }

    @Test
    public void listWithPage_callsBackend() {
        svc.list(TEST_VENDOR_CODE, TEST_PAGE);

        verify(restTemplateMock).getForObject("/api/v1/vendors/{code}/ledgerentries/page/{page}",
                VendorLedgerEntryList.class, TEST_VENDOR_CODE, TEST_PAGE);
    }

}
