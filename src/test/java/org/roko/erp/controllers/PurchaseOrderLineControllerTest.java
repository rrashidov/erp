package org.roko.erp.controllers;

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
import org.roko.erp.controllers.model.PurchaseOrderLineModel;
import org.roko.erp.model.Item;
import org.roko.erp.model.PurchaseOrder;
import org.roko.erp.model.PurchaseOrderLine;
import org.roko.erp.services.ItemService;
import org.roko.erp.services.PurchaseOrderLineService;
import org.roko.erp.services.PurchaseOrderService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

public class PurchaseOrderLineControllerTest {
    
    private static final String TEST_ITEM_CODE = "test-item-code";
    private static final String TEST_ITEM_NAME = "test-item-name";
    private static final Double TEST_ITEM_PURCHASE_PRICE = 12.00d;

    private static final String TEST_PURCHASE_ORDER_CODE = "test-purchase-order-code";

    private static final Double TEST_QTY = 10.00d;
    private static final Double TEST_PRICE = 12.00d;

    private static final Long TEST_LINE_COUNT = 123l;

    private List<Item> items = new ArrayList<>();

    @Captor
    private ArgumentCaptor<PurchaseOrderLineModel> purchaseOrderLineModelArgumentCaptor;

    @Captor
    private ArgumentCaptor<PurchaseOrderLine> purchaseOrderLineArgumentCaptor;

    @Mock
    private PurchaseOrder purchaseOrderMock;

    @Mock
    private RedirectAttributes redirectAttributesMock;

    @Mock
    private Item itemMock;

    @Mock
    private PurchaseOrderLineModel purchaseOrderLineModelMock;

    @Mock
    private Model modelMock;

    @Mock
    private PurchaseOrderLineService svcMock;

    @Mock
    private PurchaseOrderService purchaseOrderSvcMock;

    @Mock
    private ItemService itemSvcMock;

    private PurchaseOrderLineController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(purchaseOrderSvcMock.get(TEST_PURCHASE_ORDER_CODE)).thenReturn(purchaseOrderMock);

        when(itemMock.getCode()).thenReturn(TEST_ITEM_CODE);
        when(itemMock.getName()).thenReturn(TEST_ITEM_NAME);
        when(itemMock.getPurchasePrice()).thenReturn(TEST_ITEM_PURCHASE_PRICE);

        when(purchaseOrderLineModelMock.getPurchaseOrderCode()).thenReturn(TEST_PURCHASE_ORDER_CODE);
        when(purchaseOrderLineModelMock.getItemCode()).thenReturn(TEST_ITEM_CODE);
        when(purchaseOrderLineModelMock.getQuantity()).thenReturn(TEST_QTY);
        when(purchaseOrderLineModelMock.getPrice()).thenReturn(TEST_PRICE);
        when(purchaseOrderLineModelMock.getAmount()).thenReturn(TEST_QTY * TEST_PRICE);

        when(itemSvcMock.list()).thenReturn(items);
        when(itemSvcMock.get(TEST_ITEM_CODE)).thenReturn(itemMock);

        when(svcMock.count(purchaseOrderMock)).thenReturn(TEST_LINE_COUNT);

        controller = new PurchaseOrderLineController(svcMock, purchaseOrderSvcMock, itemSvcMock);
    }

    @Test
    public void wizard_returnsProperTemplate(){
        String template = controller.purchaseOrderLineWizard(TEST_PURCHASE_ORDER_CODE, modelMock);

        assertEquals("purchaseOrderLineWizardFirstPage.html", template);

        verify(modelMock).addAttribute(eq("purchaseOrderLineModel"), purchaseOrderLineModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("items", items);

        PurchaseOrderLineModel purchaseOrderLineModel = purchaseOrderLineModelArgumentCaptor.getValue();

        assertEquals(TEST_PURCHASE_ORDER_CODE, purchaseOrderLineModel.getPurchaseOrderCode());
        assertEquals(0, purchaseOrderLineModel.getLineNo());
        assertEquals("", purchaseOrderLineModel.getItemCode());
        assertEquals("", purchaseOrderLineModel.getItemName());
        assertEquals(0, purchaseOrderLineModel.getQuantity());
        assertEquals(0, purchaseOrderLineModel.getPrice());
        assertEquals(0, purchaseOrderLineModel.getAmount());
    }

    @Test
    public void postingPurchaseOrderLineWizardFirstPage_returnsProperTemplate(){
        String template = controller.postPurchaseOrderLineWizardFirstPage(purchaseOrderLineModelMock, modelMock);

        assertEquals("purchaseOrderLineWizardSecondPage.html", template);

        verify(purchaseOrderLineModelMock).setItemName(TEST_ITEM_NAME);
        verify(purchaseOrderLineModelMock).setPrice(TEST_ITEM_PURCHASE_PRICE);

        verify(modelMock).addAttribute("purchaseOrderLineModel", purchaseOrderLineModelMock);
    }

    @Test
    public void postingPurchaseOrderLineWizardSecondPage_returnsProperTemplate(){
        String template = controller.postPurchaseOrderLineWizardSecondPage(purchaseOrderLineModelMock, modelMock);

        assertEquals("purchaseOrderLineWizardThirdPage.html", template);

        verify(purchaseOrderLineModelMock).setAmount(TEST_QTY * TEST_PRICE);

        verify(modelMock).addAttribute("purchaseOrderLineModel", purchaseOrderLineModelMock);
    }

    @Test
    public void postingPurchaseOrderLineWizardThirdPage_returnsProperTemplate(){
        RedirectView redirectView = controller.postPurchaseOrderLineWizardThirdPage(purchaseOrderLineModelMock, redirectAttributesMock);

        assertEquals("/purchaseOrderCard", redirectView.getUrl());

        verify(redirectAttributesMock).addAttribute("code", TEST_PURCHASE_ORDER_CODE);

        verify(svcMock).create(purchaseOrderLineArgumentCaptor.capture());

        PurchaseOrderLine purchaseOrderLine = purchaseOrderLineArgumentCaptor.getValue();

        assertEquals(purchaseOrderMock, purchaseOrderLine.getPurchaseOrderLineId().getPurchaseOrder());
        assertEquals(TEST_LINE_COUNT + 1, purchaseOrderLine.getPurchaseOrderLineId().getLineNo());
        assertEquals(itemMock, purchaseOrderLine.getItem());
        assertEquals(TEST_QTY, purchaseOrderLine.getQuantity());
        assertEquals(TEST_PRICE, purchaseOrderLine.getPrice());
        assertEquals(TEST_QTY * TEST_PRICE, purchaseOrderLine.getAmount());
    }
}
