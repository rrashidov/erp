package org.roko.erp.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.Item;
import org.roko.erp.backend.repositories.ItemRepository;
import org.roko.erp.dto.ItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ItemServiceTest {

    private static final int TEST_PAGE = 2;

    private static final String TEST_ID = "test-id";
    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";
    private static final BigDecimal TEST_SALES_PRICE = new BigDecimal(123.12);
    private static final BigDecimal TEST_PURCHASE_PRICE = new BigDecimal(23.45);
    private static final BigDecimal TEST_INVENTORY = new BigDecimal(45.67);

    @Captor
    private ArgumentCaptor<Pageable> pageAbleArgumentCaptor;

    @Mock
    private Item itemFromDBMock;

    @Mock
    private Item itemMock;

    @Mock
    private Item itemMock1;

    @Mock
    private Item itemMock2;

    @Mock
    private ItemDTO itemDtoMock;

    @Mock
    private Page<Item> pageMock;

    @Mock
    private ItemRepository repoMock;

    private ItemService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(itemDtoMock.getCode()).thenReturn(TEST_CODE);
        when(itemDtoMock.getName()).thenReturn(TEST_NAME);
        when(itemDtoMock.getSalesPrice()).thenReturn(TEST_SALES_PRICE);
        when(itemDtoMock.getPurchasePrice()).thenReturn(TEST_PURCHASE_PRICE);

        when(itemMock.getCode()).thenReturn(TEST_CODE);
        when(itemMock.getName()).thenReturn(TEST_NAME);
        when(itemMock.getSalesPrice()).thenReturn(TEST_SALES_PRICE);
        when(itemMock.getPurchasePrice()).thenReturn(TEST_PURCHASE_PRICE);
        when(itemMock.getInventory()).thenReturn(TEST_INVENTORY);

        when(pageMock.toList()).thenReturn(Arrays.asList(itemMock, itemMock1, itemMock2));

        when(repoMock.findById(TEST_ID)).thenReturn(Optional.of(itemFromDBMock));
        when(repoMock.findAll()).thenReturn(Arrays.asList(itemMock, itemMock1, itemMock2));
        when(repoMock.findAll(any(Pageable.class))).thenReturn(pageMock);

        svc = new ItemServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo() {
        svc.create(itemMock);

        verify(repoMock).save(itemMock);
    }

    @Test 
    public void update_delegatesToRepo(){
        svc.update(TEST_ID, itemMock);

        verify(repoMock).save(itemFromDBMock);
    }

    @Test 
    public void delete_delegatesToRepo(){
        svc.delete(TEST_ID);

        verify(repoMock).findById(TEST_ID);
        verify(repoMock).delete(itemFromDBMock);
    }

    @Test 
    public void get_delegatesToRepo(){
        Item retrievedItem = svc.get(TEST_ID);

        assertEquals(itemFromDBMock, retrievedItem);

        verify(repoMock).inventory(itemFromDBMock);
    }

    @Test
    public void getReturnsNull_whenItemNotFound(){
        Item item = svc.get("non-existing-item-id");

        assertNull(item);
    }

    @Test 
    public void list_delegatesToRepo(){
        svc.list();

        verify(repoMock).findAll();

        verify(repoMock).inventory(itemMock);
        verify(repoMock).inventory(itemMock1);
        verify(repoMock).inventory(itemMock2);
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        svc.list(TEST_PAGE);

        verify(repoMock).findAll(pageAbleArgumentCaptor.capture());

        Pageable pageable = pageAbleArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(Constants.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo(){
        svc.count();

        verify(repoMock).count();
    }

    @Test
    public void fromDTO_generatesProperResult() {
        Item item = svc.fromDTO(itemDtoMock);

        assertEquals(TEST_CODE, item.getCode());
        assertEquals(TEST_NAME, item.getName());
        assertEquals(TEST_SALES_PRICE, item.getSalesPrice());
        assertEquals(TEST_PURCHASE_PRICE, item.getPurchasePrice());
    }

    @Test
    public void toDTO_generatesProperResult() {
        ItemDTO itemDto = svc.toDTO(itemMock);

        assertEquals(TEST_CODE, itemDto.getCode());
        assertEquals(TEST_NAME, itemDto.getName());
        assertEquals(TEST_SALES_PRICE, itemDto.getSalesPrice());
        assertEquals(TEST_PURCHASE_PRICE, itemDto.getPurchasePrice());
        assertEquals(TEST_INVENTORY, itemDto.getInventory());
    }
}
