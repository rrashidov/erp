package org.roko.erp.frontend.controllers;

import javax.servlet.http.HttpSession;

import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.dto.VendorDTO;
import org.roko.erp.dto.list.PurchaseDocumentLineList;
import org.roko.erp.dto.list.PurchaseDocumentList;
import org.roko.erp.dto.list.VendorList;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.services.DeleteFailedException;
import org.roko.erp.frontend.services.FeedbackService;
import org.roko.erp.frontend.services.PaymentMethodService;
import org.roko.erp.frontend.services.PostFailedException;
import org.roko.erp.frontend.services.PurchaseCreditMemoLineService;
import org.roko.erp.frontend.services.PurchaseCreditMemoPostService;
import org.roko.erp.frontend.services.PurchaseCreditMemoService;
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
public class PurchaseCreditMemoController {

    private PurchaseCreditMemoService svc;
    private PagingService pagingSvc;
    private VendorService vendorSvc;
    private PaymentMethodService paymentMethodSvc;
    private PurchaseCreditMemoLineService purchaseCreditMemoLineSvc;
    private PurchaseCreditMemoPostService purchaseCreditMemoPostSvc;
    private FeedbackService feedbackSvc;

    @Autowired
    public PurchaseCreditMemoController(PurchaseCreditMemoService svc, PagingService pagingSvc,
            VendorService vendorSvc, PaymentMethodService paymentMethodSvc,
            PurchaseCreditMemoLineService purchaseCreditMemoLineSvc,
            PurchaseCreditMemoPostService purchaseCreditMemoPostSvc,
            FeedbackService feedbackSvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
        this.vendorSvc = vendorSvc;
        this.paymentMethodSvc = paymentMethodSvc;
        this.purchaseCreditMemoLineSvc = purchaseCreditMemoLineSvc;
        this.purchaseCreditMemoPostSvc = purchaseCreditMemoPostSvc;
        this.feedbackSvc = feedbackSvc;
    }

    @GetMapping("/purchaseCreditMemoList")
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model,
            HttpSession httpSession) {
        PurchaseDocumentList purchaseCreditMemoList = svc.list(page);
        PagingData pagingData = pagingSvc.generate("purchaseCreditMemo", page, (int) purchaseCreditMemoList.getCount());
        Feedback feedback = feedbackSvc.get(httpSession);

        model.addAttribute("purchaseCreditMemos", purchaseCreditMemoList.getData());
        model.addAttribute("paging", pagingData);
        model.addAttribute("feedback", feedback);

        return "purchaseCreditMemoList.html";
    }

    @GetMapping("/purchaseCreditMemoWizard")
    public String wizard(@RequestParam(name = "code", required = false) String code, Model model) {
        PurchaseDocumentDTO purchaseCreditMemo = new PurchaseDocumentDTO();

        if (code != null) {
            purchaseCreditMemo = svc.get(code);
        }

        VendorList vendors = vendorSvc.list();

        model.addAttribute("purchaseCreditMemoModel", purchaseCreditMemo);
        model.addAttribute("vendors", vendors.getData());

        return "purchaseCreditMemoWizardFirstPage.html";
    }

    @PostMapping("/purchaseCreditMemoWizardFirstPage")
    public String postPurchaseCreditMemoWizardFirstPage(@ModelAttribute PurchaseDocumentDTO purchaseCreditMemoModel,
            Model model) {
        VendorDTO vendor = vendorSvc.get(purchaseCreditMemoModel.getVendorCode());

        purchaseCreditMemoModel.setVendorName(vendor.getName());
        purchaseCreditMemoModel.setPaymentMethodCode(vendor.getPaymentMethodCode());

        model.addAttribute("purchaseCreditMemoModel", purchaseCreditMemoModel);
        model.addAttribute("paymentMethods", paymentMethodSvc.list().getData());

        return "purchaseCreditMemoWizardSecondPage.html";
    }

    @PostMapping("/purchaseCreditMemoWizardSecondPage")
    public RedirectView postPurchaseCreditMemoWizardSecondPage(
            @ModelAttribute PurchaseDocumentDTO purchaseCreditMemoModel, RedirectAttributes redirectAttributes) {
        String code = purchaseCreditMemoModel.getCode();

        if (purchaseCreditMemoModel.getCode().isEmpty()) {
            code = svc.create(purchaseCreditMemoModel);
        } else {
            svc.update(purchaseCreditMemoModel.getCode(), purchaseCreditMemoModel);
        }

        redirectAttributes.addAttribute("code", code);

        return new RedirectView("/purchaseCreditMemoCard");
    }

    @GetMapping("/deletePurchaseCreditMemo")
    public RedirectView delete(@RequestParam(name = "code") String code, HttpSession httpSession) {
        try {
            svc.delete(code);

            feedbackSvc.give(FeedbackType.INFO, String.format("Purchase Credit Memo %s deleted", code), httpSession);
        } catch (DeleteFailedException e) {
            feedbackSvc.give(FeedbackType.ERROR, e.getMessage(), httpSession);
        }

        return new RedirectView("/purchaseCreditMemoList");
    }

    @GetMapping("/purchaseCreditMemoCard")
    public String card(@RequestParam(name = "code") String code,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        PurchaseDocumentDTO purchaseCreditMemo = svc.get(code);

        PurchaseDocumentLineList purchaseCreditMemoLineList = purchaseCreditMemoLineSvc.list(code, page);

        PagingData pagingData = pagingSvc.generate("purchaseCreditMemoCard", code, page,
                (int) purchaseCreditMemoLineList.getCount());

        model.addAttribute("purchaseCreditMemo", purchaseCreditMemo);
        model.addAttribute("purchaseCreditMemoLines", purchaseCreditMemoLineList.getData());
        model.addAttribute("paging", pagingData);

        return "purchaseCreditMemoCard.html";
    }

    @GetMapping("/postPurchaseCreditMemo")
    public RedirectView post(@RequestParam(name = "code") String code, HttpSession httpSessionMock) {
        try {
            purchaseCreditMemoPostSvc.post(code);

            feedbackSvc.give(FeedbackType.INFO, "Purchase credit memo " + code + " post scheduled.", httpSessionMock);
        } catch (PostFailedException e) {
            feedbackSvc.give(FeedbackType.ERROR, "Purchase credit memo " + code + " post scheduling failed: " + e.getMessage(),
                    httpSessionMock);
        }

        return new RedirectView("/purchaseCreditMemoList");
    }

}
