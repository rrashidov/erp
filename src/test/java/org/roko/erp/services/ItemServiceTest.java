package org.roko.erp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.Item;
import org.roko.erp.repositories.ItemRepository;

public class ItemServiceTest {

    private static final String TEST_ID = "test-id";

    @Mock
    private Item itemFromDBMock;

    @Mock
    private Item itemMock;

    @Mock
    private ItemRepository repoMock;

    private ItemService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(repoMock.findById(TEST_ID)).thenReturn(Optional.of(itemFromDBMock));

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
    }

    @Test
    public void count_delegatesToRepo(){
        svc.count();

        verify(repoMock).count();
    }
}
