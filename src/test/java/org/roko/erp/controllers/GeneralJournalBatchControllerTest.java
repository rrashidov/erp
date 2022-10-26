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
import org.roko.erp.model.GeneralJournalBatch;
import org.roko.erp.services.GeneralJournalBatchService;
import org.springframework.ui.Model;

public class GeneralJournalBatchControllerTest {

    private static final Long TEST_PAGE = 123l;

    private static final long TEST_COUNT = 234l;

    private List<GeneralJournalBatch> generalJournalBatchList = new ArrayList<>();

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

        when(pagingSvcMock.generate("generalJournalBatch", TEST_PAGE, TEST_COUNT)).thenReturn(pagingDataMock);

        when(svcMock.list()).thenReturn(generalJournalBatchList);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        controller = new GeneralJournalBatchController(svcMock, pagingSvcMock);
    }

    @Test
    public void listReturnsProperTemplate(){
        String template = controller.list(TEST_PAGE, modelMock);

        assertEquals("generalJournalBatchList.html", template);

        verify(modelMock).addAttribute("generalJournalBatches", generalJournalBatchList);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }
}
