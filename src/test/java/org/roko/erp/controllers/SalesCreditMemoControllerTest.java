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
import org.roko.erp.model.SalesCreditMemo;
import org.roko.erp.services.SalesCreditMemoService;
import org.springframework.ui.Model;

public class SalesCreditMemoControllerTest {

    private static final Long TEST_PAGE = 123l;

    private static final Long TEST_COUNT = 234l;

    private List<SalesCreditMemo> salesCreditMemos = new ArrayList<>();

    @Mock
    private Model modelMock;

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private SalesCreditMemoService svcMock;

    @Mock
    private PagingService pagingSvcMock;

    private SalesCreditMemoController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(svcMock.list()).thenReturn(salesCreditMemos);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        when(pagingSvcMock.generate("salesCreditMemo", TEST_PAGE, TEST_COUNT)).thenReturn(pagingDataMock);

        controller = new SalesCreditMemoController(svcMock, pagingSvcMock);
    }

    @Test
    public void listReturnsProperTemplate(){
        String template = controller.list(TEST_PAGE, modelMock);

        assertEquals("salesCreditMemoList.html", template);

        verify(modelMock).addAttribute("salesCreditMemos", salesCreditMemos);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }
}
