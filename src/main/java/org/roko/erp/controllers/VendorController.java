package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.Vendor;
import org.roko.erp.services.VendorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VendorController {
    
    private VendorService vendorSvc;
    private PagingService pagingSvc;

    public VendorController(VendorService vendorSvc, PagingService pagingSvc) {
        this.vendorSvc = vendorSvc;
        this.pagingSvc = pagingSvc;
	}

    @GetMapping("/vendorList")
	public String list(@RequestParam(name="page", required = false) Long page, Model model){
        PagingData pagingData = pagingSvc.generate("vendor", page, vendorSvc.count());
        List<Vendor> vendors = vendorSvc.list();

        model.addAttribute("vendors", vendors);
        model.addAttribute("paging", pagingData);
        
        return "vendorList.html";
    }
}
