package org.roko.erp.frontend.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.PostedSalesDocumentDTO;
import org.roko.erp.dto.list.PostedSalesDocumentList;
import org.springframework.web.client.RestTemplate;

public class PostedSalesCreditMemoServiceTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 12;

    @Mock
    private RestTemplate restTemplateMock;

    private PostedSalesCreditMemoService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new PostedSalesCreditMemoServiceImpl(restTemplateMock);
    }

    @Test
    public void get_callsBackend() {
        svc.get(TEST_CODE);

        verify(restTemplateMock).getForObject("/api/v1/postedsalescreditmemos/{code}", PostedSalesDocumentDTO.class,
                TEST_CODE);
    }

    @Test
    public void listWithPage_callsBackend() {
        svc.list(TEST_PAGE);

        verify(restTemplateMock).getForObject("/api/v1/postedsalescreditmemos/page/{page}",
                PostedSalesDocumentList.class, TEST_PAGE);
    }

}
