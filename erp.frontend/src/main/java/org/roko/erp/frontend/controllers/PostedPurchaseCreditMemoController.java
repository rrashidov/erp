package org.roko.erp.frontend.controllers;

import org.roko.erp.dto.PostedPurchaseDocumentDTO;
import org.roko.erp.dto.list.PostedPurchaseDocumentLineList;
import org.roko.erp.dto.list.PostedPurchaseDocumentList;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
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
        PostedPurchaseDocumentList postedPurchaseCreditMemos = svc.list(page);
        PagingData pagingData = pagingSvc.generate("postedPurchaseCreditMemo", page,
                (int) postedPurchaseCreditMemos.getCount());

        model.addAttribute("postedPurchaseCreditMemos", postedPurchaseCreditMemos.getData());
        model.addAttribute("paging", pagingData);

        return "postedPurchaseCreditMemoList.html";
    }

    @GetMapping("/postedPurchaseCreditMemoCard")
    public String card(@RequestParam(name = "code") String code,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        PostedPurchaseDocumentDTO postedPurchaseCreditMemo = svc.get(code);
        PostedPurchaseDocumentLineList postedPurchaseCreditMemoLines = postedPurchaseCreditMemoLineSvc
                .list(code, page);
        PagingData pagingData = pagingSvc.generate("postedPurchaseCreditMemoCard", code, page,
                (int) postedPurchaseCreditMemoLines.getCount());

        model.addAttribute("postedPurchaseCreditMemo", postedPurchaseCreditMemo);
        model.addAttribute("postedPurchaseCreditMemoLines", postedPurchaseCreditMemoLines.getData());
        model.addAttribute("paging", pagingData);

        return "postedPurchaseCreditMemoCard.html";
    }
}
