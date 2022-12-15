package org.roko.erp.frontend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.frontend.controllers.paging.PagingServiceImpl;
import org.roko.erp.frontend.model.CodeSerie;
import org.roko.erp.frontend.repositories.CodeSerieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class CodeSerieServiceTest {

    private static final String TEST_LAST_CODE =            "CODE000023";
    private static final String ALL_ZEROS_TEST_LAST_CODE =  "CODE000000";
    private static final String LAST_CODE_WITH_NO_ZEROS =   "CODE121212";

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<CodeSerie> pageMock;

    @Mock
    private CodeSerie codeSerieMock;

    @Mock
    private CodeSerieRepository repoMock;

    private CodeSerieService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(codeSerieMock.getLastCode()).thenReturn(TEST_LAST_CODE);

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(codeSerieMock));
        when(repoMock.findAll(any(Pageable.class))).thenReturn(pageMock);

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
    public void listWithPage_delegatesToRepo() {
        svc.list(TEST_PAGE);

        verify(repoMock).findAll(pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(PagingServiceImpl.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo(){
        svc.count();

        verify(repoMock).count();
    }

    @Test
    public void generate_generatesProperCode(){
        String generatedCode = svc.generate(TEST_CODE);

        assertEquals("CODE000024", generatedCode);

        verify(repoMock).findById(TEST_CODE);
        verify(repoMock).save(codeSerieMock);
    }

    @Test
    public void generate_generatesProperCode_whenStartsWithZeros(){
        when(codeSerieMock.getLastCode()).thenReturn(ALL_ZEROS_TEST_LAST_CODE);

        String generatedCode = svc.generate(TEST_CODE);

        assertEquals("CODE000001", generatedCode);

        verify(repoMock).findById(TEST_CODE);
        verify(repoMock).save(codeSerieMock);
    }

    @Test
    public void generate_generatesProperCode_whenNoZeros(){
        when(codeSerieMock.getLastCode()).thenReturn(LAST_CODE_WITH_NO_ZEROS);

        String generatedCode = svc.generate(TEST_CODE);

        assertEquals("CODE121213", generatedCode);

        verify(repoMock).findById(TEST_CODE);
        verify(repoMock).save(codeSerieMock);
    }

}
