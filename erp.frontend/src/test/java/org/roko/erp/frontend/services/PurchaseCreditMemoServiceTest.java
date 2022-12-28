package org.roko.erp.frontend.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.dto.list.PurchaseDocumentList;
import org.springframework.web.client.RestTemplate;

public class PurchaseCreditMemoServiceTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 12;

    @Mock
    private PurchaseDocumentDTO purchaseCreditMemoMock;

    @Mock
    private RestTemplate restTemplateMock;

    private PurchaseCreditMemoService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new PurchaseCreditMemoServiceImpl(restTemplateMock);
    }

    @Test
    public void create_callsBackend() {
        svc.create(purchaseCreditMemoMock);

        verify(restTemplateMock).postForObject("/api/v1/purchasecreditmemos", purchaseCreditMemoMock, String.class);
    }

    @Test
    public void update_callsBackend() {
        svc.update(TEST_CODE, purchaseCreditMemoMock);

        verify(restTemplateMock).put("/api/v1/purchasecreditmemos/{code}", purchaseCreditMemoMock, TEST_CODE);
    }

    @Test
    public void delete_delegatesToRepo() {
        svc.delete(TEST_CODE);

        verify(restTemplateMock).delete("/api/v1/purchasecreditmemos/{code}", TEST_CODE);
    }

    @Test
    public void get_delegatesToRepo() {
        svc.get(TEST_CODE);

        verify(restTemplateMock).getForObject("/api/v1/purchasecreditmemos/{code}", PurchaseDocumentDTO.class, TEST_CODE);
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        svc.list(TEST_PAGE);

        verify(restTemplateMock).getForObject("/api/v1/purchasecreditmemos/page/{page}", PurchaseDocumentList.class, TEST_PAGE);
    }

}
