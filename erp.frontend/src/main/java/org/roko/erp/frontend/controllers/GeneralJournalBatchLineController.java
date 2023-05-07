package org.roko.erp.frontend.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.dto.GeneralJournalBatchDTO;
import org.roko.erp.dto.GeneralJournalBatchLineDTO;
import org.roko.erp.dto.GeneralJournalBatchLineOperationType;
import org.roko.erp.dto.GeneralJournalBatchLineType;
import org.roko.erp.frontend.controllers.model.GeneralJournalBatchLineSource;
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
                GeneralJournalBatchLineDTO generalJournalBatchLineModel = new GeneralJournalBatchLineDTO();
        generalJournalBatchLineModel.setGeneralJournalBatchCode(code);
        generalJournalBatchLineModel.setDate(new Date());

        if (lineNo != 0) {
            GeneralJournalBatchLineDTO generalJournalBatchLine = svc.get(code, lineNo);
            toModel(generalJournalBatchLine, generalJournalBatchLineModel);
        }

        model.addAttribute("generalJournalBatchLine", generalJournalBatchLineModel);

        return "generalJournalBatchLineWizardFirstPage.html";
    }

    @PostMapping("/generalJournalBatchLineWizardFirstPage")
    public String postGeneralJournalBatchLineWizardFirstPage(
            @ModelAttribute GeneralJournalBatchLineDTO generalJournalBatchLine, Model model) {

        List<GeneralJournalBatchLineSource> sources = new ArrayList<>();

        if (generalJournalBatchLine.getType().equals(GeneralJournalBatchLineType.BANK_ACCOUNT)) {
            addBankAccounts(sources);
        }

        if (generalJournalBatchLine.getType().equals(GeneralJournalBatchLineType.CUSTOMER)) {
            addCustomers(sources);
        }

        if (generalJournalBatchLine.getType().equals(GeneralJournalBatchLineType.VENDOR)) {
            addVendors(sources);
        }

        model.addAttribute("generalJournalBatchLine", generalJournalBatchLine);
        model.addAttribute("sources", sources);

        return "generalJournalBatchLineWizardSecondPage.html";
    }

    @PostMapping("/generalJournalBatchLineWizardSecondPage")
    public String postGeneralJournalBatchLineWizardSecondPage(
            @ModelAttribute GeneralJournalBatchLineDTO generalJournalBatchLine, Model model) {
        setSourceName(generalJournalBatchLine);

        model.addAttribute("generalJournalBatchLine", generalJournalBatchLine);
        model.addAttribute("bankAccounts", bankAccountSvc.list().getData()); 

        return "generalJournalBatchLineWizardThirdPage.html";
    }

    @PostMapping("/generalJournalBatchLineWizardThirdPage")
    public RedirectView postGeneralJournalBatchLineWizardThirdPage(
            @ModelAttribute GeneralJournalBatchLineDTO generalJournalBatchLine,
            RedirectAttributes redirectAttributes) {

        GeneralJournalBatchDTO generalJournalBatch = generalJournalBatchSvc
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
            @RequestParam(name = "lineNo") int lineNo,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            RedirectAttributes redirectAttributes) {
        svc.delete(code, lineNo);

        redirectAttributes.addAttribute("code", code);
        redirectAttributes.addAttribute("page", page);

        return new RedirectView("/generalJournalBatchCard");
    }

    private void create(GeneralJournalBatchDTO generalJournalBatch,
    GeneralJournalBatchLineDTO generalJournalBatchLine) {
        GeneralJournalBatchLineDTO line = new GeneralJournalBatchLineDTO();

        fromModel(line, generalJournalBatchLine);

        svc.create(generalJournalBatchLine.getGeneralJournalBatchCode(), line);
    }

    private void update(GeneralJournalBatchDTO generalJournalBatch,
    GeneralJournalBatchLineDTO generalJournalBatchLine) {
        GeneralJournalBatchLineDTO line = svc.get(generalJournalBatchLine.getGeneralJournalBatchCode(),
                generalJournalBatchLine.getLineNo());

        fromModel(line, generalJournalBatchLine);

        svc.update(generalJournalBatchLine.getGeneralJournalBatchCode(), generalJournalBatchLine.getLineNo(), line);
    }

    public void fromModel(GeneralJournalBatchLineDTO line, GeneralJournalBatchLineDTO model) {
        line.setType(model.getType());
        line.setCode(model.getCode());
        line.setName(model.getName());
        line.setOperationType(org.roko.erp.dto.GeneralJournalBatchLineOperationType.valueOf(model.getOperationType().name()));
        line.setDocumentCode(model.getDocumentCode());
        line.setDate(model.getDate());
        line.setAmount(model.getAmount());
        line.setBankAccountCode(model.getBankAccountCode());
    }

    private void toModel(GeneralJournalBatchLineDTO generalJournalBatchLine,
    GeneralJournalBatchLineDTO generalJournalBatchLineModel) {
        generalJournalBatchLineModel.setLineNo(generalJournalBatchLine.getLineNo());
        generalJournalBatchLineModel.setType(generalJournalBatchLine.getType());
        generalJournalBatchLineModel.setCode(generalJournalBatchLine.getCode());
        generalJournalBatchLineModel.setName(generalJournalBatchLine.getName());
        generalJournalBatchLineModel.setOperationType(
                GeneralJournalBatchLineOperationType.valueOf(generalJournalBatchLine.getOperationType().name()));
        generalJournalBatchLineModel.setDocumentCode(generalJournalBatchLine.getDocumentCode());
        generalJournalBatchLineModel.setDate(generalJournalBatchLine.getDate());
        generalJournalBatchLineModel.setAmount(generalJournalBatchLine.getAmount());
        if (generalJournalBatchLine.getBankAccountCode() != null) {
            generalJournalBatchLineModel.setBankAccountCode(generalJournalBatchLine.getBankAccountCode());
        }
    }

    private void setSourceName(GeneralJournalBatchLineDTO generalJournalBatchLine) {
        if (generalJournalBatchLine.getType().equals(GeneralJournalBatchLineType.BANK_ACCOUNT)) {
            generalJournalBatchLine
                    .setName(bankAccountSvc.get(generalJournalBatchLine.getCode()).getName());
        }

        if (generalJournalBatchLine.getType().equals(GeneralJournalBatchLineType.CUSTOMER)) {
            generalJournalBatchLine.setName(customerSvc.get(generalJournalBatchLine.getCode()).getName());
        }

        if (generalJournalBatchLine.getType().equals(GeneralJournalBatchLineType.VENDOR)) {
            generalJournalBatchLine.setName(vendorSvc.get(generalJournalBatchLine.getCode()).getName());
        }
    }

    private void addVendors(List<GeneralJournalBatchLineSource> sources) {
        sources.addAll(vendorSvc.list().getData().stream()
                .map(x -> {
                    GeneralJournalBatchLineSource s = new GeneralJournalBatchLineSource();
                    s.setCode(x.getCode());
                    s.setName(x.getName());
                    return s;
                })
                .collect(Collectors.toList()));
    }

    private void addCustomers(List<GeneralJournalBatchLineSource> sources) {
        sources.addAll(customerSvc.list().getData().stream()
                .map(c -> {
                    GeneralJournalBatchLineSource s = new GeneralJournalBatchLineSource();
                    s.setCode(c.getCode());
                    s.setName(c.getName());
                    return s;
                })
                .collect(Collectors.toList()));
    }

    private void addBankAccounts(List<GeneralJournalBatchLineSource> sources) {
        sources.addAll(bankAccountSvc.list().getData().stream()
                .map(x -> {
                    GeneralJournalBatchLineSource s = new GeneralJournalBatchLineSource();
                    s.setCode(x.getCode());
                    s.setName(x.getName());
                    return s;
                })
                .collect(Collectors.toList()));
    }
}
