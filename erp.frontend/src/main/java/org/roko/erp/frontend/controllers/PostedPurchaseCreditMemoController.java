package org.roko.erp.frontend.controllers;

import java.util.List;

import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.model.PostedPurchaseCreditMemo;
import org.roko.erp.frontend.model.PostedPurchaseCreditMemoLine;
import org.roko.erp.frontend.services.PostedPurchaseCreditMemoLineService;
import org.roko.erp.frontend.services.PostedPurchaseCreditMemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostedPurchaseCreditMemoController {

    private PostedPurchaseCreditMemoService svc;
    private PagingService pagingSvc;
    private PostedPurchaseCreditMemoLineService postedPurchaseCreditMemoLineSvc;

    @Autowired
    public PostedPurchaseCreditMemoController(PostedPurchaseCreditMemoService svc,
            PostedPurchaseCreditMemoLineService postedPurchaseCreditMemoLineSvc, PagingService pagingSvc) {
        this.svc = svc;
        this.postedPurchaseCreditMemoLineSvc = postedPurchaseCreditMemoLineSvc;
        this.pagingSvc = pagingSvc;
    }

    @GetMapping("/postedPurchaseCreditMemoList")
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        List<PostedPurchaseCreditMemo> postedPurchaseCreditMemos = svc.list(page);
        PagingData pagingData = pagingSvc.generate("postedPurchaseCreditMemo", 1, svc.count());

        model.addAttribute("postedPurchaseCreditMemos", postedPurchaseCreditMemos);
        model.addAttribute("paging", pagingData);

        return "postedPurchaseCreditMemoList.html";
    }

    @GetMapping("/postedPurchaseCreditMemoCard")
    public String card(@RequestParam(name = "code") String code,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        PostedPurchaseCreditMemo postedPurchaseCreditMemo = svc.get(code);
        List<PostedPurchaseCreditMemoLine> postedPurchaseCreditMemoLines = postedPurchaseCreditMemoLineSvc
                .list(postedPurchaseCreditMemo, page);
        PagingData pagingData = pagingSvc.generate("postedPurchaseCreditMemoCard", code, page,
                postedPurchaseCreditMemoLineSvc.count(postedPurchaseCreditMemo));

        model.addAttribute("postedPurchaseCreditMemo", postedPurchaseCreditMemo);
        model.addAttribute("postedPurchaseCreditMemoLines", postedPurchaseCreditMemoLines);
        model.addAttribute("paging", pagingData);

        return "postedPurchaseCreditMemoCard.html";
    }
}
