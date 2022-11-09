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
import org.roko.erp.model.PurchaseCreditMemo;
import org.roko.erp.services.PurchaseCreditMemoService;
import org.springframework.ui.Model;

public class PurchaseCreditMemoControllerTest {
    
    private static final Long TEST_PAGE = 123l;

    private static final long TEST_COUNT = 234l;

    private List<PurchaseCreditMemo> purchaseCreditMemos = new ArrayList<>();

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private PagingService pagingSvcMock;

    @Mock
    private Model modelMock;

    @Mock
    private PurchaseCreditMemoService svcMock;

    private PurchaseCreditMemoController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(svcMock.list()).thenReturn(purchaseCreditMemos);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        when(pagingSvcMock.generate("purchaseCreditMemo", TEST_PAGE, TEST_COUNT)).thenReturn(pagingDataMock);

        controller = new PurchaseCreditMemoController(svcMock, pagingSvcMock);
    }

    @Test
    public void list_returnsProperTemplate(){
        String template = controller.list(TEST_PAGE, modelMock);

        assertEquals("purchaseCreditMemoList.html", template);

        verify(modelMock).addAttribute("purchaseCreditMemos", purchaseCreditMemos);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }
}
