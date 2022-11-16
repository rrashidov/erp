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
import org.roko.erp.controllers.model.PurchaseCreditMemoLineModel;
import org.roko.erp.model.Item;
import org.roko.erp.model.PurchaseCreditMemo;
import org.roko.erp.model.PurchaseCreditMemoLine;
import org.roko.erp.model.jpa.PurchaseCreditMemoLineId;
import org.roko.erp.services.ItemService;
import org.roko.erp.services.PurchaseCreditMemoLineService;
import org.roko.erp.services.PurchaseCreditMemoService;
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

    private static final int TEST_LINE_COUNT = 123;
    private static final int TEST_LINE_NO = 234;

    private List<Item> items = new ArrayList<>();

    @Captor
    private ArgumentCaptor<PurchaseCreditMemoLine> purchaseCreditMemoLineArgumentCaptor;

    @Captor
    private ArgumentCaptor<PurchaseCreditMemoLineModel> purchaseCreditMemoLineModelArgumentCaptor;

    @Captor
    private ArgumentCaptor<PurchaseCreditMemoLineId> purchaseCreditMemoLineIdArgumentCaptor;

    @Mock
    private PurchaseCreditMemoLine purchaseCreditMemoLineMock;

    @Mock
    private PurchaseCreditMemo purchaseCreditMemoMock;

    @Mock
    private RedirectAttributes redirectAttributesMock;

    @Mock
    private Item itemMock;

    @Mock
    private PurchaseCreditMemoLineModel purchaseCreditMemoLineModelMock;

    @Mock
    private Model modelMock;

    @Mock
    private PurchaseCreditMemoLineService svcMock;

    @Mock
    private ItemService itemSvcMock;

    @Mock
    private PurchaseCreditMemoService purchaseCreditMemoSvcMock;

    private PurchaseCreditMemoLineController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        PurchaseCreditMemoLineId purchaseCreditMemoLineId = new PurchaseCreditMemoLineId();
        purchaseCreditMemoLineId.setLineNo(TEST_LINE_NO);
        purchaseCreditMemoLineId.setPurchaseCreditMemo(purchaseCreditMemoMock);

        when(itemMock.getCode()).thenReturn(TEST_ITEM_CODE);
        when(itemMock.getName()).thenReturn(TEST_ITEM_NAME);
        when(itemMock.getPurchasePrice()).thenReturn(TEST_ITEM_PURCHASE_PRICE);

        when(itemSvcMock.list()).thenReturn(items);
        when(itemSvcMock.get(TEST_ITEM_CODE)).thenReturn(itemMock);

        when(purchaseCreditMemoLineModelMock.getPurchaseCreditMemoCode()).thenReturn(TEST_PURCHASE_CREDIT_MEMO_CODE);
        when(purchaseCreditMemoLineModelMock.getItemCode()).thenReturn(TEST_ITEM_CODE);
        when(purchaseCreditMemoLineModelMock.getQuantity()).thenReturn(TEST_QTY);
        when(purchaseCreditMemoLineModelMock.getPrice()).thenReturn(TEST_ITEM_PURCHASE_PRICE);
        when(purchaseCreditMemoLineModelMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(purchaseCreditMemoMock.getCode()).thenReturn(TEST_PURCHASE_CREDIT_MEMO_CODE);

        when(purchaseCreditMemoSvcMock.get(TEST_PURCHASE_CREDIT_MEMO_CODE)).thenReturn(purchaseCreditMemoMock);

        when(purchaseCreditMemoLineMock.getPurchaseCreditMemoLineId()).thenReturn(purchaseCreditMemoLineId);
        when(purchaseCreditMemoLineMock.getPurchaseCreditMemo()).thenReturn(purchaseCreditMemoMock);
        when(purchaseCreditMemoLineMock.getItem()).thenReturn(itemMock);
        when(purchaseCreditMemoLineMock.getQuantity()).thenReturn(TEST_QTY);
        when(purchaseCreditMemoLineMock.getPrice()).thenReturn(TEST_PRICE);
        when(purchaseCreditMemoLineMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(svcMock.count(purchaseCreditMemoMock)).thenReturn(TEST_LINE_COUNT);
        when(svcMock.get(purchaseCreditMemoLineId)).thenReturn(purchaseCreditMemoLineMock);

        controller = new PurchaseCreditMemoLineController(svcMock, purchaseCreditMemoSvcMock, itemSvcMock);
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

        verify(svcMock).create(purchaseCreditMemoLineArgumentCaptor.capture());

        PurchaseCreditMemoLine purchaseCreditMemoLine = purchaseCreditMemoLineArgumentCaptor.getValue();

        assertEquals(TEST_PURCHASE_CREDIT_MEMO_CODE, purchaseCreditMemoLine.getPurchaseCreditMemoLineId().getPurchaseCreditMemo().getCode());
        assertEquals(TEST_LINE_COUNT + 1, purchaseCreditMemoLine.getPurchaseCreditMemoLineId().getLineNo());
        assertEquals(itemMock, purchaseCreditMemoLine.getItem());
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

        verify(svcMock).update(purchaseCreditMemoLineIdArgumentCaptor.capture(), purchaseCreditMemoLineArgumentCaptor.capture());

        PurchaseCreditMemoLineId purchaseCreditMemoLineId = purchaseCreditMemoLineIdArgumentCaptor.getValue();

        assertEquals(purchaseCreditMemoMock, purchaseCreditMemoLineId.getPurchaseCreditMemo());
        assertEquals(TEST_LINE_NO, purchaseCreditMemoLineId.getLineNo());

        PurchaseCreditMemoLine purchaseCreditMemoLine = purchaseCreditMemoLineArgumentCaptor.getValue();

        assertEquals(TEST_PURCHASE_CREDIT_MEMO_CODE, purchaseCreditMemoLine.getPurchaseCreditMemoLineId().getPurchaseCreditMemo().getCode());
        assertEquals(TEST_LINE_NO, purchaseCreditMemoLine.getPurchaseCreditMemoLineId().getLineNo());
        assertEquals(itemMock, purchaseCreditMemoLine.getItem());
        assertEquals(TEST_QTY, purchaseCreditMemoLine.getQuantity());
        assertEquals(TEST_PRICE, purchaseCreditMemoLine.getPrice());
        assertEquals(TEST_AMOUNT, purchaseCreditMemoLine.getAmount());
    }

    @Test
    public void delete_deletesEntity(){
        RedirectView redirectView = controller.delete(TEST_PURCHASE_CREDIT_MEMO_CODE, TEST_LINE_NO, redirectAttributesMock);

        assertEquals("/purchaseCreditMemoCard", redirectView.getUrl());

        verify(redirectAttributesMock).addAttribute("code", TEST_PURCHASE_CREDIT_MEMO_CODE);

        verify(svcMock).delete(purchaseCreditMemoLineIdArgumentCaptor.capture());

        PurchaseCreditMemoLineId purchaseCreditMemoLineId = purchaseCreditMemoLineIdArgumentCaptor.getValue();

        assertEquals(purchaseCreditMemoMock, purchaseCreditMemoLineId.getPurchaseCreditMemo());
        assertEquals(TEST_LINE_NO, purchaseCreditMemoLineId.getLineNo());
    }
}
