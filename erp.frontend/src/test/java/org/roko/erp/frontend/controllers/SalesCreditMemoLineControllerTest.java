package org.roko.erp.frontend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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
    private static final BigDecimal TEST_ITEM_SALES_PRICE = new BigDecimal(12);
    private static final BigDecimal TEST_QTY = new BigDecimal(10);

    private static final int TEST_LINE_NO = 123;

    private static final BigDecimal TEST_PRICE = new BigDecimal(12);
    private static final BigDecimal TEST_AMOUNT = new BigDecimal(120);

    private static final int TEST_PAGE = 12;

    private List<ItemDTO> items = new ArrayList<>();

    @Captor
    private ArgumentCaptor<SalesDocumentLineDTO> salesCreditMemoLineModelArgumentCaptor;

    @Captor
    private ArgumentCaptor<SalesDocumentLineDTO> salesCreditMemoLineArgumentCaptor;

    @Mock
    private Model modelMock;

    @Mock
    private SalesDocumentLineDTO salesCreditMemoLineModelMock;

    @Mock
    private ItemDTO itemMock;

    @Mock
    private ItemService itemSvcMock;

    @Mock
    private ItemList itemList;

    @Mock
    private SalesDocumentDTO salesCreditMemoMock;

    @Mock
    private SalesCreditMemoService salesCreditMemoSvcMock;

    @Mock
    private RedirectAttributes redirectAttributesMock;

    @Mock
    private SalesDocumentLineDTO salesCreditMemoLineMock;

    @Mock
    private SalesCreditMemoLineService svcMock;

    private SalesCreditMemoLineController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(salesCreditMemoLineMock.getSalesDocumentCode()).thenReturn(TEST_SALES_CREDIT_MEMO_CODE);
        when(salesCreditMemoLineMock.getLineNo()).thenReturn(TEST_LINE_NO);
        when(salesCreditMemoLineMock.getPage()).thenReturn(TEST_PAGE);
        when(salesCreditMemoLineMock.getItemCode()).thenReturn(TEST_ITEM_CODE);
        when(salesCreditMemoLineMock.getItemName()).thenReturn(TEST_ITEM_NAME);
        when(salesCreditMemoLineMock.getQuantity()).thenReturn(TEST_QTY);
        when(salesCreditMemoLineMock.getPrice()).thenReturn(TEST_PRICE);
        when(salesCreditMemoLineMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(itemMock.getCode()).thenReturn(TEST_ITEM_CODE);
        when(itemMock.getName()).thenReturn(TEST_ITEM_NAME);
        when(itemMock.getSalesPrice()).thenReturn(TEST_ITEM_SALES_PRICE);

        when(itemList.getData()).thenReturn(items);
        when(itemSvcMock.list()).thenReturn(itemList);
        when(itemSvcMock.get(TEST_ITEM_CODE)).thenReturn(itemMock);

        when(salesCreditMemoSvcMock.get(TEST_SALES_CREDIT_MEMO_CODE)).thenReturn(salesCreditMemoMock);

        when(salesCreditMemoLineModelMock.getSalesDocumentCode()).thenReturn(TEST_SALES_CREDIT_MEMO_CODE);
        when(salesCreditMemoLineModelMock.getPage()).thenReturn(TEST_PAGE);
        when(salesCreditMemoLineModelMock.getItemCode()).thenReturn(TEST_ITEM_CODE);
        when(salesCreditMemoLineModelMock.getQuantity()).thenReturn(TEST_QTY);
        when(salesCreditMemoLineModelMock.getPrice()).thenReturn(TEST_ITEM_SALES_PRICE);
        when(salesCreditMemoLineModelMock.getAmount()).thenReturn(TEST_QTY.multiply(TEST_ITEM_SALES_PRICE));

        when(svcMock.get(TEST_SALES_CREDIT_MEMO_CODE, TEST_LINE_NO)).thenReturn(salesCreditMemoLineMock);

        controller = new SalesCreditMemoLineController(svcMock, itemSvcMock);
    }

    @Test
    public void wizard_returnsProperTemplate_whenCalledForNew() {
        String template = controller.wizard(TEST_SALES_CREDIT_MEMO_CODE, null, TEST_PAGE, modelMock);

        assertEquals("salesCreditMemoLineWizardFirstPage.html", template);

        verify(modelMock).addAttribute("items", items);
    }

    @Test
    public void wizard_returnsProperTemplate_whenCalledForexisting() {
        String template = controller.wizard(TEST_SALES_CREDIT_MEMO_CODE, TEST_LINE_NO, TEST_PAGE, modelMock);

        assertEquals("salesCreditMemoLineWizardFirstPage.html", template);

        verify(modelMock).addAttribute("items", items);

        verify(modelMock).addAttribute(eq("salesCreditMemoLineModel"),
                salesCreditMemoLineModelArgumentCaptor.capture());

        SalesDocumentLineDTO salesCreditMemoLineModel = salesCreditMemoLineModelArgumentCaptor.getValue();

        assertEquals(TEST_LINE_NO, salesCreditMemoLineModel.getLineNo());
        assertEquals(TEST_PAGE, salesCreditMemoLineModel.getPage());
        assertEquals(TEST_SALES_CREDIT_MEMO_CODE, salesCreditMemoLineModel.getSalesDocumentCode());
        assertEquals(TEST_ITEM_CODE, salesCreditMemoLineModel.getItemCode());
        assertEquals(TEST_ITEM_NAME, salesCreditMemoLineModel.getItemName());
        assertEquals(TEST_PRICE, salesCreditMemoLineModel.getPrice());
        assertEquals(TEST_QTY, salesCreditMemoLineModel.getQuantity());
        assertEquals(TEST_AMOUNT, salesCreditMemoLineModel.getAmount());
    }

    @Test
    public void postingWizardFirstPage_returnsProperTemplate() {
        String template = controller.postSalesCreditMemoLineWizardFirstPage(salesCreditMemoLineModelMock, modelMock);

        assertEquals("salesCreditMemoLineWizardSecondPage.html", template);

        verify(modelMock).addAttribute("salesCreditMemoLineModel", salesCreditMemoLineModelMock);

        verify(salesCreditMemoLineModelMock).setItemName(TEST_ITEM_NAME);
        verify(salesCreditMemoLineModelMock).setPrice(TEST_ITEM_SALES_PRICE);
    }

    @Test
    public void postingWizardSecondPage_returnsProperTemplate() {
        String template = controller.postSalesCreditMemoLineWizardSecondPage(salesCreditMemoLineModelMock, modelMock);

        assertEquals("salesCreditMemoLineWizardThirdPage.html", template);

        verify(modelMock).addAttribute("salesCreditMemoLineModel", salesCreditMemoLineModelMock);

        verify(salesCreditMemoLineModelMock).setAmount(TEST_ITEM_SALES_PRICE.multiply(TEST_QTY));
    }

    @Test
    public void postingWizardThirdPage_createsNewEntity() {
        RedirectView redirectView = controller.postSalesCreditMemoLineWizardThirdPage(salesCreditMemoLineModelMock,
                redirectAttributesMock);

        assertEquals("/salesCreditMemoCard", redirectView.getUrl());

        verify(redirectAttributesMock).addAttribute("code", TEST_SALES_CREDIT_MEMO_CODE);
        verify(redirectAttributesMock).addAttribute("page", TEST_PAGE);

        verify(svcMock).create(eq(TEST_SALES_CREDIT_MEMO_CODE), salesCreditMemoLineArgumentCaptor.capture());

        SalesDocumentLineDTO createdSalesCreditMemoLine = salesCreditMemoLineArgumentCaptor.getValue();

        assertEquals(TEST_ITEM_CODE, createdSalesCreditMemoLine.getItemCode());
        assertEquals(TEST_QTY, createdSalesCreditMemoLine.getQuantity());
        assertEquals(TEST_ITEM_SALES_PRICE, createdSalesCreditMemoLine.getPrice());
        assertEquals(TEST_QTY.multiply(TEST_ITEM_SALES_PRICE), createdSalesCreditMemoLine.getAmount());
    }

    @Test
    public void postingWizardThirdPage_updatesExistingEntity_whenCalledWithLineNo() {
        when(salesCreditMemoLineModelMock.getLineNo()).thenReturn(TEST_LINE_NO);

        RedirectView redirectView = controller.postSalesCreditMemoLineWizardThirdPage(salesCreditMemoLineModelMock,
                redirectAttributesMock);

        assertEquals("/salesCreditMemoCard", redirectView.getUrl());

        verify(redirectAttributesMock).addAttribute("code", TEST_SALES_CREDIT_MEMO_CODE);

        verify(svcMock).update(eq(TEST_SALES_CREDIT_MEMO_CODE), eq(TEST_LINE_NO), salesCreditMemoLineArgumentCaptor.capture());

        SalesDocumentLineDTO createdSalesCreditMemoLine = salesCreditMemoLineArgumentCaptor.getValue();

        assertEquals(TEST_ITEM_CODE, createdSalesCreditMemoLine.getItemCode());
        assertEquals(TEST_QTY, createdSalesCreditMemoLine.getQuantity());
        assertEquals(TEST_ITEM_SALES_PRICE, createdSalesCreditMemoLine.getPrice());
        assertEquals(TEST_QTY.multiply(TEST_ITEM_SALES_PRICE), createdSalesCreditMemoLine.getAmount());
    }

    @Test
    public void deleting_deletesEntity() {
        RedirectView redirectView = controller.delete(TEST_SALES_CREDIT_MEMO_CODE, TEST_LINE_NO, TEST_PAGE,
                redirectAttributesMock);

        assertEquals("/salesCreditMemoCard", redirectView.getUrl());

        verify(svcMock).delete(TEST_SALES_CREDIT_MEMO_CODE, TEST_LINE_NO);

        verify(redirectAttributesMock).addAttribute("page", TEST_PAGE);
    }

}
