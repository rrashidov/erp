package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.PostedPurchaseOrder;
import org.roko.erp.services.PostedPurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostedPurchaseOrderController {

    private PostedPurchaseOrderService svc;
    private PagingService pagingSvc;

    @Autowired
    public PostedPurchaseOrderController(PostedPurchaseOrderService svc, PagingService pagingSvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
    }

    @GetMapping("/postedPurchaseOrderList")
    public String list(Model model) {
        List<PostedPurchaseOrder> postedPurchaseOrders = svc.list();
        PagingData pagingData = pagingSvc.generate("postedPurchaseOrder", null, svc.count());

        model.addAttribute("postedPurchaseOrders", postedPurchaseOrders);
        model.addAttribute("paging", pagingData);

        return "postedPurchaseOrderList.html";
    }
}
