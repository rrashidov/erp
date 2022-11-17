package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.controllers.model.PurchaseCreditMemoModel;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.PurchaseCreditMemo;
import org.roko.erp.model.PurchaseCreditMemoLine;
import org.roko.erp.model.Vendor;
import org.roko.erp.services.PaymentMethodService;
import org.roko.erp.services.PurchaseCreditMemoLineService;
import org.roko.erp.services.PurchaseCreditMemoPostService;
import org.roko.erp.services.PurchaseCreditMemoService;
import org.roko.erp.services.VendorService;
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

    @Autowired
    public PurchaseCreditMemoController(PurchaseCreditMemoService svc, PagingService pagingSvc,
            VendorService vendorSvc, PaymentMethodService paymentMethodSvc,
            PurchaseCreditMemoLineService purchaseCreditMemoLineSvc,
            PurchaseCreditMemoPostService purchaseCreditMemoPostSvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
        this.vendorSvc = vendorSvc;
        this.paymentMethodSvc = paymentMethodSvc;
        this.purchaseCreditMemoLineSvc = purchaseCreditMemoLineSvc;
        this.purchaseCreditMemoPostSvc = purchaseCreditMemoPostSvc;
    }

    @GetMapping("/purchaseCreditMemoList")
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        List<PurchaseCreditMemo> purchaseCreditMemos = svc.list(page);
        PagingData pagingData = pagingSvc.generate("purchaseCreditMemo", page, svc.count());

        model.addAttribute("purchaseCreditMemos", purchaseCreditMemos);
        model.addAttribute("paging", pagingData);

        return "purchaseCreditMemoList.html";
    }

    @GetMapping("/purchaseCreditMemoWizard")
    public String wizard(@RequestParam(name = "code", required = false) String code, Model model) {
        PurchaseCreditMemoModel purchaseCreditMemoModel = new PurchaseCreditMemoModel();

        if (code != null) {
            PurchaseCreditMemo purchaseCreditMemo = svc.get(code);
            toModel(purchaseCreditMemo, purchaseCreditMemoModel);
        }

        List<Vendor> vendors = vendorSvc.list();

        model.addAttribute("purchaseCreditMemoModel", purchaseCreditMemoModel);
        model.addAttribute("vendors", vendors);

        return "purchaseCreditMemoWizardFirstPage.html";
    }

    @PostMapping("/purchaseCreditMemoWizardFirstPage")
    public String postPurchaseCreditMemoWizardFirstPage(@ModelAttribute PurchaseCreditMemoModel purchaseCreditMemoModel,
            Model model) {
        Vendor vendor = vendorSvc.get(purchaseCreditMemoModel.getVendorCode());

        purchaseCreditMemoModel.setVendorName(vendor.getName());
        purchaseCreditMemoModel.setPaymentMethodCode(vendor.getPaymentMethod().getCode());

        model.addAttribute("purchaseCreditMemoModel", purchaseCreditMemoModel);
        model.addAttribute("paymentMethods", paymentMethodSvc.list());

        return "purchaseCreditMemoWizardSecondPage.html";
    }

    @PostMapping("/purchaseCreditMemoWizardSecondPage")
    public RedirectView postPurchaseCreditMemoWizardSecondPage(
            @ModelAttribute PurchaseCreditMemoModel purchaseCreditMemoModel, RedirectAttributes redirectAttributes) {
        if (purchaseCreditMemoModel.getCode().isEmpty()) {
            String purchaseCreditMemoCode = createPurchaseCreditMemo(purchaseCreditMemoModel);

            redirectAttributes.addAttribute("code", purchaseCreditMemoCode);
        } else {
            updatePurchaseCreditMemo(purchaseCreditMemoModel);

            redirectAttributes.addAttribute("code", purchaseCreditMemoModel.getCode());
        }

        return new RedirectView("/purchaseCreditMemoCard");
    }

    @GetMapping("/deletePurchaseCreditMemo")
    public RedirectView delete(@RequestParam(name = "code") String code) {
        svc.delete(code);

        return new RedirectView("/purchaseCreditMemoList");
    }

    @GetMapping("/purchaseCreditMemoCard")
    public String card(@RequestParam(name = "code") String code, Model model) {
        PurchaseCreditMemo purchaseCreditMemo = svc.get(code);
        List<PurchaseCreditMemoLine> purchaseCreditMemoLines = purchaseCreditMemoLineSvc.list(purchaseCreditMemo);
        PagingData pagingData = pagingSvc.generate("purchaseCreditMemoLine", 1,
                purchaseCreditMemoLineSvc.count(purchaseCreditMemo));

        model.addAttribute("purchaseCreditMemo", purchaseCreditMemo);
        model.addAttribute("purchaseCreditMemoLines", purchaseCreditMemoLines);
        model.addAttribute("paging", pagingData);

        return "purchaseCreditMemoCard.html";
    }

    @GetMapping("/postPurchaseCreditMemo")
    public RedirectView post(@RequestParam(name = "code") String code) {
        purchaseCreditMemoPostSvc.post(code);
        
        return new RedirectView("/purchaseCreditMemoList");
    }

    private String createPurchaseCreditMemo(PurchaseCreditMemoModel purchaseCreditMemoModel) {
        PurchaseCreditMemo purchaseCreditMemo = new PurchaseCreditMemo();
        purchaseCreditMemo.setCode("PCM" + System.currentTimeMillis());
        fromModel(purchaseCreditMemoModel, purchaseCreditMemo);
        svc.create(purchaseCreditMemo);

        return purchaseCreditMemo.getCode();
    }

    private void updatePurchaseCreditMemo(PurchaseCreditMemoModel purchaseCreditMemoModel) {
        PurchaseCreditMemo purchaseCreditMemo = svc.get(purchaseCreditMemoModel.getCode());
        fromModel(purchaseCreditMemoModel, purchaseCreditMemo);
        svc.update(purchaseCreditMemoModel.getCode(), purchaseCreditMemo);
    }

    private void fromModel(PurchaseCreditMemoModel purchaseCreditMemoModel, PurchaseCreditMemo purchaseCreditMemo) {
        purchaseCreditMemo.setVendor(vendorSvc.get(purchaseCreditMemoModel.getVendorCode()));
        purchaseCreditMemo.setDate(purchaseCreditMemoModel.getDate());
        purchaseCreditMemo.setPaymentMethod(paymentMethodSvc.get(purchaseCreditMemoModel.getPaymentMethodCode()));
    }

    private void toModel(PurchaseCreditMemo purchaseCreditMemo, PurchaseCreditMemoModel purchaseCreditMemoModel) {
        purchaseCreditMemoModel.setCode(purchaseCreditMemo.getCode());
        purchaseCreditMemoModel.setVendorCode(purchaseCreditMemo.getVendor().getCode());
        purchaseCreditMemoModel.setVendorName(purchaseCreditMemo.getVendor().getName());
        purchaseCreditMemoModel.setDate(purchaseCreditMemo.getDate());
        purchaseCreditMemoModel.setPaymentMethodCode(purchaseCreditMemo.getPaymentMethod().getCode());
    }
}
