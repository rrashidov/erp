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
import org.roko.erp.frontend.controllers.model.SalesCreditMemoLineModel;
import org.roko.erp.frontend.model.Item;
import org.roko.erp.frontend.model.SalesCreditMemo;
import org.roko.erp.frontend.model.SalesCreditMemoLine;
import org.roko.erp.frontend.model.jpa.SalesCreditMemoLineId;
import org.roko.erp.frontend.services.ItemService;
import org.roko.erp.frontend.services.SalesCreditMemoLineService;
import org.roko.erp.frontend.services.SalesCreditMemoService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

public class SalesCreditMemoLineControllerTest {

    private static final String TEST_SALES_CREDIT_MEMO_CODE = "test-sales-credit-memo-code";

    private static final String TEST_ITEM_CODE = "test-item-code";
    private static final String TEST_ITEM_NAME = "test-item-name";
    private static final double TEST_ITEM_SALES_PRICE = 12.00d;
    private static final Double TEST_QTY = 10.00d;

    private static final int TEST_LINE_NO = 123;
    private static final int TEST_LINE_COUNT = 234;

    private static final Double TEST_PRICE = 12.00d;
    private static final Double TEST_AMOUNT = 120.00d;

    private List<Item> itemList = new ArrayList<>();

    @Captor
    private ArgumentCaptor<SalesCreditMemoLineModel> salesCreditMemoLineModelArgumentCaptor;

    @Captor
    private ArgumentCaptor<SalesCreditMemoLine> salesCreditMemoLineArgumentCaptor;

    @Captor
    private ArgumentCaptor<SalesCreditMemoLineId> salesCreditMemoLineIdArgumentCaptor;

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
    private SalesCreditMemoLine salesCreditMemoLineMock;

    @Mock
    private SalesCreditMemoLineService svcMock;

    private SalesCreditMemoLineController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        SalesCreditMemoLineId salesCreditMemoLineId = new SalesCreditMemoLineId();
        salesCreditMemoLineId.setLineNo(TEST_LINE_NO);
        salesCreditMemoLineId.setSalesCreditMemo(salesCreditMemoMock);

        when(salesCreditMemoLineMock.getSalesCreditMemoLineId()).thenReturn(salesCreditMemoLineId);
        when(salesCreditMemoLineMock.getSalesCreditMemo()).thenReturn(salesCreditMemoMock);
        when(salesCreditMemoLineMock.getItem()).thenReturn(itemMock);
        when(salesCreditMemoLineMock.getQuantity()).thenReturn(TEST_QTY);
        when(salesCreditMemoLineMock.getPrice()).thenReturn(TEST_PRICE);
        when(salesCreditMemoLineMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(itemMock.getCode()).thenReturn(TEST_ITEM_CODE);
        when(itemMock.getName()).thenReturn(TEST_ITEM_NAME);
        when(itemMock.getSalesPrice()).thenReturn(TEST_ITEM_SALES_PRICE);

        //when(itemSvcMock.list()).thenReturn(itemList);
        //when(itemSvcMock.get(TEST_ITEM_CODE)).thenReturn(itemMock);

        when(salesCreditMemoSvcMock.get(TEST_SALES_CREDIT_MEMO_CODE)).thenReturn(salesCreditMemoMock);

        when(salesCreditMemoLineModelMock.getSalesCreditMemoCode()).thenReturn(TEST_SALES_CREDIT_MEMO_CODE);
        when(salesCreditMemoLineModelMock.getItemCode()).thenReturn(TEST_ITEM_CODE);
        when(salesCreditMemoLineModelMock.getQuantity()).thenReturn(TEST_QTY);
        when(salesCreditMemoLineModelMock.getPrice()).thenReturn(TEST_ITEM_SALES_PRICE);
        when(salesCreditMemoLineModelMock.getAmount()).thenReturn(TEST_QTY * TEST_ITEM_SALES_PRICE);

        when(svcMock.maxLineNo(salesCreditMemoMock)).thenReturn(TEST_LINE_COUNT);
        when(svcMock.get(salesCreditMemoLineId)).thenReturn(salesCreditMemoLineMock);

        controller = new SalesCreditMemoLineController(svcMock, salesCreditMemoSvcMock, itemSvcMock);
    }

    // @Test
    // public void wizard_returnsProperTemplate_whenCalledForNew() {
    //     String template = controller.wizard(TEST_SALES_CREDIT_MEMO_CODE, null, modelMock);

    //     assertEquals("salesCreditMemoLineWizardFirstPage.html", template);

    //     verify(modelMock).addAttribute("items", itemList);
    // }

    // // @Test
    // // public void wizard_returnsProperTemplate_whenCalledForexisting() {
    // //     String template = controller.wizard(TEST_SALES_CREDIT_MEMO_CODE, TEST_LINE_NO, modelMock);

    // //     assertEquals("salesCreditMemoLineWizardFirstPage.html", template);

    // //     verify(modelMock).addAttribute("items", itemList);

    // //     verify(modelMock).addAttribute(eq("salesCreditMemoLineModel"),
    // //             salesCreditMemoLineModelArgumentCaptor.capture());

    // //     SalesCreditMemoLineModel salesCreditMemoLineModel = salesCreditMemoLineModelArgumentCaptor.getValue();

    // //     assertEquals(TEST_LINE_NO, salesCreditMemoLineModel.getLineNo());
    // //     assertEquals(TEST_SALES_CREDIT_MEMO_CODE, salesCreditMemoLineModel.getSalesCreditMemoCode());
    // //     assertEquals(TEST_ITEM_CODE, salesCreditMemoLineModel.getItemCode());
    // //     assertEquals(TEST_ITEM_NAME, salesCreditMemoLineModel.getItemName());
    // //     assertEquals(TEST_PRICE, salesCreditMemoLineModel.getPrice());
    // //     assertEquals(TEST_QTY, salesCreditMemoLineModel.getQuantity());
    // //     assertEquals(TEST_AMOUNT, salesCreditMemoLineModel.getAmount());
    // // }

    // // @Test
    // // public void postingWizardFirstPage_returnsProperTemplate() {
    // //     String template = controller.postSalesCreditMemoLineWizardFirstPage(salesCreditMemoLineModelMock, modelMock);

    // //     assertEquals("salesCreditMemoLineWizardSecondPage.html", template);

    // //     verify(modelMock).addAttribute("salesCreditMemoLineModel", salesCreditMemoLineModelMock);

    // //     verify(salesCreditMemoLineModelMock).setItemName(TEST_ITEM_NAME);
    // //     verify(salesCreditMemoLineModelMock).setPrice(TEST_ITEM_SALES_PRICE);
    // // }

    // @Test
    // public void postingWizardSecondPage_returnsProperTemplate() {
    //     String template = controller.postSalesCreditMemoLineWizardSecondPage(salesCreditMemoLineModelMock, modelMock);

    //     assertEquals("salesCreditMemoLineWizardThirdPage.html", template);

    //     verify(modelMock).addAttribute("salesCreditMemoLineModel", salesCreditMemoLineModelMock);

    //     verify(salesCreditMemoLineModelMock).setAmount(TEST_ITEM_SALES_PRICE * TEST_QTY);
    // }

    // @Test
    // public void postingWizardThirdPage_createsNewEntity() {
    //     RedirectView redirectView = controller.postSalesCreditMemoLineWizardThirdPage(salesCreditMemoLineModelMock,
    //             redirectAttributesMock);

    //     assertEquals("/salesCreditMemoCard", redirectView.getUrl());

    //     verify(redirectAttributesMock).addAttribute("code", TEST_SALES_CREDIT_MEMO_CODE);

    //     verify(svcMock).create(salesCreditMemoLineArgumentCaptor.capture());

    //     SalesCreditMemoLine createdSalesCreditMemoLine = salesCreditMemoLineArgumentCaptor.getValue();

    //     assertEquals(salesCreditMemoMock, createdSalesCreditMemoLine.getSalesCreditMemoLineId().getSalesCreditMemo());
    //     assertEquals(TEST_LINE_COUNT + 1, createdSalesCreditMemoLine.getSalesCreditMemoLineId().getLineNo());

    //     assertEquals(itemMock, createdSalesCreditMemoLine.getItem());
    //     assertEquals(TEST_QTY, createdSalesCreditMemoLine.getQuantity());
    //     assertEquals(TEST_ITEM_SALES_PRICE, createdSalesCreditMemoLine.getPrice());
    //     assertEquals(TEST_QTY * TEST_ITEM_SALES_PRICE, createdSalesCreditMemoLine.getAmount());
    // }

    // @Test
    // public void postingWizardThirdPage_updatesExistingEntity_whenCalledWithLineNo() {
    //     when(salesCreditMemoLineModelMock.getLineNo()).thenReturn(TEST_LINE_NO);

    //     RedirectView redirectView = controller.postSalesCreditMemoLineWizardThirdPage(salesCreditMemoLineModelMock,
    //             redirectAttributesMock);

    //     assertEquals("/salesCreditMemoCard", redirectView.getUrl());

    //     verify(redirectAttributesMock).addAttribute("code", TEST_SALES_CREDIT_MEMO_CODE);

    //     verify(svcMock).update(salesCreditMemoLineIdArgumentCaptor.capture(), salesCreditMemoLineArgumentCaptor.capture());

    //     SalesCreditMemoLineId salesCreditMemoLineId = salesCreditMemoLineIdArgumentCaptor.getValue();

    //     assertEquals(TEST_LINE_NO, salesCreditMemoLineId.getLineNo());
    //     assertEquals(salesCreditMemoMock, salesCreditMemoLineId.getSalesCreditMemo());
    // }

    // @Test
    // public void deleting_deletesEntity(){
    //     RedirectView redirectView = controller.delete(TEST_SALES_CREDIT_MEMO_CODE, TEST_LINE_NO, redirectAttributesMock);

    //     assertEquals("/salesCreditMemoCard", redirectView.getUrl());

    //     verify(svcMock).delete(salesCreditMemoLineIdArgumentCaptor.capture());

    //     SalesCreditMemoLineId deletedSalesCreditMemoLineId = salesCreditMemoLineIdArgumentCaptor.getValue();

    //     assertEquals(salesCreditMemoMock, deletedSalesCreditMemoLineId.getSalesCreditMemo());
    //     assertEquals(TEST_LINE_NO, deletedSalesCreditMemoLineId.getLineNo());
    // }

}
