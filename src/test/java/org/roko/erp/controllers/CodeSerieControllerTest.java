package org.roko.erp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.CodeSerie;
import org.roko.erp.services.CodeSerieService;
import org.springframework.ui.Model;

public class CodeSerieControllerTest {
    
    private static final Long TEST_PAGE = 123l;

    private static final long TEST_COUNT = 345l;

    private List<CodeSerie> codeSeries = new ArrayList<>();

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
}
