package org.roko.erp.backend.controllers;

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

public class CodeSerieControllerTest {
    
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

        controller = new CodeSerieController(svcMock);
    }

    @Test
    public void list_delegatesToService() {
        controller.list();

        verify(svcMock).list();
        verify(svcMock).toDTO(codeSerieMock);
    }

    @Test
    public void listPage_delegatesToService() {
        controller.list(TEST_PAGE);

        verify(svcMock).list(TEST_PAGE);
        verify(svcMock).toDTO(codeSerieMock);
    }

    @Test
    public void get_delegatesToService() {
        controller.get(TEST_CODE);

        verify(svcMock).get(TEST_CODE);
        verify(svcMock).toDTO(codeSerieMock);
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
        controller.delete(TEST_CODE);

        verify(svcMock).delete(TEST_CODE);
    }
}
