package org.roko.erp.controllers;

import java.util.Date;
import java.util.List;

import org.roko.erp.controllers.model.SalesCreditMemoModel;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.Customer;
import org.roko.erp.model.SalesCreditMemo;
import org.roko.erp.model.SalesCreditMemoLine;
import org.roko.erp.services.CustomerService;
import org.roko.erp.services.PaymentMethodService;
import org.roko.erp.services.SalesCreditMemoLineService;
import org.roko.erp.services.SalesCreditMemoPostService;
import org.roko.erp.services.SalesCreditMemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class SalesCreditMemoController {

    private SalesCreditMemoService svc;
    private PagingService pagingSvc;
    private CustomerService customerSvc;
    private PaymentMethodService paymentMethodSvc;
    private SalesCreditMemoLineService salesCreditMemoLineSvc;
    private SalesCreditMemoPostService salesCreditMemoPostSvc;

    @Autowired
    public SalesCreditMemoController(SalesCreditMemoService svc, PagingService pagingSvc, CustomerService customerSvc,
            PaymentMethodService paymentMethodSvc, SalesCreditMemoLineService salesCreditMemoLineSvc,
            SalesCreditMemoPostService salesCreditMemoPostSvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
        this.customerSvc = customerSvc;
        this.paymentMethodSvc = paymentMethodSvc;
        this.salesCreditMemoLineSvc = salesCreditMemoLineSvc;
        this.salesCreditMemoPostSvc = salesCreditMemoPostSvc;
    }

    @GetMapping("/salesCreditMemoList")
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        List<SalesCreditMemo> salesCreditMemos = svc.list(page);
        PagingData pagingData = pagingSvc.generate("salesCreditMemo", page, svc.count());

        model.addAttribute("salesCreditMemos", salesCreditMemos);
        model.addAttribute("paging", pagingData);

        return "salesCreditMemoList.html";
    }

    @GetMapping("/salesCreditMemoWizard")
    public String wizard(@RequestParam(name = "code", required = false) String code, Model model) {
        SalesCreditMemoModel salesCreditMemoModel = new SalesCreditMemoModel();

        if (code != null) {
            SalesCreditMemo salesCreditMemo = svc.get(code);
            toModel(salesCreditMemo, salesCreditMemoModel);
        }

        List<Customer> customers = customerSvc.list();

        model.addAttribute("salesCreditMemoModel", salesCreditMemoModel);
        model.addAttribute("customers", customers);

        return "salesCreditMemoWizardFirstPage.html";
    }

    @PostMapping("/salesCreditMemoWizardFirstPage")
    public String postWizardFirstPage(@ModelAttribute SalesCreditMemoModel salesCreditMemoModel, Model model) {
        Customer customer = customerSvc.get(salesCreditMemoModel.getCustomerCode());

        salesCreditMemoModel.setCustomerName(customer.getName());
        salesCreditMemoModel.setDate(new Date());
        salesCreditMemoModel.setPaymentMethodCode(customer.getPaymentMethod().getCode());

        model.addAttribute("paymentMethods", paymentMethodSvc.list());

        return "salesCreditMemoWizardSecondPage.html";
    }

    @PostMapping("/salesCreditMemoWizardSecondPage")
    public RedirectView postWizardSecondPage(@ModelAttribute SalesCreditMemoModel salesCreditMemoModel,
            RedirectAttributes redirectAttributesMock) {
        if (salesCreditMemoModel.getCode().isEmpty()) {
            SalesCreditMemo createSalesCreditMemo = createSalesCreditMemo(salesCreditMemoModel);

            redirectAttributesMock.addAttribute("code", createSalesCreditMemo.getCode());
        } else {
            updateSalesCreditMemo(salesCreditMemoModel);
            
            redirectAttributesMock.addAttribute("code", salesCreditMemoModel.getCode());
        }

        return new RedirectView("/salesCreditMemoCard");
    }

    @GetMapping("/deleteSalesCreditMemo")
    public RedirectView delete(@RequestParam(name = "code") String code) {
        svc.delete(code);

        return new RedirectView("/salesCreditMemoList");
    }

    @GetMapping("/salesCreditMemoCard")
    public String card(@RequestParam(name="code") String code, Model model) {
        SalesCreditMemo salesCreditMemo = svc.get(code);
        List<SalesCreditMemoLine> salesCreditMemoLines = salesCreditMemoLineSvc.list(salesCreditMemo);
        PagingData pagingData = pagingSvc.generate("salesCreditMemoLine", 1, salesCreditMemoLineSvc.count(salesCreditMemo));

        model.addAttribute("salesCreditMemo", salesCreditMemo);
        model.addAttribute("salesCreditMemoLines", salesCreditMemoLines);
        model.addAttribute("paging", pagingData);
        
        return "salesCreditMemoCard.html";
    }

    @GetMapping("/postSalesCreditMemo")
    public RedirectView post(@RequestParam(name="code") String code) {
        salesCreditMemoPostSvc.post(code);
        
        return new RedirectView("/salesCreditMemoList");
    }

    private SalesCreditMemo createSalesCreditMemo(SalesCreditMemoModel salesCreditMemoModel) {
        SalesCreditMemo salesCreditMemo = new SalesCreditMemo();
        salesCreditMemo.setCode("SCM" + System.currentTimeMillis());
        salesCreditMemo.setCustomer(customerSvc.get(salesCreditMemoModel.getCustomerCode()));
        salesCreditMemo.setDate(salesCreditMemoModel.getDate());
        salesCreditMemo.setPaymentMethod(paymentMethodSvc.get(salesCreditMemoModel.getPaymentMethodCode()));

        svc.create(salesCreditMemo);

        return salesCreditMemo;
    }

    private void updateSalesCreditMemo(SalesCreditMemoModel salesCreditMemoModel) {
        SalesCreditMemo salesCreditMemo = svc.get(salesCreditMemoModel.getCode());
        salesCreditMemo.setCustomer(customerSvc.get(salesCreditMemoModel.getCustomerCode()));
        salesCreditMemo.setDate(salesCreditMemoModel.getDate());
        salesCreditMemo.setPaymentMethod(paymentMethodSvc.get(salesCreditMemoModel.getPaymentMethodCode()));

        svc.update(salesCreditMemoModel.getCode(), salesCreditMemo);
    }

    private void toModel(SalesCreditMemo salesCreditMemo, SalesCreditMemoModel salesCreditMemoModel) {
        salesCreditMemoModel.setCode(salesCreditMemo.getCode());
        salesCreditMemoModel.setCustomerCode(salesCreditMemo.getCustomer().getCode());
        salesCreditMemoModel.setCustomerName(salesCreditMemo.getCustomer().getName());
        salesCreditMemoModel.setDate(salesCreditMemo.getDate());
        salesCreditMemoModel.setPaymentMethodCode(salesCreditMemo.getPaymentMethod().getCode());
    }

}
