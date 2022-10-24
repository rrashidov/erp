package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.BankAccount;
import org.roko.erp.services.BankAccountService;
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
    
    private static final String BANK_ACCOUNTS_MODEL_ATTRIBUTE = "bankAccounts";
    private static final String PAGING_MODEL_ATTRIBUTE = "paging";

    private static final String OBJECT_NAME = "bankAccount";
    private static final String BANK_ACCOUNT_LIST_TEMPLATE = "bankAccountList.html";

    private BankAccountService svc;
    private PagingService pagingSvc;

    @Autowired
    public BankAccountController(BankAccountService svc, PagingService pagingSvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
    }

    @GetMapping("/bankAccountList")
    public String list(@RequestParam(name = "page", required = false) Long page, Model model){
        PagingData pagingData = pagingSvc.generate(OBJECT_NAME, page, svc.count());
        List<BankAccount> bankAccounts = svc.list();

        model.addAttribute(PAGING_MODEL_ATTRIBUTE, pagingData);
        model.addAttribute(BANK_ACCOUNTS_MODEL_ATTRIBUTE, bankAccounts);

        return BANK_ACCOUNT_LIST_TEMPLATE;
    }

    @GetMapping("/bankAccountCard")
    public String card(@RequestParam(name="code", required=false) String code, Model model){
        BankAccount bankAccount = new BankAccount();

        if (code != null){
            bankAccount = svc.get(code);
        }

        model.addAttribute("bankAccount", bankAccount);

        return "bankAccountCard.html";
    }

    @PostMapping("/bankAccountCard")
    public RedirectView postCard(@ModelAttribute BankAccount bankAccount){
        BankAccount bankAccountFromDB = svc.get(bankAccount.getCode());

        if (bankAccountFromDB == null){
            bankAccountFromDB = new BankAccount();
            bankAccountFromDB.setCode(bankAccount.getCode());
        }

        bankAccountFromDB.setName(bankAccount.getName());

        svc.create(bankAccountFromDB);

        return new RedirectView("/bankAccountList");
    }
}
