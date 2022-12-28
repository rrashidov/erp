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
import org.roko.erp.frontend.controllers.model.PurchaseCreditMemoLineModel;
import org.roko.erp.frontend.model.jpa.PurchaseCreditMemoLineId;
import org.roko.erp.frontend.services.ItemService;
import org.roko.erp.frontend.services.PurchaseCreditMemoLineService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

public class PurchaseCreditMemoLineControllerTest {

    private static final String TEST_ITEM_CODE = "test-item-code";
    private static final String TEST_ITEM_NAME = "test-item-name";
    private static final double TEST_ITEM_PURCHASE_PRICE = 12.12d;

    private static final String TEST_PURCHASE_CREDIT_MEMO_CODE = "test-purchase-credit-memo-code";

    private static final Double TEST_QTY = 10.00d;
    private static final Double TEST_PRICE = 23.45;
    private static final Double TEST_AMOUNT = 123.12;

    private static final int TEST_LINE_NO = 234;

    private List<ItemDTO> items = new ArrayList<>();

    @Captor
    private ArgumentCaptor<PurchaseDocumentLineDTO> purchaseCreditMemoLineArgumentCaptor;

    @Captor
    private ArgumentCaptor<PurchaseCreditMemoLineModel> purchaseCreditMemoLineModelArgumentCaptor;

    @Captor
    private ArgumentCaptor<PurchaseCreditMemoLineId> purchaseCreditMemoLineIdArgumentCaptor;

    @Mock
    private PurchaseDocumentLineDTO purchaseCreditMemoLineMock;

    @Mock
    private PurchaseDocumentDTO purchaseCreditMemoMock;

    @Mock
    private RedirectAttributes redirectAttributesMock;

    @Mock
    private ItemDTO itemMock;

    @Mock
    private PurchaseCreditMemoLineModel purchaseCreditMemoLineModelMock;

    @Mock
    private Model modelMock;

    @Mock
    private PurchaseCreditMemoLineService svcMock;

    @Mock
    private ItemService itemSvcMock;

    @Mock
    private ItemList itemList;

    private PurchaseCreditMemoLineController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(itemMock.getCode()).thenReturn(TEST_ITEM_CODE);
        when(itemMock.getName()).thenReturn(TEST_ITEM_NAME);
        when(itemMock.getPurchasePrice()).thenReturn(TEST_ITEM_PURCHASE_PRICE);

        when(itemList.getData()).thenReturn(items);

        when(itemSvcMock.list()).thenReturn(itemList);
        when(itemSvcMock.get(TEST_ITEM_CODE)).thenReturn(itemMock);

        when(purchaseCreditMemoLineModelMock.getPurchaseCreditMemoCode()).thenReturn(TEST_PURCHASE_CREDIT_MEMO_CODE);
        when(purchaseCreditMemoLineModelMock.getItemCode()).thenReturn(TEST_ITEM_CODE);
        when(purchaseCreditMemoLineModelMock.getQuantity()).thenReturn(TEST_QTY);
        when(purchaseCreditMemoLineModelMock.getPrice()).thenReturn(TEST_ITEM_PURCHASE_PRICE);
        when(purchaseCreditMemoLineModelMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(purchaseCreditMemoMock.getCode()).thenReturn(TEST_PURCHASE_CREDIT_MEMO_CODE);

        when(purchaseCreditMemoLineMock.getPurchaseDocumentCode()).thenReturn(TEST_PURCHASE_CREDIT_MEMO_CODE);
        when(purchaseCreditMemoLineMock.getLineNo()).thenReturn(TEST_LINE_NO);
        when(purchaseCreditMemoLineMock.getItemCode()).thenReturn(TEST_ITEM_CODE);
        when(purchaseCreditMemoLineMock.getItemName()).thenReturn(TEST_ITEM_NAME);
        when(purchaseCreditMemoLineMock.getQuantity()).thenReturn(TEST_QTY);
        when(purchaseCreditMemoLineMock.getPrice()).thenReturn(TEST_PRICE);
        when(purchaseCreditMemoLineMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(svcMock.get(TEST_PURCHASE_CREDIT_MEMO_CODE, TEST_LINE_NO)).thenReturn(purchaseCreditMemoLineMock);

        controller = new PurchaseCreditMemoLineController(svcMock, itemSvcMock);
    }

    @Test
    public void wizard_returnsProperTemplate_whenCalledForNew(){
        String template = controller.wizard(TEST_PURCHASE_CREDIT_MEMO_CODE, null, modelMock);

        assertEquals("purchaseCreditMemoLineWizardFirstPage.html", template);

        verify(modelMock).addAttribute(eq("purchaseCreditMemoLineModel"), purchaseCreditMemoLineModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("items", items);

        PurchaseCreditMemoLineModel purchaseCreditMemoLineModel = purchaseCreditMemoLineModelArgumentCaptor.getValue();

        assertEquals(TEST_PURCHASE_CREDIT_MEMO_CODE, purchaseCreditMemoLineModel.getPurchaseCreditMemoCode());
        assertEquals(0, purchaseCreditMemoLineModel.getLineNo());
        assertEquals("", purchaseCreditMemoLineModel.getItemCode());
        assertEquals("", purchaseCreditMemoLineModel.getItemName());
        assertEquals(0, purchaseCreditMemoLineModel.getQuantity());
        assertEquals(0, purchaseCreditMemoLineModel.getPrice());
        assertEquals(0, purchaseCreditMemoLineModel.getAmount());
    }

    @Test
    public void wizard_returnsProperTemplate_whenCalledForExisting(){
        String template = controller.wizard(TEST_PURCHASE_CREDIT_MEMO_CODE, TEST_LINE_NO, modelMock);

        assertEquals("purchaseCreditMemoLineWizardFirstPage.html", template);

        verify(modelMock).addAttribute(eq("purchaseCreditMemoLineModel"), purchaseCreditMemoLineModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("items", items);

        PurchaseCreditMemoLineModel purchaseCreditMemoLineModel = purchaseCreditMemoLineModelArgumentCaptor.getValue();

        assertEquals(TEST_PURCHASE_CREDIT_MEMO_CODE, purchaseCreditMemoLineModel.getPurchaseCreditMemoCode());
        assertEquals(TEST_LINE_NO, purchaseCreditMemoLineModel.getLineNo());
        assertEquals(TEST_ITEM_CODE, purchaseCreditMemoLineModel.getItemCode());
        assertEquals(TEST_ITEM_NAME, purchaseCreditMemoLineModel.getItemName());
        assertEquals(TEST_QTY, purchaseCreditMemoLineModel.getQuantity());
        assertEquals(TEST_PRICE, purchaseCreditMemoLineModel.getPrice());
        assertEquals(TEST_AMOUNT, purchaseCreditMemoLineModel.getAmount());
    }

    @Test
    public void postPurchaseCreditMemoLineWizardFirstPage_returnsProperTemplate(){
        String template = controller.postPurchaseCreditMemoLineWizardFirstPage(purchaseCreditMemoLineModelMock, modelMock);

        assertEquals("purchaseCreditMemoLineWizardSecondPage.html", template);

        verify(modelMock).addAttribute("purchaseCreditMemoLineModel", purchaseCreditMemoLineModelMock);

        verify(purchaseCreditMemoLineModelMock).setItemName(TEST_ITEM_NAME);
        verify(purchaseCreditMemoLineModelMock).setPrice(TEST_ITEM_PURCHASE_PRICE);
        verify(purchaseCreditMemoLineModelMock).setAmount(TEST_QTY * TEST_ITEM_PURCHASE_PRICE);
    }

    @Test
    public void postPurchaseCreditMemoLineWizardSecondPage_returnsProperTemplate() {
        String template = controller.postPurchaseCreditMemoLineWizardSecondPage(purchaseCreditMemoLineModelMock, modelMock);

        assertEquals("purchaseCreditMemoLineWizardThirdPage.html", template);

        verify(modelMock).addAttribute("purchaseCreditMemoLineModel", purchaseCreditMemoLineModelMock);

        verify(purchaseCreditMemoLineModelMock).setAmount(TEST_QTY * TEST_ITEM_PURCHASE_PRICE);
    }

    @Test
    public void postPurchaseCreditMemoLineWizardThirdPage_createsEntityAndReturnsProperTemplate_whenCalledForNew(){
        RedirectView redirectView = controller.postPurchaseCreditMemoLineWizardThirdPage(purchaseCreditMemoLineModelMock, redirectAttributesMock);

        assertEquals("/purchaseCreditMemoCard", redirectView.getUrl());

        verify(redirectAttributesMock).addAttribute("code", TEST_PURCHASE_CREDIT_MEMO_CODE);

        verify(svcMock).create(eq(TEST_PURCHASE_CREDIT_MEMO_CODE), purchaseCreditMemoLineArgumentCaptor.capture());

        PurchaseDocumentLineDTO purchaseCreditMemoLine = purchaseCreditMemoLineArgumentCaptor.getValue();

        assertEquals(TEST_ITEM_CODE, purchaseCreditMemoLine.getItemCode());
        assertEquals(TEST_QTY, purchaseCreditMemoLine.getQuantity());
        assertEquals(TEST_ITEM_PURCHASE_PRICE, purchaseCreditMemoLine.getPrice());
        assertEquals(TEST_AMOUNT, purchaseCreditMemoLine.getAmount());
    }

    @Test
    public void postPurchaseCreditMemoLineWizardThirdPage_updatesEntityAndReturnsProperTemplate_whenCalledForExisting(){
        when(purchaseCreditMemoLineModelMock.getLineNo()).thenReturn(TEST_LINE_NO);
        
        RedirectView redirectView = controller.postPurchaseCreditMemoLineWizardThirdPage(purchaseCreditMemoLineModelMock, redirectAttributesMock);

        assertEquals("/purchaseCreditMemoCard", redirectView.getUrl());

        verify(redirectAttributesMock).addAttribute("code", TEST_PURCHASE_CREDIT_MEMO_CODE);

        verify(svcMock).update(eq(TEST_PURCHASE_CREDIT_MEMO_CODE), eq(TEST_LINE_NO), purchaseCreditMemoLineArgumentCaptor.capture());

        PurchaseDocumentLineDTO purchaseCreditMemoLine = purchaseCreditMemoLineArgumentCaptor.getValue();

        assertEquals(TEST_ITEM_CODE, purchaseCreditMemoLine.getItemCode());
        assertEquals(TEST_QTY, purchaseCreditMemoLine.getQuantity());
        assertEquals(TEST_PRICE, purchaseCreditMemoLine.getPrice());
        assertEquals(TEST_AMOUNT, purchaseCreditMemoLine.getAmount());
    }

    @Test
    public void delete_deletesEntity(){
        RedirectView redirectView = controller.delete(TEST_PURCHASE_CREDIT_MEMO_CODE, TEST_LINE_NO, redirectAttributesMock);

        assertEquals("/purchaseCreditMemoCard", redirectView.getUrl());

        verify(redirectAttributesMock).addAttribute("code", TEST_PURCHASE_CREDIT_MEMO_CODE);

        verify(svcMock).delete(TEST_PURCHASE_CREDIT_MEMO_CODE, TEST_LINE_NO);
    }
}
