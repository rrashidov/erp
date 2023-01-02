package org.roko.erp.frontend.services;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.CodeSerieDTO;
import org.roko.erp.dto.list.CodeSerieList;
import org.roko.erp.frontend.model.CodeSerie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class CodeSerieServiceTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<CodeSerie> pageMock;

    @Mock
    private CodeSerieDTO codeSerieMock;

    @Mock
    private RestTemplate restTemplateMock;

    private CodeSerieService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new CodeSerieServiceImpl(restTemplateMock);
    }

    @Test
    public void create_callsBackend() {
        svc.create(codeSerieMock);

        verify(restTemplateMock).postForObject("/api/v1/codeseries", codeSerieMock, String.class);
    }

    @Test
    public void update_delegatesToRepo() {
        svc.update(TEST_CODE, codeSerieMock);

        verify(restTemplateMock).put("/api/v1/codeseries/{code}", codeSerieMock, TEST_CODE);
    }

    @Test
    public void delete_callsBackend() {
        svc.delete(TEST_CODE);

        verify(restTemplateMock).delete("/api/v1/codeseries/{code}", TEST_CODE);
    }

    @Test
    public void get_callsBackend() {
        svc.get(TEST_CODE);

        verify(restTemplateMock).getForObject("/api/v1/codeseries/{code}", CodeSerieDTO.class, TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenCalledWithNonExistingCode() {
        when(restTemplateMock.getForObject("/api/v1/codeseries/{code}", CodeSerieDTO.class, "non-existing-code")).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        CodeSerieDTO codeSerie = svc.get("non-existing-code");

        assertNull(codeSerie);
    }

    @Test
    public void list_callsBackend() {
        svc.list();

        verify(restTemplateMock).getForObject("/api/v1/codeseries", CodeSerieList.class);
    }

    @Test
    public void listWithPage_callsBackend() {
        svc.list(TEST_PAGE);

        verify(restTemplateMock).getForObject("/api/v1/codeseries/page/{page}", CodeSerieList.class, TEST_PAGE);
    }
}
