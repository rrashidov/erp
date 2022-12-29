package org.roko.erp.frontend.controllers;

import org.roko.erp.dto.PostedSalesDocumentDTO;
import org.roko.erp.dto.list.PostedSalesDocumentLineList;
import org.roko.erp.dto.list.PostedSalesDocumentList;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.services.PostedSalesOrderLineService;
import org.roko.erp.frontend.services.PostedSalesOrderService;
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
    public PostedSalesOrderController(PostedSalesOrderService svc, PostedSalesOrderLineService lineSvc,
            PagingService pagingSvc) {
        this.svc = svc;
        this.lineSvc = lineSvc;
        this.pagingSvc = pagingSvc;
    }

    @GetMapping("/postedSalesOrderList")
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        PostedSalesDocumentList postedSalesOrders = svc.list(page);
        PagingData pagingData = pagingSvc.generate("postedSalesOrder", page, (int) postedSalesOrders.getCount());

        model.addAttribute("postedSalesOrders", postedSalesOrders.getData());
        model.addAttribute("paging", pagingData);

        return "postedSalesOrderList.html";
    }

    @GetMapping("/postedSalesOrderCard")
    public String card(@RequestParam(name = "code") String code,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        PostedSalesDocumentDTO postedSalesOrder = svc.get(code);

        PostedSalesDocumentLineList postedSalesOrderLines = lineSvc.list(code, page);

        PagingData pagingData = pagingSvc.generate("postedSalesOrderCard", code, page,
                (int) postedSalesOrderLines.getCount());

        model.addAttribute("postedSalesOrder", postedSalesOrder);
        model.addAttribute("postedSalesOrderLines", postedSalesOrderLines.getData());
        model.addAttribute("paging", pagingData);

        return "postedSalesOrderCard.html";
    }
}
