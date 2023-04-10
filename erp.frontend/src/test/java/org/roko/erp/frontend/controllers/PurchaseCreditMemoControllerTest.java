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
import org.roko.erp.frontend.services.FeedbackService;
import org.roko.erp.frontend.services.PaymentMethodService;
import org.roko.erp.frontend.services.PostFailedException;
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
        private static final long TEST_COUNT = 234;

        private static final Date TEST_DATE = new Date();

        private static final long TEST_LINE_COUNT = 12;

        private List<PurchaseDocumentDTO> purchaseCreditMemos = new ArrayList<>();

        private List<VendorDTO> vendors = new ArrayList<>();

        private List<PaymentMethodDTO> paymentMethods = new ArrayList<>();

        private List<PurchaseDocumentLineDTO> purchaseCreditMemoLines = new ArrayList<>();

        @Captor
        private ArgumentCaptor<PurchaseDocumentDTO> purchaseCreditMemoModelArgumentCaptor;

        @Captor
        private ArgumentCaptor<PurchaseDocumentDTO> purchaseCreditMemoArgumentCaptor;

        @Mock
        private RedirectAttributes redirectAttributesMock;

        @Mock
        private PagingData purchaseCreditMemoLinePagingData;

        @Mock
        private PurchaseDocumentDTO purchaseCreditMemoMock;

        @Mock
        private PurchaseDocumentLineDTO purchaseCreditMemoLineMock;

        @Mock
        private PaymentMethodDTO paymentMethodMock;

        @Mock
        private VendorDTO vendorMock;

        @Mock
        private PaymentMethodService paymentMethodSvcMock;

        @Mock
        private PurchaseDocumentDTO purchaseCreditMemoModelMock;

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
        private FeedbackService feedbackSvcMock;

        @Mock
        private HttpSession httpSessionMock;

        @Mock
        private Feedback feedbackMock;

        @Mock
        private PurchaseDocumentList purchaseCreditMemoList;

        @Mock
        private PurchaseDocumentLineList purchaseCreditMemoLineList;

        @Mock
        private VendorList vendorList;

        @Mock
        private PaymentMethodList paymentMethodList;

        private PurchaseCreditMemoController controller;

        @BeforeEach
        public void setup() {
                MockitoAnnotations.openMocks(this);

                purchaseCreditMemos = Arrays.asList(purchaseCreditMemoMock);

                purchaseCreditMemoLines = Arrays.asList(purchaseCreditMemoLineMock);

                paymentMethods = Arrays.asList(paymentMethodMock);

                when(purchaseCreditMemoModelMock.getCode()).thenReturn("");
                when(purchaseCreditMemoModelMock.getVendorCode()).thenReturn(TEST_VENDOR_CODE);
                when(purchaseCreditMemoModelMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);
                when(purchaseCreditMemoModelMock.getDate()).thenReturn(TEST_DATE);

                when(purchaseCreditMemoMock.getCode()).thenReturn(TEST_PURCHASE_CREDIT_MEMO_CODE);
                when(purchaseCreditMemoMock.getVendorCode()).thenReturn(TEST_VENDOR_CODE);
                when(purchaseCreditMemoMock.getVendorName()).thenReturn(TEST_VENDOR_NAME);
                when(purchaseCreditMemoMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);
                when(purchaseCreditMemoMock.getDate()).thenReturn(TEST_DATE);

                when(purchaseCreditMemoList.getData()).thenReturn(purchaseCreditMemos);
                when(purchaseCreditMemoList.getCount()).thenReturn(TEST_COUNT);

                when(svcMock.list(TEST_PAGE)).thenReturn(purchaseCreditMemoList);
                when(svcMock.get(TEST_PURCHASE_CREDIT_MEMO_CODE)).thenReturn(purchaseCreditMemoMock);
                when(svcMock.create(any(PurchaseDocumentDTO.class))).thenReturn(TEST_NEW_CREDIT_MEMO_CODE);

                when(pagingSvcMock.generate("purchaseCreditMemo", TEST_PAGE, (int) TEST_COUNT))
                                .thenReturn(pagingDataMock);
                when(pagingSvcMock.generate("purchaseCreditMemoCard", TEST_PURCHASE_CREDIT_MEMO_CODE, TEST_PAGE,
                                (int) TEST_LINE_COUNT))
                                .thenReturn(purchaseCreditMemoLinePagingData);

                when(vendorMock.getCode()).thenReturn(TEST_VENDOR_CODE);
                when(vendorMock.getName()).thenReturn(TEST_VENDOR_NAME);
                when(vendorMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

                when(vendorList.getData()).thenReturn(vendors);

                when(vendorSvcMock.list()).thenReturn(vendorList);
                when(vendorSvcMock.get(TEST_VENDOR_CODE)).thenReturn(vendorMock);

                when(paymentMethodMock.getCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

                when(paymentMethodList.getData()).thenReturn(paymentMethods);

                when(paymentMethodSvcMock.list()).thenReturn(paymentMethodList);
                when(paymentMethodSvcMock.get(TEST_PAYMENT_METHOD_CODE)).thenReturn(paymentMethodMock);

                when(purchaseCreditMemoLineList.getData()).thenReturn(purchaseCreditMemoLines);
                when(purchaseCreditMemoLineList.getCount()).thenReturn(TEST_LINE_COUNT);

                when(purchaseCreditMemoLineSvcMock.list(TEST_PURCHASE_CREDIT_MEMO_CODE, TEST_PAGE))
                                .thenReturn(purchaseCreditMemoLineList);

                when(feedbackSvcMock.get(httpSessionMock)).thenReturn(feedbackMock);

                controller = new PurchaseCreditMemoController(svcMock, pagingSvcMock, vendorSvcMock,
                                paymentMethodSvcMock,
                                purchaseCreditMemoLineSvcMock, purchaseCreditMemoPostSvcMock,
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

                verify(modelMock).addAttribute(eq("purchaseCreditMemoModel"),
                                purchaseCreditMemoModelArgumentCaptor.capture());
                verify(modelMock).addAttribute("vendors", vendors);

                PurchaseDocumentDTO purchaseCreditMemoModel = purchaseCreditMemoModelArgumentCaptor.getValue();

                assertEquals("", purchaseCreditMemoModel.getCode());
                assertEquals("", purchaseCreditMemoModel.getVendorCode());
                assertEquals("", purchaseCreditMemoModel.getVendorName());
                assertEquals("", purchaseCreditMemoModel.getPaymentMethodCode());
        }

        @Test
        public void gettingWizard_returnsProperTemplate_whenCalledForExisting() {
                String template = controller.wizard(TEST_PURCHASE_CREDIT_MEMO_CODE,
                                modelMock);

                assertEquals("purchaseCreditMemoWizardFirstPage.html", template);

                verify(modelMock).addAttribute(eq("purchaseCreditMemoModel"),
                                purchaseCreditMemoModelArgumentCaptor.capture());
                verify(modelMock).addAttribute("vendors", vendors);

                PurchaseDocumentDTO purchaseCreditMemoModel = purchaseCreditMemoModelArgumentCaptor.getValue();

                assertEquals(TEST_PURCHASE_CREDIT_MEMO_CODE,
                                purchaseCreditMemoModel.getCode());
                assertEquals(TEST_VENDOR_CODE, purchaseCreditMemoModel.getVendorCode());
                assertEquals(TEST_VENDOR_NAME, purchaseCreditMemoModel.getVendorName());
                assertEquals(TEST_PAYMENT_METHOD_CODE,
                                purchaseCreditMemoModel.getPaymentMethodCode());
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
                RedirectView redirectView = controller.postPurchaseCreditMemoWizardSecondPage(
                                purchaseCreditMemoModelMock,
                                redirectAttributesMock);

                assertEquals("/purchaseCreditMemoCard", redirectView.getUrl());

                verify(svcMock).create(purchaseCreditMemoArgumentCaptor.capture());

                verify(redirectAttributesMock).addAttribute("code", TEST_NEW_CREDIT_MEMO_CODE);

                PurchaseDocumentDTO purchaseCreditMemo = purchaseCreditMemoArgumentCaptor.getValue();

                assertEquals(TEST_VENDOR_CODE, purchaseCreditMemo.getVendorCode());
                assertEquals(TEST_PAYMENT_METHOD_CODE, purchaseCreditMemo.getPaymentMethodCode());
                assertEquals(TEST_DATE, purchaseCreditMemo.getDate());
        }

        @Test
        public void postingWizardSecondPage_updatesNewEntity_andReturnsProperTemplate_whenCalledForExisting(){
                when(purchaseCreditMemoModelMock.getCode()).thenReturn(TEST_PURCHASE_CREDIT_MEMO_CODE);

                RedirectView redirectView = controller.postPurchaseCreditMemoWizardSecondPage(purchaseCreditMemoModelMock, redirectAttributesMock);

                assertEquals("/purchaseCreditMemoCard", redirectView.getUrl());

                verify(svcMock).update(TEST_PURCHASE_CREDIT_MEMO_CODE, purchaseCreditMemoModelMock);

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
                String template = controller.card(TEST_PURCHASE_CREDIT_MEMO_CODE, TEST_PAGE,
                                modelMock);

                assertEquals("purchaseCreditMemoCard.html", template);

                verify(modelMock).addAttribute("purchaseCreditMemo", purchaseCreditMemoMock);
                verify(modelMock).addAttribute("purchaseCreditMemoLines",
                                purchaseCreditMemoLines);
                verify(modelMock).addAttribute("paging", purchaseCreditMemoLinePagingData);
        }

        @Test
        public void post_returnsProperTemplate() throws PostFailedException {
                RedirectView redirectView = controller.post(TEST_PURCHASE_CREDIT_MEMO_CODE,
                                httpSessionMock);

                assertEquals("/purchaseCreditMemoList", redirectView.getUrl());

                verify(purchaseCreditMemoPostSvcMock).post(TEST_PURCHASE_CREDIT_MEMO_CODE);

                verify(feedbackSvcMock).give(FeedbackType.INFO,
                                "Purchase credit memo " + TEST_PURCHASE_CREDIT_MEMO_CODE + " post scheduled.",
                                httpSessionMock);
        }

        @Test
        public void postReturnsProperFeedback_whenPostingFails() throws PostFailedException {
                doThrow(new PostFailedException(TEST_POST_FAILED_MSG)).when(purchaseCreditMemoPostSvcMock)
                                .post(TEST_PURCHASE_CREDIT_MEMO_CODE);

                RedirectView redirectView = controller.post(TEST_PURCHASE_CREDIT_MEMO_CODE,
                                httpSessionMock);

                assertEquals("/purchaseCreditMemoList", redirectView.getUrl());

                verify(purchaseCreditMemoPostSvcMock).post(TEST_PURCHASE_CREDIT_MEMO_CODE);

                verify(feedbackSvcMock).give(FeedbackType.ERROR,
                                "Purchase credit memo " + TEST_PURCHASE_CREDIT_MEMO_CODE + " post scheduling failed: " +
                                                TEST_POST_FAILED_MSG,
                                httpSessionMock);
        }
}