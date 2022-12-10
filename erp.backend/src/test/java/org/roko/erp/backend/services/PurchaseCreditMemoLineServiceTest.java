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
import org.roko.erp.backend.model.PurchaseCreditMemo;
import org.roko.erp.backend.model.PurchaseCreditMemoLine;
import org.roko.erp.backend.model.jpa.PurchaseCreditMemoLineId;
import org.roko.erp.backend.repositories.PurchaseCreditMemoLineRepository;
import org.roko.erp.model.dto.PurchaseDocumentLineDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PurchaseCreditMemoLineServiceTest {

    private static final String TEST_CODE = "test-code";

    private static final String TEST_ITEM_CODE = "test-item-code";
    private static final String TEST_ITEM_NAME = "test-item-name";

    private static final int TEST_LINE_NO = 1234;

    private static final double TEST_QUANTITY = 12.12;
    private static final double TEST_PRICE = 10.00;
    private static final double TEST_AMOUNT = 123.12;

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private PurchaseCreditMemo purchaseCreditMemoMock;

    @Mock
    private PurchaseCreditMemoLineId nonExistingPurchaseCreditMemoLineIdMock;

    @Mock
    private PurchaseCreditMemoLineId purchaseCreditMemoLineIdMock;

    @Mock
    private PurchaseCreditMemoLine purchaseCreditMemoLineMock;

    @Mock
    private PurchaseCreditMemoLineRepository repoMock;

    @Mock
    private Page<PurchaseCreditMemoLine> pageMock;

    @Mock
    private Item itemMock;

    @Mock
    private PurchaseDocumentLineDTO dtoMock;

    @Mock
    private ItemService itemSvcMock;

    @Mock
    private PurchaseCreditMemoService purchaseCreditMemoSvcMock;

    private PurchaseCreditMemoLineService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(itemSvcMock.get(TEST_ITEM_CODE)).thenReturn(itemMock);

        when(purchaseCreditMemoSvcMock.get(TEST_CODE)).thenReturn(purchaseCreditMemoMock);

        when(dtoMock.getPurchaseDocumentCode()).thenReturn(TEST_CODE);
        when(dtoMock.getLineNo()).thenReturn(TEST_LINE_NO);
        when(dtoMock.getItemCode()).thenReturn(TEST_ITEM_CODE);
        when(dtoMock.getQuantity()).thenReturn(TEST_QUANTITY);
        when(dtoMock.getPrice()).thenReturn(TEST_PRICE);
        when(dtoMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(purchaseCreditMemoMock.getCode()).thenReturn(TEST_CODE);

        when(itemMock.getCode()).thenReturn(TEST_ITEM_CODE);
        when(itemMock.getName()).thenReturn(TEST_ITEM_NAME);

        PurchaseCreditMemoLineId purchaseCreditMemoLineId = new PurchaseCreditMemoLineId();
        purchaseCreditMemoLineId.setPurchaseCreditMemo(purchaseCreditMemoMock);
        purchaseCreditMemoLineId.setLineNo(TEST_LINE_NO);

        when(purchaseCreditMemoLineMock.getPurchaseCreditMemoLineId()).thenReturn(purchaseCreditMemoLineId);
        when(purchaseCreditMemoLineMock.getItem()).thenReturn(itemMock);
        when(purchaseCreditMemoLineMock.getQuantity()).thenReturn(TEST_QUANTITY);
        when(purchaseCreditMemoLineMock.getPrice()).thenReturn(TEST_PRICE);
        when(purchaseCreditMemoLineMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(repoMock.findById(purchaseCreditMemoLineIdMock)).thenReturn(Optional.of(purchaseCreditMemoLineMock));
        when(repoMock.findFor(eq(purchaseCreditMemoMock), any(Pageable.class))).thenReturn(pageMock);

        svc = new PurchaseCreditMemoLineServiceImpl(repoMock, purchaseCreditMemoSvcMock, itemSvcMock);
    }

    @Test
    public void create_delegatesToRepo() {
        svc.create(purchaseCreditMemoLineMock);

        verify(repoMock).save(purchaseCreditMemoLineMock);
    }

    @Test
    public void update_delegatesToRepo() {
        svc.update(purchaseCreditMemoLineIdMock, purchaseCreditMemoLineMock);

        verify(repoMock).findById(purchaseCreditMemoLineIdMock);
        verify(repoMock).save(purchaseCreditMemoLineMock);
    }

    @Test
    public void delete_delegatesToRepo() {
        svc.delete(purchaseCreditMemoLineIdMock);

        verify(repoMock).delete(purchaseCreditMemoLineMock);
    }

    @Test
    public void get_delegatesToRepo() {
        svc.get(purchaseCreditMemoLineIdMock);

        verify(repoMock).findById(purchaseCreditMemoLineIdMock);
    }

    @Test
    public void getReturnsNull_whenCalledWithNonExistingId() {
        PurchaseCreditMemoLine purchaseCreditMemoLine = svc.get(nonExistingPurchaseCreditMemoLineIdMock);

        assertNull(purchaseCreditMemoLine);
    }

    @Test
    public void list_delegatesToRepo() {
        svc.list(purchaseCreditMemoMock);

        verify(repoMock).findFor(purchaseCreditMemoMock);
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        svc.list(purchaseCreditMemoMock, TEST_PAGE);

        verify(repoMock).findFor(eq(purchaseCreditMemoMock), pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(Constants.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo() {
        svc.count(purchaseCreditMemoMock);

        verify(repoMock).count(purchaseCreditMemoMock);
    }

    @Test
    public void maxLineNo_delegatesToRepo() {
        svc.maxLineNo(purchaseCreditMemoMock);

        verify(repoMock).maxLineNo(purchaseCreditMemoMock);
    }

    @Test
    public void toDTO_returnsProperResult() {
        PurchaseDocumentLineDTO dto = svc.toDTO(purchaseCreditMemoLineMock);

        assertEquals(TEST_CODE, dto.getPurchaseDocumentCode());
        assertEquals(TEST_LINE_NO, dto.getLineNo());
        assertEquals(TEST_ITEM_CODE, dto.getItemCode());
        assertEquals(TEST_ITEM_NAME, dto.getItemName());
        assertEquals(TEST_QUANTITY, dto.getQuantity());
        assertEquals(TEST_PRICE, dto.getPrice());
        assertEquals(TEST_AMOUNT, dto.getAmount());
    }

    @Test
    public void fromDTO_returnsProperResult() {
        PurchaseCreditMemoLine purchaseCreditMemoLine = svc.fromDTO(dtoMock);

        assertEquals(purchaseCreditMemoMock, purchaseCreditMemoLine.getPurchaseCreditMemoLineId().getPurchaseCreditMemo());
        assertEquals(TEST_LINE_NO, purchaseCreditMemoLine.getPurchaseCreditMemoLineId().getLineNo());
        assertEquals(itemMock, purchaseCreditMemoLine.getItem());
        assertEquals(TEST_QUANTITY, purchaseCreditMemoLine.getQuantity());
        assertEquals(TEST_PRICE, purchaseCreditMemoLine.getPrice());
        assertEquals(TEST_AMOUNT, purchaseCreditMemoLine.getAmount());
    }
}
