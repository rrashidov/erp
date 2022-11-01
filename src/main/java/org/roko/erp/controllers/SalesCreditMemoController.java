package org.roko.erp.controllers;

import java.util.Date;
import java.util.List;

import org.roko.erp.controllers.model.SalesCreditMemoModel;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.Customer;
import org.roko.erp.model.SalesCreditMemo;
import org.roko.erp.services.CustomerService;
import org.roko.erp.services.PaymentMethodService;
import org.roko.erp.services.SalesCreditMemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class SalesCreditMemoController {

    private SalesCreditMemoService svc;
    private PagingService pagingSvc;
    private CustomerService customerSvc;
    private PaymentMethodService paymentMethodSvc;

    @Autowired
    public SalesCreditMemoController(SalesCreditMemoService svc, PagingService pagingSvc, CustomerService customerSvc,
            PaymentMethodService paymentMethodSvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
        this.customerSvc = customerSvc;
        this.paymentMethodSvc = paymentMethodSvc;
    }

    @GetMapping("/salesCreditMemoList")
    public String list(@RequestParam(name = "page", required = false) Long page, Model model) {
        List<SalesCreditMemo> salesCreditMemos = svc.list();
        PagingData pagingData = pagingSvc.generate("salesCreditMemo", page, svc.count());

        model.addAttribute("salesCreditMemos", salesCreditMemos);
        model.addAttribute("paging", pagingData);

        return "salesCreditMemoList.html";
    }

    @GetMapping("/salesCreditMemoWizard")
    public String wizard(Model model){
        SalesCreditMemoModel salesCreditMemoModel = new SalesCreditMemoModel();
        List<Customer> customers = customerSvc.list();

        model.addAttribute("salesCreditMemoModel", salesCreditMemoModel);
        model.addAttribute("customers", customers);

        return "salesCreditMemoWizardFirstPage.html";
    }

    @PostMapping("/salesCreditMemoWizardFirstPage")
    public String postWizardFirstPage(@ModelAttribute SalesCreditMemoModel salesCreditMemoModel, Model model){
        Customer customer = customerSvc.get(salesCreditMemoModel.getCustomerCode());

        salesCreditMemoModel.setCustomerName(customer.getName());
        salesCreditMemoModel.setDate(new Date());
        salesCreditMemoModel.setPaymentMethodCode(customer.getPaymentMethod().getCode());

        model.addAttribute("paymentMethods", paymentMethodSvc.list());

        return "salesCreditMemoWizardSecondPage.html";
    }

    @PostMapping("/salesCreditMemoWizardSecondPage")
    public RedirectView postWizardSecondPage(@ModelAttribute SalesCreditMemoModel salesCreditMemoModel){

        SalesCreditMemo salesCreditMemo = new SalesCreditMemo();
        salesCreditMemo.setCode("SCM" + System.currentTimeMillis());
        salesCreditMemo.setCustomer(customerSvc.get(salesCreditMemoModel.getCustomerCode()));
        salesCreditMemo.setDate(salesCreditMemoModel.getDate());
        salesCreditMemo.setPaymentMethod(paymentMethodSvc.get(salesCreditMemoModel.getPaymentMethodCode()));

        svc.create(salesCreditMemo);

        return new RedirectView("/salesCreditMemoList");
    }

}
