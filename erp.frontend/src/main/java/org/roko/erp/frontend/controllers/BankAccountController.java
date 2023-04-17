package org.roko.erp.frontend.controllers;

import org.roko.erp.dto.BankAccountDTO;
import org.roko.erp.dto.list.BankAccountLedgerEntryList;
import org.roko.erp.dto.list.BankAccountList;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.services.BankAccountLedgerEntryService;
import org.roko.erp.frontend.services.BankAccountService;
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
public class BankAccountController {

    private static final String BANK_ACCOUNT_LIST_URL = "/bankAccountList";

    private static final String LIST_MODEL_NAME = "bankAccounts";
    private static final String PAGING_MODEL_NAME = "paging";

    private static final String OBJECT_NAME = "bankAccount";

    private static final String LIST_TEMPLATE = "bankAccountList.html";
    private static final String CARD_TEMPLATE = "bankAccountCard.html";

    private BankAccountService svc;
    private PagingService pagingSvc;

    private BankAccountLedgerEntryService bankAccountLedgerEntrySvc;

    @Autowired
    public BankAccountController(BankAccountService svc, PagingService pagingSvc,
            BankAccountLedgerEntryService bankAccountLedgerEntrySvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
        this.bankAccountLedgerEntrySvc = bankAccountLedgerEntrySvc;
    }

    @GetMapping(BANK_ACCOUNT_LIST_URL)
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        BankAccountList bankAccountList = svc.list(page);

        PagingData pagingData = pagingSvc.generate(OBJECT_NAME, page, (int) bankAccountList.getCount());

        model.addAttribute(PAGING_MODEL_NAME, pagingData);
        model.addAttribute(LIST_MODEL_NAME, bankAccountList.getData());

        return LIST_TEMPLATE;
    }

    @GetMapping("/bankAccountCard")
    public String card(@RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            Model model) {
        BankAccountDTO bankAccount = new BankAccountDTO();

        if (code != null) {
            bankAccount = svc.get(code);

            BankAccountLedgerEntryList bankAccountLedgerEntryList = bankAccountLedgerEntrySvc.list(code, page);

            model.addAttribute("bankAccountLedgerEntries", bankAccountLedgerEntryList.getData());
            model.addAttribute("paging",
                    pagingSvc.generate("bankAccountCard", code, page, (int) bankAccountLedgerEntryList.getCount()));
        }

        model.addAttribute("bankAccount", bankAccount);

        return CARD_TEMPLATE;
    }

    @PostMapping("/bankAccountCard")
    public RedirectView postCard(@ModelAttribute BankAccountDTO bankAccount) {
        BankAccountDTO bankAccountFromDB = svc.get(bankAccount.getCode());

        if (bankAccountFromDB == null) {
            svc.create(bankAccount);
        } else {
            svc.update(bankAccount.getCode(), bankAccount);
        }

        return new RedirectView(BANK_ACCOUNT_LIST_URL);
    }

    @GetMapping("/deleteBankAccount")
    public RedirectView delete(@RequestParam(name = "code") String code,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            RedirectAttributes redirectAttributes) {
        svc.delete(code);

        redirectAttributes.addAttribute("page", page);

        return new RedirectView(BANK_ACCOUNT_LIST_URL);
    }
}
