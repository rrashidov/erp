package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.PurchaseOrder;
import org.roko.erp.services.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PurchaseOrderController {
    
    private PurchaseOrderService svc;
    private PagingService pagingSvc;

    @Autowired
    public PurchaseOrderController(PurchaseOrderService svc, PagingService pagingSvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
    }

    @GetMapping("/purchaseOrderList")
    public String list(@RequestParam(name="page", required = false) Long page, Model model){
        List<PurchaseOrder> purchaseOrders = svc.list();
        PagingData pagingData = pagingSvc.generate("purchaseOrder", page, svc.count());

        model.addAttribute("purchaseOrders", purchaseOrders);
        model.addAttribute("paging", pagingData);

        return "purchaseOrderList.html";
    }
}
