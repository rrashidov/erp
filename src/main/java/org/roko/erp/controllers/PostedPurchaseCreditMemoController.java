package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.PostedPurchaseCreditMemo;
import org.roko.erp.services.PostedPurchaseCreditMemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostedPurchaseCreditMemoController {

    private PostedPurchaseCreditMemoService svc;
    private PagingService pagingSvc;

    @Autowired
    public PostedPurchaseCreditMemoController(PostedPurchaseCreditMemoService svc, PagingService pagingSvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
    }

    @GetMapping("/postedPurchaseCreditMemoList")
    public String list(Model model) {
        List<PostedPurchaseCreditMemo> postedPurchaseCreditMemos = svc.list();
        PagingData pagingData = pagingSvc.generate("postedPurchaseCreditMemo", null, svc.count());

        model.addAttribute("postedPurchaseCreditMemos", postedPurchaseCreditMemos);
        model.addAttribute("paging", pagingData);

        return "postedPurchaseCreditMemoList.html";
    }
}
