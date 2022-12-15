package org.roko.erp.frontend.controllers;

import java.util.List;

import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.model.PostedSalesCreditMemo;
import org.roko.erp.frontend.model.PostedSalesCreditMemoLine;
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
        List<PostedSalesCreditMemo> postedSalesCreditMemos = svc.list(page);
        PagingData pagingData = pagingSvc.generate("postedSalesCreditMemo", page, svc.count());

        model.addAttribute("postedSalesCreditMemos", postedSalesCreditMemos);
        model.addAttribute("paging", pagingData);

        return "postedSalesCreditMemoList.html";
    }

    @GetMapping("/postedSalesCreditMemoCard")
    public String card(@RequestParam(name = "code") String code,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        PostedSalesCreditMemo postedSalesCreditMemo = svc.get(code);
        List<PostedSalesCreditMemoLine> postedSalesCreditMemoLines = postedSalesCreditMemoLineSvc
                .list(postedSalesCreditMemo, page);
        PagingData pagingData = pagingSvc.generate("postedSalesCreditMemoCard", code, page,
                postedSalesCreditMemoLineSvc.count(postedSalesCreditMemo));

        model.addAttribute("postedSalesCreditMemo", postedSalesCreditMemo);
        model.addAttribute("postedSalesCreditMemoLines", postedSalesCreditMemoLines);
        model.addAttribute("paging", pagingData);

        return "postedSalesCreditMemoCard.html";
    }
}
