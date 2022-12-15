package org.roko.erp.backend.controllers;

import java.util.Date;
import java.util.List;

import org.roko.erp.backend.model.BankAccount;
import org.roko.erp.backend.model.BankAccountLedgerEntry;
import org.roko.erp.backend.model.BankAccountLedgerEntryType;
import org.roko.erp.backend.services.BankAccountLedgerEntryService;
import org.roko.erp.backend.services.BankAccountService;
import org.roko.erp.dto.BankAccountDTO;
import org.roko.erp.dto.BankAccountLedgerEntryDTO;
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
    public List<BankAccountDTO> list() {
        return svc.list();
    }

    @GetMapping("/page/{page}")
    public List<BankAccountDTO> list(@PathVariable("page") int page) {
        return svc.list(page);
    }

    @GetMapping("/{code}")
    public BankAccountDTO get(@PathVariable("code") String code) {
        return svc.getDTO(code);
    }

    @GetMapping("/{code}/ledgerentries/page/{page}")
    public List<BankAccountLedgerEntryDTO> listLedgerEntries(@PathVariable("code") String code,
            @PathVariable("page") int page) {
        BankAccount bankAccount = svc.get(code);

        BankAccountLedgerEntry bankAccountLedgerEntry = new BankAccountLedgerEntry();
        bankAccountLedgerEntry.setBankAccount(bankAccount);
        bankAccountLedgerEntry.setType(BankAccountLedgerEntryType.FREE_ENTRY);
        bankAccountLedgerEntry.setAmount(123.12);
        bankAccountLedgerEntry.setDate(new Date());
        bankAccountLedgerEntry.setDocumentCode("BA001");
        bankAccountLedgerEntrySvc.create(bankAccountLedgerEntry);

        return bankAccountLedgerEntrySvc.list(bankAccount, page);
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
