package org.roko.erp.frontend.services;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.ItemDTO;
import org.roko.erp.dto.list.ItemList;
import org.roko.erp.frontend.model.Item;
import org.roko.erp.frontend.repositories.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class ItemServiceTest {

    private static final int TEST_PAGE = 2;

    private static final String NON_EXISTING_ITEM_ID = "non-existing-item-id";

    private static final String TEST_ID = "test-id";

    @Captor
    private ArgumentCaptor<Pageable> pageAbleArgumentCaptor;

    @Mock
    private Item itemFromDBMock;

    @Mock
    private ItemDTO itemMock;

    @Mock
    private Item itemMock1;

    @Mock
    private Item itemMock2;

    @Mock
    private Page<Item> pageMock;

    @Mock
    private ItemRepository repoMock;

    @Mock
    private RestTemplate restTemplateMock;

    private ItemService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new ItemServiceImpl(restTemplateMock);
    }

    @Test
    public void create_callsBackend() {
        svc.create(itemMock);

        verify(restTemplateMock).postForObject("/api/v1/items", itemMock, String.class);
    }

    @Test 
    public void update_callsBackend(){
        svc.update(TEST_ID, itemMock);

        verify(restTemplateMock).put("/api/v1/items/{code}", itemMock, TEST_ID);
    }

    @Test 
    public void delete_callsBackend(){
        svc.delete(TEST_ID);

        verify(restTemplateMock).delete("/api/v1/items/{code}", TEST_ID);
    }

    @Test 
    public void get_callsBackend(){
        svc.get(TEST_ID);

        verify(restTemplateMock).getForObject("/api/v1/items/{code}", ItemDTO.class, TEST_ID);
    }

    @Test
    public void getReturnsNull_whenItemNotFound(){
        when(restTemplateMock.getForObject("/api/v1/items/{code}", ItemDTO.class, NON_EXISTING_ITEM_ID)).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        ItemDTO item = svc.get(NON_EXISTING_ITEM_ID);

        assertNull(item);
    }

    @Test 
    public void list_callsBackend(){
        svc.list();

        verify(restTemplateMock).getForObject("/api/v1/items", ItemList.class);
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        svc.list(TEST_PAGE);

        verify(restTemplateMock).getForObject("/api/v1/items/page/{page}", ItemList.class, TEST_PAGE);
    }

}
