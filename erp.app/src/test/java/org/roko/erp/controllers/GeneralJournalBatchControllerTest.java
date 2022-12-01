package org.roko.erp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.GeneralJournalBatch;
import org.roko.erp.model.GeneralJournalBatchLine;
import org.roko.erp.services.FeedbackService;
import org.roko.erp.services.GeneralJournalBatchLineService;
import org.roko.erp.services.GeneralJournalBatchPostService;
import org.roko.erp.services.GeneralJournalBatchService;
import org.roko.erp.services.PostFailedException;
import org.roko.erp.services.util.FeedbackType;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

public class GeneralJournalBatchControllerTest {

    private static final String TEST_POST_FAILED_MSG = "test-post-failed-msg";

    private static final int TEST_PAGE = 123;
    private static final int TEST_COUNT = 234;

    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";

    private List<GeneralJournalBatch> generalJournalBatchList;

    private List<GeneralJournalBatchLine> generalJournalBatchLines;

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
    private GeneralJournalBatchLineService generalJournalBatchLineSvcMock;

    @Mock
    private PagingService pagingSvcMock;

    @Mock
    private GeneralJournalBatchLine generalJournalbatchLineMock;

    @Mock
    private GeneralJournalBatchPostService generalJournalBatchPostSvcMock;

    @Mock
    private FeedbackService feedbackSvcMock;

    @Mock
    private HttpSession httpSessionMock;

    private GeneralJournalBatchController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        generalJournalBatchList = Arrays.asList(generalJournalBatchMock);

        generalJournalBatchLines = Arrays.asList(generalJournalbatchLineMock);

        when(generalJournalBatchMock.getCode()).thenReturn(TEST_CODE);
        when(generalJournalBatchMock.getName()).thenReturn(TEST_NAME);

        when(pagingSvcMock.generate("generalJournalBatch", TEST_PAGE, TEST_COUNT)).thenReturn(pagingDataMock);
        when(pagingSvcMock.generate("generalJournalBatchCard", TEST_CODE, TEST_PAGE, TEST_COUNT))
                .thenReturn(pagingDataMock);

        when(svcMock.list(TEST_PAGE)).thenReturn(generalJournalBatchList);
        when(svcMock.count()).thenReturn(TEST_COUNT);
        when(svcMock.get(TEST_CODE)).thenReturn(generalJournalBatchMock);

        when(generalJournalBatchLineSvcMock.list(generalJournalBatchMock, TEST_PAGE))
                .thenReturn(generalJournalBatchLines);
        when(generalJournalBatchLineSvcMock.count(generalJournalBatchMock)).thenReturn(TEST_COUNT);
        when(generalJournalBatchLineSvcMock.list(generalJournalBatchMock, TEST_PAGE))
                .thenReturn(generalJournalBatchLines);

        controller = new GeneralJournalBatchController(svcMock, generalJournalBatchLineSvcMock,
                generalJournalBatchPostSvcMock, pagingSvcMock, feedbackSvcMock);
    }

    @Test
    public void listReturnsProperTemplate() {
        String template = controller.list(TEST_PAGE, modelMock, httpSessionMock);

        assertEquals("generalJournalBatchList.html", template);

        verify(modelMock).addAttribute("generalJournalBatches", generalJournalBatchList);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }

    @Test
    public void cardReturnsProperTemplate_whenCalledForNew() {
        String template = controller.card(null, TEST_PAGE, modelMock);

        assertEquals("generalJournalBatchCard.html", template);

        verify(modelMock).addAttribute(eq("generalJournalBatch"), generalJournalBatchArgumentCaptor.capture());

        GeneralJournalBatch generalJournalBatch = generalJournalBatchArgumentCaptor.getValue();

        assertEquals("", generalJournalBatch.getCode());
        assertEquals("", generalJournalBatch.getName());
    }

    @Test
    public void cardReturnsProperTemplate_whenCalledForExisting() {
        String template = controller.card(TEST_CODE, TEST_PAGE, modelMock);

        assertEquals("generalJournalBatchCard.html", template);

        verify(modelMock).addAttribute(eq("generalJournalBatch"), generalJournalBatchArgumentCaptor.capture());
        verify(modelMock).addAttribute("generalJournalBatchLines", generalJournalBatchLines);
        verify(modelMock).addAttribute("paging", pagingDataMock);

        GeneralJournalBatch generalJournalBatch = generalJournalBatchArgumentCaptor.getValue();

        assertEquals(TEST_CODE, generalJournalBatch.getCode());
        assertEquals(TEST_NAME, generalJournalBatch.getName());
    }

    @Test
    public void postCreatesNewEntity_whenCalledFromEmpty() {
        RedirectView redirectView = controller.post(generalJournalBatchMock);

        assertEquals("/generalJournalBatchList", redirectView.getUrl());

        verify(svcMock).create(generalJournalBatchMock);
    }

    @Test
    public void delete_deletesEntity() {
        RedirectView redirectView = controller.delete(TEST_CODE);

        assertEquals("/generalJournalBatchList", redirectView.getUrl());

        verify(svcMock).delete(TEST_CODE);
    }

    @Test
    public void post_returnsProperTemplate() throws PostFailedException {
        RedirectView redirectView = controller.post(TEST_CODE, httpSessionMock);

        assertEquals("/generalJournalBatchList", redirectView.getUrl());

        verify(generalJournalBatchPostSvcMock).post(TEST_CODE);
        verify(feedbackSvcMock).give(FeedbackType.INFO, "General journal batch " + TEST_CODE + " posted.", httpSessionMock);
    }

    @Test
    public void post_returnsProperFeedbach_whenPostFails() throws PostFailedException {
        doThrow(new PostFailedException(TEST_POST_FAILED_MSG)).when(generalJournalBatchPostSvcMock).post(TEST_CODE);

        RedirectView redirectView = controller.post(TEST_CODE, httpSessionMock);

        assertEquals("/generalJournalBatchList", redirectView.getUrl());

        verify(generalJournalBatchPostSvcMock).post(TEST_CODE);
        verify(feedbackSvcMock).give(FeedbackType.ERROR, "General journal batch " + TEST_CODE + " post failed: " + TEST_POST_FAILED_MSG, httpSessionMock);
    }
}
