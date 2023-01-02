package org.roko.erp.frontend.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.PurchaseDocumentLineDTO;
import org.roko.erp.dto.list.PurchaseDocumentLineList;
import org.springframework.web.client.RestTemplate;

public class PurchaseOrderLineServiceTest {

    private static final int TEST_PAGE = 12;

    private static final String TEST_CODE = "test-code";

    private static final int TEST_LINE_NO = 123;

    @Mock
    private PurchaseDocumentLineDTO purchaseOrderLineMock;

    @Mock
    private RestTemplate restTemplate;

    private PurchaseOrderLineService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new PurchaseOrderLineServiceImpl(restTemplate);
    }

    @Test
    public void create_callsBackend() {
        svc.create(TEST_CODE, purchaseOrderLineMock);

        verify(restTemplate).postForObject("/api/v1/purchaseorders/{code}/lines", purchaseOrderLineMock, Integer.class, TEST_CODE);
    }

    @Test
    public void update_callsBackend() {
        svc.update(TEST_CODE, TEST_LINE_NO, purchaseOrderLineMock);

        verify(restTemplate).put("/api/v1/purchaseorders/{code}/lines/{lineNo}", purchaseOrderLineMock, TEST_CODE, TEST_LINE_NO);
    }

    @Test
    public void delete_callsBackend() {
        svc.delete(TEST_CODE, TEST_LINE_NO);

        verify(restTemplate).delete("/api/v1/purchaseorders/{code}/lines/{lineNo}", TEST_CODE, TEST_LINE_NO);
    }

    @Test
    public void get_callsBackend() {
        svc.get(TEST_CODE, TEST_LINE_NO);

        verify(restTemplate).getForObject("/api/v1/purchaseorders/{code}/lines/{lineNo}", PurchaseDocumentLineDTO.class, TEST_CODE, TEST_LINE_NO);
    }

    @Test
    public void listWithPage_callsBackend(){
        svc.list(TEST_CODE, TEST_PAGE);

        verify(restTemplate).getForObject("/api/v1/purchaseorders/{code}/lines/page/{page}", PurchaseDocumentLineList.class, TEST_CODE, TEST_PAGE);
    }

}
