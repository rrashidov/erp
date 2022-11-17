package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.controllers.model.VendorModel;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.Vendor;
import org.roko.erp.services.PaymentMethodService;
import org.roko.erp.services.VendorLedgerEntryService;
import org.roko.erp.services.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private VendorLedgerEntryService vendorLedgerEntrySvc;

    @Autowired
    public VendorController(VendorService vendorSvc, PagingService pagingSvc, PaymentMethodService paymentMethodSvc,
            VendorLedgerEntryService vendorLedgerEntrySvc) {
        this.vendorSvc = vendorSvc;
        this.pagingSvc = pagingSvc;
        this.paymentMethodSvc = paymentMethodSvc;
        this.vendorLedgerEntrySvc = vendorLedgerEntrySvc;
    }

    @GetMapping("/vendorList")
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        List<Vendor> vendors = vendorSvc.list(page);
        PagingData pagingData = pagingSvc.generate("vendor", page, vendorSvc.count());

        model.addAttribute("vendors", vendors);
        model.addAttribute("paging", pagingData);

        return "vendorList.html";
    }

    @GetMapping("/vendorCard")
    public String card(@RequestParam(name = "code", required = false) String code, Model model) {
        VendorModel vendorModel = new VendorModel();

        if (code != null){
            Vendor vendor = vendorSvc.get(code);

            vendorModel.setCode(vendor.getCode());
            vendorModel.setName(vendor.getName());
            vendorModel.setAddress(vendor.getAddress());
            vendorModel.setPaymentMethodCode(vendor.getPaymentMethod().getCode());

            model.addAttribute("vendorLedgerEntries", vendorLedgerEntrySvc.findFor(vendor));
            model.addAttribute("paging", pagingSvc.generate("vendorLedgerEntry", 1, vendorLedgerEntrySvc.count(vendor)));
        }

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

    @GetMapping("/deleteVendor")
    public RedirectView delete(@RequestParam(name="code", required = true) String code){
        vendorSvc.delete(code);
        
        return new RedirectView("/vendorList");
    }
}
