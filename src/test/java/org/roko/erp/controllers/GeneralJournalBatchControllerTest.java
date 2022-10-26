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
import org.roko.erp.model.GeneralJournalBatch;
import org.roko.erp.services.GeneralJournalBatchService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

public class GeneralJournalBatchControllerTest {

    private static final Long TEST_PAGE = 123l;

    private static final long TEST_COUNT = 234l;

    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";

    private List<GeneralJournalBatch> generalJournalBatchList = new ArrayList<>();

    @Mock
    private GeneralJournalBatch generalJournalBatchMock;

    @Captor
    private ArgumentCaptor<GeneralJournalBatch> generalJournalBatchArgumentCaptor;

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private Model modelMock;

    @Mock
    private GeneralJournalBatchService svcMock;

    @Mock
    private PagingService pagingSvcMock;

    private GeneralJournalBatchController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(generalJournalBatchMock.getCode()).thenReturn(TEST_CODE);
        when(generalJournalBatchMock.getName()).thenReturn(TEST_NAME);

        when(pagingSvcMock.generate("generalJournalBatch", TEST_PAGE, TEST_COUNT)).thenReturn(pagingDataMock);

        when(svcMock.list()).thenReturn(generalJournalBatchList);
        when(svcMock.count()).thenReturn(TEST_COUNT);
        when(svcMock.get(TEST_CODE)).thenReturn(generalJournalBatchMock);

        controller = new GeneralJournalBatchController(svcMock, pagingSvcMock);
    }

    @Test
    public void listReturnsProperTemplate(){
        String template = controller.list(TEST_PAGE, modelMock);

        assertEquals("generalJournalBatchList.html", template);

        verify(modelMock).addAttribute("generalJournalBatches", generalJournalBatchList);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }

    @Test
    public void cardReturnsProperTemplate_whenCalledForNew(){
        String template = controller.card(null, modelMock);

        assertEquals("generalJournalBatchCard.html", template);

        verify(modelMock).addAttribute(eq("generalJournalBatch"), generalJournalBatchArgumentCaptor.capture());

        GeneralJournalBatch generalJournalBatch = generalJournalBatchArgumentCaptor.getValue();

        assertEquals("", generalJournalBatch.getCode());
        assertEquals("", generalJournalBatch.getName());
    }

    @Test
    public void cardReturnsProperTemplate_whenCalledForExisting(){
        String template = controller.card(TEST_CODE, modelMock);

        assertEquals("generalJournalBatchCard.html", template);

        verify(modelMock).addAttribute(eq("generalJournalBatch"), generalJournalBatchArgumentCaptor.capture());

        GeneralJournalBatch generalJournalBatch = generalJournalBatchArgumentCaptor.getValue();

        assertEquals(TEST_CODE, generalJournalBatch.getCode());
        assertEquals(TEST_NAME, generalJournalBatch.getName());
    }


    @Test
    public void postCreatesNewEntity_whenCalledFromEmpty(){
        RedirectView redirectView = controller.post(generalJournalBatchMock);

        assertEquals("/generalJournalBatchList", redirectView.getUrl());

        verify(svcMock).create(generalJournalBatchMock);
    }
}
