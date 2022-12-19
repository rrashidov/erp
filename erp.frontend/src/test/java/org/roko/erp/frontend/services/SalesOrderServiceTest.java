package org.roko.erp.frontend.services;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.dto.list.SalesDocumentList;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class SalesOrderServiceTest {

    private static final String NON_EXISTING_CODE = "non-existing-code";

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 12;

    @Mock
    private SalesDocumentDTO salesOrderMock;

    @Mock
    private RestTemplate restTemplate;

    private SalesOrderService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new SalesOrderServiceImpl(restTemplate);
    }

    @Test
    public void create_delegatesToRepo() {
        svc.create(salesOrderMock);

        verify(restTemplate).postForObject("/api/v1/salesorders", salesOrderMock, String.class);
    }

    @Test
    public void update_delegatesToRepo() {
        svc.update(TEST_CODE, salesOrderMock);

        verify(restTemplate).put("/api/v1/salesorders/{code}", salesOrderMock, TEST_CODE);
    }

    @Test
    public void delete_delegatesToRepo() {
        svc.delete(TEST_CODE);

        verify(restTemplate).delete("/api/v1/salesorders/{code}", TEST_CODE);
    }

    @Test
    public void get_delegatesToRepo() {
        svc.get(TEST_CODE);

        verify(restTemplate).getForObject("/api/v1/salesorders/{code}", SalesDocumentDTO.class, TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenEntityNotFound(){
        when(restTemplate.getForObject("/api/v1/salesorders/{code}", SalesDocumentDTO.class, NON_EXISTING_CODE)).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        SalesDocumentDTO salesOrder = svc.get(NON_EXISTING_CODE);

        assertNull(salesOrder);
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        svc.list(TEST_PAGE);

        verify(restTemplate).getForObject("/api/v1/salesorders/page/{page}", SalesDocumentList.class, TEST_PAGE);
    }

}
