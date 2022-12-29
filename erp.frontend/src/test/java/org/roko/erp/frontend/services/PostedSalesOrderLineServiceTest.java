package org.roko.erp.frontend.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.list.PostedSalesDocumentLineList;
import org.springframework.web.client.RestTemplate;

public class PostedSalesOrderLineServiceTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 12;

    @Mock
    private RestTemplate restTemplateMock;

    private PostedSalesOrderLineService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new PostedSalesOrderLineServiceImpl(restTemplateMock);
    }

    @Test
    public void listWithPage_callsBackend() {
        svc.list(TEST_CODE, TEST_PAGE);

        verify(restTemplateMock).getForObject("/api/v1/postedsalesorders/{code}/lines/page/{page}",
                PostedSalesDocumentLineList.class, TEST_CODE, TEST_PAGE);
    }

}
