package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.SalesCreditMemo;
import org.roko.erp.services.SalesCreditMemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SalesCreditMemoController {

    private SalesCreditMemoService svc;
    private PagingService pagingSvc;

    @Autowired
    public SalesCreditMemoController(SalesCreditMemoService svc, PagingService pagingSvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
    }

    @GetMapping("/salesCreditMemoList")
    public String list(@RequestParam(name = "page", required = false) Long page, Model model) {
        List<SalesCreditMemo> salesCreditMemos = svc.list();
        PagingData pagingData = pagingSvc.generate("salesCreditMemo", page, svc.count());

        model.addAttribute("salesCreditMemos", salesCreditMemos);
        model.addAttribute("paging", pagingData);

        return "salesCreditMemoList.html";
    }
}
