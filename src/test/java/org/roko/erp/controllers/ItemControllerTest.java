package org.roko.erp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.Item;
import org.roko.erp.services.ItemService;
import org.springframework.ui.Model;

public class ItemControllerTest {
    
    private static final String EXPECTED_ITEM_LIST_TEMPLATE = "itemList.html";

    private List<Item> itemListMock;

    @Mock
    private Model modelMock;

    @Mock 
    private ItemService itemServiceMock;

    private ItemController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(itemServiceMock.list()).thenReturn(itemListMock);

        controller = new ItemController(itemServiceMock);
    }

    @Test 
    public void listReturnsProperTemplate(){
        String returnedTemplate = controller.list(0, modelMock);

        assertEquals(EXPECTED_ITEM_LIST_TEMPLATE, returnedTemplate);

        verify(modelMock).addAttribute("items", itemListMock);
    }
}
