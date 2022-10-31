package org.roko.erp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.controllers.model.SalesOrderLineModel;
import org.roko.erp.controllers.model.SalesOrderModel;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.Customer;
import org.roko.erp.model.Item;
import org.roko.erp.model.PaymentMethod;
import org.roko.erp.model.SalesOrder;
import org.roko.erp.model.SalesOrderLine;
import org.roko.erp.model.jpa.SalesOrderLineId;
import org.roko.erp.services.CustomerService;
import org.roko.erp.services.ItemService;
import org.roko.erp.services.PaymentMethodService;
import org.roko.erp.services.SalesOrderLineService;
import org.roko.erp.services.SalesOrderService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

public class SalesOrderControllerTest {

    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";

    private static final String TEST_CODE = "test-code";
    private static final String TEST_CUSTOMER_CODE = "test-customer-code";
    private static final String TEST_CUSTOMER_NAME = "test-customer-name";

    private static final Long TEST_PAGE = 123l;

    private static final long TEST_COUNT = 234;
    private static final long TEST_SALES_ORDER_LINE_COUNT = 789l;

    private static final String TEST_ITEM_CODE = "test-item-code";
    private static final String TEST_ITEM_NAME = "test-item-name";
    private static final double TEST_ITEM_SALES_PRICE = 12.12d;

    private static final double TEST_SALES_ORDER_LINE_QTY = 10.0d;
    private static final double TEST_SALES_ORDER_LINE_AMOUNT = 123.89d;

    private static final Integer TEST_SALES_ORDER_LINENO = 1;

    private List<SalesOrder> salesOrderList = new ArrayList<>();

    private List<Customer> customerList = new ArrayList<>();

    private List<PaymentMethod> paymentMethodList = new ArrayList<>();

    private List<SalesOrderLine> salesOrderLineList = new ArrayList<>();

    private List<Item> itemList = new ArrayList<>();

    @Captor
    private ArgumentCaptor<SalesOrder> salesOrderArgumentCaptor;

    @Captor
    private ArgumentCaptor<SalesOrderModel> salesOrderModelArgumentCaptor;

    @Captor
    private ArgumentCaptor<SalesOrderLine> salesOrderLineArgumentCaptor;

    @Captor
    private ArgumentCaptor<SalesOrderLineModel> salesOrderLineModelArgumentCaptor;

    @Captor
    private ArgumentCaptor<SalesOrderLineId> salesOrderLineIdArgumentCaptor;

    @Mock
    private Date dateMock;

    @Mock
    private SalesOrder salesOrderMock;

    @Mock
    private PaymentMethod paymentMethodMock;

    @Mock
    private Customer customerMock;

    @Mock
    private Item itemMock;

    @Mock
    private SalesOrderModel salesOrderModelMock;

    @Mock
    private SalesOrderLineModel salesOrderLineModelMock;

    @Mock
    private SalesOrderLine salesOrderLineMock;

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private PagingData salesOrderLinePagingMock;

    @Mock
    private Model modelMock;

    @Mock
    private SalesOrderService svcMock;

    @Mock
    private PagingService pagingSvcMock;

    @Mock
    private CustomerService customerSvcMock;

    @Mock
    private PaymentMethodService paymentMethodSvc;

    @Mock
    private SalesOrderLineService salesOrderLineSvcMock;

    @Mock
    private RedirectAttributes redirectAttributesMock;

    @Mock
    private ItemService itemSvcMock;
    
    private SalesOrderController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(salesOrderMock.getCode()).thenReturn(TEST_CODE);
        when(salesOrderMock.getCustomer()).thenReturn(customerMock);
        when(salesOrderMock.getDate()).thenReturn(dateMock);
        when(salesOrderMock.getPaymentMethod()).thenReturn(paymentMethodMock);

        when(paymentMethodMock.getCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(customerMock.getCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(customerMock.getPaymentMethod()).thenReturn(paymentMethodMock);
        when(customerMock.getName()).thenReturn(TEST_CUSTOMER_NAME);

        when(salesOrderModelMock.getCode()).thenReturn("");
        when(salesOrderModelMock.getCustomerCode()).thenReturn(TEST_CUSTOMER_CODE);
        when(salesOrderModelMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(paymentMethodSvc.list()).thenReturn(paymentMethodList);
        when(paymentMethodSvc.get(TEST_PAYMENT_METHOD_CODE)).thenReturn(paymentMethodMock);

        when(customerSvcMock.list()).thenReturn(customerList);
        when(customerSvcMock.get(TEST_CUSTOMER_CODE)).thenReturn(customerMock);


        SalesOrderLineId salesOrderLineId = new SalesOrderLineId();
        salesOrderLineId.setSalesOrder(salesOrderMock);
        salesOrderLineId.setLineNo(TEST_SALES_ORDER_LINENO);

        when(salesOrderLineMock.getSalesOrderLineId()).thenReturn(salesOrderLineId);
        when(salesOrderLineMock.getSalesOrder()).thenReturn(salesOrderMock);
        when(salesOrderLineMock.getItem()).thenReturn(itemMock);
        when(salesOrderLineMock.getQuantity()).thenReturn(TEST_SALES_ORDER_LINE_QTY);
        when(salesOrderLineMock.getPrice()).thenReturn(TEST_ITEM_SALES_PRICE);
        when(salesOrderLineMock.getAmount()).thenReturn(TEST_SALES_ORDER_LINE_AMOUNT);

        when(salesOrderLineSvcMock.list(salesOrderMock)).thenReturn(salesOrderLineList);
        when(salesOrderLineSvcMock.count(salesOrderMock)).thenReturn(TEST_SALES_ORDER_LINE_COUNT);
        when(salesOrderLineSvcMock.get(salesOrderLineId)).thenReturn(salesOrderLineMock);

        when(salesOrderLineModelMock.getSalesOrderCode()).thenReturn(TEST_CODE);
        when(salesOrderLineModelMock.getItemCode()).thenReturn(TEST_ITEM_CODE);
        when(salesOrderLineModelMock.getQuantity()).thenReturn(TEST_SALES_ORDER_LINE_QTY);
        when(salesOrderLineModelMock.getPrice()).thenReturn(TEST_ITEM_SALES_PRICE);
        when(salesOrderLineModelMock.getAmount()).thenReturn(TEST_SALES_ORDER_LINE_AMOUNT);

        when(svcMock.list()).thenReturn(salesOrderList);
        when(svcMock.count()).thenReturn(TEST_COUNT);
        when(svcMock.get(TEST_CODE)).thenReturn(salesOrderMock);

        when(itemMock.getCode()).thenReturn(TEST_ITEM_CODE);
        when(itemMock.getName()).thenReturn(TEST_ITEM_NAME);
        when(itemMock.getSalesPrice()).thenReturn(TEST_ITEM_SALES_PRICE);

        when(itemSvcMock.list()).thenReturn(itemList);
        when(itemSvcMock.get(TEST_ITEM_CODE)).thenReturn(itemMock);

        when(pagingSvcMock.generate("salesOrder", TEST_PAGE, TEST_COUNT)).thenReturn(pagingDataMock);
        when(pagingSvcMock.generate("salesOrderLine", null, TEST_SALES_ORDER_LINE_COUNT)).thenReturn(salesOrderLinePagingMock);

        controller = new SalesOrderController(svcMock, pagingSvcMock, customerSvcMock, paymentMethodSvc, salesOrderLineSvcMock, itemSvcMock);
    }

    @Test
    public void listReturnsProperTemplate(){
        String template = controller.list(TEST_PAGE, modelMock);

        assertEquals("salesOrderList.html", template);

        verify(modelMock).addAttribute("salesOrders", salesOrderList);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }

    @Test
    public void salesOrderWizardReturnsProperTemplate_whenCalledForNewSalesOrder(){
        String template = controller.wizard(null, modelMock);

        assertEquals("salesOrderWizardFirstPage.html", template);

        verify(modelMock).addAttribute(eq("salesOrder"), salesOrderModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("customers", customerList);

        SalesOrderModel salesOrderModel = salesOrderModelArgumentCaptor.getValue();

        assertEquals("", salesOrderModel.getCode());
        assertEquals("", salesOrderModel.getCustomerCode());
    }

    @Test
    public void salesOrderWizardReturnsProperTemplate_whenCalledForExistingSalesOrder(){
        String template = controller.wizard(TEST_CODE, modelMock);

        assertEquals("salesOrderWizardFirstPage.html", template);

        verify(modelMock).addAttribute("customers", customerList);
        verify(modelMock).addAttribute(eq("salesOrder"), salesOrderModelArgumentCaptor.capture());

        SalesOrderModel salesOrderModel = salesOrderModelArgumentCaptor.getValue();

        assertEquals(TEST_CODE, salesOrderModel.getCode());
        assertEquals(TEST_CUSTOMER_CODE, salesOrderModel.getCustomerCode());
    }

    @Test
    public void postSalesOrderWizardFirstPage_returnsProperTemplate(){
        String template = controller.postWizardFirstPage(salesOrderModelMock, modelMock);

        assertEquals("salesOrderWizardSecondPage.html", template);

        verify(salesOrderModelMock).setPaymentMethodCode(TEST_PAYMENT_METHOD_CODE);
        verify(salesOrderModelMock).setCustomerName(TEST_CUSTOMER_NAME);

        verify(modelMock).addAttribute("salesOrder", salesOrderModelMock);
        verify(modelMock).addAttribute("paymentMethods", paymentMethodList);
    }

    @Test
    public void postSalesOrderWizardSecondPage_createsSalesOrder(){
        Date testDate = new Date();

        when(salesOrderModelMock.getDate()).thenReturn(testDate);
        
        RedirectView redirectView = controller.postWizardSecondPage(salesOrderModelMock);

        assertEquals("/salesOrderList", redirectView.getUrl());

        verify(svcMock).create(salesOrderArgumentCaptor.capture());

        SalesOrder salesOrder = salesOrderArgumentCaptor.getValue();

        assertEquals(customerMock, salesOrder.getCustomer());
        assertEquals(paymentMethodMock, salesOrder.getPaymentMethod());
        assertEquals(testDate, salesOrder.getDate());
    }

    @Test
    public void postSalesOrderWizardSecondPage_updatesSalesOrder_whenCalledWithExistingCode(){
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setCode(TEST_CODE);
        salesOrder.setCustomer(customerMock);
        salesOrder.setDate(new Date());
        salesOrder.setPaymentMethod(paymentMethodMock);

        when(svcMock.get(TEST_CODE)).thenReturn(salesOrder);
        
        Date testDate = new Date();
        when(salesOrderModelMock.getCode()).thenReturn(TEST_CODE);
        when(salesOrderModelMock.getDate()).thenReturn(testDate);
        
        RedirectView redirectView = controller.postWizardSecondPage(salesOrderModelMock);

        assertEquals("/salesOrderList", redirectView.getUrl());

        verify(svcMock).update(eq(TEST_CODE), salesOrderArgumentCaptor.capture());

        SalesOrder updatedSalesOrder = salesOrderArgumentCaptor.getValue();

        assertEquals(customerMock, updatedSalesOrder.getCustomer());
        assertEquals(paymentMethodMock, updatedSalesOrder.getPaymentMethod());
        assertEquals(testDate, updatedSalesOrder.getDate());
    }

    @Test
    public void deleteSalesOrder_deletesSalesOrder(){
        RedirectView redirectView = controller.delete(TEST_CODE);

        assertEquals("/salesOrderList", redirectView.getUrl());

        verify(svcMock).delete(TEST_CODE);
    }

    @Test
    public void salesOrderCard_returnsProperTemplate(){
        String template = controller.card(TEST_CODE, modelMock);

        assertEquals("salesOrderCard.html", template);

        verify(modelMock).addAttribute("salesOrder", salesOrderMock);
        verify(modelMock).addAttribute("salesOrderLines", salesOrderLineList);
        verify(modelMock).addAttribute("paging", salesOrderLinePagingMock);
    }

    @Test
    public void salesOrderLineWizard_returnsProperTemplate_whenCalledForNew(){
        String template = controller.salesOrderLineWizard(TEST_CODE, null, modelMock);

        assertEquals("salesOrderLineWizardFirstPage.html", template);

        verify(modelMock).addAttribute("items", itemList);
        verify(modelMock).addAttribute(eq("salesOrderLine"), salesOrderLineModelArgumentCaptor.capture());

        SalesOrderLineModel salesOrderLineModel = salesOrderLineModelArgumentCaptor.getValue();

        assertEquals(TEST_CODE, salesOrderLineModel.getSalesOrderCode());
        assertEquals("", salesOrderLineModel.getItemCode());
    }

    @Test
    public void salesOrderLineWizard_returnsProperTemplate_whenCalledForExisting(){
        String template = controller.salesOrderLineWizard(TEST_CODE, TEST_SALES_ORDER_LINENO, modelMock);

        assertEquals("salesOrderLineWizardFirstPage.html", template);

        verify(modelMock).addAttribute("items", itemList);
        verify(modelMock).addAttribute(eq("salesOrderLine"), salesOrderLineModelArgumentCaptor.capture());

        SalesOrderLineModel salesOrderLineModel = salesOrderLineModelArgumentCaptor.getValue();

        assertEquals(TEST_SALES_ORDER_LINENO, salesOrderLineModel.getLineNo());
        assertEquals(TEST_CODE, salesOrderLineModel.getSalesOrderCode());
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

        verify(salesOrderLineSvcMock).create(salesOrderLineArgumentCaptor.capture());

        SalesOrderLine createOrderLine = salesOrderLineArgumentCaptor.getValue();

        assertEquals(itemMock, createOrderLine.getItem());
        assertEquals(TEST_SALES_ORDER_LINE_COUNT + 1, createOrderLine.getSalesOrderLineId().getLineNo());
        assertEquals(salesOrderMock, createOrderLine.getSalesOrderLineId().getSalesOrder());
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

        verify(salesOrderLineSvcMock).update(salesOrderLineIdArgumentCaptor.capture(), salesOrderLineArgumentCaptor.capture());

        SalesOrderLineId salesOrderLineId = salesOrderLineIdArgumentCaptor.getValue();

        assertEquals(salesOrderMock, salesOrderLineId.getSalesOrder());
        assertEquals(TEST_SALES_ORDER_LINENO, salesOrderLineId.getLineNo());

        SalesOrderLine updatedSalesOrderLine = salesOrderLineArgumentCaptor.getValue();

        assertEquals(itemMock, updatedSalesOrderLine.getItem());
        assertEquals(TEST_SALES_ORDER_LINE_QTY, updatedSalesOrderLine.getQuantity());
        assertEquals(TEST_ITEM_SALES_PRICE, updatedSalesOrderLine.getPrice());
        assertEquals(TEST_SALES_ORDER_LINE_AMOUNT, updatedSalesOrderLine.getAmount());
    }

}
