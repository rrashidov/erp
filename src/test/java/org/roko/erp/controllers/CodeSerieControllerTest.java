package org.roko.erp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.CodeSerie;
import org.roko.erp.services.CodeSerieService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

public class CodeSerieControllerTest {
    
    private static final int TEST_PAGE = 123;

    private static final int TEST_COUNT = 345;

    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";
    private static final String TEST_FIRST_CODE = "test-first-code";
    private static final String TEST_LAST_CODE = "test-last-code";

    private List<CodeSerie> codeSeries;

    @Mock
    private CodeSerie codeSerieMock;

    @Captor
    private ArgumentCaptor<CodeSerie> codeSerieArgumentCaptor;

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private CodeSerieService svcMock;

    @Mock
    private Model modelMock;

    @Mock
    private PagingService pagingSvcMock;

    private CodeSerieController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        codeSeries = Arrays.asList(codeSerieMock);

        when(codeSerieMock.getCode()).thenReturn(TEST_CODE);
        when(codeSerieMock.getName()).thenReturn(TEST_NAME);
        when(codeSerieMock.getFirstCode()).thenReturn(TEST_FIRST_CODE);
        when(codeSerieMock.getLastCode()).thenReturn(TEST_LAST_CODE);

        when(svcMock.list(TEST_PAGE)).thenReturn(codeSeries);
        when(svcMock.count()).thenReturn(TEST_COUNT);
        when(svcMock.get(TEST_CODE)).thenReturn(codeSerieMock);

        when(pagingSvcMock.generate("codeSerie", TEST_PAGE, TEST_COUNT)).thenReturn(pagingDataMock);
        
        controller = new CodeSerieController(svcMock, pagingSvcMock);
    }

    @Test
    public void listReturnsProperTemplate(){
        String template = controller.list(TEST_PAGE, modelMock);

        assertEquals("codeSerieList.html", template);

        verify(modelMock).addAttribute("codeSeries", codeSeries);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }

    @Test
    public void cardReturnsProperTemplate_whenCalledForNew(){
        String template = controller.card(null, modelMock);

        assertEquals("codeSerieCard.html", template);

        verify(modelMock).addAttribute(eq("codeSerie"), codeSerieArgumentCaptor.capture());

        CodeSerie codeSerie = codeSerieArgumentCaptor.getValue();

        assertEquals("", codeSerie.getCode());
        assertEquals("", codeSerie.getName());
        assertEquals("", codeSerie.getFirstCode());
        assertEquals("", codeSerie.getLastCode());
    }

    @Test
    public void cardReturnsProperTemplate_whenCalledForUpdate(){
        String template = controller.card(TEST_CODE, modelMock);

        assertEquals("codeSerieCard.html", template);

        verify(modelMock).addAttribute(eq("codeSerie"), codeSerieArgumentCaptor.capture());

        CodeSerie codeSerie = codeSerieArgumentCaptor.getValue();

        assertEquals(TEST_CODE, codeSerie.getCode());
        assertEquals(TEST_NAME, codeSerie.getName());
        assertEquals(TEST_FIRST_CODE, codeSerie.getFirstCode());
        assertEquals(TEST_LAST_CODE, codeSerie.getLastCode());
    }

    @Test
    public void postSavesEntity(){
        when(svcMock.get(TEST_CODE)).thenReturn(null);

        RedirectView redirectView = controller.post(codeSerieMock);

        assertEquals("/codeSerieList", redirectView.getUrl());

        verify(svcMock).create(codeSerieMock);
    }

    @Test
    public void postUpdatesEntity_whenCalledForExistingOne(){
        RedirectView redirectView = controller.post(codeSerieMock);

        assertEquals("/codeSerieList", redirectView.getUrl());

        verify(svcMock).update(TEST_CODE, codeSerieMock);
    }

    @Test
    public void deleteReturnsProperTemplate(){
        RedirectView redirectView = controller.delete(TEST_CODE);

        assertEquals("/codeSerieList", redirectView.getUrl());

        verify(svcMock).delete(TEST_CODE);
    }
}
