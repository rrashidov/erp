package org.roko.erp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.roko.erp.model.VendorLedgerEntry;
import org.roko.erp.services.PaymentMethodService;
import org.roko.erp.services.VendorLedgerEntryService;
import org.roko.erp.services.VendorService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

public class VendorControllerTest {

    private static final int TEST_PAGE = 123;
    private static final int TEST_RECORD_COUNT = 234;

    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";
    private static final String TEST_ADDRESS = "test-address";
    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";

    private static final int TEST_VENDOR_LEDGER_ENTRY_COUNT = 123;

    private List<Vendor> vendorList;

    private List<PaymentMethod> paymentMethodList = new ArrayList<>();

    private List<VendorLedgerEntry> vendorLedgerEntries = new ArrayList<>();

    @Mock
    private Vendor vendorMock;

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
    private PagingData vendorLedgerEntryPagingDataMock;

    @Mock
    private Model modelMock;

    @Mock
    private VendorService vendorSvcMock;

    @Mock
    private PagingService pagingSvcMock;

    @Mock
    private PaymentMethodService paymentMethodSvcMock;

    @Mock
    private VendorLedgerEntryService vendorLedgerEntrySvcMock;
    
    private VendorController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        vendorList = Arrays.asList(vendorMock);

        when(paymentMethodMock.getCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(vendorMock.getCode()).thenReturn(TEST_CODE);
        when(vendorMock.getName()).thenReturn(TEST_NAME);
        when(vendorMock.getAddress()).thenReturn(TEST_ADDRESS);
        when(vendorMock.getPaymentMethod()).thenReturn(paymentMethodMock);

        when(vendorModelMock.getCode()).thenReturn(TEST_CODE);
        when(vendorModelMock.getName()).thenReturn(TEST_NAME);
        when(vendorModelMock.getAddress()).thenReturn(TEST_ADDRESS);
        when(vendorModelMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(paymentMethodSvcMock.list()).thenReturn(paymentMethodList);
        when(paymentMethodSvcMock.get(TEST_PAYMENT_METHOD_CODE)).thenReturn(paymentMethodMock);

        when(vendorLedgerEntrySvcMock.findFor(vendorMock)).thenReturn(vendorLedgerEntries);
        when(vendorLedgerEntrySvcMock.count(vendorMock)).thenReturn(TEST_VENDOR_LEDGER_ENTRY_COUNT);

        when(vendorSvcMock.list(TEST_PAGE)).thenReturn(vendorList);
        when(vendorSvcMock.count()).thenReturn(TEST_RECORD_COUNT);
        when(vendorSvcMock.get(TEST_CODE)).thenReturn(vendorMock);

        when(pagingSvcMock.generate("vendor", TEST_PAGE, TEST_RECORD_COUNT)).thenReturn(pagingDataMock);
        when(pagingSvcMock.generate("vendorLedgerEntry", 1, TEST_VENDOR_LEDGER_ENTRY_COUNT)).thenReturn(vendorLedgerEntryPagingDataMock);

        controller = new VendorController(vendorSvcMock, pagingSvcMock, paymentMethodSvcMock, vendorLedgerEntrySvcMock);
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
    public void cardReturnsProperTemplate_whenCalledForExistingVendor(){
        String template = controller.card(TEST_CODE, modelMock);

        assertEquals("vendorCard.html", template);

        verify(modelMock).addAttribute(eq("vendor"), vendorModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("paymentMethods", paymentMethodList);
        verify(modelMock).addAttribute("vendorLedgerEntries", vendorLedgerEntries);
        verify(modelMock).addAttribute("paging", vendorLedgerEntryPagingDataMock);

        VendorModel vendorModel = vendorModelArgumentCaptor.getValue();

        assertEquals(TEST_CODE, vendorModel.getCode());
        assertEquals(TEST_NAME, vendorModel.getName());
        assertEquals(TEST_ADDRESS, vendorModel.getAddress());
        assertEquals(TEST_PAYMENT_METHOD_CODE, vendorModel.getPaymentMethodCode());
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

    @Test
    public void delete_deletesVendor(){
        RedirectView redirectView = controller.delete(TEST_CODE);

        assertEquals("/vendorList", redirectView.getUrl());

        verify(vendorSvcMock).delete(TEST_CODE);
    }
}
