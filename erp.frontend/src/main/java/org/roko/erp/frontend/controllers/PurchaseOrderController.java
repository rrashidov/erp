package org.roko.erp.frontend.controllers;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.roko.erp.dto.PurchaseDocumentDTO;
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
import org.roko.erp.frontend.services.PurchaseOrderLineService;
import org.roko.erp.frontend.services.PurchaseOrderPostService;
import org.roko.erp.frontend.services.PurchaseOrderService;
import org.roko.erp.frontend.services.VendorService;
import org.roko.erp.frontend.services.util.Feedback;
import org.roko.erp.frontend.services.util.FeedbackType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class PurchaseOrderController {

    private PurchaseOrderService svc;
    private PagingService pagingSvc;
    private VendorService vendorSvc;
    private PaymentMethodService paymentMethodSvc;
    private PurchaseOrderLineService purchaseOrderLineSvc;
    private PurchaseOrderPostService purchaseOrderPostSvc;
    private FeedbackService feedbackSvc;

    @Autowired
    public PurchaseOrderController(PurchaseOrderService svc, PurchaseOrderLineService purchaseOrderLineSvc,
            VendorService vendorSvc,
            PaymentMethodService paymentMethodSvc, PagingService pagingSvc,
            PurchaseOrderPostService purchaseOrderPostSvc,
            FeedbackService feedbackSvc) {
        this.svc = svc;
        this.purchaseOrderLineSvc = purchaseOrderLineSvc;
        this.vendorSvc = vendorSvc;
        this.paymentMethodSvc = paymentMethodSvc;
        this.pagingSvc = pagingSvc;
        this.purchaseOrderPostSvc = purchaseOrderPostSvc;
        this.feedbackSvc = feedbackSvc;
    }

    @GetMapping("/purchaseOrderList")
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model,
            HttpSession httpSession) {
        PurchaseDocumentList purchaseOrderList = svc.list(page);
        PagingData pagingData = pagingSvc.generate("purchaseOrder", page, (int) purchaseOrderList.getCount());
        Feedback feedback = feedbackSvc.get(httpSession);

        model.addAttribute("purchaseOrders", purchaseOrderList.getData());
        model.addAttribute("paging", pagingData);
        model.addAttribute("feedback", feedback);

        return "purchaseOrderList.html";
    }

    @GetMapping("/purchaseOrderWizard")
    public String wizard(@RequestParam(name = "code", required = false) String code, Model model) {
        PurchaseDocumentDTO purchaseOrderModel = new PurchaseDocumentDTO();

        if (code != null) {
            PurchaseDocumentDTO purchaseOrder = svc.get(code);
            toModel(purchaseOrder, purchaseOrderModel);
        }

        VendorList vendorList = vendorSvc.list();

        model.addAttribute("purchaseOrderModel", purchaseOrderModel);
        model.addAttribute("vendors", vendorList.getData());

        return "purchaseOrderWizardFirstPage.html";
    }

    @PostMapping("/purchaseOrderWizardFirstPage")
    public String postPurchaseOrderWizardFirstPage(@ModelAttribute PurchaseDocumentDTO purchaseOrderModel, Model model) {
        VendorDTO vendor = vendorSvc.get(purchaseOrderModel.getVendorCode());

        purchaseOrderModel.setVendorName(vendor.getName());
        purchaseOrderModel.setDate(new Date());
        purchaseOrderModel.setPaymentMethodCode(vendor.getPaymentMethodCode());

        PaymentMethodList paymentMethodList = paymentMethodSvc.list();

        model.addAttribute("purchaseOrderModel", purchaseOrderModel);
        model.addAttribute("paymentMethods", paymentMethodList.getData());

        return "purchaseOrderWizardSecondPage.html";
    }

    @PostMapping("/purchaseOrderWizardSecondPage")
    public RedirectView postPurchaseOrderWizardSecondPage(@ModelAttribute PurchaseDocumentDTO purchaseOrderModel,
            RedirectAttributes redirectAttributes) {
        if (purchaseOrderModel.getCode().isEmpty()) {
            PurchaseDocumentDTO purchaseOrder = fromModel(purchaseOrderModel);

            String code = svc.create(purchaseOrder);

            redirectAttributes.addAttribute("code", code);
        } else {
            PurchaseDocumentDTO purchaseOrder = svc.get(purchaseOrderModel.getCode());

            fromModel(purchaseOrder, purchaseOrderModel);

            svc.update(purchaseOrderModel.getCode(), purchaseOrder);

            redirectAttributes.addAttribute("code", purchaseOrder.getCode());
        }

        return new RedirectView("/purchaseOrderCard");
    }

    @GetMapping("/deletePurchaseOrder")
    public RedirectView delete(@RequestParam(name = "code") String code) {
        svc.delete(code);

        return new RedirectView("/purchaseOrderList");
    }

    @GetMapping("/purchaseOrderCard")
    public String card(@RequestParam(name = "code") String code,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        PurchaseDocumentDTO purchaseOrder = svc.get(code);

        PurchaseDocumentLineList purchaseOrderLineList = purchaseOrderLineSvc.list(code, page);

        PagingData pagingData = pagingSvc.generate("purchaseOrderCard", code, page,
                (int) purchaseOrderLineList.getCount());

        model.addAttribute("purchaseOrder", purchaseOrder);
        model.addAttribute("purchaseOrderLines", purchaseOrderLineList.getData());
        model.addAttribute("paging", pagingData);

        return "purchaseOrderCard.html";
    }

    @GetMapping("/postPurchaseOrder")
    public RedirectView post(@RequestParam(name = "code") String code, HttpSession httpSessionMock) {
        try {
            purchaseOrderPostSvc.post(code);

            feedbackSvc.give(FeedbackType.INFO, "Purchase order " + code + " posted.", httpSessionMock);
        } catch (PostFailedException e) {
            feedbackSvc.give(FeedbackType.ERROR, "Purchase order " + code + " post failed: " + e.getMessage(),
                    httpSessionMock);
        }

        return new RedirectView("/purchaseOrderList");
    }

    private PurchaseDocumentDTO fromModel(PurchaseDocumentDTO purchaseOrderModel) {
        PurchaseDocumentDTO purchaseOrder = new PurchaseDocumentDTO();
        purchaseOrder.setVendorCode(purchaseOrderModel.getVendorCode());
        purchaseOrder.setDate(purchaseOrderModel.getDate());
        purchaseOrder.setPaymentMethodCode(purchaseOrderModel.getPaymentMethodCode());
        return purchaseOrder;
    }

    private void fromModel(PurchaseDocumentDTO purchaseOrder, PurchaseDocumentDTO purchaseOrderModel) {
        purchaseOrder.setVendorCode(purchaseOrderModel.getVendorCode());
        purchaseOrder.setDate(purchaseOrderModel.getDate());
        purchaseOrder.setPaymentMethodCode(purchaseOrderModel.getPaymentMethodCode());
    }

    private void toModel(PurchaseDocumentDTO purchaseOrder, PurchaseDocumentDTO purchaseOrderModel) {
        purchaseOrderModel.setCode(purchaseOrder.getCode());
        purchaseOrderModel.setDate(purchaseOrder.getDate());
        purchaseOrderModel.setPaymentMethodCode(purchaseOrder.getPaymentMethodCode());
        purchaseOrderModel.setVendorCode(purchaseOrder.getVendorCode());
        purchaseOrderModel.setVendorName(purchaseOrder.getVendorName());
    }
}
