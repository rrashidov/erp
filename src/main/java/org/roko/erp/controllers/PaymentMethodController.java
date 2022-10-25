package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.controllers.model.PaymentMethodModel;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.BankAccount;
import org.roko.erp.model.PaymentMethod;
import org.roko.erp.services.BankAccountService;
import org.roko.erp.services.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class PaymentMethodController {
    
    private PaymentMethodService svc;
    private PagingService pagingSvc;
    private BankAccountService bankAccountSvc;

    @Autowired
    public PaymentMethodController(PaymentMethodService svc, PagingService pagingSvc, BankAccountService bankAccountSvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
        this.bankAccountSvc = bankAccountSvc;
    }

    @GetMapping("/paymentMethodList")
    public String list(@RequestParam(name="page", required = false) Long page, Model model){
        PagingData pagingData = pagingSvc.generate("paymentMethod", page, svc.count());
        List<PaymentMethod> paymentMethodList = svc.list();

        model.addAttribute("paging", pagingData);
        model.addAttribute("paymentMethods", paymentMethodList);

        return "paymentMethodList.html";
    }

    @GetMapping("/paymentMethodCard")
    public String card(@RequestParam(name="code", required=false) String code, Model model){
        PaymentMethodModel paymentMethodModel = new PaymentMethodModel();

        if (code != null){
            PaymentMethod paymentMethod = svc.get(code);
            paymentMethodModel.setCode(paymentMethod.getCode());
            paymentMethodModel.setName(paymentMethod.getName());
            if (paymentMethod.getBankAccount() != null){
                paymentMethodModel.setBankAccountCode(paymentMethod.getBankAccount().getCode());
            }
        }

        model.addAttribute("bankAccounts", bankAccountSvc.list());
        model.addAttribute("paymentMethod", paymentMethodModel);

        return "paymentMethodCard.html";
    }

    @PostMapping("/paymentMethodCard")
    public RedirectView postCard(@ModelAttribute PaymentMethodModel model){
        PaymentMethod paymentMethod = new PaymentMethod();

        paymentMethod.setCode(model.getCode());
        paymentMethod.setName(model.getName());
        if (!model.getBankAccountCode().isEmpty()) {
            BankAccount bankAccount = bankAccountSvc.get(model.getBankAccountCode());
            paymentMethod.setBankAccount(bankAccount);
        }

        svc.create(paymentMethod);

        return new RedirectView("/paymentMethodList");
    }

    @GetMapping("/deletePaymentMethod")
    public RedirectView delete(@RequestParam(name="code") String code){
        svc.delete(code);

        return new RedirectView("/paymentMethodList");
    }
}
