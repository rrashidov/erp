package org.roko.erp.frontend.controllers;

import java.util.List;

import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.model.BankAccount;
import org.roko.erp.frontend.services.BankAccountLedgerEntryService;
import org.roko.erp.frontend.services.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        PagingData pagingData = pagingSvc.generate(OBJECT_NAME, page, svc.count());

        List<BankAccount> bankAccounts = svc.list(page);

        model.addAttribute(PAGING_MODEL_NAME, pagingData);
        model.addAttribute(LIST_MODEL_NAME, bankAccounts);

        return LIST_TEMPLATE;
    }

    @GetMapping("/bankAccountCard")
    public String card(@RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            Model model) {
        BankAccount bankAccount = new BankAccount();

        if (code != null) {
            bankAccount = svc.get(code);

            model.addAttribute("bankAccountLedgerEntries", bankAccountLedgerEntrySvc.findFor(bankAccount, page));
            model.addAttribute("paging",
                    pagingSvc.generate("bankAccountCard", code, page, bankAccountLedgerEntrySvc.count(bankAccount)));
        }

        model.addAttribute("bankAccount", bankAccount);

        return CARD_TEMPLATE;
    }

    @PostMapping("/bankAccountCard")
    public RedirectView postCard(@ModelAttribute BankAccount bankAccount) {
        BankAccount bankAccountFromDB = svc.get(bankAccount.getCode());

        if (bankAccountFromDB == null) {
            bankAccountFromDB = new BankAccount();
            bankAccountFromDB.setCode(bankAccount.getCode());
        }

        bankAccountFromDB.setName(bankAccount.getName());

        svc.create(bankAccountFromDB);

        return new RedirectView(BANK_ACCOUNT_LIST_URL);
    }

    @GetMapping("/deleteBankAccount")
    public RedirectView delete(@RequestParam(name = "code") String code) {
        svc.delete(code);

        return new RedirectView(BANK_ACCOUNT_LIST_URL);
    }
}
