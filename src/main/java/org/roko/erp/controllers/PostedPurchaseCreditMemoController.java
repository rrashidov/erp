package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.PostedPurchaseCreditMemo;
import org.roko.erp.model.PostedPurchaseCreditMemoLine;
import org.roko.erp.services.PostedPurchaseCreditMemoLineService;
import org.roko.erp.services.PostedPurchaseCreditMemoService;
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
    public String list(Model model) {
        List<PostedPurchaseCreditMemo> postedPurchaseCreditMemos = svc.list();
        PagingData pagingData = pagingSvc.generate("postedPurchaseCreditMemo", 1, svc.count());

        model.addAttribute("postedPurchaseCreditMemos", postedPurchaseCreditMemos);
        model.addAttribute("paging", pagingData);

        return "postedPurchaseCreditMemoList.html";
    }

    @GetMapping("/postedPurchaseCreditMemoCard")
    public String card(@RequestParam(name = "code") String code, Model model) {
        PostedPurchaseCreditMemo postedPurchaseCreditMemo = svc.get(code);
        List<PostedPurchaseCreditMemoLine> postedPurchaseCreditMemoLines = postedPurchaseCreditMemoLineSvc.list(postedPurchaseCreditMemo);
        PagingData pagingData = pagingSvc.generate("postedPurchaseCreditMemoLine", 1, postedPurchaseCreditMemoLineSvc.count(postedPurchaseCreditMemo));

        model.addAttribute("postedPurchaseCreditMemo", postedPurchaseCreditMemo);
        model.addAttribute("postedPurchaseCreditMemoLines", postedPurchaseCreditMemoLines);
        model.addAttribute("paging", pagingData);

        return "postedPurchaseCreditMemoCard.html";
    }
}
