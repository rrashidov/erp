package org.roko.erp.frontend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.PaymentMethodDTO;
import org.roko.erp.dto.VendorDTO;
import org.roko.erp.dto.VendorLedgerEntryDTO;
import org.roko.erp.dto.list.PaymentMethodList;
import org.roko.erp.dto.list.VendorLedgerEntryList;
import org.roko.erp.dto.list.VendorList;
import org.roko.erp.frontend.controllers.model.VendorModel;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.services.PaymentMethodService;
import org.roko.erp.frontend.services.VendorLedgerEntryService;
import org.roko.erp.frontend.services.VendorService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

public class VendorControllerTest {

    private static final int TEST_PAGE = 123;
    private static final long TEST_RECORD_COUNT = 234;

    private static final long TEST_VENDOR_LEDGER_ENTRY_COUNT = 123;

    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";
    private static final String TEST_ADDRESS = "test-address";
    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";

    private List<VendorDTO> vendors;

    private List<VendorLedgerEntryDTO> vendorLedgerEntries;

    private List<PaymentMethodDTO> paymentMethods;

    @Mock
    private VendorDTO vendorMock;

    @Mock
    private VendorLedgerEntryDTO vendorLedgerentryMock;

    @Mock
    private PaymentMethodDTO paymentMethodMock;

    @Mock
    private VendorModel vendorModelMock;

    @Captor
    private ArgumentCaptor<VendorDTO> vendorArgumentCaptor;

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
    private VendorList vendorList;

    @Mock
    private PagingService pagingSvcMock;

    @Mock
    private PaymentMethodService paymentMethodSvcMock;

    @Mock
    private PaymentMethodList paymentMethodList;

    @Mock
    private VendorLedgerEntryService vendorLedgerEntrySvcMock;

    @Mock
    private VendorLedgerEntryList vendorLedgerEntryList;
    
    private VendorController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        vendors = Arrays.asList(vendorMock);

        vendorLedgerEntries = Arrays.asList(vendorLedgerentryMock);

        paymentMethods = Arrays.asList(paymentMethodMock);

        when(paymentMethodMock.getCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(vendorMock.getCode()).thenReturn(TEST_CODE);
        when(vendorMock.getName()).thenReturn(TEST_NAME);
        when(vendorMock.getAddress()).thenReturn(TEST_ADDRESS);
        when(vendorMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(vendorModelMock.getCode()).thenReturn(TEST_CODE);
        when(vendorModelMock.getName()).thenReturn(TEST_NAME);
        when(vendorModelMock.getAddress()).thenReturn(TEST_ADDRESS);
        when(vendorModelMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(paymentMethodList.getData()).thenReturn(paymentMethods);
        
        when(paymentMethodSvcMock.list()).thenReturn(paymentMethodList);
        when(paymentMethodSvcMock.get(TEST_PAYMENT_METHOD_CODE)).thenReturn(paymentMethodMock);

        when(vendorLedgerEntryList.getData()).thenReturn(vendorLedgerEntries);
        when(vendorLedgerEntryList.getCount()).thenReturn(TEST_VENDOR_LEDGER_ENTRY_COUNT);

        when(vendorLedgerEntrySvcMock.list(TEST_CODE, TEST_PAGE)).thenReturn(vendorLedgerEntryList);

        when(vendorList.getData()).thenReturn(vendors);
        when(vendorList.getCount()).thenReturn(TEST_RECORD_COUNT);

        when(vendorSvcMock.list(TEST_PAGE)).thenReturn(vendorList);
        when(vendorSvcMock.get(TEST_CODE)).thenReturn(vendorMock);

        when(pagingSvcMock.generate("vendor", TEST_PAGE, (int) TEST_RECORD_COUNT)).thenReturn(pagingDataMock);
        when(pagingSvcMock.generate("vendorCard", TEST_CODE, TEST_PAGE, (int) TEST_VENDOR_LEDGER_ENTRY_COUNT)).thenReturn(vendorLedgerEntryPagingDataMock);

        controller = new VendorController(vendorSvcMock, pagingSvcMock, paymentMethodSvcMock, vendorLedgerEntrySvcMock);
    }

    @Test
    public void listReturnsProperTemplate(){
        String template = controller.list(TEST_PAGE, modelMock);

        assertEquals("vendorList.html", template);

        verify(modelMock).addAttribute("vendors", vendors);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }

    @Test
    public void cardReturnsProperTemplate_whenCalledForNewVendor(){
        String template = controller.card(null, TEST_PAGE, modelMock);

        assertEquals("vendorCard.html", template);

        verify(modelMock).addAttribute(eq("vendor"), vendorModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("paymentMethods", paymentMethods);

        VendorModel vendorModel = vendorModelArgumentCaptor.getValue();

        assertEquals("", vendorModel.getCode());
        assertEquals("", vendorModel.getName());
        assertEquals("", vendorModel.getAddress());
        assertEquals("", vendorModel.getPaymentMethodCode());
    }

    @Test
    public void cardReturnsProperTemplate_whenCalledForExistingVendor(){
        String template = controller.card(TEST_CODE, TEST_PAGE, modelMock);

        assertEquals("vendorCard.html", template);

        verify(modelMock).addAttribute(eq("vendor"), vendorModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("paymentMethods", paymentMethods);
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
        when(vendorSvcMock.get(TEST_CODE)).thenReturn(null);

        RedirectView redirectView = controller.post(vendorModelMock);

        assertEquals("/vendorList", redirectView.getUrl());

        verify(vendorSvcMock).create(vendorArgumentCaptor.capture());

        VendorDTO vendor = vendorArgumentCaptor.getValue();

        assertEquals(TEST_CODE, vendor.getCode());
        assertEquals(TEST_NAME, vendor.getName());
        assertEquals(TEST_ADDRESS, vendor.getAddress());
        assertEquals(TEST_PAYMENT_METHOD_CODE, vendor.getPaymentMethodCode());
    }

    @Test
    public void postingCard_updatesVendor_whenCalledForExisting(){
        controller.post(vendorModelMock);

        verify(vendorSvcMock).update(eq(TEST_CODE), vendorArgumentCaptor.capture());

        VendorDTO vendor = vendorArgumentCaptor.getValue();

        assertEquals(TEST_NAME, vendor.getName());
        assertEquals(TEST_ADDRESS, vendor.getAddress());
        assertEquals(TEST_PAYMENT_METHOD_CODE, vendor.getPaymentMethodCode());
    }

    @Test
    public void delete_deletesVendor(){
        RedirectView redirectView = controller.delete(TEST_CODE);

        assertEquals("/vendorList", redirectView.getUrl());

        verify(vendorSvcMock).delete(TEST_CODE);
    }
}
