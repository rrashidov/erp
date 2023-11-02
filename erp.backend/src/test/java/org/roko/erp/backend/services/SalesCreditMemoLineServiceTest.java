package org.roko.erp.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.Item;
import org.roko.erp.backend.model.SalesCreditMemo;
import org.roko.erp.backend.model.SalesCreditMemoLine;
import org.roko.erp.backend.model.jpa.SalesCreditMemoLineId;
import org.roko.erp.backend.repositories.SalesCreditMemoLineRepository;
import org.roko.erp.dto.SalesDocumentLineDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class SalesCreditMemoLineServiceTest {
    
    private static final String TEST_SALES_CREDIT_MEMO_CODE = "test-sales-credit-memo-code";
    private static final int TEST_LINE_NO = 123;
    private static final String TEST_ITEM_CODE = "test-item-code";
    private static final String TEST_ITEM_NAME = "test-item-name";

    private static final int TEST_PAGE = 12;

    private static final BigDecimal TEST_QTY = new BigDecimal(10);
    private static final BigDecimal TEST_PRICE = new BigDecimal(12);
    private static final BigDecimal TEST_AMOUNT = new BigDecimal(120);

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<SalesCreditMemoLine> pageMock;

    @Mock
    private Item itemMock;

    @Mock
    private SalesCreditMemoLineId salesCreditMemoLineIdMock;

    @Mock
    private SalesCreditMemoLineId nonExistingSalesCreditMemoLineIdMock;

    @Mock
    private SalesCreditMemoLine salesCreditMemoLineMock;

    @Mock
    private SalesCreditMemoLine salesCreditMemoLineMockToUpdate;

    @Mock
    private SalesCreditMemo salesCreditMemoMock;

    @Mock
    private SalesCreditMemoLineRepository repoMock;

    @Mock
    private SalesDocumentLineDTO dtoMock;

    @Mock
    private ItemService itemSvcMock;

    private SalesCreditMemoLineService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(salesCreditMemoMock.getCode()).thenReturn(TEST_SALES_CREDIT_MEMO_CODE);

        when(itemMock.getCode()).thenReturn(TEST_ITEM_CODE);
        when(itemMock.getName()).thenReturn(TEST_ITEM_NAME);

        SalesCreditMemoLineId salesCreditMemoLineId = new SalesCreditMemoLineId();
        salesCreditMemoLineId.setSalesCreditMemo(salesCreditMemoMock);
        salesCreditMemoLineId.setLineNo(TEST_LINE_NO);

        when(salesCreditMemoLineMock.getSalesCreditMemoLineId()).thenReturn(salesCreditMemoLineId);
        when(salesCreditMemoLineMock.getItem()).thenReturn(itemMock);
        when(salesCreditMemoLineMock.getQuantity()).thenReturn(TEST_QTY);
        when(salesCreditMemoLineMock.getPrice()).thenReturn(TEST_PRICE);
        when(salesCreditMemoLineMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(itemSvcMock.get(TEST_ITEM_CODE)).thenReturn(itemMock);

        when(dtoMock.getSalesDocumentCode()).thenReturn(TEST_SALES_CREDIT_MEMO_CODE);
        when(dtoMock.getLineNo()).thenReturn(TEST_LINE_NO);
        when(dtoMock.getItemCode()).thenReturn(TEST_ITEM_CODE);
        when(dtoMock.getQuantity()).thenReturn(TEST_QTY);
        when(dtoMock.getPrice()).thenReturn(TEST_PRICE);
        when(dtoMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(salesCreditMemoLineMockToUpdate.getItem()).thenReturn(itemMock);
        when(salesCreditMemoLineMockToUpdate.getQuantity()).thenReturn(TEST_QTY);
        when(salesCreditMemoLineMockToUpdate.getPrice()).thenReturn(TEST_PRICE);
        when(salesCreditMemoLineMockToUpdate.getAmount()).thenReturn(TEST_AMOUNT);

        when(repoMock.findById(salesCreditMemoLineIdMock)).thenReturn(Optional.of(salesCreditMemoLineMock));
        when(repoMock.findForSalesCreditMemo(eq(salesCreditMemoMock), any(Pageable.class))).thenReturn(pageMock);

        svc = new SalesCreditMemoLineServiceImpl(repoMock, itemSvcMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(salesCreditMemoLineMock);

        verify(repoMock).save(salesCreditMemoLineMock);
    }

    @Test
    public void update_delegatesToRepo(){
        svc.update(salesCreditMemoLineIdMock, salesCreditMemoLineMockToUpdate);

        verify(repoMock).findById(salesCreditMemoLineIdMock);
        verify(repoMock).save(salesCreditMemoLineMock);

        verify(salesCreditMemoLineMock).setItem(itemMock);
        verify(salesCreditMemoLineMock).setQuantity(TEST_QTY);
        verify(salesCreditMemoLineMock).setPrice(TEST_PRICE);
        verify(salesCreditMemoLineMock).setAmount(TEST_AMOUNT);
    }

    @Test
    public void delete_delegatesToRepo(){
        svc.delete(salesCreditMemoLineIdMock);

        verify(repoMock).delete(salesCreditMemoLineMock);
    }

    @Test
    public void get_delegatesToRepo(){
        svc.get(salesCreditMemoLineIdMock);

        verify(repoMock).findById(salesCreditMemoLineIdMock);
    }

    @Test
    public void getReturnsNull_whenCalledForNonExisting(){
        SalesCreditMemoLine salesCreditMemoLine = svc.get(nonExistingSalesCreditMemoLineIdMock);

        assertNull(salesCreditMemoLine);
    }

    @Test
    public void list_delegatesToRepo(){
        svc.list(salesCreditMemoMock);

        verify(repoMock).findForSalesCreditMemo(salesCreditMemoMock);
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        svc.list(salesCreditMemoMock, TEST_PAGE);

        verify(repoMock).findForSalesCreditMemo(eq(salesCreditMemoMock), pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(Constants.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo(){
        svc.count(salesCreditMemoMock);

        verify(repoMock).countForSalesCreditMemo(salesCreditMemoMock);
    }

    @Test
    public void maxLineNo_delegatesToRepo() {
        svc.maxLineNo(salesCreditMemoMock);

        verify(repoMock).maxLineNo(salesCreditMemoMock);
    }

    @Test
    public void fromDTO_returnsProperResult() {
        SalesCreditMemoLine salesCreditMemoLine = svc.fromDTO(dtoMock);

        assertEquals(itemMock, salesCreditMemoLine.getItem());
        assertEquals(TEST_QTY, salesCreditMemoLine.getQuantity());
        assertEquals(TEST_PRICE, salesCreditMemoLine.getPrice());
        assertEquals(TEST_AMOUNT, salesCreditMemoLine.getAmount());
    }

    @Test
    public void toDTO_returnsProperResult() {
        SalesDocumentLineDTO dto = svc.toDTO(salesCreditMemoLineMock);

        assertEquals(TEST_SALES_CREDIT_MEMO_CODE, dto.getSalesDocumentCode());
        assertEquals(TEST_LINE_NO, dto.getLineNo());
        assertEquals(TEST_ITEM_CODE, dto.getItemCode());
        assertEquals(TEST_ITEM_NAME, dto.getItemName());
        assertEquals(TEST_QTY, dto.getQuantity());
        assertEquals(TEST_PRICE, dto.getPrice());
        assertEquals(TEST_AMOUNT, dto.getAmount());
    }
}
