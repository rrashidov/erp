package org.roko.erp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
    
    private static final Long TEST_PAGE = 123l;

    private static final long TEST_COUNT = 345l;

    private List<CodeSerie> codeSeries = new ArrayList<>();

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

        when(svcMock.list()).thenReturn(codeSeries);
        when(svcMock.count()).thenReturn(TEST_COUNT);

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
        String template = controller.card(modelMock);

        assertEquals("codeSerieCard.html", template);

        verify(modelMock).addAttribute(eq("codeSerie"), codeSerieArgumentCaptor.capture());

        CodeSerie codeSerie = codeSerieArgumentCaptor.getValue();

        assertEquals("", codeSerie.getCode());
        assertEquals("", codeSerie.getName());
        assertEquals("", codeSerie.getFirstCode());
        assertEquals("", codeSerie.getLastCode());
    }

    @Test
    public void postSavesEntity(){
        RedirectView redirectView = controller.post(codeSerieMock);

        assertEquals("/codeSerieList", redirectView.getUrl());

        verify(svcMock).create(codeSerieMock);
    }
}
