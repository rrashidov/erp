package org.roko.erp.services;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.PostedSalesOrder;
import org.roko.erp.repositories.PostedSalesOrderRepository;

public class PostedSalesOrderServiceTest {
    
    private static final String TEST_CODE = "test-code";

    @Mock
    private PostedSalesOrder postedSalesOrderMock;

    @Mock
    private PostedSalesOrderRepository repoMock;

    private PostedSalesOrderService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(postedSalesOrderMock));

        svc = new PostedSalesOrderServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(postedSalesOrderMock);

        verify(repoMock).save(postedSalesOrderMock);
    }

    @Test
    public void get_delegatesToRepo(){
        svc.get(TEST_CODE);

        verify(repoMock).findById(TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenCalledWithNonExistingId(){
        PostedSalesOrder postedSalesOrder = svc.get("non-existing-code");

        assertNull(postedSalesOrder);
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
