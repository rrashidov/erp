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
import org.roko.erp.controllers.model.VendorModel;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.PaymentMethod;
import org.roko.erp.model.Vendor;
import org.roko.erp.services.PaymentMethodService;
import org.roko.erp.services.VendorService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

public class VendorControllerTest {

    private static final Long TEST_PAGE = 123l;

    private static final long TEST_RECORD_COUNT = 234l;

    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";
    private static final String TEST_ADDRESS = "test-address";
    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";

    private List<Vendor> vendorList = new ArrayList<>();

    private List<PaymentMethod> paymentMethodList = new ArrayList<>();

    @Mock
    private PaymentMethod paymentMethodMock;

    @Mock
    private VendorModel vendorModelMock;

    @Captor
    private ArgumentCaptor<Vendor> vendorArgumentCaptor;

    @Captor
    private ArgumentCaptor<VendorModel> vendorModelArgumentCaptor;

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private Model modelMock;

    @Mock
    private VendorService vendorSvcMock;

    @Mock
    private PagingService pagingSvcMock;

    @Mock
    private PaymentMethodService paymentMethodSvcMock;
    
    private VendorController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(vendorModelMock.getCode()).thenReturn(TEST_CODE);
        when(vendorModelMock.getName()).thenReturn(TEST_NAME);
        when(vendorModelMock.getAddress()).thenReturn(TEST_ADDRESS);
        when(vendorModelMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(paymentMethodSvcMock.list()).thenReturn(paymentMethodList);
        when(paymentMethodSvcMock.get(TEST_PAYMENT_METHOD_CODE)).thenReturn(paymentMethodMock);

        when(vendorSvcMock.list()).thenReturn(vendorList);
        when(vendorSvcMock.count()).thenReturn(TEST_RECORD_COUNT);

        when(pagingSvcMock.generate("vendor", TEST_PAGE, TEST_RECORD_COUNT)).thenReturn(pagingDataMock);

        controller = new VendorController(vendorSvcMock, pagingSvcMock, paymentMethodSvcMock);
    }

    @Test
    public void listReturnsProperTemplate(){
        String template = controller.list(TEST_PAGE, modelMock);

        assertEquals("vendorList.html", template);

        verify(modelMock).addAttribute("vendors", vendorList);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }

    @Test
    public void cardReturnsProperTemplate_whenCalledForNewVendor(){
        String template = controller.card(null, modelMock);

        assertEquals("vendorCard.html", template);

        verify(modelMock).addAttribute(eq("vendor"), vendorModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("paymentMethods", paymentMethodList);

        VendorModel vendorModel = vendorModelArgumentCaptor.getValue();

        assertEquals("", vendorModel.getCode());
        assertEquals("", vendorModel.getName());
        assertEquals("", vendorModel.getAddress());
        assertEquals("", vendorModel.getPaymentMethodCode());
    }

    @Test
    public void postingCard_createsNewVendor(){
        RedirectView redirectView = controller.post(vendorModelMock);

        assertEquals("/vendorList", redirectView.getUrl());

        verify(vendorSvcMock).create(vendorArgumentCaptor.capture());

        Vendor vendor = vendorArgumentCaptor.getValue();

        assertEquals(TEST_CODE, vendor.getCode());
        assertEquals(TEST_NAME, vendor.getName());
        assertEquals(TEST_ADDRESS, vendor.getAddress());
        assertEquals(paymentMethodMock, vendor.getPaymentMethod());
    }
}
