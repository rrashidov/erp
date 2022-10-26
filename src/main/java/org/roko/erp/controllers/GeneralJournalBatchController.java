package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.GeneralJournalBatch;
import org.roko.erp.services.GeneralJournalBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class GeneralJournalBatchController {

    private GeneralJournalBatchService svc;
    private PagingService pagingSvc;

    @Autowired
    public GeneralJournalBatchController(GeneralJournalBatchService svc, PagingService pagingSvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
    }

    @GetMapping("/generalJournalBatchList")
    public String list(@RequestParam(name = "page", required = false) Long page, Model model) {
        List<GeneralJournalBatch> generalJournalBatches = svc.list();
        PagingData pagingData = pagingSvc.generate("generalJournalBatch", page, svc.count());

        model.addAttribute("generalJournalBatches", generalJournalBatches);
        model.addAttribute("paging", pagingData);

        return "generalJournalBatchList.html";
    }

    @GetMapping("/generalJournalBatchCard")
    public String card(@RequestParam(name = "code", required = false) String code, Model model) {
        GeneralJournalBatch generalJournalBatch = new GeneralJournalBatch();

        if (code != null){
            generalJournalBatch = svc.get(code);
        }

        model.addAttribute("generalJournalBatch", generalJournalBatch);

        return "generalJournalBatchCard.html";
    }

    @PostMapping("/generalJournalBatchCard")
    public RedirectView post(@ModelAttribute GeneralJournalBatch generalJournalBatch){
        svc.create(generalJournalBatch);
        
        return new RedirectView("/generalJournalBatchList");
    }
}
