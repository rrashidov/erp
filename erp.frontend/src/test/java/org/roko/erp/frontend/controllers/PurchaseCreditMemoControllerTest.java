package org.roko.erp.frontend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.roko.erp.frontend.controllers.model.PurchaseCreditMemoModel;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.model.PaymentMethod;
import org.roko.erp.frontend.model.PurchaseCreditMemo;
import org.roko.erp.frontend.model.PurchaseCreditMemoLine;
import org.roko.erp.frontend.model.Vendor;
import org.roko.erp.frontend.services.FeedbackService;
import org.roko.erp.frontend.services.PaymentMethodService;
import org.roko.erp.frontend.services.PostFailedException;
import org.roko.erp.frontend.services.PurchaseCodeSeriesService;
import org.roko.erp.frontend.services.PurchaseCreditMemoLineService;
import org.roko.erp.frontend.services.PurchaseCreditMemoPostService;
import org.roko.erp.frontend.services.PurchaseCreditMemoService;
import org.roko.erp.frontend.services.VendorService;
import org.roko.erp.frontend.services.util.Feedback;
import org.roko.erp.frontend.services.util.FeedbackType;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

public class PurchaseCreditMemoControllerTest {

    private static final String TEST_POST_FAILED_MSG = "test-post-failed-msg";

    private static final String TEST_NEW_CREDIT_MEMO_CODE = "TEST_NEW_CREDIT_MEMO_CODE";

    private static final String TEST_PURCHASE_CREDIT_MEMO_CODE = "test-purchase-credit-memo-code";

    private static final String TEST_VENDOR_CODE = "test-vendor-code";
    private static final String TEST_VENDOR_NAME = "test-vendor-name";
    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";

    private static final int TEST_PAGE = 123;
    private static final int TEST_COUNT = 234;

    private static final Date TEST_DATE = new Date();

    private static final int TEST_LINE_COUNT = 12;

    private List<PurchaseCreditMemo> purchaseCreditMemos = new ArrayList<>();

    private List<Vendor> vendors = new ArrayList<>();

    private List<PaymentMethod> paymentMethods = new ArrayList<>();

    private List<PurchaseCreditMemoLine> purchaseCreditMemoLines = new ArrayList<>();

    @Captor
    private ArgumentCaptor<PurchaseCreditMemoModel> purchaseCreditMemoModelArgumentCaptor;

    @Captor
    private ArgumentCaptor<PurchaseCreditMemo> purchaseCreditMemoArgumentCaptor;

    @Mock
    private RedirectAttributes redirectAttributesMock;

    @Mock
    private PagingData purchaseCreditMemoLinePagingData;

    @Mock
    private PurchaseCreditMemo purchaseCreditMemoMock;

    @Mock
    private PurchaseCreditMemoLine purchaseCreditMemoLineMock;

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

    @Mock
    private PurchaseCreditMemoLineService purchaseCreditMemoLineSvcMock;

    @Mock
    private PurchaseCreditMemoPostService purchaseCreditMemoPostSvcMock;

    @Mock
    private PurchaseCodeSeriesService purchaseCodeSeriesSvcMock;

    @Mock
    private FeedbackService feedbackSvcMock;

    @Mock
    private HttpSession httpSessionMock;

    @Mock
    private Feedback feedbackMock;

    private PurchaseCreditMemoController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        purchaseCreditMemos = Arrays.asList(purchaseCreditMemoMock);

        purchaseCreditMemoLines = Arrays.asList(purchaseCreditMemoLineMock);

        when(purchaseCreditMemoModelMock.getCode()).thenReturn("");
        when(purchaseCreditMemoModelMock.getVendorCode()).thenReturn(TEST_VENDOR_CODE);
        when(purchaseCreditMemoModelMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);
        when(purchaseCreditMemoModelMock.getDate()).thenReturn(TEST_DATE);

        when(purchaseCreditMemoMock.getCode()).thenReturn(TEST_PURCHASE_CREDIT_MEMO_CODE);
        when(purchaseCreditMemoMock.getVendor()).thenReturn(vendorMock);
        when(purchaseCreditMemoMock.getPaymentMethod()).thenReturn(paymentMethodMock);
        when(purchaseCreditMemoMock.getDate()).thenReturn(TEST_DATE);

        when(svcMock.list(TEST_PAGE)).thenReturn(purchaseCreditMemos);
        when(svcMock.count()).thenReturn(TEST_COUNT);
        when(svcMock.get(TEST_PURCHASE_CREDIT_MEMO_CODE)).thenReturn(purchaseCreditMemoMock);

        when(pagingSvcMock.generate("purchaseCreditMemo", TEST_PAGE, TEST_COUNT)).thenReturn(pagingDataMock);
        when(pagingSvcMock.generate("purchaseCreditMemoCard", TEST_PURCHASE_CREDIT_MEMO_CODE, TEST_PAGE,
                TEST_LINE_COUNT))
                .thenReturn(purchaseCreditMemoLinePagingData);

        when(vendorMock.getCode()).thenReturn(TEST_VENDOR_CODE);
        when(vendorMock.getName()).thenReturn(TEST_VENDOR_NAME);
        when(vendorMock.getPaymentMethod()).thenReturn(paymentMethodMock);

        when(vendorSvcMock.list()).thenReturn(vendors);
        when(vendorSvcMock.get(TEST_VENDOR_CODE)).thenReturn(vendorMock);

        when(paymentMethodMock.getCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(paymentMethodSvcMock.list()).thenReturn(paymentMethods);
        when(paymentMethodSvcMock.get(TEST_PAYMENT_METHOD_CODE)).thenReturn(paymentMethodMock);

        when(purchaseCreditMemoLineSvcMock.list(purchaseCreditMemoMock, TEST_PAGE)).thenReturn(purchaseCreditMemoLines);
        when(purchaseCreditMemoLineSvcMock.count(purchaseCreditMemoMock)).thenReturn(TEST_LINE_COUNT);

        when(purchaseCodeSeriesSvcMock.creditMemoCode()).thenReturn(TEST_NEW_CREDIT_MEMO_CODE);

        when(feedbackSvcMock.get(httpSessionMock)).thenReturn(feedbackMock);

        controller = new PurchaseCreditMemoController(svcMock, pagingSvcMock, vendorSvcMock, paymentMethodSvcMock,
                purchaseCreditMemoLineSvcMock, purchaseCreditMemoPostSvcMock, purchaseCodeSeriesSvcMock,
                feedbackSvcMock);
    }

    @Test
    public void list_returnsProperTemplate() {
        String template = controller.list(TEST_PAGE, modelMock, httpSessionMock);

        assertEquals("purchaseCreditMemoList.html", template);

        verify(modelMock).addAttribute("purchaseCreditMemos", purchaseCreditMemos);
        verify(modelMock).addAttribute("paging", pagingDataMock);
        verify(modelMock).addAttribute("feedback", feedbackMock);
    }

    @Test
    public void gettingWizard_returnsProperTemplate_whenCalledForNew() {
        String template = controller.wizard(null, modelMock);

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
    public void gettingWizard_returnsProperTemplate_whenCalledForExisting() {
        String template = controller.wizard(TEST_PURCHASE_CREDIT_MEMO_CODE, modelMock);

        assertEquals("purchaseCreditMemoWizardFirstPage.html", template);

        verify(modelMock).addAttribute(eq("purchaseCreditMemoModel"), purchaseCreditMemoModelArgumentCaptor.capture());
        verify(modelMock).addAttribute("vendors", vendors);

        PurchaseCreditMemoModel purchaseCreditMemoModel = purchaseCreditMemoModelArgumentCaptor.getValue();

        assertEquals(TEST_PURCHASE_CREDIT_MEMO_CODE, purchaseCreditMemoModel.getCode());
        assertEquals(TEST_VENDOR_CODE, purchaseCreditMemoModel.getVendorCode());
        assertEquals(TEST_VENDOR_NAME, purchaseCreditMemoModel.getVendorName());
        assertEquals(TEST_PAYMENT_METHOD_CODE, purchaseCreditMemoModel.getPaymentMethodCode());
        assertEquals(TEST_DATE, purchaseCreditMemoModel.getDate());
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
    public void postingWizardSecondPage_createsNewEntity_andReturnsProperTemplate_whenCalledForNew() {
        RedirectView redirectView = controller.postPurchaseCreditMemoWizardSecondPage(purchaseCreditMemoModelMock,
                redirectAttributesMock);

        assertEquals("/purchaseCreditMemoCard", redirectView.getUrl());

        verify(svcMock).create(purchaseCreditMemoArgumentCaptor.capture());

        PurchaseCreditMemo purchaseCreditMemo = purchaseCreditMemoArgumentCaptor.getValue();

        assertEquals(TEST_NEW_CREDIT_MEMO_CODE, purchaseCreditMemo.getCode());
        assertEquals(vendorMock, purchaseCreditMemo.getVendor());
        assertEquals(paymentMethodMock, purchaseCreditMemo.getPaymentMethod());
        assertEquals(TEST_DATE, purchaseCreditMemo.getDate());
    }

    @Test
    public void postingWizardSecondPage_updatesNewEntity_andReturnsProperTemplate_whenCalledForExisting(){
        when(purchaseCreditMemoModelMock.getCode()).thenReturn(TEST_PURCHASE_CREDIT_MEMO_CODE);

        RedirectView redirectView = controller.postPurchaseCreditMemoWizardSecondPage(purchaseCreditMemoModelMock, redirectAttributesMock);

        assertEquals("/purchaseCreditMemoCard", redirectView.getUrl());

        verify(svcMock).update(TEST_PURCHASE_CREDIT_MEMO_CODE, purchaseCreditMemoMock);

        verify(redirectAttributesMock).addAttribute("code", TEST_PURCHASE_CREDIT_MEMO_CODE);
    }

    @Test
    public void delete_deletesEntity() {
        RedirectView redirectView = controller.delete(TEST_PURCHASE_CREDIT_MEMO_CODE);

        assertEquals("/purchaseCreditMemoList", redirectView.getUrl());

        verify(svcMock).delete(TEST_PURCHASE_CREDIT_MEMO_CODE);
    }

    @Test
    public void card_returnsProperTemplate() {
        String template = controller.card(TEST_PURCHASE_CREDIT_MEMO_CODE, TEST_PAGE, modelMock);

        assertEquals("purchaseCreditMemoCard.html", template);

        verify(modelMock).addAttribute("purchaseCreditMemo", purchaseCreditMemoMock);
        verify(modelMock).addAttribute("purchaseCreditMemoLines", purchaseCreditMemoLines);
        verify(modelMock).addAttribute("paging", purchaseCreditMemoLinePagingData);
    }

    @Test
    public void post_returnsProperTemplate() throws PostFailedException {
        RedirectView redirectView = controller.post(TEST_PURCHASE_CREDIT_MEMO_CODE, httpSessionMock);

        assertEquals("/purchaseCreditMemoList", redirectView.getUrl());

        verify(purchaseCreditMemoPostSvcMock).post(TEST_PURCHASE_CREDIT_MEMO_CODE);

        verify(feedbackSvcMock).give(FeedbackType.INFO,
                "Purchase credit memo " + TEST_PURCHASE_CREDIT_MEMO_CODE + " posted.", httpSessionMock);
    }

    @Test
    public void postReturnsProperFeedback_whenPostingFails() throws PostFailedException {
        doThrow(new PostFailedException(TEST_POST_FAILED_MSG)).when(purchaseCreditMemoPostSvcMock)
                .post(TEST_PURCHASE_CREDIT_MEMO_CODE);

        RedirectView redirectView = controller.post(TEST_PURCHASE_CREDIT_MEMO_CODE, httpSessionMock);

        assertEquals("/purchaseCreditMemoList", redirectView.getUrl());

        verify(purchaseCreditMemoPostSvcMock).post(TEST_PURCHASE_CREDIT_MEMO_CODE);

        verify(feedbackSvcMock).give(FeedbackType.ERROR,
                "Purchase credit memo " + TEST_PURCHASE_CREDIT_MEMO_CODE + " post failed: " + TEST_POST_FAILED_MSG,
                httpSessionMock);
    }
}