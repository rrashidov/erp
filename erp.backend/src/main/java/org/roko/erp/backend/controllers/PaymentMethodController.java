package org.roko.erp.backend.controllers;

import java.util.List;

import org.roko.erp.backend.model.PaymentMethod;
import org.roko.erp.backend.services.BankAccountService;
import org.roko.erp.backend.services.PaymentMethodService;
import org.roko.erp.dto.PaymentMethodDTO;
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
@RequestMapping("/api/v1/paymentmethods")
public class PaymentMethodController {
    
    private PaymentMethodService svc;
    private BankAccountService bankAccountSvc;

    @Autowired
    public PaymentMethodController(PaymentMethodService svc, BankAccountService bankAccountSvc) {
        this.svc = svc;
        this.bankAccountSvc = bankAccountSvc;
    }

    @GetMapping
    public List<PaymentMethodDTO> list(){
        return svc.list();
    }

    @GetMapping("/page/{page}")
    public List<PaymentMethodDTO> list(@PathVariable("page") int page) {
        return svc.list(page);
    }

    @GetMapping("/{code}")
    public PaymentMethodDTO get(@PathVariable("code") String code) {
        return svc.getDTO(code);
    }

    @PostMapping
    public String post(@RequestBody PaymentMethodDTO paymentMethodDTO) {
        PaymentMethod paymentMethod = fromDTO(paymentMethodDTO);
        svc.create(paymentMethod);
        return paymentMethod.getCode();
    }

    @PutMapping("/{code}")
    public String put(@PathVariable("code") String code, @RequestBody PaymentMethodDTO bankAccountDTO) {
        PaymentMethod paymentMethod = svc.get(code);
        updateFromDTO(paymentMethod, bankAccountDTO);
        svc.update(code, paymentMethod);
        return paymentMethod.getCode();
    }

    @DeleteMapping("/{code}")
    public String delete(@PathVariable("code") String code) {
        svc.delete(code);
        return code;
    }

    private PaymentMethod fromDTO(PaymentMethodDTO paymentMethodDTO) {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setCode(paymentMethodDTO.getCode());
        updateFromDTO(paymentMethod, paymentMethodDTO);
        return paymentMethod;
    }

    private void updateFromDTO(PaymentMethod paymentMethod, PaymentMethodDTO paymentMethodDTO){
        paymentMethod.setName(paymentMethodDTO.getName());
        if (!paymentMethodDTO.getBankAccountCode().isEmpty()) {
            paymentMethod.setBankAccount(bankAccountSvc.get(paymentMethodDTO.getBankAccountCode()));
        }
    }
}
