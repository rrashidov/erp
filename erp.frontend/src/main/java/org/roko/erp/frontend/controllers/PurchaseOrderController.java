package org.roko.erp.frontend.controllers;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.roko.erp.frontend.controllers.model.PurchaseOrderModel;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.model.PurchaseOrder;
import org.roko.erp.frontend.model.PurchaseOrderLine;
import org.roko.erp.frontend.model.Vendor;
import org.roko.erp.frontend.services.FeedbackService;
import org.roko.erp.frontend.services.PaymentMethodService;
import org.roko.erp.frontend.services.PostFailedException;
import org.roko.erp.frontend.services.PurchaseCodeSeriesService;
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
    private PurchaseCodeSeriesService purchaseCodeSeriesSvc;
    private FeedbackService feedbackSvc;

    @Autowired
    public PurchaseOrderController(PurchaseOrderService svc, PurchaseOrderLineService purchaseOrderLineSvc,
            VendorService vendorSvc,
            PaymentMethodService paymentMethodSvc, PagingService pagingSvc,
            PurchaseOrderPostService purchaseOrderPostSvc, PurchaseCodeSeriesService purchaseCodeSeriesSvc,
            FeedbackService feedbackSvc) {
        this.svc = svc;
        this.purchaseOrderLineSvc = purchaseOrderLineSvc;
        this.vendorSvc = vendorSvc;
        this.paymentMethodSvc = paymentMethodSvc;
        this.pagingSvc = pagingSvc;
        this.purchaseOrderPostSvc = purchaseOrderPostSvc;
        this.purchaseCodeSeriesSvc = purchaseCodeSeriesSvc;
        this.feedbackSvc = feedbackSvc;
    }

    @GetMapping("/purchaseOrderList")
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model,
            HttpSession httpSession) {
        List<PurchaseOrder> purchaseOrders = svc.list(page);
        PagingData pagingData = pagingSvc.generate("purchaseOrder", page, svc.count());
        Feedback feedback = feedbackSvc.get(httpSession);

        model.addAttribute("purchaseOrders", purchaseOrders);
        model.addAttribute("paging", pagingData);
        model.addAttribute("feedback", feedback);

        return "purchaseOrderList.html";
    }

    @GetMapping("/purchaseOrderWizard")
    public String wizard(@RequestParam(name = "code", required = false) String code, Model model) {
        PurchaseOrderModel purchaseOrderModel = new PurchaseOrderModel();

        if (code != null) {
            PurchaseOrder purchaseOrder = svc.get(code);
            toModel(purchaseOrder, purchaseOrderModel);
        }

        List<Vendor> vendors = vendorSvc.list();

        model.addAttribute("purchaseOrderModel", purchaseOrderModel);
        model.addAttribute("vendors", vendors);

        return "purchaseOrderWizardFirstPage.html";
    }

    @PostMapping("/purchaseOrderWizardFirstPage")
    public String postPurchaseOrderWizardFirstPage(@ModelAttribute PurchaseOrderModel purchaseOrderModel, Model model) {
        Vendor vendor = vendorSvc.get(purchaseOrderModel.getVendorCode());

        purchaseOrderModel.setVendorName(vendor.getName());
        purchaseOrderModel.setDate(new Date());
        purchaseOrderModel.setPaymentMethodCode(vendor.getPaymentMethod().getCode());

        model.addAttribute("purchaseOrderModel", purchaseOrderModel);
        model.addAttribute("paymentMethods", paymentMethodSvc.list());

        return "purchaseOrderWizardSecondPage.html";
    }

    @PostMapping("/purchaseOrderWizardSecondPage")
    public RedirectView postPurchaseOrderWizardSecondPage(@ModelAttribute PurchaseOrderModel purchaseOrderModel,
            RedirectAttributes redirectAttributes) {
        if (purchaseOrderModel.getCode().isEmpty()) {
            PurchaseOrder purchaseOrder = fromModel(purchaseOrderModel);

            svc.create(purchaseOrder);

            redirectAttributes.addAttribute("code", purchaseOrder.getCode());
        } else {
            PurchaseOrder purchaseOrder = svc.get(purchaseOrderModel.getCode());
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
        PurchaseOrder purchaseOrder = svc.get(code);
        List<PurchaseOrderLine> purchaseOrderLines = purchaseOrderLineSvc.list(purchaseOrder, page);
        PagingData pagingData = pagingSvc.generate("purchaseOrderCard", code, page,
                purchaseOrderLineSvc.count(purchaseOrder));

        model.addAttribute("purchaseOrder", purchaseOrder);
        model.addAttribute("purchaseOrderLines", purchaseOrderLines);
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

    private PurchaseOrder fromModel(PurchaseOrderModel purchaseOrderModel) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setCode(purchaseCodeSeriesSvc.orderCode());
        purchaseOrder.setVendor(vendorSvc.get(purchaseOrderModel.getVendorCode()));
        purchaseOrder.setDate(purchaseOrderModel.getDate());
        purchaseOrder.setPaymentMethod(paymentMethodSvc.get(purchaseOrderModel.getPaymentMethodCode()));
        return purchaseOrder;
    }

    private void fromModel(PurchaseOrder purchaseOrder, PurchaseOrderModel purchaseOrderModel) {
        purchaseOrder.setVendor(vendorSvc.get(purchaseOrderModel.getVendorCode()));
        purchaseOrder.setDate(purchaseOrderModel.getDate());
        purchaseOrder.setPaymentMethod(paymentMethodSvc.get(purchaseOrderModel.getPaymentMethodCode()));
    }

    private void toModel(PurchaseOrder purchaseOrder, PurchaseOrderModel purchaseOrderModel) {
        purchaseOrderModel.setCode(purchaseOrder.getCode());
        purchaseOrderModel.setDate(purchaseOrder.getDate());
        purchaseOrderModel.setPaymentMethodCode(purchaseOrder.getPaymentMethod().getCode());
        purchaseOrderModel.setVendorCode(purchaseOrder.getVendor().getCode());
        purchaseOrderModel.setVendorName(purchaseOrder.getVendor().getName());
    }
}
