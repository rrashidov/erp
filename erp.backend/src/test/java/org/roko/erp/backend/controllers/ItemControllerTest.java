package org.roko.erp.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.roko.erp.backend.model.ItemLedgerEntry;
import org.roko.erp.backend.services.ItemLedgerEntryService;
import org.roko.erp.backend.services.ItemService;

import org.roko.erp.dto.ItemDTO;
import org.roko.erp.dto.ItemLedgerEntryDTO;
import org.roko.erp.dto.list.ItemLedgerEntryList;
import org.roko.erp.dto.list.ItemList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

public class ItemControllerTest {
    
    private static final String NON_EXISTING_CODE = "non-existing-code";
    
    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";
    private static final double TEST_SALES_PRICE = 12.12;
    private static final double TEST_PURCHASE_PRICE = 34.56;

    private static final int TEST_PAGE = 12;

    private static final int TEST_COUNT = 222;

    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;

    @Mock
    private Item itemMock;

    @Mock
    private ItemLedgerEntry itemLedgerEntryMock;

    @Mock
    private ItemDTO itemDtoMock;

    @Mock
    private ItemService svcMock;

    @Mock
    private ItemLedgerEntryService itemLedgerEntrySvcMock;

    @Mock
    private ItemLedgerEntryDTO itemLedgerEntryDtoMock;
    
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
        when(svcMock.toDTO(itemMock)).thenReturn(itemDtoMock);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        when(itemDtoMock.getCode()).thenReturn(TEST_CODE);
        when(itemDtoMock.getName()).thenReturn(TEST_NAME);
        when(itemDtoMock.getSalesPrice()).thenReturn(TEST_SALES_PRICE);
        when(itemDtoMock.getPurchasePrice()).thenReturn(TEST_PURCHASE_PRICE);

        when(itemLedgerEntrySvcMock.list(itemMock, TEST_PAGE)).thenReturn(Arrays.asList(itemLedgerEntryMock));
        when(itemLedgerEntrySvcMock.toDTO(itemLedgerEntryMock)).thenReturn(itemLedgerEntryDtoMock);
        when(itemLedgerEntrySvcMock.count(itemMock)).thenReturn(TEST_COUNT);

        controller = new ItemController(svcMock, itemLedgerEntrySvcMock);
    }

    @Test
    public void list_delegatesToSvc(){
        ItemList list = controller.list();

        assertEquals(itemDtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void listWithPage_delegatesToSvc() {
        ItemList list = controller.list(TEST_PAGE);

        assertEquals(itemDtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void listLedgerEntries_delegatesToSvc() {
        ItemLedgerEntryList list = controller.ledgerEntries(TEST_CODE, TEST_PAGE);

        assertEquals(itemLedgerEntryDtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void get_delegatesToSvc() {
        controller.get(TEST_CODE);

        verify(svcMock).get(TEST_CODE);
        verify(svcMock).toDTO(itemMock);
    }

    @Test
    public void get_throwsException_whenCalledWithNonExistingCode() {
        assertThrows(ResponseStatusException.class, () -> {
            controller.get(NON_EXISTING_CODE);
        });
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
        ResponseEntity<String> response = controller.delete(TEST_CODE);

        verify(svcMock).delete(TEST_CODE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(TEST_CODE, response.getBody());
    }
}
