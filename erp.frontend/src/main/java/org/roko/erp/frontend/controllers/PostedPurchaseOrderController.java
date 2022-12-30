package org.roko.erp.frontend.controllers;

import org.roko.erp.dto.PostedPurchaseDocumentDTO;
import org.roko.erp.dto.list.PostedPurchaseDocumentLineList;
import org.roko.erp.dto.list.PostedPurchaseDocumentList;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.services.PostedPurchaseOrderLineService;
import org.roko.erp.frontend.services.PostedPurchaseOrderService;
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
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        PostedPurchaseDocumentList postedPurchaseOrders = svc.list(page);
        PagingData pagingData = pagingSvc.generate("postedPurchaseOrder", page, (int) postedPurchaseOrders.getCount());

        model.addAttribute("postedPurchaseOrders", postedPurchaseOrders.getData());
        model.addAttribute("paging", pagingData);

        return "postedPurchaseOrderList.html";
    }

    @GetMapping("/postedPurchaseOrderCard")
    public String card(@RequestParam(name = "code") String code,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        PostedPurchaseDocumentDTO postedPurchaseOrder = svc.get(code);
        PostedPurchaseDocumentLineList postedPurchaseOrderLines = postedPurchaseOrderLineSvc.list(code,
                page);
        PagingData pagingData = pagingSvc.generate("postedPurchaseOrderCard", code, page,
                (int) postedPurchaseOrderLines.getCount());

        model.addAttribute("postedPurchaseOrder", postedPurchaseOrder);
        model.addAttribute("postedPurchaseOrderLines", postedPurchaseOrderLines.getData());
        model.addAttribute("paging", pagingData);

        return "postedPurchaseOrderCard.html";
    }
}
