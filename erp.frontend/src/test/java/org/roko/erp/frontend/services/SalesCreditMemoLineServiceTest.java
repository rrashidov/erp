package org.roko.erp.frontend.services;

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
import org.roko.erp.frontend.controllers.paging.PagingServiceImpl;
import org.roko.erp.frontend.model.Item;
import org.roko.erp.frontend.model.SalesCreditMemo;
import org.roko.erp.frontend.model.SalesCreditMemoLine;
import org.roko.erp.frontend.model.jpa.SalesCreditMemoLineId;
import org.roko.erp.frontend.repositories.SalesCreditMemoLineRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class SalesCreditMemoLineServiceTest {
    
    private static final int TEST_PAGE = 12;

    private static final double TEST_QTY = 10.0d;
    private static final double TEST_PRICE = 12.0d;
    private static final double TEST_AMOUNT = 120.0d;

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

    private SalesCreditMemoLineService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(salesCreditMemoLineMockToUpdate.getItem()).thenReturn(itemMock);
        when(salesCreditMemoLineMockToUpdate.getQuantity()).thenReturn(TEST_QTY);
        when(salesCreditMemoLineMockToUpdate.getPrice()).thenReturn(TEST_PRICE);
        when(salesCreditMemoLineMockToUpdate.getAmount()).thenReturn(TEST_AMOUNT);

        when(repoMock.findById(salesCreditMemoLineIdMock)).thenReturn(Optional.of(salesCreditMemoLineMock));
        when(repoMock.findForSalesCreditMemo(eq(salesCreditMemoMock), any(Pageable.class))).thenReturn(pageMock);

        svc = new SalesCreditMemoLineServiceImpl(repoMock);
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
        assertEquals(PagingServiceImpl.RECORDS_PER_PAGE, pageable.getPageSize());
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
}
