package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.controllers.model.VendorModel;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.Vendor;
import org.roko.erp.services.PaymentMethodService;
import org.roko.erp.services.VendorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class VendorController {

    private VendorService vendorSvc;
    private PagingService pagingSvc;
    private PaymentMethodService paymentMethodSvc;

    public VendorController(VendorService vendorSvc, PagingService pagingSvc, PaymentMethodService paymentMethodSvc) {
        this.vendorSvc = vendorSvc;
        this.pagingSvc = pagingSvc;
        this.paymentMethodSvc = paymentMethodSvc;
    }

    @GetMapping("/vendorList")
    public String list(@RequestParam(name = "page", required = false) Long page, Model model) {
        PagingData pagingData = pagingSvc.generate("vendor", page, vendorSvc.count());
        List<Vendor> vendors = vendorSvc.list();

        model.addAttribute("vendors", vendors);
        model.addAttribute("paging", pagingData);

        return "vendorList.html";
    }

    @GetMapping("/vendorCard")
    public String card(@RequestParam(name = "code", required = false) String code, Model model) {
        VendorModel vendorModel = new VendorModel();

        model.addAttribute("vendor", vendorModel);
        model.addAttribute("paymentMethods", paymentMethodSvc.list());

        return "vendorCard.html";
    }

    @PostMapping("/vendorCard")
    public RedirectView post(@ModelAttribute VendorModel vendorModel){
        Vendor vendor = new Vendor();

        vendor.setCode(vendorModel.getCode());
        vendor.setName(vendorModel.getName());
        vendor.setAddress(vendorModel.getAddress());
        vendor.setPaymentMethod(paymentMethodSvc.get(vendorModel.getPaymentMethodCode()));

        vendorSvc.create(vendor);

        return new RedirectView("/vendorList");
    }
}
