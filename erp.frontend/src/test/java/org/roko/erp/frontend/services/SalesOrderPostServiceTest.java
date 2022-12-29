package org.roko.erp.frontend.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class SalesOrderPostServiceTest {

    private static final String TEST_CODE = "test-code";

    @Mock
    private RestTemplate restTemplateMock;

    private SalesOrderPostService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new SalesOrderPostServiceImpl(restTemplateMock);
    }

    @Test
    public void post_callsBackend() throws PostFailedException {
        svc.post(TEST_CODE);

        verify(restTemplateMock).getForObject("/api/v1/salesorders/{code}/operations/post", String.class, TEST_CODE);
    }

    @Test
    public void post_throwsException_whenBackendReturnsNotOK(){
        when(restTemplateMock.getForObject("/api/v1/salesorders/{code}/operations/post", String.class, TEST_CODE)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        assertThrows(PostFailedException.class, () -> {
            svc.post(TEST_CODE);
        });
    }
}
