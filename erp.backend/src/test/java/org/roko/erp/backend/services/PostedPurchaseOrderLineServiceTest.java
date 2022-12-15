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
import org.roko.erp.backend.model.PostedPurchaseOrder;
import org.roko.erp.backend.model.PostedPurchaseOrderLine;
import org.roko.erp.backend.model.jpa.PostedPurchaseOrderLineId;
import org.roko.erp.backend.repositories.PostedPurchaseOrderLineRepository;
import org.roko.erp.dto.PostedPurchaseDocumentLineDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PostedPurchaseOrderLineServiceTest {
    
    private static final String TEST_CODE = "test-code";

    private static final String TEST_ITEM_CODE = "test-item-code";
    private static final String TEST_ITEM_NAME = "test-item-name";

    private static final int TEST_PAGE = 12;

    private static final int TEST_LINE_NO = 123;

    private static final double TEST_QUANTITY = 10.00;
    private static final double TEST_PRICE = 12.00;
    private static final double TEST_AMOUNT = 120.00;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private PostedPurchaseOrder postedPurchaseOrderMock;

    @Mock
    private PostedPurchaseOrderLine postedPurchaseOrderLineMock;

    @Mock
    private Page<PostedPurchaseOrderLine> pageMock;

    @Mock
    private PostedPurchaseOrderLineRepository repoMock;

    @Mock
    private Item itemMock;

    private PostedPurchaseOrderLineService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(postedPurchaseOrderMock.getCode()).thenReturn(TEST_CODE);

        when(itemMock.getCode()).thenReturn(TEST_ITEM_CODE);
        when(itemMock.getName()).thenReturn(TEST_ITEM_NAME);

        PostedPurchaseOrderLineId postedPurchaseOrderLineId = new PostedPurchaseOrderLineId();
        postedPurchaseOrderLineId.setPostedPurchaseOrder(postedPurchaseOrderMock);
        postedPurchaseOrderLineId.setLineNo(TEST_LINE_NO);

        when(postedPurchaseOrderLineMock.getPostedPurchaseOrderLineId()).thenReturn(postedPurchaseOrderLineId);
        when(postedPurchaseOrderLineMock.getItem()).thenReturn(itemMock);
        when(postedPurchaseOrderLineMock.getQuantity()).thenReturn(TEST_QUANTITY);
        when(postedPurchaseOrderLineMock.getPrice()).thenReturn(TEST_PRICE);
        when(postedPurchaseOrderLineMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(repoMock.findFor(eq(postedPurchaseOrderMock), any(Pageable.class))).thenReturn(pageMock);

        svc = new PostedPurchaseOrderLineServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(postedPurchaseOrderLineMock);

        verify(repoMock).save(postedPurchaseOrderLineMock);
    }

    @Test
    public void list_delegatesToRepo(){
        svc.list(postedPurchaseOrderMock);

        verify(repoMock).findFor(postedPurchaseOrderMock);
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        svc.list(postedPurchaseOrderMock, TEST_PAGE);

        verify(repoMock).findFor(eq(postedPurchaseOrderMock), pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(Constants.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo(){
        svc.count(postedPurchaseOrderMock);

        verify(repoMock).count(postedPurchaseOrderMock);
    }

    @Test
    public void toDTO_returnsProperValue() {
        PostedPurchaseDocumentLineDTO dto = svc.toDTO(postedPurchaseOrderLineMock);

        assertEquals(TEST_CODE, dto.getPurchaseDocumentCode());
        assertEquals(TEST_LINE_NO, dto.getLineNo());
        assertEquals(TEST_ITEM_CODE, dto.getItemCode());
        assertEquals(TEST_ITEM_NAME, dto.getItemName());
        assertEquals(TEST_QUANTITY, dto.getQuantity());
        assertEquals(TEST_PRICE, dto.getPrice());
        assertEquals(TEST_AMOUNT, dto.getAmount());
    }
}
