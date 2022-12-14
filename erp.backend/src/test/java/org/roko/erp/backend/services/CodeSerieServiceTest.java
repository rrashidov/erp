package org.roko.erp.backend.services;

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
import org.roko.erp.backend.model.CodeSerie;
import org.roko.erp.backend.repositories.CodeSerieRepository;
import org.roko.erp.dto.CodeSerieDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class CodeSerieServiceTest {

    private static final String TEST_FIRST_CODE =           "test-first-code";
    private static final String TEST_LAST_CODE =            "CODE000023";

    private static final String ALL_ZEROS_TEST_LAST_CODE =  "CODE000000";
    private static final String LAST_CODE_WITH_NO_ZEROS =   "CODE121212";

    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<CodeSerie> pageMock;

    @Mock
    private CodeSerie codeSerieMock;

    @Mock
    private CodeSerieRepository repoMock;

    @Mock
    private CodeSerieDTO dtoMock;

    private CodeSerieService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(dtoMock.getCode()).thenReturn(TEST_CODE);
        when(dtoMock.getName()).thenReturn(TEST_NAME);
        when(dtoMock.getFirstCode()).thenReturn(TEST_FIRST_CODE);
        when(dtoMock.getLastCode()).thenReturn(TEST_LAST_CODE);

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
        assertEquals(Constants.RECORDS_PER_PAGE, pageable.getPageSize());
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

    @Test
    public void toDTO_returnsProperValue() {
        when(codeSerieMock.getCode()).thenReturn(TEST_CODE);
        when(codeSerieMock.getName()).thenReturn(TEST_NAME);
        when(codeSerieMock.getFirstCode()).thenReturn(TEST_FIRST_CODE);
        when(codeSerieMock.getLastCode()).thenReturn(TEST_LAST_CODE);

        CodeSerieDTO dto = svc.toDTO(codeSerieMock);

        assertEquals(TEST_CODE, dto.getCode());
        assertEquals(TEST_NAME, dto.getName());
        assertEquals(TEST_FIRST_CODE, dto.getFirstCode());
        assertEquals(TEST_LAST_CODE, dto.getLastCode());
    }

    @Test
    public void fromDTO_returnsProperValue() {
        CodeSerie codeSerie = svc.fromDTO(dtoMock);

        assertEquals(TEST_CODE, codeSerie.getCode());
        assertEquals(TEST_NAME, codeSerie.getName());
        assertEquals(TEST_FIRST_CODE, codeSerie.getFirstCode());
        assertEquals(TEST_LAST_CODE, codeSerie.getLastCode());
    }
}
