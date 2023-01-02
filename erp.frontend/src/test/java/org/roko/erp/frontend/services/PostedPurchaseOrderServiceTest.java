package org.roko.erp.frontend.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.PostedPurchaseDocumentDTO;
import org.roko.erp.dto.list.PostedPurchaseDocumentList;
import org.springframework.web.client.RestTemplate;

public class PostedPurchaseOrderServiceTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 12;

    @Mock
    private RestTemplate restTemplateMock;

    private PostedPurchaseOrderService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new PostedPurchaseOrderServiceImpl(restTemplateMock);
    }

    @Test
    public void get_callsBackend() {
        svc.get(TEST_CODE);

        verify(restTemplateMock).getForObject("/api/v1/postedpurchaseorders/{code}", PostedPurchaseDocumentDTO.class,
                TEST_CODE);
    }

    @Test
    public void listWithPage_dcallsBackend() {
        svc.list(TEST_PAGE);

        verify(restTemplateMock).getForObject("/api/v1/postedpurchaseorders/page/{page}",
                PostedPurchaseDocumentList.class, TEST_PAGE);
    }

}
