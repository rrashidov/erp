package org.roko.erp.frontend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.model.Item;
import org.roko.erp.frontend.model.ItemLedgerEntry;
import org.roko.erp.frontend.services.ItemLedgerEntryService;
import org.roko.erp.frontend.services.ItemService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

public class ItemControllerTest {
    
    private static final String TEST_ITEM_CODE = "test-item-code";
    private static final String TEST_ITEM_NAME = "test-item-name";
    private static final double TEST_ITEM_SALES_PRICE = 12.12;
    private static final double TEST_ITEM_PURCHASE_PRICE = 23.23;

    private static final int TEST_PAGE = 12;
    private static final int TEST_ITEM_COUNT = 123123;
    private static final int TEST_ITEM_LEDGER_ENTRY_COUNT = 123;

    private static final String EXPECTED_ITEM_LIST_TEMPLATE = "itemList.html";
    private static final String EXPECTED_ITEM_CARD_TEMPLATE = "itemCard.html";

    private List<Item> itemListMock = new ArrayList<>();

    private List<ItemLedgerEntry> itemLedgerEntries = new ArrayList<>();

    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;

    @Mock
    private Item itemMock;

    @Mock
    private ItemLedgerEntry itemLedgerEntryMock;

    @Mock
    private Model modelMock;

    @Mock
    private RedirectAttributes redirectAttributesMock;

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private PagingData itemLedgerEntryPagingDataMock;

    @Mock 
    private ItemService itemServiceMock;

    @Mock
    private ItemLedgerEntryService itemLedgerEntrySvcMock;

    @Mock 
    private PagingService pagingServiceMock;

    private ItemController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        itemLedgerEntries = Arrays.asList(itemLedgerEntryMock);

        when(itemMock.getCode()).thenReturn(TEST_ITEM_CODE);
        when(itemMock.getName()).thenReturn(TEST_ITEM_NAME);
        when(itemMock.getSalesPrice()).thenReturn(TEST_ITEM_SALES_PRICE);
        when(itemMock.getPurchasePrice()).thenReturn(TEST_ITEM_PURCHASE_PRICE);

        when(itemServiceMock.list()).thenReturn(itemListMock);
        when(itemServiceMock.count()).thenReturn(TEST_ITEM_COUNT);
        when(itemServiceMock.get(TEST_ITEM_CODE)).thenReturn(itemMock);

        when(itemLedgerEntrySvcMock.list(itemMock, TEST_PAGE)).thenReturn(itemLedgerEntries);
        when(itemLedgerEntrySvcMock.count(itemMock)).thenReturn(TEST_ITEM_LEDGER_ENTRY_COUNT);

        when(pagingServiceMock.generate(eq("item"), anyInt(), eq(TEST_ITEM_COUNT))).thenReturn(pagingDataMock);
        when(pagingServiceMock.generate("itemCard", TEST_ITEM_CODE, TEST_PAGE, TEST_ITEM_LEDGER_ENTRY_COUNT)).thenReturn(itemLedgerEntryPagingDataMock);

        controller = new ItemController(itemServiceMock, pagingServiceMock, itemLedgerEntrySvcMock);
    }

    @Test 
    public void listReturnsProperTemplate(){
        String returnedTemplate = controller.list(0, modelMock);

        assertEquals(EXPECTED_ITEM_LIST_TEMPLATE, returnedTemplate);

        verify(modelMock).addAttribute("items", itemListMock);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }

    @Test
    public void itemCardReturnsProperTemplate(){
        String returnedTemplate = controller.card(null, TEST_PAGE, modelMock);

        assertEquals(EXPECTED_ITEM_CARD_TEMPLATE, returnedTemplate);

        verify(modelMock).addAttribute(eq("item"), any(Item.class));
    }

    @Test
    public void itemCardReturnsProperData_whenCalledWithExistingItem(){
        String returnedTemplate = controller.card(TEST_ITEM_CODE, TEST_PAGE, modelMock);

        assertEquals(EXPECTED_ITEM_CARD_TEMPLATE, returnedTemplate);

        verify(modelMock).addAttribute("item", itemMock);
        verify(modelMock).addAttribute("itemLedgerEntries", itemLedgerEntries);
        verify(modelMock).addAttribute("paging", itemLedgerEntryPagingDataMock);
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
        assertEquals(TEST_ITEM_NAME, createdItem.getName());
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
