package org.roko.erp.frontend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.PostedPurchaseDocumentDTO;
import org.roko.erp.dto.PostedPurchaseDocumentLineDTO;
import org.roko.erp.dto.list.PostedPurchaseDocumentLineList;
import org.roko.erp.dto.list.PostedPurchaseDocumentList;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.services.PostedPurchaseCreditMemoLineService;
import org.roko.erp.frontend.services.PostedPurchaseCreditMemoService;
import org.springframework.ui.Model;

public class PostedPurchaseCreditMemoControllerTest {
    
    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 23;
    private static final long TEST_COUNT = 123;
    private static final long TEST_LINE_COUNT = 234;

    private List<PostedPurchaseDocumentDTO> postedPurchaseCreditMemos = new ArrayList<>();

    private List<PostedPurchaseDocumentLineDTO> postedPurchaseCreditMemoLines = new ArrayList<>();

    @Mock
    private PostedPurchaseDocumentDTO postedPurchaseCreditMemoMock;

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

    @Mock
    private PostedPurchaseDocumentList postedPurchaseCreditMemoList;

    @Mock
    private PostedPurchaseDocumentLineList postedPurchaseCreditMemoLineList;

    private PostedPurchaseCreditMemoController controller;

    @BeforeEach
    public void setup () {
        MockitoAnnotations.openMocks(this);

        postedPurchaseCreditMemos = Arrays.asList(postedPurchaseCreditMemoMock);

        when(postedPurchaseCreditMemoList.getData()).thenReturn(postedPurchaseCreditMemos);
        when(postedPurchaseCreditMemoList.getCount()).thenReturn(TEST_COUNT);

        when(svcMock.list(TEST_PAGE)).thenReturn(postedPurchaseCreditMemoList);
        when(svcMock.get(TEST_CODE)).thenReturn(postedPurchaseCreditMemoMock);

        when(postedPurchaseCreditMemoLineList.getData()).thenReturn(postedPurchaseCreditMemoLines);
        when(postedPurchaseCreditMemoLineList.getCount()).thenReturn(TEST_LINE_COUNT);

        when(postedPurchaseCreditMemoLineSvcMock.list(TEST_CODE, TEST_PAGE)).thenReturn(postedPurchaseCreditMemoLineList);

        when(pagingSvcMock.generate("postedPurchaseCreditMemo", TEST_PAGE, (int) TEST_COUNT)).thenReturn(pagingDataMock);
        when(pagingSvcMock.generate("postedPurchaseCreditMemoCard", TEST_CODE, TEST_PAGE, (int) TEST_LINE_COUNT)).thenReturn(linesPagingDataMock);

        controller = new PostedPurchaseCreditMemoController(svcMock, postedPurchaseCreditMemoLineSvcMock, pagingSvcMock);
    }

    @Test
    public void list_returnsProperTemplate() {
        String template = controller.list(TEST_PAGE, modelMock);

        assertEquals("postedPurchaseCreditMemoList.html", template);

        verify(modelMock).addAttribute("postedPurchaseCreditMemos", postedPurchaseCreditMemos);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }

    @Test
    public void card_returnsProperTemplate() {
        String template = controller.card(TEST_CODE, TEST_PAGE, modelMock);

        assertEquals("postedPurchaseCreditMemoCard.html", template);

        verify(modelMock).addAttribute("postedPurchaseCreditMemo", postedPurchaseCreditMemoMock);
        verify(modelMock).addAttribute("postedPurchaseCreditMemoLines", postedPurchaseCreditMemoLines);
        verify(modelMock).addAttribute("paging", linesPagingDataMock);
    }
}
