package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.PostedPurchaseOrder;
import org.roko.erp.model.PostedPurchaseOrderLine;
import org.roko.erp.services.PostedPurchaseOrderLineService;
import org.roko.erp.services.PostedPurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostedPurchaseOrderController {

    private PostedPurchaseOrderService svc;
    private PagingService pagingSvc;
    private PostedPurchaseOrderLineService postedPurchaseOrderLineSvc;

    @Autowired
    public PostedPurchaseOrderController(PostedPurchaseOrderService svc,
            PostedPurchaseOrderLineService postedPurchaseOrderLineSvc, PagingService pagingSvc) {
        this.svc = svc;
        this.postedPurchaseOrderLineSvc = postedPurchaseOrderLineSvc;
        this.pagingSvc = pagingSvc;
    }

    @GetMapping("/postedPurchaseOrderList")
    public String list(Model model) {
        List<PostedPurchaseOrder> postedPurchaseOrders = svc.list();
        PagingData pagingData = pagingSvc.generate("postedPurchaseOrder", 1, svc.count());

        model.addAttribute("postedPurchaseOrders", postedPurchaseOrders);
        model.addAttribute("paging", pagingData);

        return "postedPurchaseOrderList.html";
    }

    @GetMapping("/postedPurchaseOrderCard")
    public String card(@RequestParam(name = "code") String code, Model model) {
        PostedPurchaseOrder postedPurchaseOrder = svc.get(code);
        List<PostedPurchaseOrderLine> postedPurchaseOrderLines = postedPurchaseOrderLineSvc.list(postedPurchaseOrder);
        PagingData pagingData = pagingSvc.generate("postedPurchaseOrderLine", 1, postedPurchaseOrderLineSvc.count(postedPurchaseOrder));

        model.addAttribute("postedPurchaseOrder", postedPurchaseOrder);
        model.addAttribute("postedPurchaseOrderLines", postedPurchaseOrderLines);
        model.addAttribute("paging", pagingData);
        
        return "postedPurchaseOrderCard.html";
    }
}
