package org.roko.erp.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.Item;
import org.roko.erp.backend.model.SalesOrder;
import org.roko.erp.backend.model.SalesOrderLine;
import org.roko.erp.backend.model.jpa.SalesOrderLineId;
import org.roko.erp.backend.repositories.SalesOrderLineRepository;
import org.roko.erp.model.dto.SalesOrderLineDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class SalesOrderLineServiceTest {

    private static final String TEST_SALES_ORDER_CODE = "test-sales-order-code";
    private static final int TEST_LINE_NO = 123;
    private static final String TEST_ITEM_CODE = "test-item-code";
    private static final String TEST_ITEM_NAME = "test-item-name";

    private static final double TEST_PRICE = 123.23;
    private static final double TEST_QUANTITY = 23.00;
    private static final double TEST_AMOUNT = 23.34;
    
    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<SalesOrderLine> pageMock;

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
    private SalesOrderLineDTO salesOrderLineDtoMock;

    @Mock
    private SalesOrderLine existingSalesOrderLineMock;

    @Mock
    private SalesOrderLineRepository repoMock;

    @Mock
    private SalesOrderService salesOrderSvcMock;

    @Mock
    private ItemService itemSvcMock;
    
    private SalesOrderLineService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(salesOrderMock.getCode()).thenReturn(TEST_SALES_ORDER_CODE);

        when(itemMock.getCode()).thenReturn(TEST_ITEM_CODE);
        when(itemMock.getName()).thenReturn(TEST_ITEM_NAME);

        when(salesOrderSvcMock.get(TEST_SALES_ORDER_CODE)).thenReturn(salesOrderMock);

        when(itemSvcMock.get(TEST_ITEM_CODE)).thenReturn(itemMock);

        when(salesOrderLineDtoMock.getSalesOrderCode()).thenReturn(TEST_SALES_ORDER_CODE);
        when(salesOrderLineDtoMock.getLineNo()).thenReturn(TEST_LINE_NO);
        when(salesOrderLineDtoMock.getItemCode()).thenReturn(TEST_ITEM_CODE);
        when(salesOrderLineDtoMock.getPrice()).thenReturn(TEST_PRICE);
        when(salesOrderLineDtoMock.getQuantity()).thenReturn(TEST_QUANTITY);
        when(salesOrderLineDtoMock.getAmount()).thenReturn(TEST_AMOUNT);

        SalesOrderLineId salesOrderLineId = new SalesOrderLineId();
        salesOrderLineId.setSalesOrder(salesOrderMock);
        salesOrderLineId.setLineNo(TEST_LINE_NO);
        when(salesOrderLineMock.getSalesOrderLineId()).thenReturn(salesOrderLineId);
        when(salesOrderLineMock.getSalesOrder()).thenReturn(salesOrderMock);
        when(salesOrderLineMock.getItem()).thenReturn(itemMock);
        when(salesOrderLineMock.getPrice()).thenReturn(TEST_PRICE);
        when(salesOrderLineMock.getQuantity()).thenReturn(TEST_QUANTITY);
        when(salesOrderLineMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(repoMock.findById(salesOrderLineIdMock)).thenReturn(Optional.of(existingSalesOrderLineMock));
        when(repoMock.findForSalesOrder(eq(salesOrderMock), any(Pageable.class))).thenReturn(pageMock);

        svc = new SalesOrderLineServiceImpl(repoMock, salesOrderSvcMock, itemSvcMock);
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
    public void listWithPage_delegatesToRepo() {
        svc.list(salesOrderMock, TEST_PAGE);

        verify(repoMock).findForSalesOrder(eq(salesOrderMock), pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(Constants.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void couunt_delegatesToRepo(){
        svc.count(salesOrderMock);

        verify(repoMock).countForSalesOrder(salesOrderMock);
    }

    @Test
    public void maxLineNo_delegatesToRepo() {
        svc.maxLineNo(salesOrderMock);

        verify(repoMock).maxLineNo(salesOrderMock);
    }

    @Test
    public void fromDTO_returnsProperValue(){
        SalesOrderLine salesOrderLine = svc.fromDTO(salesOrderLineDtoMock);

        assertEquals(salesOrderMock, salesOrderLine.getSalesOrderLineId().getSalesOrder());
        assertEquals(TEST_LINE_NO, salesOrderLine.getSalesOrderLineId().getLineNo());
        assertEquals(itemMock, salesOrderLine.getItem());
        assertEquals(TEST_PRICE, salesOrderLine.getPrice());
        assertEquals(TEST_QUANTITY, salesOrderLine.getQuantity());
        assertEquals(TEST_AMOUNT, salesOrderLine.getAmount());
    }

    @Test
    public void toDTO_returnsProperValue() {
        SalesOrderLineDTO dto = svc.toDTO(salesOrderLineMock);

        assertEquals(TEST_SALES_ORDER_CODE, dto.getSalesOrderCode());
        assertEquals(TEST_LINE_NO, dto.getLineNo());
        assertEquals(TEST_ITEM_CODE, dto.getItemCode());
        assertEquals(TEST_ITEM_NAME, dto.getItemName());
        assertEquals(TEST_QUANTITY, dto.getQuantity());
        assertEquals(TEST_PRICE, dto.getPrice());
        assertEquals(TEST_AMOUNT, dto.getAmount());
    }
}
