package org.roko.erp.frontend.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.PurchaseDocumentLineDTO;
import org.roko.erp.dto.list.PurchaseDocumentLineList;
import org.springframework.web.client.RestTemplate;

public class PurchaseCreditMemoLineServiceTest {

    private static final int TEST_PAGE = 12;

    private static final String TEST_CODE = "test-code";

    private static final int TEST_LINE_NO = 123;

    @Mock
    private PurchaseDocumentLineDTO purchaseCreditMemoLineMock;

    @Mock
    private RestTemplate restTemplateMock;

    private PurchaseCreditMemoLineService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new PurchaseCreditMemoLineServiceImpl(restTemplateMock);
    }

    @Test
    public void create_callsBackend() {
        svc.create(TEST_CODE, purchaseCreditMemoLineMock);

        verify(restTemplateMock).postForObject("/api/v1/purchasecreditmemos/{code}/lines", purchaseCreditMemoLineMock,
                Integer.class, TEST_CODE);
    }

    @Test
    public void update_callsBackend() {
        svc.update(TEST_CODE, TEST_LINE_NO, purchaseCreditMemoLineMock);

        verify(restTemplateMock).put("/api/v1/purchasecreditmemos/{code}/lines/{lineNo}", purchaseCreditMemoLineMock,
                TEST_CODE, TEST_LINE_NO);
    }

    @Test
    public void delete_callsBackend() {
        svc.delete(TEST_CODE, TEST_LINE_NO);

        verify(restTemplateMock).delete("/api/v1/purchasecreditmemos/{code}/lines/{lineNo}", TEST_CODE, TEST_LINE_NO);
    }

    @Test
    public void get_callsBackend() {
        svc.get(TEST_CODE, TEST_LINE_NO);

        verify(restTemplateMock).getForObject("/api/v1/purchasecreditmemos/{code}/lines/{lineNo}",
                PurchaseDocumentLineDTO.class, TEST_CODE, TEST_LINE_NO);
    }

    @Test
    public void listWithPage_callsBackend() {
        svc.list(TEST_CODE, TEST_PAGE);

        verify(restTemplateMock).getForObject("/api/v1/purchasecreditmemos/{code}/lines/page/{page}",
                PurchaseDocumentLineList.class, TEST_CODE, TEST_PAGE);
    }

}
