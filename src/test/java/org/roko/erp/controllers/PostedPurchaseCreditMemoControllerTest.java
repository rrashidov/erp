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
import org.roko.erp.model.PostedPurchaseCreditMemo;
import org.roko.erp.model.PostedPurchaseCreditMemoLine;
import org.roko.erp.services.PostedPurchaseCreditMemoLineService;
import org.roko.erp.services.PostedPurchaseCreditMemoService;
import org.springframework.ui.Model;

public class PostedPurchaseCreditMemoControllerTest {
    
    private static final String TEST_CODE = "test-code";

    private static final int TEST_COUNT = 123;
    private static final int TEST_LINE_COUNT = 234;

    private List<PostedPurchaseCreditMemo> postedPurchaseCreditMemos = new ArrayList<>();

    private List<PostedPurchaseCreditMemoLine> postedPurchaseCreditMemoLines = new ArrayList<>();

    @Mock
    private PostedPurchaseCreditMemo postedPurchaseCreditMemoMock;

    @Mock
    private Model modelMock;

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private PagingData linesPagingDataMock;

    @Mock
    private PostedPurchaseCreditMemoService svcMock;

    @Mock
    private PostedPurchaseCreditMemoLineService postedPurchaseCreditMemoLineSvcMock;

    @Mock
    private PagingService pagingSvcMock;

    private PostedPurchaseCreditMemoController controller;

    @BeforeEach
    public void setup () {
        MockitoAnnotations.openMocks(this);

        when(svcMock.list()).thenReturn(postedPurchaseCreditMemos);
        when(svcMock.count()).thenReturn(TEST_COUNT);
        when(svcMock.get(TEST_CODE)).thenReturn(postedPurchaseCreditMemoMock);

        when(postedPurchaseCreditMemoLineSvcMock.list(postedPurchaseCreditMemoMock)).thenReturn(postedPurchaseCreditMemoLines);
        when(postedPurchaseCreditMemoLineSvcMock.count(postedPurchaseCreditMemoMock)).thenReturn(TEST_LINE_COUNT);

        when(pagingSvcMock.generate("postedPurchaseCreditMemo", 1, TEST_COUNT)).thenReturn(pagingDataMock);
        when(pagingSvcMock.generate("postedPurchaseCreditMemoLine", 1, TEST_LINE_COUNT)).thenReturn(linesPagingDataMock);

        controller = new PostedPurchaseCreditMemoController(svcMock, postedPurchaseCreditMemoLineSvcMock, pagingSvcMock);
    }

    @Test
    public void list_returnsProperTemplate() {
        String template = controller.list(modelMock);

        assertEquals("postedPurchaseCreditMemoList.html", template);

        verify(modelMock).addAttribute("postedPurchaseCreditMemos", postedPurchaseCreditMemos);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }

    @Test
    public void card_returnsProperTemplate() {
        String template = controller.card(TEST_CODE, modelMock);

        assertEquals("postedPurchaseCreditMemoCard.html", template);

        verify(modelMock).addAttribute("postedPurchaseCreditMemo", postedPurchaseCreditMemoMock);
        verify(modelMock).addAttribute("postedPurchaseCreditMemoLines", postedPurchaseCreditMemoLines);
        verify(modelMock).addAttribute("paging", linesPagingDataMock);
    }
}
