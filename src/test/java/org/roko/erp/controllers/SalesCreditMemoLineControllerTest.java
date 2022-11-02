package org.roko.erp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.controllers.model.SalesCreditMemoLineModel;
import org.roko.erp.model.Item;
import org.roko.erp.model.SalesCreditMemo;
import org.roko.erp.model.SalesCreditMemoLine;
import org.roko.erp.services.ItemService;
import org.roko.erp.services.SalesCreditMemoLineService;
import org.roko.erp.services.SalesCreditMemoService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

public class SalesCreditMemoLineControllerTest {

    private static final String TEST_SALES_CREDIT_MEMO_CODE = "test-sales-credit-memo-code";
    
    private static final String TEST_ITEM_CODE = "test-item-code";
    private static final String TEST_ITEM_NAME = "test-item-name";
    private static final double TEST_ITEM_SALES_PRICE = 12.00d;
    private static final Double TEST_QTY = 10.00d;

    private static final Long TEST_LINE_NO = 123l;

    private List<Item> itemList = new ArrayList<>();

    @Captor
    private ArgumentCaptor<SalesCreditMemoLineModel> salesCreditMemoLineModelArgumentCaptor;

    @Captor
    private ArgumentCaptor<SalesCreditMemoLine> salesCreditMemoLineArgumentCaptor;

    @Mock
    private Model modelMock;

    @Mock
    private SalesCreditMemoLineModel salesCreditMemoLineModelMock;

    @Mock
    private Item itemMock;

    @Mock
    private ItemService itemSvcMock;

    @Mock
    private SalesCreditMemo salesCreditMemoMock;

    @Mock
    private SalesCreditMemoService salesCreditMemoSvcMock;

    @Mock
    private RedirectAttributes redirectAttributesMock;

    @Mock
    private SalesCreditMemoLineService svcMock;

    private SalesCreditMemoLineController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(itemMock.getCode()).thenReturn(TEST_ITEM_CODE);
        when(itemMock.getName()).thenReturn(TEST_ITEM_NAME);
        when(itemMock.getSalesPrice()).thenReturn(TEST_ITEM_SALES_PRICE);

        when(itemSvcMock.list()).thenReturn(itemList);
        when(itemSvcMock.get(TEST_ITEM_CODE)).thenReturn(itemMock);

        when(salesCreditMemoSvcMock.get(TEST_SALES_CREDIT_MEMO_CODE)).thenReturn(salesCreditMemoMock);

        when(salesCreditMemoLineModelMock.getSalesCreditMemoCode()).thenReturn(TEST_SALES_CREDIT_MEMO_CODE);
        when(salesCreditMemoLineModelMock.getItemCode()).thenReturn(TEST_ITEM_CODE);
        when(salesCreditMemoLineModelMock.getQuantity()).thenReturn(TEST_QTY);
        when(salesCreditMemoLineModelMock.getPrice()).thenReturn(TEST_ITEM_SALES_PRICE);
        when(salesCreditMemoLineModelMock.getAmount()).thenReturn(TEST_QTY * TEST_ITEM_SALES_PRICE);

        when(svcMock.count(salesCreditMemoMock)).thenReturn(TEST_LINE_NO);

        controller = new SalesCreditMemoLineController(svcMock, salesCreditMemoSvcMock, itemSvcMock);
    }

    @Test
    public void wizard_returnsProperTemplate(){
        String template = controller.wizard(TEST_SALES_CREDIT_MEMO_CODE, modelMock);

        assertEquals("salesCreditMemoLineWizardFirstPage.html", template);

        verify(modelMock).addAttribute("items", itemList);
    }

    @Test
    public void postingWizardFirstPage_returnsProperTemplate(){
        String template = controller.postSalesCreditMemoLineWizardFirstPage(salesCreditMemoLineModelMock, modelMock);

        assertEquals("salesCreditMemoLineWizardSecondPage.html", template);

        verify(modelMock).addAttribute("salesCreditMemoLineModel", salesCreditMemoLineModelMock);

        verify(salesCreditMemoLineModelMock).setItemName(TEST_ITEM_NAME);
        verify(salesCreditMemoLineModelMock).setPrice(TEST_ITEM_SALES_PRICE);
    }

    @Test
    public void postingWizardSecondPage_returnsProperTemplate(){
        String template = controller.postSalesCreditMemoLineWizardSecondPage(salesCreditMemoLineModelMock, modelMock);

        assertEquals("salesCreditMemoLineWizardThirdPage.html", template);

        verify(modelMock).addAttribute("salesCreditMemoLineModel", salesCreditMemoLineModelMock);

        verify(salesCreditMemoLineModelMock).setAmount(TEST_ITEM_SALES_PRICE * TEST_QTY);
    }

    @Test
    public void postingWizardThirdPage_createsNewEntity(){
        RedirectView redirectView = controller.postSalesCreditMemoLineWizardThirdPage(salesCreditMemoLineModelMock, redirectAttributesMock);

        assertEquals("/salesCreditMemoCard", redirectView.getUrl());

        verify(redirectAttributesMock).addAttribute("code", TEST_SALES_CREDIT_MEMO_CODE);

        verify(svcMock).create(salesCreditMemoLineArgumentCaptor.capture());

        SalesCreditMemoLine createdSalesCreditMemoLine = salesCreditMemoLineArgumentCaptor.getValue();

        assertEquals(salesCreditMemoMock, createdSalesCreditMemoLine.getSalesCreditMemoLineId().getSalesCreditMemo());
        assertEquals(TEST_LINE_NO + 1, createdSalesCreditMemoLine.getSalesCreditMemoLineId().getLineNo());

        assertEquals(itemMock, createdSalesCreditMemoLine.getItem());
        assertEquals(TEST_QTY, createdSalesCreditMemoLine.getQuantity());
        assertEquals(TEST_ITEM_SALES_PRICE, createdSalesCreditMemoLine.getPrice());
        assertEquals(TEST_QTY * TEST_ITEM_SALES_PRICE, createdSalesCreditMemoLine.getAmount());
    }
}
