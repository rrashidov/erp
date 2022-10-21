package org.roko.erp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
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
import org.roko.erp.model.Item;
import org.roko.erp.services.ItemService;
import org.springframework.ui.Model;

public class ItemControllerTest {
    
    private static final long TEST_ITEM_COUNT = 123123;

    private static final String EXPECTED_ITEM_LIST_TEMPLATE = "itemList.html";

    private List<Item> itemListMock = new ArrayList<>();

    @Mock
    private Model modelMock;

    @Mock
    private PagingData pagingDataMock;

    @Mock 
    private ItemService itemServiceMock;

    @Mock 
    private PagingService pagingServiceMock;

    private ItemController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(itemServiceMock.list()).thenReturn(itemListMock);
        when(itemServiceMock.count()).thenReturn(TEST_ITEM_COUNT);

        when(pagingServiceMock.generate(eq("item"), anyLong(), eq(TEST_ITEM_COUNT))).thenReturn(pagingDataMock);

        controller = new ItemController(itemServiceMock, pagingServiceMock);
    }

    @Test 
    public void listReturnsProperTemplate(){
        String returnedTemplate = controller.list(0l, modelMock);

        assertEquals(EXPECTED_ITEM_LIST_TEMPLATE, returnedTemplate);

        verify(modelMock).addAttribute("items", itemListMock);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }
}
