package org.roko.erp.frontend.controllers;

import jakarta.servlet.http.HttpSession;

import org.roko.erp.dto.GeneralJournalBatchDTO;
import org.roko.erp.dto.list.GeneralJournalBatchLineList;
import org.roko.erp.dto.list.GeneralJournalBatchList;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.services.DeleteFailedException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class GeneralJournalBatchController {

    private static final String DELETED_MSG_TMPL = "General Journal Batch %s deleted";

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
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model,
            HttpSession httpSession) {
        GeneralJournalBatchList generalJournalBatches = svc.list(page);
        PagingData pagingData = pagingSvc.generate("generalJournalBatch", page, (int) generalJournalBatches.getCount());

        model.addAttribute("generalJournalBatches", generalJournalBatches.getData());
        model.addAttribute("paging", pagingData);
        model.addAttribute("feedback", feedbackSvc.get(httpSession));

        return "generalJournalBatchList.html";
    }

    @GetMapping("/generalJournalBatchCard")
    public String card(@RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        GeneralJournalBatchDTO generalJournalBatch = new GeneralJournalBatchDTO();

        if (code != null) {
            generalJournalBatch = svc.get(code);

            GeneralJournalBatchLineList generalJournalBatchLines = generalJournalBatchLineSvc
                    .list(code, page);
            PagingData pagingData = pagingSvc.generate("generalJournalBatchCard", code, page,
                    (int) generalJournalBatchLines.getCount());

            model.addAttribute("generalJournalBatchLines", generalJournalBatchLines.getData());
            model.addAttribute("paging", pagingData);
        }

        model.addAttribute("generalJournalBatch", generalJournalBatch);

        return "generalJournalBatchCard.html";
    }

    @PostMapping("/generalJournalBatchCard")
    public RedirectView post(@ModelAttribute GeneralJournalBatchDTO generalJournalBatch) {
        svc.create(generalJournalBatch);

        return new RedirectView("/generalJournalBatchList");
    }

    @GetMapping("/deleteGeneralJournalBatch")
    public RedirectView delete(@RequestParam(name = "code") String code,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            RedirectAttributes redirectAttributes, HttpSession httpSession)
            throws DeleteFailedException {
        try {
            svc.delete(code);
            feedbackSvc.give(FeedbackType.INFO, String.format(DELETED_MSG_TMPL, code), httpSession);
        } catch (DeleteFailedException e) {
            feedbackSvc.give(FeedbackType.ERROR, e.getMessage(), httpSession);
        }

        redirectAttributes.addAttribute("page", page);

        return new RedirectView("/generalJournalBatchList");
    }

    @GetMapping("/postGeneralJournalBatch")
    public RedirectView post(@RequestParam(name = "code") String code, HttpSession httpSession) {
        try {
            generalJournalBatchPostSvc.post(code);

            feedbackSvc.give(FeedbackType.INFO, "General journal batch " + code + " post scheduled.", httpSession);
        } catch (PostFailedException e) {
            feedbackSvc.give(FeedbackType.ERROR,
                    "General journal batch " + code + " post scheduling failed: " + e.getMessage(), httpSession);
        }

        return new RedirectView("/generalJournalBatchList");
    }
}
