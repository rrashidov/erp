package org.roko.erp.services;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.SalesOrder;
import org.roko.erp.repositories.SalesOrderRepository;

public class SalesOrderServiceTest {

    private static final String NON_EXISTING_CODE = "non-existing-code";

    private static final String TEST_CODE = "test-code";

    @Mock
	private SalesOrder salesOrderMock;

    @Mock
    private SalesOrderRepository repoMock;
    
    private SalesOrderService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        
        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(salesOrderMock));

        svc = new SalesOrderServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(salesOrderMock);

        verify(repoMock).save(salesOrderMock);
    }

    @Test
    public void update_delegatesToRepo(){
        svc.update(TEST_CODE, salesOrderMock);

        verify(repoMock).findById(TEST_CODE);
        verify(repoMock).save(salesOrderMock);
    }

    @Test
    public void delete_delegatesToRepo(){
        svc.delete(TEST_CODE);

        verify(repoMock).delete(salesOrderMock);
    }

    @Test
    public void get_delegatesToRepo(){
        svc.get(TEST_CODE);

        verify(repoMock).findById(TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenEntityNotFound(){
        SalesOrder salesOrder = svc.get(NON_EXISTING_CODE);

        assertNull(salesOrder);
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
