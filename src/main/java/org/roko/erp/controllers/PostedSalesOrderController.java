package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.PostedSalesOrder;
import org.roko.erp.model.PostedSalesOrderLine;
import org.roko.erp.services.PostedSalesOrderLineService;
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
    private PostedSalesOrderLineService lineSvc;

    @Autowired
    public PostedSalesOrderController(PostedSalesOrderService svc, PostedSalesOrderLineService lineSvc, PagingService pagingSvc) {
        this.svc = svc;
        this.lineSvc = lineSvc;
        this.pagingSvc = pagingSvc;
    }

    @GetMapping("/postedSalesOrderList")
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        List<PostedSalesOrder> postedSalesOrders = svc.list(page);
        PagingData pagingData = pagingSvc.generate("postedSalesOrder", page, svc.count());

        model.addAttribute("postedSalesOrders", postedSalesOrders);
        model.addAttribute("paging", pagingData);

        return "postedSalesOrderList.html";
    }

    @GetMapping("/postedSalesOrderCard")
    public String card(@RequestParam(name="code") String code, Model model) {
        PostedSalesOrder postedSalesOrder = svc.get(code);
        List<PostedSalesOrderLine> postedSalesOrderLines = lineSvc.list(postedSalesOrder);
        PagingData pagingData = pagingSvc.generate("postedSalesOrderLine", 1, lineSvc.count(postedSalesOrder));

        model.addAttribute("postedSalesOrder", postedSalesOrder);
        model.addAttribute("postedSalesOrderLines", postedSalesOrderLines);
        model.addAttribute("paging", pagingData);
        
        return "postedSalesOrderCard.html";
    }
}
