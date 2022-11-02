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
import org.roko.erp.controllers.model.PurchaseOrderModel;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.PaymentMethod;
import org.roko.erp.model.PurchaseOrder;
import org.roko.erp.model.Vendor;
import org.roko.erp.services.PaymentMethodService;
import org.roko.erp.services.PurchaseOrderService;
import org.roko.erp.services.VendorService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

public class PurchaseOrderControllerTest {
    
    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";

    private static final String TEST_VENDOR_CODE = "test-vendor-code";
    private static final String TEST_VENDOR_NAME = "test-vendor-name";

    private static final Long TEST_PAGE = 123l;

    private static final long TEST_COUNT = 234l;

    private List<PurchaseOrder> purchaseOrders = new ArrayList<>();

    private List<Vendor> vendors = new ArrayList<>();

    private List<PaymentMethod> paymentMethods = new ArrayList<>();

    @Captor
    private ArgumentCaptor<PurchaseOrderModel> purchaseOrderModelArgumentCaptor;

    @Captor
    private ArgumentCaptor<PurchaseOrder> purchaseOrderArgumentCaptor;

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
    private PagingService pagingSvcMock;

    private PurchaseOrderController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(paymentMethodSvcMock.list()).thenReturn(paymentMethods);
        when(paymentMethodSvcMock.get(TEST_PAYMENT_METHOD_CODE)).thenReturn(paymentMethodMock);

        when(paymentMethodMock.getCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(vendorMock.getName()).thenReturn(TEST_VENDOR_NAME);
        when(vendorMock.getPaymentMethod()).thenReturn(paymentMethodMock);

        when(purchaseOrderModelMock.getVendorCode()).thenReturn(TEST_VENDOR_CODE);
        when(purchaseOrderModelMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(vendorSvcMock.list()).thenReturn(vendors);
        when(vendorSvcMock.get(TEST_VENDOR_CODE)).thenReturn(vendorMock);

        when(svcMock.list()).thenReturn(purchaseOrders);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        when(pagingSvcMock.generate("purchaseOrder", TEST_PAGE, TEST_COUNT)).thenReturn(pagingDataMock);

        controller = new PurchaseOrderController(svcMock, vendorSvcMock, paymentMethodSvcMock, pagingSvcMock);
    }

    @Test
    public void list_returnsProperTemplate(){
        String template = controller.list(TEST_PAGE, modelMock);

        assertEquals("purchaseOrderList.html", template);

        verify(modelMock).addAttribute("purchaseOrders", purchaseOrders);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }

    @Test
    public void wizard_returnsProperTemplate(){
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
    public void purchaseOrderWizardFirstPage_returnsProperTemplate(){
        String template = controller.postPurchaseOrderWizardFirstPage(purchaseOrderModelMock, modelMock);

        assertEquals("purchaseOrderWizardSecondPage.html", template);

        verify(modelMock).addAttribute("purchaseOrderModel", purchaseOrderModelMock);
        verify(modelMock).addAttribute("paymentMethods", paymentMethods);

        verify(purchaseOrderModelMock).setVendorName(TEST_VENDOR_NAME);
        verify(purchaseOrderModelMock).setPaymentMethodCode(TEST_PAYMENT_METHOD_CODE);
    }

    @Test
    public void purchaseOrderWizardSecondPage_createsPurchaseOrder(){
        RedirectView redirectView = controller.postPurchaseOrderWizardSecondPage(purchaseOrderModelMock);

        assertEquals("/purchaseOrderList", redirectView.getUrl());

        verify(svcMock).create(purchaseOrderArgumentCaptor.capture());

        PurchaseOrder createdPurchaseOrder = purchaseOrderArgumentCaptor.getValue();

        assertEquals(vendorMock, createdPurchaseOrder.getVendor());
        assertEquals(paymentMethodMock, createdPurchaseOrder.getPaymentMethod());
    }
}
