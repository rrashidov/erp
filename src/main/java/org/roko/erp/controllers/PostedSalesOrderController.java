package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.PostedSalesOrder;
import org.roko.erp.services.PostedSalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostedSalesOrderController {

    private PostedSalesOrderService svc;
    private PagingService pagingSvc;

    @Autowired
    public PostedSalesOrderController(PostedSalesOrderService svc, PagingService pagingSvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
    }

    @GetMapping("/postedSalesOrderList")
    public String list(@RequestParam(name = "page", required = false) Long page, Model model) {
        List<PostedSalesOrder> postedSalesOrders = svc.list();
        PagingData pagingData = pagingSvc.generate("postedSalesOrder", page, svc.count());

        model.addAttribute("postedSalesOrders", postedSalesOrders);
        model.addAttribute("paging", pagingData);

        return "postedSalesOrderList.html";
    }
}
