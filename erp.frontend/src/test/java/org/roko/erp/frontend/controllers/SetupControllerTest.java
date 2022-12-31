package org.roko.erp.frontend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.CodeSerieDTO;
import org.roko.erp.dto.SetupDTO;
import org.roko.erp.dto.list.CodeSerieList;
import org.roko.erp.frontend.controllers.model.SetupModel;
import org.roko.erp.frontend.services.CodeSerieService;
import org.roko.erp.frontend.services.SetupService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

public class SetupControllerTest {

    private List<CodeSerieDTO> codeSeries = new ArrayList<>();

    @Mock
    private SetupDTO setupMock;

    @Mock
    private SetupModel setupModelMock;

    @Mock
    private Model modelMock;

    @Mock
    private SetupService svcMock;

    @Mock
    private CodeSerieService codeSerieSvcMock;

    @Mock
    private CodeSerieList codeSerieList;

    private SetupController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(svcMock.get()).thenReturn(setupMock);

        when(codeSerieList.getData()).thenReturn(codeSeries);

        when(codeSerieSvcMock.list()).thenReturn(codeSerieList);

        controller = new SetupController(svcMock, codeSerieSvcMock);
    }

    @Test
    public void cardReturnsProperTemplate(){
        String template = controller.card(modelMock);

        assertEquals("setupCard.html", template);

        verify(modelMock).addAttribute(eq("setup"), any(SetupModel.class));
        verify(modelMock).addAttribute("codeSeries", codeSeries);
    }

    @Test
    public void postCardUpdatesSetup(){
        RedirectView redirectView = controller.post(new SetupModel());

        assertEquals("/", redirectView.getUrl());

        verify(svcMock).update(setupMock);
    }
}
