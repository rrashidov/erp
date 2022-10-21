package org.roko.erp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.Item;
import org.roko.erp.services.ItemService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

public class ItemControllerTest {
    
    private static final String TEST_ITEM_CODE = "test-item-code";
    private static final String TEST_ITEM_DESCRIPTION = "test-item-description";
    private static final double TEST_ITEM_SALES_PRICE = 12.12;
    private static final double TEST_ITEM_PURCHASE_PRICE = 23.23;

    private static final long TEST_ITEM_COUNT = 123123;

    private static final String EXPECTED_ITEM_LIST_TEMPLATE = "itemList.html";
    private static final String EXPECTED_ITEM_CARD_TEMPLATE = "itemCard.html";

    private List<Item> itemListMock = new ArrayList<>();

    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;

    @Mock
    private Item itemMock;

    @Mock
    private Model modelMock;

    @Mock
    private RedirectAttributes redirectAttributesMock;

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

        when(itemMock.getCode()).thenReturn(TEST_ITEM_CODE);
        when(itemMock.getDescription()).thenReturn(TEST_ITEM_DESCRIPTION);
        when(itemMock.getSalesPrice()).thenReturn(TEST_ITEM_SALES_PRICE);
        when(itemMock.getPurchasePrice()).thenReturn(TEST_ITEM_PURCHASE_PRICE);

        when(itemServiceMock.list()).thenReturn(itemListMock);
        when(itemServiceMock.count()).thenReturn(TEST_ITEM_COUNT);
        when(itemServiceMock.get(TEST_ITEM_CODE)).thenReturn(itemMock);

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

    @Test
    public void itemCardReturnsProperTemplate(){
        String returnedTemplate = controller.card(null, modelMock);

        assertEquals(EXPECTED_ITEM_CARD_TEMPLATE, returnedTemplate);

        verify(modelMock).addAttribute(eq("item"), any(Item.class));
    }

    @Test
    public void itemCardReturnsProperData_whenCalledWithExistingItem(){
        String returnedTemplate = controller.card(TEST_ITEM_CODE, modelMock);

        assertEquals(EXPECTED_ITEM_CARD_TEMPLATE, returnedTemplate);

        verify(modelMock).addAttribute("item", itemMock);
    }

    @Test
    public void postingItemCard_createsItem_ifDoesNotExist(){
        // mock that the item does not exist
        when(itemServiceMock.get(TEST_ITEM_CODE)).thenReturn(null);

        RedirectView redirect = controller.postCard(itemMock, modelMock, redirectAttributesMock);

        assertEquals("/itemList", redirect.getUrl());

        verify(itemServiceMock).get(TEST_ITEM_CODE);
        verify(itemServiceMock).create(itemArgumentCaptor.capture());

        Item createdItem = itemArgumentCaptor.getValue();

        assertEquals(TEST_ITEM_CODE, createdItem.getCode());
        assertEquals(TEST_ITEM_DESCRIPTION, createdItem.getDescription());
        assertEquals(TEST_ITEM_SALES_PRICE, createdItem.getSalesPrice());
        assertEquals(TEST_ITEM_PURCHASE_PRICE, createdItem.getPurchasePrice());
    }

    @Test
    public void deletingItem_deletesItem(){
        RedirectView redirect = controller.delete(TEST_ITEM_CODE);

        verify(itemServiceMock).delete(TEST_ITEM_CODE);

        assertEquals("/itemList", redirect.getUrl());
    }
}
