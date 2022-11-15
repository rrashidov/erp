package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.PostedSalesCreditMemo;
import org.roko.erp.services.PostedSalesCreditMemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostedSalesCreditMemoController {

    private PostedSalesCreditMemoService svc;
    private PagingService pagingSvc;

    @Autowired
    public PostedSalesCreditMemoController(PostedSalesCreditMemoService svc, PagingService pagingSvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
    }

    @GetMapping("/postedSalesCreditMemoList")
    public String list(@RequestParam(name = "page", required = false) Long page, Model model) {
        List<PostedSalesCreditMemo> postedSalesCreditMemos = svc.list();
        PagingData pagingData = pagingSvc.generate("postedSalesCreditMemo", null, svc.count());

        model.addAttribute("postedSalesCreditMemos", postedSalesCreditMemos);
        model.addAttribute("paging", pagingData);

        return "postedSalesCreditMemoList.html";
    }
}
