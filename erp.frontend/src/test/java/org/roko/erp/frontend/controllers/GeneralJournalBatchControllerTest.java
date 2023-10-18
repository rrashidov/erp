package org.roko.erp.frontend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.GeneralJournalBatchDTO;
import org.roko.erp.dto.GeneralJournalBatchLineDTO;
import org.roko.erp.dto.list.GeneralJournalBatchLineList;
import org.roko.erp.dto.list.GeneralJournalBatchList;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.services.DeleteFailedException;
import org.roko.erp.frontend.services.FeedbackService;
import org.roko.erp.frontend.services.GeneralJournalBatchLineService;
import org.roko.erp.frontend.services.GeneralJournalBatchPostService;
import org.roko.erp.frontend.services.GeneralJournalBatchService;
import org.roko.erp.frontend.services.PostFailedException;
import org.roko.erp.frontend.services.util.FeedbackType;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

public class GeneralJournalBatchControllerTest {

    private static final String DELETE_ERR_MSG = "delete-err-msg";

    private static final String TEST_POST_FAILED_MSG = "test-post-failed-msg";

    private static final int TEST_PAGE = 123;
    private static final long TEST_COUNT = 234;

    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";

    private List<GeneralJournalBatchDTO> generalJournalBatches;

    private List<GeneralJournalBatchLineDTO> generalJournalBatchLines;

    @Mock
    private GeneralJournalBatchDTO generalJournalBatchMock;

    @Captor
    private ArgumentCaptor<GeneralJournalBatchDTO> generalJournalBatchArgumentCaptor;

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
    private GeneralJournalBatchLineDTO generalJournalbatchLineMock;

    @Mock
    private GeneralJournalBatchPostService generalJournalBatchPostSvcMock;

    @Mock
    private FeedbackService feedbackSvcMock;

    @Mock
    private HttpSession httpSessionMock;

    @Mock
    private GeneralJournalBatchList generalJournalBatchList;

    @Mock
    private GeneralJournalBatchLineList generalJournalBatchLineList;

    @Mock
    private RedirectAttributes redirectAttributesMock;

    private GeneralJournalBatchController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        generalJournalBatches = Arrays.asList(generalJournalBatchMock);

        generalJournalBatchLines = Arrays.asList(generalJournalbatchLineMock);

        when(generalJournalBatchMock.getCode()).thenReturn(TEST_CODE);
        when(generalJournalBatchMock.getName()).thenReturn(TEST_NAME);

        when(pagingSvcMock.generate("generalJournalBatch", TEST_PAGE, (int) TEST_COUNT)).thenReturn(pagingDataMock);
        when(pagingSvcMock.generate("generalJournalBatchCard", TEST_CODE, TEST_PAGE, (int) TEST_COUNT))
                .thenReturn(pagingDataMock);

        when(generalJournalBatchList.getData()).thenReturn(generalJournalBatches);
        when(generalJournalBatchList.getCount()).thenReturn(TEST_COUNT);

        when(svcMock.list(TEST_PAGE)).thenReturn(generalJournalBatchList);
        when(svcMock.get(TEST_CODE)).thenReturn(generalJournalBatchMock);

        when(generalJournalBatchLineList.getData()).thenReturn(generalJournalBatchLines);
        when(generalJournalBatchLineList.getCount()).thenReturn(TEST_COUNT);

        when(generalJournalBatchLineSvcMock.list(TEST_CODE, TEST_PAGE))
                .thenReturn(generalJournalBatchLineList);

        controller = new GeneralJournalBatchController(svcMock, generalJournalBatchLineSvcMock,
                generalJournalBatchPostSvcMock, pagingSvcMock, feedbackSvcMock);
    }

    @Test
    public void listReturnsProperTemplate() {
        String template = controller.list(TEST_PAGE, modelMock, httpSessionMock);

        assertEquals("generalJournalBatchList.html", template);

        verify(modelMock).addAttribute("generalJournalBatches", generalJournalBatches);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }

    @Test
    public void cardReturnsProperTemplate_whenCalledForNew() {
        String template = controller.card(null, TEST_PAGE, modelMock);

        assertEquals("generalJournalBatchCard.html", template);

        verify(modelMock).addAttribute(eq("generalJournalBatch"), generalJournalBatchArgumentCaptor.capture());

        GeneralJournalBatchDTO generalJournalBatch = generalJournalBatchArgumentCaptor.getValue();

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

        GeneralJournalBatchDTO generalJournalBatch = generalJournalBatchArgumentCaptor.getValue();

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
    public void delete_deletesEntity() throws DeleteFailedException {
        RedirectView redirectView = controller.delete(TEST_CODE, TEST_PAGE, redirectAttributesMock, httpSessionMock);

        assertEquals("/generalJournalBatchList", redirectView.getUrl());

        verify(svcMock).delete(TEST_CODE);

        verify(feedbackSvcMock).give(FeedbackType.INFO, String.format("General Journal Batch %s deleted", TEST_CODE),
                httpSessionMock);

        verify(redirectAttributesMock).addAttribute("page", TEST_PAGE);
    }

    @Test
    public void deleteGivesErrorFeedback_whenDeleteFails() throws DeleteFailedException {
        doThrow(new DeleteFailedException(DELETE_ERR_MSG)).when(svcMock).delete(TEST_CODE);

        RedirectView redirectView = controller.delete(TEST_CODE, TEST_PAGE, redirectAttributesMock, httpSessionMock);

        assertEquals("/generalJournalBatchList", redirectView.getUrl());

        verify(svcMock).delete(TEST_CODE);

        verify(feedbackSvcMock).give(FeedbackType.ERROR, DELETE_ERR_MSG,
                httpSessionMock);

        verify(redirectAttributesMock).addAttribute("page", TEST_PAGE);
    }

    @Test
    public void post_returnsProperTemplate() throws PostFailedException {
        RedirectView redirectView = controller.post(TEST_CODE, httpSessionMock);

        assertEquals("/generalJournalBatchList", redirectView.getUrl());

        verify(generalJournalBatchPostSvcMock).post(TEST_CODE);
        verify(feedbackSvcMock).give(FeedbackType.INFO, "General journal batch " + TEST_CODE + " post scheduled.", httpSessionMock);
    }

    @Test
    public void post_returnsProperFeedbach_whenPostFails() throws PostFailedException {
        doThrow(new PostFailedException(TEST_POST_FAILED_MSG)).when(generalJournalBatchPostSvcMock).post(TEST_CODE);

        RedirectView redirectView = controller.post(TEST_CODE, httpSessionMock);

        assertEquals("/generalJournalBatchList", redirectView.getUrl());

        verify(generalJournalBatchPostSvcMock).post(TEST_CODE);
        verify(feedbackSvcMock).give(FeedbackType.ERROR, "General journal batch " + TEST_CODE + " post scheduling failed: " + TEST_POST_FAILED_MSG, httpSessionMock);
    }
}
