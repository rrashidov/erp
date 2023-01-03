package org.roko.erp.frontend.controllers;

import org.roko.erp.dto.PaymentMethodDTO;
import org.roko.erp.dto.list.BankAccountList;
import org.roko.erp.dto.list.PaymentMethodList;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.services.BankAccountService;
import org.roko.erp.frontend.services.PaymentMethodService;
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
    public String list(@RequestParam(name="page", required = false, defaultValue = "1") int page, Model model){
        PaymentMethodList paymentMethodList = svc.list(page);

        PagingData pagingData = pagingSvc.generate("paymentMethod", page, (int) paymentMethodList.getCount());

        model.addAttribute("paging", pagingData);
        model.addAttribute("paymentMethods", paymentMethodList.getData());

        return "paymentMethodList.html";
    }

    @GetMapping("/paymentMethodCard")
    public String card(@RequestParam(name="code", required=false) String code, Model model){
        PaymentMethodDTO paymentMethodModel = new PaymentMethodDTO();

        if (code != null){
            PaymentMethodDTO paymentMethod = svc.get(code);
            paymentMethodModel.setCode(paymentMethod.getCode());
            paymentMethodModel.setName(paymentMethod.getName());
            paymentMethodModel.setBankAccountCode(paymentMethod.getBankAccountCode());
        }

        BankAccountList bankAccountList = bankAccountSvc.list();

        model.addAttribute("bankAccounts", bankAccountList.getData());
        model.addAttribute("paymentMethod", paymentMethodModel);

        return "paymentMethodCard.html";
    }

    @PostMapping("/paymentMethodCard")
    public RedirectView postCard(@ModelAttribute PaymentMethodDTO model){
        PaymentMethodDTO paymentMethod = svc.get(model.getCode());

        if (paymentMethod == null) {
            paymentMethod = new PaymentMethodDTO();
            paymentMethod.setCode(model.getCode());
            paymentMethod.setName(model.getName());
            paymentMethod.setBankAccountCode(model.getBankAccountCode());
    
            svc.create(paymentMethod);
        } else {
            paymentMethod.setName(model.getName());
            paymentMethod.setBankAccountCode(model.getBankAccountCode());

            svc.update(model.getCode(), paymentMethod);
        }

        return new RedirectView("/paymentMethodList");
    }

    @GetMapping("/deletePaymentMethod")
    public RedirectView delete(@RequestParam(name="code") String code){
        svc.delete(code);

        return new RedirectView("/paymentMethodList");
    }
}
