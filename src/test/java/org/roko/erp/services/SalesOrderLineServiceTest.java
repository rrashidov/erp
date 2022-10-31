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
import org.roko.erp.model.SalesOrder;
import org.roko.erp.model.SalesOrderLine;
import org.roko.erp.model.jpa.SalesOrderLineId;
import org.roko.erp.repositories.SalesOrderLineRepository;

public class SalesOrderLineServiceTest {

    private static final double TEST_PRICE = 123.23;
    private static final double TEST_QUANTITY = 23.00;
    private static final double TEST_AMOUNT = 23.34;

    @Mock
    private SalesOrder salesOrderMock;

    @Mock
    private SalesOrderLineId nonExistingSalesOrderLineId;

    @Mock
    private Item itemMock;

    @Mock
    private SalesOrderLineId salesOrderLineIdMock;

    @Mock
    private SalesOrderLine salesOrderLineMock;

    @Mock
    private SalesOrderLine existingSalesOrderLineMock;

    @Mock
    private SalesOrderLineRepository repoMock;
    
    private SalesOrderLineService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(salesOrderLineMock.getItem()).thenReturn(itemMock);
        when(salesOrderLineMock.getPrice()).thenReturn(TEST_PRICE);
        when(salesOrderLineMock.getQuantity()).thenReturn(TEST_QUANTITY);
        when(salesOrderLineMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(repoMock.findById(salesOrderLineIdMock)).thenReturn(Optional.of(existingSalesOrderLineMock));

        svc = new SalesOrderLineServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(salesOrderLineMock);

        verify(repoMock).save(salesOrderLineMock);
    }

    @Test
    public void update_delegatesToRepo(){
        svc.update(salesOrderLineIdMock, salesOrderLineMock);

        verify(repoMock).findById(salesOrderLineIdMock);
        verify(repoMock).save(existingSalesOrderLineMock);

        verify(existingSalesOrderLineMock).setItem(itemMock);
        verify(existingSalesOrderLineMock).setQuantity(TEST_QUANTITY);
        verify(existingSalesOrderLineMock).setPrice(TEST_PRICE);
        verify(existingSalesOrderLineMock).setAmount(TEST_AMOUNT);
    }

    @Test
    public void delete_delegatesToRepo(){
        svc.delete(salesOrderLineIdMock);

        verify(repoMock).delete(existingSalesOrderLineMock);
    }

    @Test
    public void get_delegatesToRepo(){
        SalesOrderLine salesOrderLine = svc.get(salesOrderLineIdMock);

        assertEquals(existingSalesOrderLineMock, salesOrderLine);
    }

    @Test
    public void getReturnsNull_whenNotFound(){
        SalesOrderLine salesOrderLine = svc.get(nonExistingSalesOrderLineId);

        assertNull(salesOrderLine);
    }

    @Test
    public void list_delegatesToRepo(){
        svc.list(salesOrderMock);

        verify(repoMock).findForSalesOrder(salesOrderMock);
    }

    @Test
    public void couunt_delegatesToRepo(){
        svc.count(salesOrderMock);

        verify(repoMock).countForSalesOrder(salesOrderMock);
    }
}
