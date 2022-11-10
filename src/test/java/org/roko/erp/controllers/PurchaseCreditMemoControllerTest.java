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
import org.roko.erp.controllers.model.PurchaseCreditMemoModel;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.PaymentMethod;
import org.roko.erp.model.PurchaseCreditMemo;
import org.roko.erp.model.Vendor;
import org.roko.erp.services.PaymentMethodService;
import org.roko.erp.services.PurchaseCreditMemoService;
import org.roko.erp.services.VendorService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

public class PurchaseCreditMemoControllerTest {
    
    private static final String TEST_VENDOR_CODE = "test-vendor-code";
    private static final String TEST_VENDOR_NAME = "test-vendor-name";
    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";

    private static final Long TEST_PAGE = 123l;

    private static final long TEST_COUNT = 234l;
    private static final Date TEST_DATE = new Date();

    private List<PurchaseCreditMemo> purchaseCreditMemos = new ArrayList<>();

    private List<Vendor> vendors = new ArrayList<>();

    private List<PaymentMethod> paymentMethods = new ArrayList<>();

    @Captor
    private ArgumentCaptor<PurchaseCreditMemoModel> purchaseCreditMemoModelArgumentCaptor;

    @Captor
    private ArgumentCaptor<PurchaseCreditMemo> purchaseCreditMemoArgumentCaptor;

    @Mock
    private PaymentMethod paymentMethodMock;

    @Mock
    private Vendor vendorMock;

    @Mock
    private PaymentMethodService paymentMethodSvcMock;
    
    @Mock
    private PurchaseCreditMemoModel purchaseCreditMemoModelMock;

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private PagingService pagingSvcMock;

    @Mock
    private Model modelMock;

    @Mock
    private PurchaseCreditMemoService svcMock;

    @Mock
    private VendorService vendorSvcMock;

    private PurchaseCreditMemoController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(purchaseCreditMemoModelMock.getVendorCode()).thenReturn(TEST_VENDOR_CODE);
        when(purchaseCreditMemoModelMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);
        when(purchaseCreditMemoModelMock.getDate()).thenReturn(TEST_DATE);

        when(svcMock.list()).thenReturn(purchaseCreditMemos);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        when(pagingSvcMock.generate("purchaseCreditMemo", TEST_PAGE, TEST_COUNT)).thenReturn(pagingDataMock);

        when(vendorMock.getCode()).thenReturn(TEST_VENDOR_CODE);
        when(vendorMock.getName()).thenReturn(TEST_VENDOR_NAME);
        when(vendorMock.getPaymentMethod()).thenReturn(paymentMethodMock);

        when(vendorSvcMock.list()).thenReturn(vendors);
        when(vendorSvcMock.get(TEST_VENDOR_CODE)).thenReturn(vendorMock);

        when(paymentMethodMock.getCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);
        
        when(paymentMethodSvcMock.list()).thenReturn(paymentMethods);
        when(paymentMethodSvcMock.get(TEST_PAYMENT_METHOD_CODE)).thenReturn(paymentMethodMock);

        controller = new PurchaseCreditMemoController(svcMock, pagingSvcMock, vendorSvcMock, paymentMethodSvcMock);
    }

    @Test
    public void list_returnsProperTemplate(){
        String template = controller.list(TEST_PAGE, modelMock);

        assertEquals("purchaseCreditMemoList.html", template);

        verify(modelMock).addAttribute("purchaseCreditMemos", purchaseCreditMemos);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }

    @Test
    public void gettingWizard_returnsProperTemplate(){
        String template = controller.wizard(modelMock);

        assertEquals("purchaseCreditMemoWizardFirstPage.html", template);

        verify(modelMock).addAttribute(eq("purchaseCreditMemoModel"), purchaseCreditMemoModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("vendors", vendors);

        PurchaseCreditMemoModel purchaseCreditMemoModel = purchaseCreditMemoModelArgumentCaptor.getValue();

        assertEquals("", purchaseCreditMemoModel.getCode());
        assertEquals("", purchaseCreditMemoModel.getVendorCode());
        assertEquals("", purchaseCreditMemoModel.getVendorName());
        assertEquals("", purchaseCreditMemoModel.getPaymentMethodCode());
    }

    @Test
    public void postingWizardFirstPage_returnsProperTemplate(){
        when(purchaseCreditMemoModelMock.getVendorCode()).thenReturn(TEST_VENDOR_CODE);

        String template = controller.postPurchaseCreditMemoWizardFirstPage(purchaseCreditMemoModelMock, modelMock);

        assertEquals("purchaseCreditMemoWizardSecondPage.html", template);

        verify(modelMock).addAttribute("purchaseCreditMemoModel", purchaseCreditMemoModelMock);
        verify(modelMock).addAttribute("paymentMethods", paymentMethods);

        verify(purchaseCreditMemoModelMock).setVendorName(TEST_VENDOR_NAME);
        verify(purchaseCreditMemoModelMock).setPaymentMethodCode("test-payment-method-code");
    }

    @Test
    public void postingWizardSecondPage_createsNewEntity_andReturnsProperTemplate(){
        RedirectView redirectView = controller.postPurchaseCreditMemoWizardSecondPage(purchaseCreditMemoModelMock);

        assertEquals("/purchaseCreditMemoList", redirectView.getUrl());

        verify(svcMock).create(purchaseCreditMemoArgumentCaptor.capture());

        PurchaseCreditMemo purchaseCreditMemo = purchaseCreditMemoArgumentCaptor.getValue();

        assertEquals(vendorMock, purchaseCreditMemo.getVendor());
        assertEquals(paymentMethodMock, purchaseCreditMemo.getPaymentMethod());
        assertEquals(TEST_DATE, purchaseCreditMemo.getDate());
    }
}
