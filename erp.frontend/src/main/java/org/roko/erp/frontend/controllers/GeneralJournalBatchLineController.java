package org.roko.erp.frontend.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.frontend.controllers.model.GeneralJournalBatchLineModel;
import org.roko.erp.frontend.controllers.model.GeneralJournalBatchLineSource;
import org.roko.erp.frontend.model.GeneralJournalBatch;
import org.roko.erp.frontend.model.GeneralJournalBatchLine;
import org.roko.erp.frontend.model.GeneralJournalBatchLineType;
import org.roko.erp.frontend.model.jpa.GeneralJournalBatchLineId;
import org.roko.erp.frontend.services.BankAccountService;
import org.roko.erp.frontend.services.CustomerService;
import org.roko.erp.frontend.services.GeneralJournalBatchLineService;
import org.roko.erp.frontend.services.GeneralJournalBatchService;
import org.roko.erp.frontend.services.VendorService;
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
public class GeneralJournalBatchLineController {

    private GeneralJournalBatchLineService svc;
    private BankAccountService bankAccountSvc;
    private CustomerService customerSvc;
    private VendorService vendorSvc;
    private GeneralJournalBatchService generalJournalBatchSvc;

    @Autowired
    public GeneralJournalBatchLineController(GeneralJournalBatchLineService svc,
            GeneralJournalBatchService generalJournalBatchSvc, BankAccountService bankAccountSvc,
            CustomerService customerSvc, VendorService vendorSvc) {
        this.svc = svc;
        this.generalJournalBatchSvc = generalJournalBatchSvc;
        this.bankAccountSvc = bankAccountSvc;
        this.customerSvc = customerSvc;
        this.vendorSvc = vendorSvc;
    }

    @GetMapping("/generalJournalBatchLineWizard")
    public String wizard(@RequestParam(name = "generalJournalBatchCode") String code,
            @RequestParam(name = "lineNo", required = false, defaultValue = "0") int lineNo, Model model) {
        GeneralJournalBatchLineModel generalJournalBatchLineModel = new GeneralJournalBatchLineModel();
        generalJournalBatchLineModel.setGeneralJournalBatchCode(code);

        if (lineNo != 0) {
            GeneralJournalBatchLineId generalJournalBatchLineId = new GeneralJournalBatchLineId();
            generalJournalBatchLineId.setGeneralJournalBatch(generalJournalBatchSvc.get(code));
            generalJournalBatchLineId.setLineNo(lineNo);

            GeneralJournalBatchLine generalJournalBatchLine = svc.get(generalJournalBatchLineId);
            toModel(generalJournalBatchLine, generalJournalBatchLineModel);
        }

        model.addAttribute("generalJournalBatchLine", generalJournalBatchLineModel);

        return "generalJournalBatchLineWizardFirstPage.html";
    }

    @PostMapping("/generalJournalBatchLineWizardFirstPage")
    public String postGeneralJournalBatchLineWizardFirstPage(
            @ModelAttribute GeneralJournalBatchLineModel generalJournalBatchLine, Model model) {

        List<GeneralJournalBatchLineSource> sources = new ArrayList<>();

        if (generalJournalBatchLine.getSourceType().equals(GeneralJournalBatchLineType.BANK_ACCOUNT)) {
            addBankAccounts(sources);
        }

        if (generalJournalBatchLine.getSourceType().equals(GeneralJournalBatchLineType.CUSTOMER)) {
            addCustomers(sources);
        }

        if (generalJournalBatchLine.getSourceType().equals(GeneralJournalBatchLineType.VENDOR)) {
            addVendors(sources);
        }

        model.addAttribute("generalJournalBatchLine", generalJournalBatchLine);
        model.addAttribute("sources", sources);

        return "generalJournalBatchLineWizardSecondPage.html";
    }

    @PostMapping("/generalJournalBatchLineWizardSecondPage")
    public String postGeneralJournalBatchLineWizardSecondPage(
            @ModelAttribute GeneralJournalBatchLineModel generalJournalBatchLine, Model model) {
        setSourceName(generalJournalBatchLine);

        model.addAttribute("generalJournalBatchLine", generalJournalBatchLine);
        model.addAttribute("bankAccounts", bankAccountSvc.list());

        return "generalJournalBatchLineWizardThirdPage.html";
    }

    @PostMapping("/generalJournalBatchLineWizardThirdPage")
    public RedirectView postGeneralJournalBatchLineWizardThirdPage(
            @ModelAttribute GeneralJournalBatchLineModel generalJournalBatchLine,
            RedirectAttributes redirectAttributes) {

        GeneralJournalBatch generalJournalBatch = generalJournalBatchSvc
                .get(generalJournalBatchLine.getGeneralJournalBatchCode());

        if (generalJournalBatchLine.getLineNo() == 0) {
            create(generalJournalBatch, generalJournalBatchLine);
        } else {
            update(generalJournalBatch, generalJournalBatchLine);
        }

        redirectAttributes.addAttribute("code", generalJournalBatchLine.getGeneralJournalBatchCode());

        return new RedirectView("/generalJournalBatchCard");
    }

    @GetMapping("/deleteGeneralJournalBatchLine")
    public RedirectView deleteGeneralJournalBatchLine(@RequestParam(name = "generalJournalBatchCode") String code,
            @RequestParam(name = "lineNo") int lineNo, RedirectAttributes redirectAttributes) {
        GeneralJournalBatchLineId generalJournalBatchLineId = new GeneralJournalBatchLineId();
        generalJournalBatchLineId.setGeneralJournalBatch(generalJournalBatchSvc.get(code));
        generalJournalBatchLineId.setLineNo(lineNo);

        svc.delete(generalJournalBatchLineId);

        redirectAttributes.addAttribute("code", code);

        return new RedirectView("/generalJournalBatchCard");
    }

    private void create(GeneralJournalBatch generalJournalBatch, GeneralJournalBatchLineModel generalJournalBatchLine) {
        GeneralJournalBatchLineId id = new GeneralJournalBatchLineId();
        id.setGeneralJournalBatch(generalJournalBatch);
        id.setLineNo(svc.count(generalJournalBatch) + 1);

        GeneralJournalBatchLine line = new GeneralJournalBatchLine();
        line.setGeneralJournalBatchLineId(id);

        fromModel(line, generalJournalBatchLine);

        svc.create(line);
    }

    private void update(GeneralJournalBatch generalJournalBatch, GeneralJournalBatchLineModel generalJournalBatchLine) {
        GeneralJournalBatchLineId id = new GeneralJournalBatchLineId();
        id.setGeneralJournalBatch(generalJournalBatch);
        id.setLineNo(generalJournalBatchLine.getLineNo());

        GeneralJournalBatchLine line = svc.get(id);

        fromModel(line, generalJournalBatchLine);

        svc.update(id, line);
    }

    public void fromModel(GeneralJournalBatchLine line, GeneralJournalBatchLineModel model) {
        line.setSourceType(model.getSourceType());
        line.setSourceCode(model.getSourceCode());
        line.setSourceName(model.getSourceName());
        line.setOperationType(model.getOperationType());
        line.setDocumentCode(model.getDocumentCode());
        line.setDate(model.getDate());
        line.setAmount(model.getAmount());
        //line.setTarget(bankAccountSvc.get(model.getBankAccountCode()));
    }

    private void toModel(GeneralJournalBatchLine generalJournalBatchLine,
            GeneralJournalBatchLineModel generalJournalBatchLineModel) {
        generalJournalBatchLineModel.setLineNo(generalJournalBatchLine.getGeneralJournalBatchLineId().getLineNo());
        generalJournalBatchLineModel.setSourceType(generalJournalBatchLine.getSourceType());
        generalJournalBatchLineModel.setSourceCode(generalJournalBatchLine.getSourceCode());
        generalJournalBatchLineModel.setSourceName(generalJournalBatchLine.getSourceName());
        generalJournalBatchLineModel.setOperationType(generalJournalBatchLine.getOperationType());
        generalJournalBatchLineModel.setDocumentCode(generalJournalBatchLine.getDocumentCode());
        generalJournalBatchLineModel.setDate(generalJournalBatchLine.getDate());
        generalJournalBatchLineModel.setAmount(generalJournalBatchLine.getAmount());
        if (generalJournalBatchLine.getTarget() != null) {
            generalJournalBatchLineModel.setBankAccountCode(generalJournalBatchLine.getTarget().getCode());
        }
    }

    private void setSourceName(GeneralJournalBatchLineModel generalJournalBatchLine) {
        if (generalJournalBatchLine.getSourceType().equals(GeneralJournalBatchLineType.BANK_ACCOUNT)) {
            generalJournalBatchLine
                    .setSourceName(bankAccountSvc.get(generalJournalBatchLine.getSourceCode()).getName());
        }

        if (generalJournalBatchLine.getSourceType().equals(GeneralJournalBatchLineType.CUSTOMER)) {
            generalJournalBatchLine.setSourceName(customerSvc.get(generalJournalBatchLine.getSourceCode()).getName());
        }

        if (generalJournalBatchLine.getSourceType().equals(GeneralJournalBatchLineType.VENDOR)) {
            generalJournalBatchLine.setSourceName(vendorSvc.get(generalJournalBatchLine.getSourceCode()).getName());
        }
    }

    private void addVendors(List<GeneralJournalBatchLineSource> sources) {
        sources.addAll(vendorSvc.list().stream()
                .map(x -> {
                    GeneralJournalBatchLineSource s = new GeneralJournalBatchLineSource();
                    s.setCode(x.getCode());
                    s.setName(x.getName());
                    return s;
                })
                .collect(Collectors.toList()));
    }

    private void addCustomers(List<GeneralJournalBatchLineSource> sources) {
        sources.addAll(customerSvc.list().stream()
                .map(c -> {
                    GeneralJournalBatchLineSource s = new GeneralJournalBatchLineSource();
                    s.setCode(c.getCode());
                    s.setName(c.getName());
                    return s;
                })
                .collect(Collectors.toList()));
    }

    private void addBankAccounts(List<GeneralJournalBatchLineSource> sources) {
        /*
        sources.addAll(bankAccountSvc.list().stream()
                .map(x -> {
                    GeneralJournalBatchLineSource s = new GeneralJournalBatchLineSource();
                    s.setCode(x.getCode());
                    s.setName(x.getName());
                    return s;
                })
                .collect(Collectors.toList()));
        */
    }
}
