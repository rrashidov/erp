package org.roko.erp.frontend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.PaymentMethodDTO;
import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.dto.PurchaseDocumentLineDTO;
import org.roko.erp.dto.VendorDTO;
import org.roko.erp.dto.list.PaymentMethodList;
import org.roko.erp.dto.list.PurchaseDocumentLineList;
import org.roko.erp.dto.list.PurchaseDocumentList;
import org.roko.erp.dto.list.VendorList;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.services.DeleteFailedException;
import org.roko.erp.frontend.services.FeedbackService;
import org.roko.erp.frontend.services.PaymentMethodService;
import org.roko.erp.frontend.services.PostFailedException;
import org.roko.erp.frontend.services.PurchaseOrderLineService;
import org.roko.erp.frontend.services.PurchaseOrderPostService;
import org.roko.erp.frontend.services.PurchaseOrderService;
import org.roko.erp.frontend.services.VendorService;
import org.roko.erp.frontend.services.util.Feedback;
import org.roko.erp.frontend.services.util.FeedbackType;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

public class PurchaseOrderControllerTest {

    private static final String TEST_ERROR_MSG = "test-error-msg";

    private static final String TEST_POST_FAILED_MSG = "test-post-failed-msg";

    private static final String TEST_NEW_PURCHASE_ORDER_CODE = "test-new-purchase-order-code";

    private static final String TEST_CODE = "test-purchase-order-code";

    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";

    private static final String TEST_VENDOR_CODE = "test-vendor-code";
    private static final String TEST_VENDOR_NAME = "test-vendor-name";

    private static final int TEST_PAGE = 123;
    private static final long TEST_COUNT = 234;

    private static final Date TEST_DATE = new Date();

    private static final long TEST_LINES_COUNT = 23;

    private List<PurchaseDocumentDTO> purchaseOrders;

    private List<VendorDTO> vendors = new ArrayList<>();

    private List<PaymentMethodDTO> paymentMethods = new ArrayList<>();

    private List<PurchaseDocumentLineDTO> purchaseOrderLines = new ArrayList<>();

    @Captor
    private ArgumentCaptor<PurchaseDocumentDTO> purchaseOrderModelArgumentCaptor;

    @Captor
    private ArgumentCaptor<PurchaseDocumentDTO> purchaseOrderArgumentCaptor;

    @Mock
    private RedirectAttributes redirectAttributesMock;

    @Mock
    private PagingData purchaseOrderLinePagingMock;

    @Mock
    private PurchaseDocumentDTO purchaseOrderMock;

    @Mock
    private PurchaseDocumentLineDTO purchaseOrderLineMock;

    @Mock
    private PaymentMethodDTO paymentMethodMock;

    @Mock
    private VendorDTO vendorMock;

    @Mock
    private PurchaseDocumentDTO purchaseOrderModelMock;

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

    @Mock
    private FeedbackService feedbackSvcMock;

    @Mock
    private HttpSession httpSessionMock;

    @Mock
    private Feedback feedbackMock;

    @Mock
    private PurchaseDocumentList purchaseOrderList;

    @Mock
    private PurchaseDocumentLineList purchaseOrderLineList;

    @Mock
    private VendorList vendorList;

    @Mock
    private PaymentMethodList paymentMethodList;

    private PurchaseOrderController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        purchaseOrders = Arrays.asList(purchaseOrderMock);

        purchaseOrderLines = Arrays.asList(purchaseOrderLineMock);

        when(purchaseOrderMock.getCode()).thenReturn(TEST_CODE);
        when(purchaseOrderMock.getVendorCode()).thenReturn(TEST_VENDOR_CODE);
        when(purchaseOrderMock.getVendorName()).thenReturn(TEST_VENDOR_NAME);
        when(purchaseOrderMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);
        when(purchaseOrderMock.getDate()).thenReturn(TEST_DATE);

        when(paymentMethodList.getData()).thenReturn(paymentMethods);

        when(paymentMethodSvcMock.list()).thenReturn(paymentMethodList);
        when(paymentMethodSvcMock.get(TEST_PAYMENT_METHOD_CODE)).thenReturn(paymentMethodMock);

        when(paymentMethodMock.getCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(vendorMock.getCode()).thenReturn(TEST_VENDOR_CODE);
        when(vendorMock.getName()).thenReturn(TEST_VENDOR_NAME);
        when(vendorMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(purchaseOrderModelMock.getCode()).thenReturn("");
        when(purchaseOrderModelMock.getVendorCode()).thenReturn(TEST_VENDOR_CODE);
        when(purchaseOrderModelMock.getDate()).thenReturn(TEST_DATE);
        when(purchaseOrderModelMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(vendorList.getData()).thenReturn(vendors);

        when(vendorSvcMock.list()).thenReturn(vendorList);
        when(vendorSvcMock.get(TEST_VENDOR_CODE)).thenReturn(vendorMock);

        when(purchaseOrderList.getData()).thenReturn(purchaseOrders);
        when(purchaseOrderList.getCount()).thenReturn(TEST_COUNT);

        when(svcMock.list(TEST_PAGE)).thenReturn(purchaseOrderList);
        when(svcMock.get(TEST_CODE)).thenReturn(purchaseOrderMock);
        when(svcMock.create(any(PurchaseDocumentDTO.class))).thenReturn(TEST_NEW_PURCHASE_ORDER_CODE);

        when(purchaseOrderLineList.getData()).thenReturn(purchaseOrderLines);
        when(purchaseOrderLineList.getCount()).thenReturn(TEST_LINES_COUNT);

        when(purchaseOrderLineSvcMock.list(TEST_CODE, TEST_PAGE)).thenReturn(purchaseOrderLineList);

        when(pagingSvcMock.generate("purchaseOrder", TEST_PAGE, (int) TEST_COUNT)).thenReturn(pagingDataMock);
        when(pagingSvcMock.generate("purchaseOrderCard", TEST_CODE, TEST_PAGE, (int) TEST_LINES_COUNT))
                .thenReturn(purchaseOrderLinePagingMock);

        when(feedbackSvcMock.get(httpSessionMock)).thenReturn(feedbackMock);

        controller = new PurchaseOrderController(svcMock, purchaseOrderLineSvcMock, vendorSvcMock, paymentMethodSvcMock,
                pagingSvcMock, purchaseOrderPostSvcMock, feedbackSvcMock);
    }

    @Test
    public void list_returnsProperTemplate() {
        String template = controller.list(TEST_PAGE, modelMock, httpSessionMock);

        assertEquals("purchaseOrderList.html", template);

        verify(modelMock).addAttribute("purchaseOrders", purchaseOrders);
        verify(modelMock).addAttribute("paging", pagingDataMock);
        verify(modelMock).addAttribute("feedback", feedbackMock);
    }

    @Test
    public void wizard_returnsProperTemplate_whenCalledForNew() {
        String template = controller.wizard(null, modelMock);

        assertEquals("purchaseOrderWizardFirstPage.html", template);

        verify(modelMock).addAttribute(eq("purchaseOrderModel"), purchaseOrderModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("vendors", vendors);

        PurchaseDocumentDTO purchaseOrderModel = purchaseOrderModelArgumentCaptor.getValue();

        assertEquals("", purchaseOrderModel.getCode());
        assertEquals("", purchaseOrderModel.getVendorCode());
        assertEquals("", purchaseOrderModel.getVendorName());
        assertEquals("", purchaseOrderModel.getPaymentMethodCode());
    }

    @Test
    public void wizard_returnsProperTemplate_whenCalledForExisting() {
        String template = controller.wizard(TEST_CODE, modelMock);

        assertEquals("purchaseOrderWizardFirstPage.html", template);

        verify(modelMock).addAttribute(eq("purchaseOrderModel"), purchaseOrderModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("vendors", vendors);

        PurchaseDocumentDTO purchaseOrderModel = purchaseOrderModelArgumentCaptor.getValue();

        assertEquals(TEST_CODE, purchaseOrderModel.getCode());
        assertEquals(TEST_VENDOR_CODE, purchaseOrderModel.getVendorCode());
        assertEquals(TEST_VENDOR_NAME, purchaseOrderModel.getVendorName());
        assertEquals(TEST_PAYMENT_METHOD_CODE, purchaseOrderModel.getPaymentMethodCode());
    }

    @Test
    public void purchaseOrderWizardFirstPage_returnsProperTemplate() {
        String template = controller.postPurchaseOrderWizardFirstPage(purchaseOrderModelMock, modelMock);

        assertEquals("purchaseOrderWizardSecondPage.html", template);

        verify(modelMock).addAttribute("purchaseOrderModel", purchaseOrderModelMock);
        verify(modelMock).addAttribute("paymentMethods", paymentMethods);

        verify(purchaseOrderModelMock).setVendorName(TEST_VENDOR_NAME);
        verify(purchaseOrderModelMock).setPaymentMethodCode(TEST_PAYMENT_METHOD_CODE);
    }

    @Test
    public void purchaseOrderWizardSecondPage_createsPurchaseOrder_whenCalledForNew() {
        RedirectView redirectView = controller.postPurchaseOrderWizardSecondPage(purchaseOrderModelMock,
                redirectAttributesMock);

        assertEquals("/purchaseOrderCard", redirectView.getUrl());

        verify(redirectAttributesMock).addAttribute("code", TEST_NEW_PURCHASE_ORDER_CODE);

        verify(svcMock).create(purchaseOrderArgumentCaptor.capture());

        PurchaseDocumentDTO createdPurchaseOrder = purchaseOrderArgumentCaptor.getValue();

        assertEquals(TEST_VENDOR_CODE, createdPurchaseOrder.getVendorCode());
        assertEquals(TEST_PAYMENT_METHOD_CODE, createdPurchaseOrder.getPaymentMethodCode());
    }

    @Test
    public void purchaseOrderWizardSecondPage_updatesPurchaseOrder_whenCalledForExisting(){
        when(purchaseOrderModelMock.getCode()).thenReturn(TEST_CODE);

        RedirectView redirectView = controller.postPurchaseOrderWizardSecondPage(purchaseOrderModelMock, redirectAttributesMock);

        assertEquals("/purchaseOrderCard", redirectView.getUrl());

        verify(redirectAttributesMock).addAttribute("code", TEST_CODE);

        verify(svcMock).update(TEST_CODE, purchaseOrderModelMock);
    }

    @Test
    public void delete_deletesTheEntity() throws DeleteFailedException {
        RedirectView redirectView = controller.delete(TEST_CODE, httpSessionMock);

        assertEquals("/purchaseOrderList", redirectView.getUrl());

        verify(svcMock).delete(TEST_CODE);

        verify(feedbackSvcMock).give(FeedbackType.INFO, String.format("Purchase Order %s deleted", TEST_CODE),
                httpSessionMock);
    }

    @Test
    public void deleteGivesProperFeedback_whenDeleteFails() throws DeleteFailedException {
        doThrow(new DeleteFailedException(TEST_ERROR_MSG)).when(svcMock).delete(TEST_CODE);

        controller.delete(TEST_CODE, httpSessionMock);

        verify(svcMock).delete(TEST_CODE);

        verify(feedbackSvcMock).give(FeedbackType.ERROR, TEST_ERROR_MSG,
                httpSessionMock);
    }

    @Test
    public void card_returnsProperTemplate() {
        String template = controller.card(TEST_CODE, TEST_PAGE, modelMock);

        assertEquals("purchaseOrderCard.html", template);

        verify(modelMock).addAttribute("purchaseOrder", purchaseOrderMock);
        verify(modelMock).addAttribute("purchaseOrderLines", purchaseOrderLines);
        verify(modelMock).addAttribute("paging", purchaseOrderLinePagingMock);
    }

    @Test
    public void post_returnsProperTemplate() throws PostFailedException {
        RedirectView redirectView = controller.post(TEST_CODE, httpSessionMock);

        assertEquals("/purchaseOrderList", redirectView.getUrl());

        verify(purchaseOrderPostSvcMock).post(TEST_CODE);

        verify(feedbackSvcMock).give(FeedbackType.INFO, "Purchase order " + TEST_CODE + " post scheduled.",
                httpSessionMock);
    }

    @Test
    public void postReturnsProperFeedback_whenPostingFails() throws PostFailedException {
        doThrow(new PostFailedException(TEST_POST_FAILED_MSG)).when(purchaseOrderPostSvcMock).post(TEST_CODE);

        RedirectView redirectView = controller.post(TEST_CODE, httpSessionMock);

        assertEquals("/purchaseOrderList", redirectView.getUrl());

        verify(purchaseOrderPostSvcMock).post(TEST_CODE);

        verify(feedbackSvcMock).give(FeedbackType.ERROR,
                "Purchase order " + TEST_CODE + " post scheduling failed: " + TEST_POST_FAILED_MSG, httpSessionMock);
    }

}
