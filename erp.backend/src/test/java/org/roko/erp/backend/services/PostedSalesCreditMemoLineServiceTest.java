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
import org.roko.erp.backend.model.PostedSalesCreditMemo;
import org.roko.erp.backend.model.PostedSalesCreditMemoLine;
import org.roko.erp.backend.model.jpa.PostedSalesCreditMemoLineId;
import org.roko.erp.backend.repositories.PostedSalesCreditMemoLineRepository;
import org.roko.erp.model.dto.PostedSalesDocumentLineDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PostedSalesCreditMemoLineServiceTest {
    
    private static final String TEST_ITEM_CODE = "test-item-code";
    private static final String TEST_ITEM_NAME = "test-item-name";

    private static final String TEST_POSTED_SALES_CREDIT_MEMO_CODE = "test-posted-sales-credit-memo-code";

    private static final int TEST_LINE_NO = 123;

    private static final int TEST_PAGE = 12;

    private static final double TEST_QUANTITY = 12.12;
    private static final double TEST_PRICE = 10.00;
    private static final double TEST_AMOUNT = 120.00;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<PostedSalesCreditMemoLine> pageMock;

    @Mock
    private PostedSalesCreditMemo postedSalesCreditMemoMock;

    @Mock
    private PostedSalesCreditMemoLine postedSalesCreditMemoLineMock;
    
    @Mock
    private PostedSalesCreditMemoLineRepository repoMock;

    @Mock
    private Item itemMock;

    private PostedSalesCreditMemoLineService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(itemMock.getCode()).thenReturn(TEST_ITEM_CODE);
        when(itemMock.getName()).thenReturn(TEST_ITEM_NAME);

        when(postedSalesCreditMemoMock.getCode()).thenReturn(TEST_POSTED_SALES_CREDIT_MEMO_CODE);

        PostedSalesCreditMemoLineId postedSalesCreditMemoLineId = new PostedSalesCreditMemoLineId();
        postedSalesCreditMemoLineId.setPostedSalesCreditMemo(postedSalesCreditMemoMock);
        postedSalesCreditMemoLineId.setLineNo(TEST_LINE_NO);

        when(postedSalesCreditMemoLineMock.getPostedSalesCreditMemoLineId()).thenReturn(postedSalesCreditMemoLineId);
        when(postedSalesCreditMemoLineMock.getItem()).thenReturn(itemMock);
        when(postedSalesCreditMemoLineMock.getQuantity()).thenReturn(TEST_QUANTITY);
        when(postedSalesCreditMemoLineMock.getPrice()).thenReturn(TEST_PRICE);
        when(postedSalesCreditMemoLineMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(repoMock.findFor(eq(postedSalesCreditMemoMock), any(Pageable.class))).thenReturn(pageMock);

        svc = new PostedSalesCreditMemoLineServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(postedSalesCreditMemoLineMock);

        verify(repoMock).save(postedSalesCreditMemoLineMock);
    }

    @Test
    public void list_delegatesToRepo(){
        svc.list(postedSalesCreditMemoMock);

        verify(repoMock).findFor(postedSalesCreditMemoMock);
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        svc.list(postedSalesCreditMemoMock, TEST_PAGE);

        verify(repoMock).findFor(eq(postedSalesCreditMemoMock), pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(Constants.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo() {
        svc.count(postedSalesCreditMemoMock);

        verify(repoMock).count(postedSalesCreditMemoMock);
    }

    @Test
    public void toDTO_returnsProperResult() {
        PostedSalesDocumentLineDTO dto = svc.toDTO(postedSalesCreditMemoLineMock);

        assertEquals(TEST_POSTED_SALES_CREDIT_MEMO_CODE, dto.getSalesDocumentCode());
        assertEquals(TEST_LINE_NO, dto.getLineNo());
        assertEquals(TEST_ITEM_CODE, dto.getItemCode());
        assertEquals(TEST_ITEM_NAME, dto.getItemName());
        assertEquals(TEST_QUANTITY, dto.getQuantity());
        assertEquals(TEST_PRICE, dto.getPrice());
        assertEquals(TEST_AMOUNT, dto.getAmount());
    }
}
