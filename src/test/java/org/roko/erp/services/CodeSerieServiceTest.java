package org.roko.erp.services;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.CodeSerie;
import org.roko.erp.repositories.CodeSerieRepository;

public class CodeSerieServiceTest {

    private static final String TEST_CODE = "test-code";

    @Mock
    private CodeSerie codeSerieMock;

    @Mock
    private CodeSerieRepository repoMock;

    private CodeSerieService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(codeSerieMock));

        svc = new CodeSerieServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(codeSerieMock);

        verify(repoMock).save(codeSerieMock);
    }

    @Test
    public void update_delegatesToRepo(){
        svc.update(TEST_CODE, codeSerieMock);

        verify(repoMock).findById(TEST_CODE);
        verify(repoMock).save(codeSerieMock);
    }

    @Test
    public void delete_delegatesToRepo(){
        svc.delete(TEST_CODE);

        verify(repoMock).delete(codeSerieMock);
    }

    @Test
    public void get_delegatesToRepo(){
        svc.get(TEST_CODE);

        verify(repoMock).findById(TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenCalledWithNonExistingCode(){
        CodeSerie codeSerie = svc.get("non-existing-code");

        assertNull(codeSerie);
    }

    @Test
    public void list_delegatesToRepo(){
        svc.list();

        verify(repoMock).findAll();
    }

    @Test
    public void count_delegatesToRepo(){
        svc.count();

        verify(repoMock).count();
    }
}
