package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.PaymentMethod;
import org.roko.erp.services.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaymentMethodController {
    
    private PaymentMethodService svc;
    private PagingService pagingSvc;

    @Autowired
    public PaymentMethodController(PaymentMethodService svc, PagingService pagingSvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
    }

    @GetMapping("/paymentMethodList")
    public String list(@RequestParam(name="page", required = false) Long page, Model model){
        PagingData pagingData = pagingSvc.generate("paymentMethod", page, svc.count());
        List<PaymentMethod> paymentMethodList = svc.list();

        model.addAttribute("paging", pagingData);
        model.addAttribute("paymentMethods", paymentMethodList);

        return "paymentMethodList.html";
    }
}
