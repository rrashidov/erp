package org.roko.erp.frontend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
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
import org.roko.erp.dto.ItemDTO;
import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.dto.SalesDocumentLineDTO;
import org.roko.erp.dto.list.ItemList;
import org.roko.erp.dto.list.SalesDocumentLineList;
import org.roko.erp.frontend.services.ItemService;
import org.roko.erp.frontend.services.SalesOrderLineService;
import org.roko.erp.frontend.services.SalesOrderService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

public class SalesOrderLineControllerTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_SALES_ORDER_LINENO = 1;
    private static final double TEST_SALES_ORDER_LINE_QTY = 10.0d;
    private static final double TEST_SALES_ORDER_LINE_AMOUNT = 123.89d;

    private static final String TEST_ITEM_CODE = "test-item-code";
    private static final String TEST_ITEM_NAME = "test-item-name";
    private static final double TEST_ITEM_SALES_PRICE = 12.12d;

    private static final int TEST_PAGE = 123;

    private List<ItemDTO> items = new ArrayList<>();

    @Captor
    private ArgumentCaptor<SalesDocumentLineDTO> salesOrderLineModelArgumentCaptor;

    @Captor
    private ArgumentCaptor<SalesDocumentLineDTO> salesOrderLineArgumentCaptor;

    @Mock
    private ItemDTO itemMock;

    @Mock
    private SalesDocumentDTO salesOrderMock;

    @Mock
    private SalesDocumentLineDTO salesOrderLineMock;

    @Mock
    private SalesDocumentLineDTO salesOrderLineModelMock;

    @Mock
    private Model modelMock;

    @Mock
    private RedirectAttributes redirectAttributesMock;

    @Mock
    private SalesOrderService svcMock;

    @Mock
    private SalesOrderLineService salesOrderLineSvcMock;

    @Mock
    private ItemService itemSvcMock;

    @Mock
    private SalesDocumentLineList salesOrderLineList;

    @Mock
    private ItemList itemList;

    private SalesOrderLineController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(salesOrderMock.getCode()).thenReturn(TEST_CODE);

        when(salesOrderLineMock.getSalesDocumentCode()).thenReturn(TEST_CODE);
        when(salesOrderLineMock.getLineNo()).thenReturn(TEST_SALES_ORDER_LINENO);
        when(salesOrderLineMock.getItemCode()).thenReturn(TEST_ITEM_CODE);
        when(salesOrderLineMock.getQuantity()).thenReturn(TEST_SALES_ORDER_LINE_QTY);
        when(salesOrderLineMock.getPrice()).thenReturn(TEST_ITEM_SALES_PRICE);
        when(salesOrderLineMock.getAmount()).thenReturn(TEST_SALES_ORDER_LINE_AMOUNT);

        when(salesOrderLineSvcMock.list(TEST_CODE, TEST_PAGE)).thenReturn(salesOrderLineList);
        when(salesOrderLineSvcMock.get(TEST_CODE, TEST_SALES_ORDER_LINENO)).thenReturn(salesOrderLineMock);

        when(salesOrderLineModelMock.getSalesDocumentCode()).thenReturn(TEST_CODE);
        when(salesOrderLineModelMock.getItemCode()).thenReturn(TEST_ITEM_CODE);
        when(salesOrderLineModelMock.getQuantity()).thenReturn(TEST_SALES_ORDER_LINE_QTY);
        when(salesOrderLineModelMock.getPrice()).thenReturn(TEST_ITEM_SALES_PRICE);
        when(salesOrderLineModelMock.getAmount()).thenReturn(TEST_SALES_ORDER_LINE_AMOUNT);
        
        when(itemMock.getCode()).thenReturn(TEST_ITEM_CODE);
        when(itemMock.getName()).thenReturn(TEST_ITEM_NAME);
        when(itemMock.getSalesPrice()).thenReturn(TEST_ITEM_SALES_PRICE);

        when(itemList.getData()).thenReturn(items);
        
        when(itemSvcMock.list()).thenReturn(itemList);
        when(itemSvcMock.get(TEST_ITEM_CODE)).thenReturn(itemMock);

        when(svcMock.get(TEST_CODE)).thenReturn(salesOrderMock);

        controller = new SalesOrderLineController(salesOrderLineSvcMock, itemSvcMock);
    }

    @Test
    public void salesOrderLineWizard_returnsProperTemplate_whenCalledForNew(){
        String template = controller.salesOrderLineWizard(TEST_CODE, null, modelMock);

        assertEquals("salesOrderLineWizardFirstPage.html", template);

        verify(modelMock).addAttribute("items", items);
        verify(modelMock).addAttribute(eq("salesOrderLine"), salesOrderLineModelArgumentCaptor.capture());

        SalesDocumentLineDTO salesOrderLineModel = salesOrderLineModelArgumentCaptor.getValue();

        assertEquals(TEST_CODE, salesOrderLineModel.getSalesDocumentCode());
        assertEquals("", salesOrderLineModel.getItemCode());
    }

    @Test
    public void salesOrderLineWizard_returnsProperTemplate_whenCalledForExisting(){
        String template = controller.salesOrderLineWizard(TEST_CODE, TEST_SALES_ORDER_LINENO, modelMock);

        assertEquals("salesOrderLineWizardFirstPage.html", template);

        verify(modelMock).addAttribute("items", items);
        verify(modelMock).addAttribute(eq("salesOrderLine"), salesOrderLineModelArgumentCaptor.capture());

        SalesDocumentLineDTO salesOrderLineModel = salesOrderLineModelArgumentCaptor.getValue();

        assertEquals(TEST_SALES_ORDER_LINENO, salesOrderLineModel.getLineNo());
        assertEquals(TEST_CODE, salesOrderLineModel.getSalesDocumentCode());
        assertEquals(TEST_ITEM_CODE, salesOrderLineModel.getItemCode());
        assertEquals(TEST_SALES_ORDER_LINE_QTY, salesOrderLineModel.getQuantity());
        assertEquals(TEST_ITEM_SALES_PRICE, salesOrderLineModel.getPrice());
        assertEquals(TEST_SALES_ORDER_LINE_AMOUNT, salesOrderLineModel.getAmount());
    }

    @Test
    public void postingSalesOrderLineWizardFirstPage_returnsProperTemplate(){
        String template = controller.postSalesOrderLineWizardFirstPage(salesOrderLineModelMock, modelMock);

        assertEquals("salesOrderLineWizardSecondPage.html", template);

        verify(modelMock).addAttribute("salesOrderLine", salesOrderLineModelMock);

        verify(salesOrderLineModelMock).setItemName(TEST_ITEM_NAME);
        verify(salesOrderLineModelMock).setPrice(TEST_ITEM_SALES_PRICE);
    }

    @Test
    public void postingSalesOrderLineWizardSecondPage_returnsProperTemplate(){
        String template = controller.postSalesOrderLineWizardSecondPage(salesOrderLineModelMock, modelMock);

        assertEquals("salesOrderLineWizardThirdPage.html", template);

        verify(modelMock).addAttribute("salesOrderLine", salesOrderLineModelMock);

        verify(salesOrderLineModelMock).setAmount(salesOrderLineModelMock.getPrice() * TEST_SALES_ORDER_LINE_QTY);
    }

    @Test
    public void postingSalesOrderLineWizardThirdPage_returnsProperResult(){
        RedirectView redirectView = controller.postSalesOrderLineWizardThirdPage(salesOrderLineModelMock, redirectAttributesMock);

        assertEquals("/salesOrderCard", redirectView.getUrl());

        verify(redirectAttributesMock).addAttribute("code", TEST_CODE);

        verify(salesOrderLineSvcMock).create(eq(TEST_CODE), salesOrderLineArgumentCaptor.capture());

        SalesDocumentLineDTO createOrderLine = salesOrderLineArgumentCaptor.getValue();

        assertEquals(TEST_ITEM_CODE, createOrderLine.getItemCode());
        assertEquals(TEST_SALES_ORDER_LINE_QTY, createOrderLine.getQuantity());
        assertEquals(TEST_ITEM_SALES_PRICE, createOrderLine.getPrice());
        assertEquals(TEST_SALES_ORDER_LINE_AMOUNT, createOrderLine.getAmount());
    }

    @Test
    public void postingSalesOrderLineWizardThirdPage_updatesSalesOrderLine_whenCalledWithLineNo(){
        when(salesOrderLineModelMock.getLineNo()).thenReturn(TEST_SALES_ORDER_LINENO);

        RedirectView redirectView = controller.postSalesOrderLineWizardThirdPage(salesOrderLineModelMock, redirectAttributesMock);

        assertEquals("/salesOrderCard", redirectView.getUrl());

        verify(redirectAttributesMock).addAttribute("code", TEST_CODE);

        verify(salesOrderLineSvcMock).update(eq(TEST_CODE), eq(TEST_SALES_ORDER_LINENO), salesOrderLineArgumentCaptor.capture());
    }

    @Test
    public void deletingSalesOrderLine_deletesSalesOrderLine(){
        RedirectView redirectView = controller.deleteSalesOrderLine(TEST_CODE, TEST_SALES_ORDER_LINENO, redirectAttributesMock);

        assertEquals("/salesOrderCard", redirectView.getUrl());

        verify(salesOrderLineSvcMock).delete(TEST_CODE, TEST_SALES_ORDER_LINENO);

        verify(redirectAttributesMock).addAttribute("code", TEST_CODE);
    }
}
