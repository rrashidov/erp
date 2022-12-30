package org.roko.erp.frontend.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.list.PostedPurchaseDocumentLineList;
import org.springframework.web.client.RestTemplate;

public class PostedPurchaseOrderLineServiceTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 12;

    @Mock
    private RestTemplate restTemplateMock;

    private PostedPurchaseOrderLineService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new PostedPurchaseOrderLineServiceImpl(restTemplateMock);
    }

    @Test
    public void listWithPage_callsBackend() {
        svc.list(TEST_CODE, TEST_PAGE);

        verify(restTemplateMock).getForObject("/api/v1/postedpurchaseorders/{code}/lines/page/{page}",
                PostedPurchaseDocumentLineList.class, TEST_CODE, TEST_PAGE);
    }

}
