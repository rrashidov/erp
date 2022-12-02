package org.roko.erp.backend.controllers;

import java.util.List;

import org.roko.erp.backend.model.BankAccount;
import org.roko.erp.backend.services.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bankaccounts")
public class BankAccountController {
    
    private BankAccountService svc;

    @Autowired
    public BankAccountController(BankAccountService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<BankAccount> list() {
        return svc.list();
    }

    @GetMapping("/page/{page}")
    public List<BankAccount> list(@PathVariable("page") int page){
        return svc.list(page);
    }
    
    @GetMapping("/{code}")
    public BankAccount get(@PathVariable("code") String code){
        return svc.get(code);
    }

    @PostMapping
    public String post(@RequestBody BankAccount bankAccount){
        svc.create(bankAccount);
        return bankAccount.getCode();
    }

    @PutMapping("/{code}")
    public String put(@PathVariable("code") String code, @RequestBody BankAccount bankAccount){
        svc.update(code, bankAccount);
        return code;
    }

    @DeleteMapping("/{code}")
    public String delete(@PathVariable("code") String code){
        svc.delete(code);
        return code;
    }
}
