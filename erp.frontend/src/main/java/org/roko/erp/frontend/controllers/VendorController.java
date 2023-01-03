package org.roko.erp.frontend.controllers;

import org.roko.erp.dto.VendorDTO;
import org.roko.erp.dto.list.PaymentMethodList;
import org.roko.erp.dto.list.VendorLedgerEntryList;
import org.roko.erp.dto.list.VendorList;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.services.PaymentMethodService;
import org.roko.erp.frontend.services.VendorLedgerEntryService;
import org.roko.erp.frontend.services.VendorService;
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
        VendorList vendorList = vendorSvc.list(page);
        PagingData pagingData = pagingSvc.generate("vendor", page, (int) vendorList.getCount());

        model.addAttribute("vendors", vendorList.getData());
        model.addAttribute("paging", pagingData);

        return "vendorList.html";
    }

    @GetMapping("/vendorCard")
    public String card(@RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        VendorDTO vendorModel = new VendorDTO();

        if (code != null) {
            VendorDTO vendor = vendorSvc.get(code);

            vendorModel.setCode(vendor.getCode());
            vendorModel.setName(vendor.getName());
            vendorModel.setAddress(vendor.getAddress());
            vendorModel.setPaymentMethodCode(vendor.getPaymentMethodCode());

            VendorLedgerEntryList vendorLedgerEntryList = vendorLedgerEntrySvc.list(code, page);

            model.addAttribute("vendorLedgerEntries", vendorLedgerEntryList.getData());
            model.addAttribute("paging",
                    pagingSvc.generate("vendorCard", code, page, (int) vendorLedgerEntryList.getCount()));
        }

        PaymentMethodList paymentMethodList = paymentMethodSvc.list();

        model.addAttribute("vendor", vendorModel);
        model.addAttribute("paymentMethods", paymentMethodList.getData());

        return "vendorCard.html";
    }

    @PostMapping("/vendorCard")
    public RedirectView post(@ModelAttribute VendorDTO vendorModel) {
        VendorDTO vendor = vendorSvc.get(vendorModel.getCode());

        if (vendor == null) {
            vendor = new VendorDTO();
            vendor.setCode(vendorModel.getCode());
            vendor.setName(vendorModel.getName());
            vendor.setAddress(vendorModel.getAddress());
            vendor.setPaymentMethodCode(vendorModel.getPaymentMethodCode());

            vendorSvc.create(vendor);
        } else {
            vendor.setName(vendorModel.getName());
            vendor.setAddress(vendorModel.getAddress());
            vendor.setPaymentMethodCode(vendorModel.getPaymentMethodCode());

            vendorSvc.update(vendorModel.getCode(), vendor);
        }

        return new RedirectView("/vendorList");
    }

    @GetMapping("/deleteVendor")
    public RedirectView delete(@RequestParam(name = "code", required = true) String code) {
        vendorSvc.delete(code);

        return new RedirectView("/vendorList");
    }
}
