package org.roko.erp.frontend.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.model.GeneralJournalBatch;
import org.roko.erp.frontend.model.GeneralJournalBatchLine;
import org.roko.erp.frontend.services.FeedbackService;
import org.roko.erp.frontend.services.GeneralJournalBatchLineService;
import org.roko.erp.frontend.services.GeneralJournalBatchPostService;
import org.roko.erp.frontend.services.GeneralJournalBatchService;
import org.roko.erp.frontend.services.PostFailedException;
import org.roko.erp.frontend.services.util.FeedbackType;
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
    private GeneralJournalBatchLineService generalJournalBatchLineSvc;
    private GeneralJournalBatchPostService generalJournalBatchPostSvc;
    private FeedbackService feedbackSvc;

    @Autowired
    public GeneralJournalBatchController(GeneralJournalBatchService svc,
            GeneralJournalBatchLineService generalJournalBatchLineSvc,
            GeneralJournalBatchPostService generalJournalBatchPostSvc, PagingService pagingSvc,
            FeedbackService feedbackSvc) {
        this.svc = svc;
        this.generalJournalBatchLineSvc = generalJournalBatchLineSvc;
        this.generalJournalBatchPostSvc = generalJournalBatchPostSvc;
        this.pagingSvc = pagingSvc;
        this.feedbackSvc = feedbackSvc;
    }

    @GetMapping("/generalJournalBatchList")
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model, HttpSession httpSession) {
        List<GeneralJournalBatch> generalJournalBatches = svc.list(page);
        PagingData pagingData = pagingSvc.generate("generalJournalBatch", page, svc.count());

        model.addAttribute("generalJournalBatches", generalJournalBatches);
        model.addAttribute("paging", pagingData);
        model.addAttribute("feedback", feedbackSvc.get(httpSession));

        return "generalJournalBatchList.html";
    }

    @GetMapping("/generalJournalBatchCard")
    public String card(@RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        GeneralJournalBatch generalJournalBatch = new GeneralJournalBatch();

        if (code != null) {
            generalJournalBatch = svc.get(code);

            List<GeneralJournalBatchLine> generalJournalBatchLines = generalJournalBatchLineSvc
                    .list(generalJournalBatch, page);
            PagingData pagingData = pagingSvc.generate("generalJournalBatchCard", code, page,
                    generalJournalBatchLineSvc.count(generalJournalBatch));

            model.addAttribute("generalJournalBatchLines", generalJournalBatchLines);
            model.addAttribute("paging", pagingData);
        }

        model.addAttribute("generalJournalBatch", generalJournalBatch);

        return "generalJournalBatchCard.html";
    }

    @PostMapping("/generalJournalBatchCard")
    public RedirectView post(@ModelAttribute GeneralJournalBatch generalJournalBatch) {
        svc.create(generalJournalBatch);

        return new RedirectView("/generalJournalBatchList");
    }

    @GetMapping("/deleteGeneralJournalBatch")
    public RedirectView delete(@RequestParam(name = "code") String code) {
        svc.delete(code);

        return new RedirectView("/generalJournalBatchList");
    }

    @GetMapping("/postGeneralJournalBatch")
    public RedirectView post(@RequestParam(name = "code") String code, HttpSession httpSession) {
        try {
            generalJournalBatchPostSvc.post(code);

            feedbackSvc.give(FeedbackType.INFO, "General journal batch " + code + " posted.", httpSession);
        } catch (PostFailedException e) {
            feedbackSvc.give(FeedbackType.ERROR, "General journal batch " + code + " post failed: " + e.getMessage(), httpSession);
        }

        return new RedirectView("/generalJournalBatchList");
    }
}
