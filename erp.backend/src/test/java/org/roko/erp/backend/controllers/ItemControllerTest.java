package org.roko.erp.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.roko.erp.backend.model.Item;
import org.roko.erp.backend.services.ItemService;

import org.roko.erp.model.dto.ItemDTO;

public class ItemControllerTest {
    
    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";
    private static final double TEST_SALES_PRICE = 12.12;
    private static final double TEST_PURCHASE_PRICE = 34.56;

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;

    @Mock
    private Item itemMock;

    @Mock
    private ItemDTO itemDtoMock;

    @Mock
    private ItemService svcMock;

    private ItemController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(itemMock.getCode()).thenReturn(TEST_CODE);
        when(itemMock.getName()).thenReturn(TEST_NAME);
        when(itemMock.getSalesPrice()).thenReturn(TEST_SALES_PRICE);
        when(itemMock.getPurchasePrice()).thenReturn(TEST_PURCHASE_PRICE);

        when(svcMock.list()).thenReturn(Arrays.asList(itemMock));
        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(itemMock));
        when(svcMock.get(TEST_CODE)).thenReturn(itemMock);
        when(svcMock.fromDTO(itemDtoMock)).thenReturn(itemMock);

        when(itemDtoMock.getCode()).thenReturn(TEST_CODE);
        when(itemDtoMock.getName()).thenReturn(TEST_NAME);
        when(itemDtoMock.getSalesPrice()).thenReturn(TEST_SALES_PRICE);
        when(itemDtoMock.getPurchasePrice()).thenReturn(TEST_PURCHASE_PRICE);

        controller = new ItemController(svcMock);
    }

    @Test
    public void list_delegatesToSvc(){
        controller.list();

        verify(svcMock).list();

        verify(svcMock).toDTO(itemMock);
    }

    @Test
    public void listWithPage_delegatesToSvc() {
        controller.list(TEST_PAGE);

        verify(svcMock).list(TEST_PAGE);
        verify(svcMock).toDTO(itemMock);
    }

    @Test
    public void get_delegatesToSvc() {
        controller.get(TEST_CODE);

        verify(svcMock).get(TEST_CODE);
        verify(svcMock).toDTO(itemMock);
    }

    @Test
    public void post_delegatesToSvc() {
        controller.post(itemDtoMock);

        verify(svcMock).create(itemArgumentCaptor.capture());

        Item item = itemArgumentCaptor.getValue();

        assertEquals(TEST_CODE, item.getCode());
        assertEquals(TEST_NAME, item.getName());
        assertEquals(TEST_SALES_PRICE, item.getSalesPrice());
        assertEquals(TEST_PURCHASE_PRICE, item.getPurchasePrice());
    }

    @Test
    public void put_delegatesToSvc() {
        controller.put(TEST_CODE, itemDtoMock);

        verify(svcMock).update(TEST_CODE, itemMock);
    }

    @Test
    public void delete_delegatesToRepo() {
        controller.delete(TEST_CODE);

        verify(svcMock).delete(TEST_CODE);
    }
}
