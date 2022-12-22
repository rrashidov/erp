package org.roko.erp.frontend.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.dto.list.PurchaseDocumentList;
import org.springframework.web.client.RestTemplate;

public class PurchaseOrderServiceTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 12;

    @Mock
    private PurchaseDocumentDTO purchaseOrderMock;

    @Mock
    private RestTemplate restTemplate;

    private PurchaseOrderService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new PurchaseOrderServiceImpl(restTemplate);
    }

    @Test
    public void create_callsBackend() {
        svc.create(purchaseOrderMock);

        verify(restTemplate).postForObject("/api/v1/purchaseorders", purchaseOrderMock, String.class);
    }

    @Test
    public void update_callsBackend() {
        svc.update(TEST_CODE, purchaseOrderMock);

        verify(restTemplate).put("/api/v1/purchaseorders/{code}", purchaseOrderMock, TEST_CODE);
    }

    @Test
    public void delete_callsBackend() {
        svc.delete(TEST_CODE);

        verify(restTemplate).delete("/api/v1/purchaseorders/{code}", TEST_CODE);
    }

    @Test
    public void get_callsBackend() {
        svc.get(TEST_CODE);

        verify(restTemplate).getForObject("/api/v1/purchaseorders/{code}", PurchaseDocumentDTO.class, TEST_CODE);
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        svc.list(TEST_PAGE);

        verify(restTemplate).getForObject("/api/v1/purchaseorders/page/{page}", PurchaseDocumentList.class, TEST_PAGE);
    }

}
