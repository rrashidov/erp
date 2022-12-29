package org.roko.erp.frontend.controllers;

import org.roko.erp.dto.PostedSalesDocumentDTO;
import org.roko.erp.dto.list.PostedSalesDocumentLineList;
import org.roko.erp.dto.list.PostedSalesDocumentList;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.services.PostedSalesCreditMemoLineService;
import org.roko.erp.frontend.services.PostedSalesCreditMemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostedSalesCreditMemoController {

    private PostedSalesCreditMemoService svc;
    private PagingService pagingSvc;
    private PostedSalesCreditMemoLineService postedSalesCreditMemoLineSvc;

    @Autowired
    public PostedSalesCreditMemoController(PostedSalesCreditMemoService svc,
            PostedSalesCreditMemoLineService postedSalesCreditMemoLineSvc, PagingService pagingSvc) {
        this.svc = svc;
        this.postedSalesCreditMemoLineSvc = postedSalesCreditMemoLineSvc;
        this.pagingSvc = pagingSvc;
    }

    @GetMapping("/postedSalesCreditMemoList")
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        PostedSalesDocumentList postedSalesCreditMemos = svc.list(page);
        PagingData pagingData = pagingSvc.generate("postedSalesCreditMemo", page,
                (int) postedSalesCreditMemos.getCount());

        model.addAttribute("postedSalesCreditMemos", postedSalesCreditMemos.getData());
        model.addAttribute("paging", pagingData);

        return "postedSalesCreditMemoList.html";
    }

    @GetMapping("/postedSalesCreditMemoCard")
    public String card(@RequestParam(name = "code") String code,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        PostedSalesDocumentDTO postedSalesCreditMemo = svc.get(code);
        PostedSalesDocumentLineList postedSalesCreditMemoLines = postedSalesCreditMemoLineSvc
                .list(code, page);
        PagingData pagingData = pagingSvc.generate("postedSalesCreditMemoCard", code, page,
                (int) postedSalesCreditMemoLines.getCount());

        model.addAttribute("postedSalesCreditMemo", postedSalesCreditMemo);
        model.addAttribute("postedSalesCreditMemoLines", postedSalesCreditMemoLines.getData());
        model.addAttribute("paging", pagingData);

        return "postedSalesCreditMemoCard.html";
    }
}
