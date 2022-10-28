package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.SalesOrder;
import org.roko.erp.services.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SalesOrderController {
    
    private SalesOrderService svc;
    private PagingService pagingSvc;

    @Autowired
    public SalesOrderController(SalesOrderService svc, PagingService pagingSvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
    }

    @GetMapping("/salesOrderList")
    public String list(@RequestParam(name="page", required=false) Long page, Model model){
        List<SalesOrder> salesOrders = svc.list();
        PagingData pagingData = pagingSvc.generate("salesOrder", page, svc.count());

        model.addAttribute("salesOrders", salesOrders);
        model.addAttribute("paging", pagingData);

        return "salesOrderList.html";
    }
}
