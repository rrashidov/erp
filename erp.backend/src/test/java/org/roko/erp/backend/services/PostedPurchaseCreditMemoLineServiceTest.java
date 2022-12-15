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
import org.roko.erp.backend.model.PostedPurchaseCreditMemo;
import org.roko.erp.backend.model.PostedPurchaseCreditMemoLine;
import org.roko.erp.backend.model.jpa.PostedPurchaseCreditMemoLineId;
import org.roko.erp.backend.repositories.PostedPurchaseCreditMemoLineRepository;
import org.roko.erp.dto.PostedPurchaseDocumentLineDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PostedPurchaseCreditMemoLineServiceTest {
    
    private static final String TEST_CODE = "test-code";

    private static final String TEST_ITEM_CODE = "test-item-code";
    private static final String TEST_ITEM_NAME = "test-item-name";

    private static final int TEST_LINE_NO = 234;

    private static final int TEST_PAGE = 12;

    private static final double TEST_QUANTITY = 12.00;
    private static final double TEST_PRICE = 10.00;
    private static final double TEST_AMOUNT = 120.00;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private PostedPurchaseCreditMemo postedPurchaseCreditMemoMock;

    @Mock
    private PostedPurchaseCreditMemoLine postedPurchaseCreditMemoLineMock;

    @Mock
    private PostedPurchaseCreditMemoLineRepository repoMock;

    @Mock
    private Page<PostedPurchaseCreditMemoLine> pageMock;

    @Mock
    private Item itemMock;

    private PostedPurchaseCreditMemoLineService svc;

    @BeforeEach
    public void setup () {
        MockitoAnnotations.openMocks(this);

        when(postedPurchaseCreditMemoMock.getCode()).thenReturn(TEST_CODE);

        when(itemMock.getCode()).thenReturn(TEST_ITEM_CODE);
        when(itemMock.getName()).thenReturn(TEST_ITEM_NAME);

        PostedPurchaseCreditMemoLineId postedPurchaseCreditMemoLineId = new PostedPurchaseCreditMemoLineId();
        postedPurchaseCreditMemoLineId.setPostedPurchaseCreditMemo(postedPurchaseCreditMemoMock);
        postedPurchaseCreditMemoLineId.setLineNo(TEST_LINE_NO);

        when(postedPurchaseCreditMemoLineMock.getPostedPurchaseCreditMemoLineId()).thenReturn(postedPurchaseCreditMemoLineId);
        when(postedPurchaseCreditMemoLineMock.getItem()).thenReturn(itemMock);
        when(postedPurchaseCreditMemoLineMock.getQuantity()).thenReturn(TEST_QUANTITY);
        when(postedPurchaseCreditMemoLineMock.getPrice()).thenReturn(TEST_PRICE);
        when(postedPurchaseCreditMemoLineMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(repoMock.findFor(eq(postedPurchaseCreditMemoMock), any(Pageable.class))).thenReturn(pageMock);

        svc = new PostedPurchaseCreditMemoLineServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo() {
        svc.create(postedPurchaseCreditMemoLineMock);

        verify(repoMock).save(postedPurchaseCreditMemoLineMock);
    }

    @Test
    public void list_delegatesToRepo() {
        svc.list(postedPurchaseCreditMemoMock);

        verify(repoMock).findFor(postedPurchaseCreditMemoMock);
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        svc.list(postedPurchaseCreditMemoMock, TEST_PAGE);

        verify(repoMock).findFor(eq(postedPurchaseCreditMemoMock), pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(Constants.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo() {
        svc.count(postedPurchaseCreditMemoMock);

        verify(repoMock).count(postedPurchaseCreditMemoMock);
    }

    @Test
    public void toDTO_returnsProperValue() {
        PostedPurchaseDocumentLineDTO dto = svc.toDTO(postedPurchaseCreditMemoLineMock);

        assertEquals(TEST_CODE, dto.getPurchaseDocumentCode());
        assertEquals(TEST_LINE_NO, dto.getLineNo());
        assertEquals(TEST_ITEM_CODE, dto.getItemCode());
        assertEquals(TEST_ITEM_NAME, dto.getItemName());
        assertEquals(TEST_QUANTITY, dto.getQuantity());
        assertEquals(TEST_PRICE, dto.getPrice());
        assertEquals(TEST_AMOUNT, dto.getAmount());
    }
}
