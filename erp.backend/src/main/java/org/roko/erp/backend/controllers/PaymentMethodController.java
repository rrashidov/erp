package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.PaymentMethod;
import org.roko.erp.backend.services.BankAccountService;
import org.roko.erp.backend.services.PaymentMethodService;
import org.roko.erp.dto.PaymentMethodDTO;
import org.roko.erp.dto.list.PaymentMethodList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
    public PaymentMethodList list() {
        List<PaymentMethodDTO> data = svc.list().stream()
                .map(x -> svc.toDTO(x))
                .collect(Collectors.toList());

        PaymentMethodList list = new PaymentMethodList();
        list.setData(data);
        list.setCount(svc.count());
        return list;
    }

    @GetMapping("/page/{page}")
    public PaymentMethodList list(@PathVariable("page") int page) {
        List<PaymentMethodDTO> data = svc.list(page).stream()
                .map(x -> svc.toDTO(x))
                .collect(Collectors.toList());

        PaymentMethodList list = new PaymentMethodList();
        list.setData(data);
        list.setCount(svc.count());
        return list;
    }

    @GetMapping("/{code}")
    public PaymentMethodDTO get(@PathVariable("code") String code) {
        PaymentMethod paymentMethod = svc.get(code);

        if (paymentMethod == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return svc.toDTO(paymentMethod);
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

    private void updateFromDTO(PaymentMethod paymentMethod, PaymentMethodDTO paymentMethodDTO) {
        paymentMethod.setName(paymentMethodDTO.getName());
        if (!paymentMethodDTO.getBankAccountCode().isEmpty()) {
            paymentMethod.setBankAccount(bankAccountSvc.get(paymentMethodDTO.getBankAccountCode()));
        }
    }
}
