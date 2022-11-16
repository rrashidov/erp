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
import org.roko.erp.services.PostedPurchaseCreditMemoService;
import org.springframework.ui.Model;

public class PostedPurchaseCreditMemoControllerTest {
    
    private static final Long TEST_COUNT = 123l;

    private List<PostedPurchaseCreditMemo> postedPurchaseCreditMemos = new ArrayList<>();

    @Mock
    private Model modelMock;

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private PostedPurchaseCreditMemoService svcMock;

    @Mock
    private PagingService pagingSvcMock;

    private PostedPurchaseCreditMemoController controller;

    @BeforeEach
    public void setup () {
        MockitoAnnotations.openMocks(this);

        when(svcMock.list()).thenReturn(postedPurchaseCreditMemos);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        when(pagingSvcMock.generate("postedPurchaseCreditMemo", null, TEST_COUNT)).thenReturn(pagingDataMock);

        controller = new PostedPurchaseCreditMemoController(svcMock, pagingSvcMock);
    }

    @Test
    public void test_returnsProperTemplate() {
        String template = controller.list(modelMock);

        assertEquals("postedPurchaseCreditMemoList.html", template);

        verify(modelMock).addAttribute("postedPurchaseCreditMemos", postedPurchaseCreditMemos);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }
}
