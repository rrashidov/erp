package org.roko.erp.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.CodeSerie;
import org.roko.erp.backend.services.CodeSerieService;
import org.roko.erp.dto.CodeSerieDTO;
import org.roko.erp.dto.list.CodeSerieList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

public class CodeSerieControllerTest {
    
    private static final String NON_EXISTING_CODE = "non-existing-code";

    private static final long TEST_COUNT = 222;

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 123;

    @Mock
    private CodeSerieService svcMock;

    @Mock
    private CodeSerie codeSerieMock;

    @Mock
    private CodeSerieDTO dtoMock;

    private CodeSerieController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(svcMock.list()).thenReturn(Arrays.asList(codeSerieMock));
        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(codeSerieMock));
        when(svcMock.get(TEST_CODE)).thenReturn(codeSerieMock);
        when(svcMock.fromDTO(dtoMock)).thenReturn(codeSerieMock);
        when(svcMock.toDTO(codeSerieMock)).thenReturn(dtoMock);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        controller = new CodeSerieController(svcMock);
    }

    @Test
    public void list_delegatesToService() {
        CodeSerieList list = controller.list();

        assertEquals(dtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void listPage_delegatesToService() {
        CodeSerieList list = controller.list(TEST_PAGE);

        assertEquals(dtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void get_delegatesToService() {
        controller.get(TEST_CODE);

        verify(svcMock).get(TEST_CODE);
        verify(svcMock).toDTO(codeSerieMock);
    }

    @Test
    public void get_throwsException_whenCalledWithNonExistingCode() {
        assertThrows(ResponseStatusException.class, () -> {
            controller.get(NON_EXISTING_CODE);
        });
    }

    @Test
    public void post_delegatesToService() {
        controller.post(dtoMock);

        verify(svcMock).create(codeSerieMock);
    }

    @Test
    public void put_delegatesToService() {
        controller.put(TEST_CODE, dtoMock);

        verify(svcMock).update(TEST_CODE, codeSerieMock);
    }

    @Test
    public void delete_delegatesToService() {
        ResponseEntity<String> response = controller.delete(TEST_CODE);

        verify(svcMock).delete(TEST_CODE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(TEST_CODE, response.getBody());
    }
}
