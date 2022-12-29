package org.roko.erp.frontend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.PostedSalesDocumentDTO;
import org.roko.erp.dto.PostedSalesDocumentLineDTO;
import org.roko.erp.dto.list.PostedSalesDocumentLineList;
import org.roko.erp.dto.list.PostedSalesDocumentList;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.services.PostedSalesCreditMemoLineService;
import org.roko.erp.frontend.services.PostedSalesCreditMemoService;
import org.springframework.ui.Model;

public class PostedSalesCreditMemoControllerTest {
    
    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 12;

    private static final long TEST_COUNT = 123;
    private static final long TEST_LINE_COUNT = 123;

    private List<PostedSalesDocumentDTO> postedSalesCreditMemos;

    private List<PostedSalesDocumentLineDTO> postedSalesCreditMemoLines;

    @Mock
    private PostedSalesDocumentLineDTO postedSalesCreditMemoLineMock;

    @Mock
    private PostedSalesDocumentDTO postedSalesCreditMemoMock;

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

    @Mock
    private PostedSalesDocumentList postedSalesCreditMemoList;

    @Mock
    private PostedSalesDocumentLineList postedSalesCreditMemoLineList;

    private PostedSalesCreditMemoController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        postedSalesCreditMemoLines = Arrays.asList(postedSalesCreditMemoLineMock);

        postedSalesCreditMemos = Arrays.asList(postedSalesCreditMemoMock);

        when(postedSalesCreditMemoList.getData()).thenReturn(postedSalesCreditMemos);
        when(postedSalesCreditMemoList.getCount()).thenReturn(TEST_COUNT);

        when(svcMock.list(TEST_PAGE)).thenReturn(postedSalesCreditMemoList);
        when(svcMock.get(TEST_CODE)).thenReturn(postedSalesCreditMemoMock);

        when(postedSalesCreditMemoLineList.getData()).thenReturn(postedSalesCreditMemoLines);
        when(postedSalesCreditMemoLineList.getCount()).thenReturn(TEST_LINE_COUNT);

        when(postedSalesCreditMemoLineSvcMock.list(TEST_CODE, TEST_PAGE)).thenReturn(postedSalesCreditMemoLineList);

        when(pagingSvcMock.generate("postedSalesCreditMemo", TEST_PAGE, (int) TEST_COUNT)).thenReturn(pagingDataMock);
        when(pagingSvcMock.generate("postedSalesCreditMemoCard", TEST_CODE, TEST_PAGE, (int) TEST_LINE_COUNT)).thenReturn(linesPagingDataMock);

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
