package org.roko.erp.controllers;

import java.util.Date;
import java.util.List;

import org.roko.erp.controllers.model.PurchaseOrderModel;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.PurchaseOrder;
import org.roko.erp.model.Vendor;
import org.roko.erp.services.PaymentMethodService;
import org.roko.erp.services.PurchaseOrderService;
import org.roko.erp.services.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import ch.qos.logback.core.net.SyslogOutputStream;

@Controller
public class PurchaseOrderController {

    private PurchaseOrderService svc;
    private PagingService pagingSvc;
    private VendorService vendorSvc;
    private PaymentMethodService paymentMethodSvc;

    @Autowired
    public PurchaseOrderController(PurchaseOrderService svc, VendorService vendorSvc,
            PaymentMethodService paymentMethodSvc, PagingService pagingSvc) {
        this.svc = svc;
        this.vendorSvc = vendorSvc;
        this.paymentMethodSvc = paymentMethodSvc;
        this.pagingSvc = pagingSvc;
    }

    @GetMapping("/purchaseOrderList")
    public String list(@RequestParam(name = "page", required = false) Long page, Model model) {
        List<PurchaseOrder> purchaseOrders = svc.list();
        PagingData pagingData = pagingSvc.generate("purchaseOrder", page, svc.count());

        model.addAttribute("purchaseOrders", purchaseOrders);
        model.addAttribute("paging", pagingData);

        return "purchaseOrderList.html";
    }

    @GetMapping("/purchaseOrderWizard")
    public String wizard(@RequestParam(name = "code", required = false) String code, Model model) {
        PurchaseOrderModel purchaseOrderModel = new PurchaseOrderModel();
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
    public RedirectView postPurchaseOrderWizardSecondPage(@ModelAttribute PurchaseOrderModel purchaseOrderModel) {
        PurchaseOrder purchaseOrder = fromModel(purchaseOrderModel);

        svc.create(purchaseOrder);

        return new RedirectView("/purchaseOrderList");
    }

    private PurchaseOrder fromModel(PurchaseOrderModel purchaseOrderModel) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setCode("PO" + System.currentTimeMillis());
        purchaseOrder.setVendor(vendorSvc.get(purchaseOrderModel.getVendorCode()));
        purchaseOrder.setDate(purchaseOrderModel.getDate());
        purchaseOrder.setPaymentMethod(paymentMethodSvc.get(purchaseOrderModel.getPaymentMethodCode()));
        return purchaseOrder;
    }
}
