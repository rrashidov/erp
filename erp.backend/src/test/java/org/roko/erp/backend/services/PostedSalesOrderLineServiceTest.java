package org.roko.erp.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.Item;
import org.roko.erp.backend.model.PostedSalesOrder;
import org.roko.erp.backend.model.PostedSalesOrderLine;
import org.roko.erp.backend.model.jpa.PostedSalesOrderLineId;
import org.roko.erp.backend.repositories.PostedSalesOrderLineRepository;
import org.roko.erp.model.dto.PostedSalesDocumentLineDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PostedSalesOrderLineServiceTest {

    private static final String TEST_POSTED_SALES_ORDER_CODE = "test-posted-sales-order-code";

    private static final String TEST_ITEM_CODE = "test-item-code";
    private static final String TEST_ITEM_NAME = "test-item-name";
    private static final double TEST_QUANTITY = 12.12;
    private static final double TEST_PRICE = 23.45;
    private static final double TEST_AMOUNT = 123.23;

    private static final int TEST_PAGE = 12;

    private static final int TEST_LINE_NO = 123;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<PostedSalesOrderLine> pageMock;

    @Mock
    private PostedSalesOrder postedSalesOrderMock;

    @Mock
    private PostedSalesOrderLine postedSalesOrderLineMock;

    @Mock
    private PostedSalesOrderLineRepository repoMock;

    @Mock
    private Item itemMock;

    private PostedSalesOrderLineService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(postedSalesOrderMock.getCode()).thenReturn(TEST_POSTED_SALES_ORDER_CODE);

        when(itemMock.getCode()).thenReturn(TEST_ITEM_CODE);
        when(itemMock.getName()).thenReturn(TEST_ITEM_NAME);

        PostedSalesOrderLineId postedSAlesOrderLineId = new PostedSalesOrderLineId();
        postedSAlesOrderLineId.setPostedSalesOrder(postedSalesOrderMock);
        postedSAlesOrderLineId.setLineNo(TEST_LINE_NO);
        
        when(postedSalesOrderLineMock.getPostedSalesOrderLineId()).thenReturn(postedSAlesOrderLineId);
        when(postedSalesOrderLineMock.getItem()).thenReturn(itemMock);
        when(postedSalesOrderLineMock.getQuantity()).thenReturn(TEST_QUANTITY);
        when(postedSalesOrderLineMock.getPrice()).thenReturn(TEST_PRICE);
        when(postedSalesOrderLineMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(repoMock.findFor(eq(postedSalesOrderMock), any(Pageable.class))).thenReturn(pageMock);

        svc = new PostedSalesOrderLineServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo() {
        svc.create(postedSalesOrderLineMock);

        verify(repoMock).save(postedSalesOrderLineMock);
    }

    @Test
    public void list_delegatesToRepo() {
        svc.list(postedSalesOrderMock);

        verify(repoMock).findFor(postedSalesOrderMock);
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        svc.list(postedSalesOrderMock, TEST_PAGE);

        verify(repoMock).findFor(eq(postedSalesOrderMock), pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(Constants.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo() {
        svc.count(postedSalesOrderMock);

        verify(repoMock).count(postedSalesOrderMock);
    }

    @Test
    public void toDTO_returnsProperValue() {
        PostedSalesDocumentLineDTO dto = svc.toDTO(postedSalesOrderLineMock);

        assertEquals(TEST_POSTED_SALES_ORDER_CODE, dto.getSalesDocumentCode());
        assertEquals(TEST_LINE_NO, dto.getLineNo());
        assertEquals(TEST_ITEM_CODE, dto.getItemCode());
        assertEquals(TEST_ITEM_NAME, dto.getItemName());
        assertEquals(TEST_QUANTITY, dto.getQuantity());
        assertEquals(TEST_PRICE, dto.getPrice());
        assertEquals(TEST_AMOUNT, dto.getAmount());
    }
}
