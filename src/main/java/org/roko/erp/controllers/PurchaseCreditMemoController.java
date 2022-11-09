package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.PurchaseCreditMemo;
import org.roko.erp.services.PurchaseCreditMemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PurchaseCreditMemoController {

    private PurchaseCreditMemoService svc;
    private PagingService pagingSvc;

    @Autowired
    public PurchaseCreditMemoController(PurchaseCreditMemoService svc, PagingService pagingSvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
    }

    @GetMapping("/purchaseCreditMemoList")
    public String list(@RequestParam(name = "page", required = false) Long page, Model model) {
        List<PurchaseCreditMemo> purchaseCreditMemos = svc.list();
        PagingData pagingData = pagingSvc.generate("purchaseCreditMemo", page, svc.count());

        model.addAttribute("purchaseCreditMemos", purchaseCreditMemos);
        model.addAttribute("paging", pagingData);

        return "purchaseCreditMemoList.html";
    }
}
