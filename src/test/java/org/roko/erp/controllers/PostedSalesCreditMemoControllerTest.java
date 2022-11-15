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
import org.roko.erp.model.PostedSalesCreditMemo;
import org.roko.erp.services.PostedSalesCreditMemoService;
import org.springframework.ui.Model;

public class PostedSalesCreditMemoControllerTest {
    
    private static final Long TEST_COUNT = 123l;

    private List<PostedSalesCreditMemo> postedSalesCreditMemos = new ArrayList<>();

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private Model modelMock;

    @Mock
    private PostedSalesCreditMemoService svcMock;

    @Mock
    private PagingService pagingSvcMock;

    private PostedSalesCreditMemoController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(svcMock.list()).thenReturn(postedSalesCreditMemos);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        when(pagingSvcMock.generate("postedSalesCreditMemo", null, TEST_COUNT)).thenReturn(pagingDataMock);

        controller = new PostedSalesCreditMemoController(svcMock, pagingSvcMock);
    }

    @Test
    public void list_returnsProperTemplate() {
        String template = controller.list(null, modelMock);

        assertEquals("postedSalesCreditMemoList.html", template);

        verify(modelMock).addAttribute("postedSalesCreditMemos", postedSalesCreditMemos);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }
}
