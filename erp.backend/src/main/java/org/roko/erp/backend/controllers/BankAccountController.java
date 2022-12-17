package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.BankAccount;
import org.roko.erp.backend.services.BankAccountLedgerEntryService;
import org.roko.erp.backend.services.BankAccountService;
import org.roko.erp.dto.BankAccountDTO;
import org.roko.erp.dto.BankAccountLedgerEntryDTO;
import org.roko.erp.dto.list.BankAccountLedgerEntryList;
import org.roko.erp.dto.list.BankAccountList;
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
    private BankAccountLedgerEntryService bankAccountLedgerEntrySvc;

    @Autowired
    public BankAccountController(BankAccountService svc, BankAccountLedgerEntryService bankAccountLedgerEntrySvc) {
        this.svc = svc;
        this.bankAccountLedgerEntrySvc = bankAccountLedgerEntrySvc;
    }

    @GetMapping
    public BankAccountList list() {
        List<BankAccountDTO> data = svc.list().stream()
                .map(x -> svc.toDTO(x))
                .collect(Collectors.toList());

        BankAccountList list = new BankAccountList();
        list.setData(data);
        list.setCount(svc.count());
        return list;
    }

    @GetMapping("/page/{page}")
    public BankAccountList list(@PathVariable("page") int page) {
        List<BankAccountDTO> data = svc.list(page).stream()
                .map(x -> svc.toDTO(x))
                .collect(Collectors.toList());

        BankAccountList list = new BankAccountList();
        list.setData(data);
        list.setCount(svc.count());
        return list;
    }

    @GetMapping("/{code}")
    public BankAccountDTO get(@PathVariable("code") String code) {
        return svc.toDTO(svc.get(code));
    }

    @GetMapping("/{code}/ledgerentries/page/{page}")
    public BankAccountLedgerEntryList listLedgerEntries(@PathVariable("code") String code,
            @PathVariable("page") int page) {
        BankAccount bankAccount = svc.get(code);

        List<BankAccountLedgerEntryDTO> data = bankAccountLedgerEntrySvc.findFor(bankAccount, page).stream()
                .map(x -> bankAccountLedgerEntrySvc.toDTO(x))
                .collect(Collectors.toList());

        BankAccountLedgerEntryList list = new BankAccountLedgerEntryList();
        list.setData(data);
        list.setCount(bankAccountLedgerEntrySvc.count(bankAccount));
        return list;
    }

    @PostMapping
    public String post(@RequestBody BankAccountDTO bankAccountDTO) {
        BankAccount bankAccount = svc.fromDTO(bankAccountDTO);
        svc.create(bankAccount);
        return bankAccount.getCode();
    }

    @PutMapping("/{code}")
    public String put(@PathVariable("code") String code, @RequestBody BankAccountDTO bankAccountDTO) {
        BankAccount bankAccount = svc.fromDTO(bankAccountDTO);
        svc.update(code, bankAccount);
        return code;
    }

    @DeleteMapping("/{code}")
    public String delete(@PathVariable("code") String code) {
        svc.delete(code);
        return code;
    }
}
