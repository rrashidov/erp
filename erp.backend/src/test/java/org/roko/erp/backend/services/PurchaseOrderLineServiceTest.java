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
import org.roko.erp.backend.model.PurchaseOrder;
import org.roko.erp.backend.model.PurchaseOrderLine;
import org.roko.erp.backend.model.jpa.PurchaseOrderLineId;
import org.roko.erp.backend.repositories.PurchaseOrderLineRepository;
import org.roko.erp.dto.PurchaseDocumentLineDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PurchaseOrderLineServiceTest {

    private static final String TEST_PURCHASE_ORDER_CODE = "test-purchase-order-code";

    private static final int TEST_PAGE = 12;

    private static final int TEST_LINE_NO = 123;
    private static final String TEST_ITEM_CODE = "test-item-code";
    private static final String TEST_ITEM_NAME = "test-item-name";
    private static final double TEST_QUANTITY = 12.12;
    private static final double TEST_PRICE = 10.00;
    private static final double TEST_AMOUNT = 121.20;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<PurchaseOrderLine> pageMock;

    @Mock
    private PurchaseOrder purchaseOrderMock;

    @Mock
    private PurchaseOrderLineId nonExistingPurchaseOrderLineId;

    @Mock
    private PurchaseOrderLineId purchaseOrderLineIdMock;

    @Mock
    private PurchaseOrderLine purchaseOrderLineMock;

    @Mock
    private PurchaseOrderLineRepository repoMock;

    @Mock
    private Item itemMock;

    @Mock
    private PurchaseDocumentLineDTO dtoMock;

    @Mock
    private PurchaseOrderService purchaseOrderSvcMock;

    @Mock
    private ItemService itemSvcMock;

    private PurchaseOrderLineService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(itemSvcMock.get(TEST_ITEM_CODE)).thenReturn(itemMock);

        when(purchaseOrderSvcMock.get(TEST_PURCHASE_ORDER_CODE)).thenReturn(purchaseOrderMock);

        when(dtoMock.getPurchaseDocumentCode()).thenReturn(TEST_PURCHASE_ORDER_CODE);
        when(dtoMock.getLineNo()).thenReturn(TEST_LINE_NO);
        when(dtoMock.getItemCode()).thenReturn(TEST_ITEM_CODE);
        when(dtoMock.getQuantity()).thenReturn(TEST_QUANTITY);
        when(dtoMock.getPrice()).thenReturn(TEST_PRICE);
        when(dtoMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(purchaseOrderMock.getCode()).thenReturn(TEST_PURCHASE_ORDER_CODE);

        when(itemMock.getCode()).thenReturn(TEST_ITEM_CODE);
        when(itemMock.getName()).thenReturn(TEST_ITEM_NAME);

        PurchaseOrderLineId purchaseOrderLineId = new PurchaseOrderLineId();
        purchaseOrderLineId.setPurchaseOrder(purchaseOrderMock);
        purchaseOrderLineId.setLineNo(TEST_LINE_NO);

        when(purchaseOrderLineMock.getPurchaseOrderLineId()).thenReturn(purchaseOrderLineId);
        when(purchaseOrderLineMock.getItem()).thenReturn(itemMock);
        when(purchaseOrderLineMock.getQuantity()).thenReturn(TEST_QUANTITY);
        when(purchaseOrderLineMock.getPrice()).thenReturn(TEST_PRICE);
        when(purchaseOrderLineMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(repoMock.findById(purchaseOrderLineIdMock)).thenReturn(Optional.of(purchaseOrderLineMock));
        when(repoMock.listForPurchaseOrder(eq(purchaseOrderMock), any(Pageable.class))).thenReturn(pageMock);

        svc = new PurchaseOrderLineServiceImpl(repoMock, purchaseOrderSvcMock, itemSvcMock);
    }

    @Test
    public void create_delegatesToRepo() {
        svc.create(purchaseOrderLineMock);

        verify(repoMock).save(purchaseOrderLineMock);
    }

    @Test
    public void update_delegatesToRepo() {
        svc.update(purchaseOrderLineIdMock, purchaseOrderLineMock);

        verify(repoMock).findById(purchaseOrderLineIdMock);
        verify(repoMock).save(purchaseOrderLineMock);
    }

    @Test
    public void delete_delegatesToRepo() {
        svc.delete(purchaseOrderLineIdMock);

        verify(repoMock).delete(purchaseOrderLineMock);
    }

    @Test
    public void get_delegatesToRepo() {
        svc.get(purchaseOrderLineIdMock);

        verify(repoMock).findById(purchaseOrderLineIdMock);
    }

    @Test
    public void getReturnsNull_whenCalledForNonExistingId() {
        PurchaseOrderLine purchaseOrderLine = svc.get(nonExistingPurchaseOrderLineId);

        assertNull(purchaseOrderLine);
    }

    @Test
    public void list_delegatesToRepo(){
        svc.list(purchaseOrderMock);

        verify(repoMock).listForPurchaseOrder(purchaseOrderMock);
    }

    @Test
    public void listWithPage_delegatesToRepo(){
        svc.list(purchaseOrderMock, TEST_PAGE);

        verify(repoMock).listForPurchaseOrder(eq(purchaseOrderMock), pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(Constants.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo(){
        svc.count(purchaseOrderMock);

        verify(repoMock).countForPurchaseOrder(purchaseOrderMock);
    }

    @Test
    public void maxLineNo_delegatesToRepo() {
        svc.maxLineNo(purchaseOrderMock);

        verify(repoMock).maxLineNo(purchaseOrderMock);
    }

    @Test
    public void toDTO_returnsProperValue() {
        PurchaseDocumentLineDTO dto = svc.toDTO(purchaseOrderLineMock);

        assertEquals(TEST_PURCHASE_ORDER_CODE, dto.getPurchaseDocumentCode());
        assertEquals(TEST_LINE_NO, dto.getLineNo());
        assertEquals(TEST_ITEM_CODE, dto.getItemCode());
        assertEquals(TEST_ITEM_NAME, dto.getItemName());
        assertEquals(TEST_QUANTITY, dto.getQuantity());
        assertEquals(TEST_PRICE, dto.getPrice());
        assertEquals(TEST_AMOUNT, dto.getAmount());
    }

    @Test
    public void fromDTO_returnsProperValue() {
        PurchaseOrderLine purchaseOrderLine = svc.fromDTO(dtoMock);

        assertEquals(purchaseOrderMock, purchaseOrderLine.getPurchaseOrderLineId().getPurchaseOrder());
        assertEquals(TEST_LINE_NO, purchaseOrderLine.getPurchaseOrderLineId().getLineNo());
        assertEquals(itemMock, purchaseOrderLine.getItem());
        assertEquals(TEST_QUANTITY, purchaseOrderLine.getQuantity());
        assertEquals(TEST_PRICE, purchaseOrderLine.getPrice());
        assertEquals(TEST_AMOUNT, purchaseOrderLine.getAmount());
    }
}
