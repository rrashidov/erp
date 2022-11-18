package org.roko.erp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.PostedSalesCreditMemo;
import org.roko.erp.model.PostedSalesCreditMemoLine;
import org.roko.erp.services.PostedSalesCreditMemoLineService;
import org.roko.erp.services.PostedSalesCreditMemoService;
import org.springframework.ui.Model;

public class PostedSalesCreditMemoControllerTest {
    
    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 12;
    private static final int TEST_COUNT = 123;
    private static final int TEST_LINE_COUNT = 123;

    private List<PostedSalesCreditMemo> postedSalesCreditMemos;

    private List<PostedSalesCreditMemoLine> postedSalesCreditMemoLines;

    @Mock
    private PostedSalesCreditMemoLine postedSalesCreditMemoLineMock;

    @Mock
    private PostedSalesCreditMemo postedSalesCreditMemoMock;

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private PagingData linesPagingDataMock;

    @Mock
    private Model modelMock;

    @Mock
    private PostedSalesCreditMemoService svcMock;

    @Mock
    private PostedSalesCreditMemoLineService postedSalesCreditMemoLineSvcMock;

    @Mock
    private PagingService pagingSvcMock;

    private PostedSalesCreditMemoController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        postedSalesCreditMemoLines = Arrays.asList(postedSalesCreditMemoLineMock);

        postedSalesCreditMemos = Arrays.asList(postedSalesCreditMemoMock);

        when(svcMock.list(TEST_PAGE)).thenReturn(postedSalesCreditMemos);
        when(svcMock.count()).thenReturn(TEST_COUNT);
        when(svcMock.get(TEST_CODE)).thenReturn(postedSalesCreditMemoMock);

        when(postedSalesCreditMemoLineSvcMock.list(postedSalesCreditMemoMock, TEST_PAGE)).thenReturn(postedSalesCreditMemoLines);
        when(postedSalesCreditMemoLineSvcMock.count(postedSalesCreditMemoMock)).thenReturn(TEST_LINE_COUNT);

        when(pagingSvcMock.generate("postedSalesCreditMemo", TEST_PAGE, TEST_COUNT)).thenReturn(pagingDataMock);
        when(pagingSvcMock.generate("postedSalesCreditMemoCard", TEST_CODE, TEST_PAGE, TEST_LINE_COUNT)).thenReturn(linesPagingDataMock);

        controller = new PostedSalesCreditMemoController(svcMock, postedSalesCreditMemoLineSvcMock, pagingSvcMock);
    }

    @Test
    public void list_returnsProperTemplate() {
        String template = controller.list(TEST_PAGE, modelMock);

        assertEquals("postedSalesCreditMemoList.html", template);

        verify(modelMock).addAttribute("postedSalesCreditMemos", postedSalesCreditMemos);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }

    @Test
    public void card_returnsProperTemplate(){
        String template = controller.card(TEST_CODE, TEST_PAGE, modelMock);

        assertEquals("postedSalesCreditMemoCard.html", template);

        verify(modelMock).addAttribute("postedSalesCreditMemo", postedSalesCreditMemoMock);
        verify(modelMock).addAttribute("postedSalesCreditMemoLines", postedSalesCreditMemoLines);
        verify(modelMock).addAttribute("paging", linesPagingDataMock);
    }
}
