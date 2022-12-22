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
import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.dto.PurchaseDocumentLineDTO;
import org.roko.erp.dto.list.ItemList;
import org.roko.erp.frontend.controllers.model.PurchaseOrderLineModel;
import org.roko.erp.frontend.services.ItemService;
import org.roko.erp.frontend.services.PurchaseOrderLineService;
import org.roko.erp.frontend.services.PurchaseOrderService;
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

    private static final int TEST_LINE_NO = 2;

    private List<ItemDTO> items = new ArrayList<>();

    @Captor
    private ArgumentCaptor<PurchaseOrderLineModel> purchaseOrderLineModelArgumentCaptor;

    @Captor
    private ArgumentCaptor<PurchaseDocumentLineDTO> purchaseOrderLineArgumentCaptor;

    @Mock
    private PurchaseDocumentLineDTO purchaseOrderLineMock;

    @Mock
    private PurchaseDocumentDTO purchaseOrderMock;

    @Mock
    private RedirectAttributes redirectAttributesMock;

    @Mock
    private ItemDTO itemMock;

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

    @Mock
    private ItemList itemList;

    private PurchaseOrderLineController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(purchaseOrderLineMock.getPurchaseDocumentCode()).thenReturn(TEST_PURCHASE_ORDER_CODE);
        when(purchaseOrderLineMock.getLineNo()).thenReturn(TEST_LINE_NO);
        when(purchaseOrderLineMock.getItemCode()).thenReturn(TEST_ITEM_CODE);
        when(purchaseOrderLineMock.getItemName()).thenReturn(TEST_ITEM_NAME);
        when(purchaseOrderLineMock.getQuantity()).thenReturn(TEST_QTY);
        when(purchaseOrderLineMock.getPrice()).thenReturn(TEST_PRICE);
        when(purchaseOrderLineMock.getAmount()).thenReturn(TEST_PRICE * TEST_QTY);

        when(purchaseOrderSvcMock.get(TEST_PURCHASE_ORDER_CODE)).thenReturn(purchaseOrderMock);

        when(itemMock.getCode()).thenReturn(TEST_ITEM_CODE);
        when(itemMock.getName()).thenReturn(TEST_ITEM_NAME);
        when(itemMock.getPurchasePrice()).thenReturn(TEST_ITEM_PURCHASE_PRICE);

        when(purchaseOrderLineModelMock.getPurchaseOrderCode()).thenReturn(TEST_PURCHASE_ORDER_CODE);
        when(purchaseOrderLineModelMock.getItemCode()).thenReturn(TEST_ITEM_CODE);
        when(purchaseOrderLineModelMock.getQuantity()).thenReturn(TEST_QTY);
        when(purchaseOrderLineModelMock.getPrice()).thenReturn(TEST_PRICE);
        when(purchaseOrderLineModelMock.getAmount()).thenReturn(TEST_QTY * TEST_PRICE);

        when(itemList.getData()).thenReturn(items);

        when(itemSvcMock.list()).thenReturn(itemList);
        when(itemSvcMock.get(TEST_ITEM_CODE)).thenReturn(itemMock);

        when(svcMock.get(TEST_PURCHASE_ORDER_CODE, TEST_LINE_NO)).thenReturn(purchaseOrderLineMock);

        controller = new PurchaseOrderLineController(svcMock, itemSvcMock);
    }

    @Test
    public void wizard_returnsProperTemplate_whenCalledForNew(){
        String template = controller.purchaseOrderLineWizard(TEST_PURCHASE_ORDER_CODE, null, modelMock);

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
    public void wizard_returnsProperTemplate_whenCalledForExisting(){
        String template = controller.purchaseOrderLineWizard(TEST_PURCHASE_ORDER_CODE, TEST_LINE_NO, modelMock);

        assertEquals("purchaseOrderLineWizardFirstPage.html", template);

        verify(modelMock).addAttribute(eq("purchaseOrderLineModel"), purchaseOrderLineModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("items", items);

        PurchaseOrderLineModel purchaseOrderLineModel = purchaseOrderLineModelArgumentCaptor.getValue();

        assertEquals(TEST_PURCHASE_ORDER_CODE, purchaseOrderLineModel.getPurchaseOrderCode());
        assertEquals(TEST_LINE_NO, purchaseOrderLineModel.getLineNo());
        assertEquals(TEST_ITEM_CODE, purchaseOrderLineModel.getItemCode());
        assertEquals(TEST_ITEM_NAME, purchaseOrderLineModel.getItemName());
        assertEquals(TEST_QTY, purchaseOrderLineModel.getQuantity());
        assertEquals(TEST_PRICE, purchaseOrderLineModel.getPrice());
        assertEquals(TEST_QTY * TEST_PRICE, purchaseOrderLineModel.getAmount());
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
    public void postingPurchaseOrderLineWizardThirdPage_createsEntityAndReturnsProperTemplate_whenCalledForNew(){
        RedirectView redirectView = controller.postPurchaseOrderLineWizardThirdPage(purchaseOrderLineModelMock, redirectAttributesMock);

        assertEquals("/purchaseOrderCard", redirectView.getUrl());

        verify(redirectAttributesMock).addAttribute("code", TEST_PURCHASE_ORDER_CODE);

        verify(svcMock).create(eq(TEST_PURCHASE_ORDER_CODE), purchaseOrderLineArgumentCaptor.capture());

        PurchaseDocumentLineDTO purchaseOrderLine = purchaseOrderLineArgumentCaptor.getValue();

        assertEquals(TEST_ITEM_CODE, purchaseOrderLine.getItemCode());
        assertEquals(TEST_QTY, purchaseOrderLine.getQuantity());
        assertEquals(TEST_PRICE, purchaseOrderLine.getPrice());
        assertEquals(TEST_QTY * TEST_PRICE, purchaseOrderLine.getAmount());
    }

    @Test
    public void postingPurchaseOrderLineWizardThirdPage_updatesEntityAndReturnsProperTemplate_whenCalledForExisting(){
        when(purchaseOrderLineModelMock.getLineNo()).thenReturn(TEST_LINE_NO);

        RedirectView redirectView = controller.postPurchaseOrderLineWizardThirdPage(purchaseOrderLineModelMock, redirectAttributesMock);

        assertEquals("/purchaseOrderCard", redirectView.getUrl());

        verify(redirectAttributesMock).addAttribute("code", TEST_PURCHASE_ORDER_CODE);

        verify(svcMock).update(eq(TEST_PURCHASE_ORDER_CODE), eq(TEST_LINE_NO), purchaseOrderLineArgumentCaptor.capture());
    }

    @Test
    public void delete_deletesEntity(){
        RedirectView redirectView = controller.delete(TEST_PURCHASE_ORDER_CODE, TEST_LINE_NO, redirectAttributesMock);

        assertEquals("/purchaseOrderCard", redirectView.getUrl());

        verify(redirectAttributesMock).addAttribute("code", TEST_PURCHASE_ORDER_CODE);

        verify(svcMock).delete(TEST_PURCHASE_ORDER_CODE, TEST_LINE_NO);
    }
}
