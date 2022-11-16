package org.roko.erp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.roko.erp.controllers.model.PurchaseOrderModel;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.PaymentMethod;
import org.roko.erp.model.PurchaseOrder;
import org.roko.erp.model.PurchaseOrderLine;
import org.roko.erp.model.Vendor;
import org.roko.erp.services.PaymentMethodService;
import org.roko.erp.services.PurchaseOrderLineService;
import org.roko.erp.services.PurchaseOrderPostService;
import org.roko.erp.services.PurchaseOrderService;
import org.roko.erp.services.VendorService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

public class PurchaseOrderControllerTest {
    
    private static final String TEST_CODE = "test-purchase-order-code";

    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";

    private static final String TEST_VENDOR_CODE = "test-vendor-code";
    private static final String TEST_VENDOR_NAME = "test-vendor-name";

    private static final int TEST_PAGE = 123;
    private static final int TEST_COUNT = 234;

    private static final Date TEST_DATE = new Date();

    private static final int TEST_LINES_COUNT = 23;

    private List<PurchaseOrder> purchaseOrders = new ArrayList<>();

    private List<Vendor> vendors = new ArrayList<>();

    private List<PaymentMethod> paymentMethods = new ArrayList<>();

    private List<PurchaseOrderLine> purchaseOrderLines = new ArrayList<>();

    @Captor
    private ArgumentCaptor<PurchaseOrderModel> purchaseOrderModelArgumentCaptor;

    @Captor
    private ArgumentCaptor<PurchaseOrder> purchaseOrderArgumentCaptor;

    @Mock
    private RedirectAttributes redirectAttributesMock;

    @Mock
    private PagingData purchaseOrderLinePagingMock;

    @Mock
    private PurchaseOrder purchaseOrderMock;

    @Mock
    private PaymentMethod paymentMethodMock;

    @Mock
    private Vendor vendorMock;

    @Mock
    private PurchaseOrderModel purchaseOrderModelMock;

    @Mock
    private VendorService vendorSvcMock;

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private Model modelMock;

    @Mock
    private PurchaseOrderService svcMock;

    @Mock
    private PaymentMethodService paymentMethodSvcMock;

    @Mock
    private PurchaseOrderLineService purchaseOrderLineSvcMock;

    @Mock
    private PagingService pagingSvcMock;

    @Mock
    private PurchaseOrderPostService purchaseOrderPostSvcMock;

    private PurchaseOrderController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(purchaseOrderMock.getCode()).thenReturn(TEST_CODE);
        when(purchaseOrderMock.getVendor()).thenReturn(vendorMock);
        when(purchaseOrderMock.getPaymentMethod()).thenReturn(paymentMethodMock);
        when(purchaseOrderMock.getDate()).thenReturn(TEST_DATE);

        when(paymentMethodSvcMock.list()).thenReturn(paymentMethods);
        when(paymentMethodSvcMock.get(TEST_PAYMENT_METHOD_CODE)).thenReturn(paymentMethodMock);

        when(paymentMethodMock.getCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(vendorMock.getCode()).thenReturn(TEST_VENDOR_CODE);
        when(vendorMock.getName()).thenReturn(TEST_VENDOR_NAME);
        when(vendorMock.getPaymentMethod()).thenReturn(paymentMethodMock);

        when(purchaseOrderModelMock.getCode()).thenReturn("");
        when(purchaseOrderModelMock.getVendorCode()).thenReturn(TEST_VENDOR_CODE);
        when(purchaseOrderModelMock.getDate()).thenReturn(TEST_DATE);
        when(purchaseOrderModelMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(vendorSvcMock.list()).thenReturn(vendors);
        when(vendorSvcMock.get(TEST_VENDOR_CODE)).thenReturn(vendorMock);

        when(svcMock.list()).thenReturn(purchaseOrders);
        when(svcMock.count()).thenReturn(TEST_COUNT);
        when(svcMock.get(TEST_CODE)).thenReturn(purchaseOrderMock);

        when(purchaseOrderLineSvcMock.list(purchaseOrderMock)).thenReturn(purchaseOrderLines);
        when(purchaseOrderLineSvcMock.count(purchaseOrderMock)).thenReturn(TEST_LINES_COUNT);

        when(pagingSvcMock.generate("purchaseOrder", TEST_PAGE, TEST_COUNT)).thenReturn(pagingDataMock);
        when(pagingSvcMock.generate("purchaseOrderLine", 1, TEST_LINES_COUNT)).thenReturn(purchaseOrderLinePagingMock);

        controller = new PurchaseOrderController(svcMock, purchaseOrderLineSvcMock, vendorSvcMock, paymentMethodSvcMock, pagingSvcMock, purchaseOrderPostSvcMock);
    }

    @Test
    public void list_returnsProperTemplate(){
        String template = controller.list(TEST_PAGE, modelMock);

        assertEquals("purchaseOrderList.html", template);

        verify(modelMock).addAttribute("purchaseOrders", purchaseOrders);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }

    @Test
    public void wizard_returnsProperTemplate_whenCalledForNew(){
        String template = controller.wizard(null, modelMock);

        assertEquals("purchaseOrderWizardFirstPage.html", template);

        verify(modelMock).addAttribute(eq("purchaseOrderModel"), purchaseOrderModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("vendors", vendors);

        PurchaseOrderModel purchaseOrderModel = purchaseOrderModelArgumentCaptor.getValue();

        assertEquals("", purchaseOrderModel.getCode());
        assertEquals("", purchaseOrderModel.getVendorCode());
        assertEquals("", purchaseOrderModel.getVendorName());
        assertEquals("", purchaseOrderModel.getPaymentMethodCode());
    }

    @Test
    public void wizard_returnsProperTemplate_whenCalledForExisting(){
        String template = controller.wizard(TEST_CODE, modelMock);

        assertEquals("purchaseOrderWizardFirstPage.html", template);

        verify(modelMock).addAttribute(eq("purchaseOrderModel"), purchaseOrderModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("vendors", vendors);

        PurchaseOrderModel purchaseOrderModel = purchaseOrderModelArgumentCaptor.getValue();

        assertEquals(TEST_CODE, purchaseOrderModel.getCode());
        assertEquals(TEST_VENDOR_CODE, purchaseOrderModel.getVendorCode());
        assertEquals(TEST_VENDOR_NAME, purchaseOrderModel.getVendorName());
        assertEquals(TEST_PAYMENT_METHOD_CODE, purchaseOrderModel.getPaymentMethodCode());
    }

    @Test
    public void purchaseOrderWizardFirstPage_returnsProperTemplate(){
        String template = controller.postPurchaseOrderWizardFirstPage(purchaseOrderModelMock, modelMock);

        assertEquals("purchaseOrderWizardSecondPage.html", template);

        verify(modelMock).addAttribute("purchaseOrderModel", purchaseOrderModelMock);
        verify(modelMock).addAttribute("paymentMethods", paymentMethods);

        verify(purchaseOrderModelMock).setVendorName(TEST_VENDOR_NAME);
        verify(purchaseOrderModelMock).setPaymentMethodCode(TEST_PAYMENT_METHOD_CODE);
    }

    @Test
    public void purchaseOrderWizardSecondPage_createsPurchaseOrder_whenCalledForNew(){
        RedirectView redirectView = controller.postPurchaseOrderWizardSecondPage(purchaseOrderModelMock, redirectAttributesMock);

        assertEquals("/purchaseOrderCard", redirectView.getUrl());

        verify(redirectAttributesMock).addAttribute(eq("code"), anyString());

        verify(svcMock).create(purchaseOrderArgumentCaptor.capture());

        PurchaseOrder createdPurchaseOrder = purchaseOrderArgumentCaptor.getValue();

        assertEquals(vendorMock, createdPurchaseOrder.getVendor());
        assertEquals(paymentMethodMock, createdPurchaseOrder.getPaymentMethod());
    }

    @Test
    public void purchaseOrderWizardSecondPage_createsPurchaseOrder_whenCalledForExisting(){
        when(purchaseOrderModelMock.getCode()).thenReturn(TEST_CODE);

        RedirectView redirectView = controller.postPurchaseOrderWizardSecondPage(purchaseOrderModelMock, redirectAttributesMock);

        assertEquals("/purchaseOrderCard", redirectView.getUrl());

        verify(redirectAttributesMock).addAttribute("code", TEST_CODE);

        verify(svcMock).update(TEST_CODE, purchaseOrderMock);
    }

    @Test
    public void delete_deletesTheEntity(){
        RedirectView redirectView = controller.delete(TEST_CODE);

        assertEquals("/purchaseOrderList", redirectView.getUrl());

        verify(svcMock).delete(TEST_CODE);
    }

    @Test
    public void card_returnsProperTemplate(){
        String template = controller.card(TEST_CODE, modelMock);

        assertEquals("purchaseOrderCard.html", template);

        verify(modelMock).addAttribute("purchaseOrder", purchaseOrderMock);
        verify(modelMock).addAttribute("purchaseOrderLines", purchaseOrderLines);
        verify(modelMock).addAttribute("paging", purchaseOrderLinePagingMock);
    }

    @Test
    public void post_returnsProperTemplate() {
        RedirectView redirectView = controller.post(TEST_CODE);

        assertEquals("/purchaseOrderList", redirectView.getUrl());

        verify(purchaseOrderPostSvcMock).post(TEST_CODE);
    }
}
