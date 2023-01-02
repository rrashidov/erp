package org.roko.erp.frontend.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.dto.list.SalesDocumentList;
import org.springframework.web.client.RestTemplate;

public class SalesCreditMemoServiceTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 12;

    @Mock
    private SalesDocumentDTO salesCreditMemoMock;

    @Mock
    private RestTemplate restTemplate;

    private SalesCreditMemoService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new SalesCreditMemoServiceImpl(restTemplate);
    }

    @Test
    public void create_callsBackend() {
        svc.create(salesCreditMemoMock);

        verify(restTemplate).postForObject("/api/v1/salescreditmemos", salesCreditMemoMock, String.class);
    }

    @Test
    public void update_callsBackend() {
        svc.update(TEST_CODE, salesCreditMemoMock);

        verify(restTemplate).put("/api/v1/salescreditmemos/{code}", salesCreditMemoMock, TEST_CODE);
    }

    @Test
    public void delete_callsBackend() {
        svc.delete(TEST_CODE);

        verify(restTemplate).delete("/api/v1/salescreditmemos/{code}", TEST_CODE);
    }

    @Test
    public void get_callsBackend() {
        svc.get(TEST_CODE);

        verify(restTemplate).getForObject("/api/v1/salescreditmemos/{code}", SalesDocumentDTO.class, TEST_CODE);
    }

    @Test
    public void listWithPage_callsBackend() {
        svc.list(TEST_PAGE);

        verify(restTemplate).getForObject("/api/v1/salescreditmemos/page/{page}", SalesDocumentList.class, TEST_PAGE);
    }

}
