package org.roko.erp.frontend.controllers;

import org.roko.erp.dto.CustomerDTO;
import org.roko.erp.dto.list.CustomerLedgerEntryList;
import org.roko.erp.dto.list.CustomerList;
import org.roko.erp.dto.list.PaymentMethodList;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.services.CustomerLedgerEntryService;
import org.roko.erp.frontend.services.CustomerService;
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
public class CustomerController {

    private static final String MODEL_ATTR_CUSTOMERS = "customers";
    private static final String MODEL_ATTR_PAGING = "paging";

    private static final String CUSTOMER_OBJECT_NAME = "customer";
    private CustomerService svc;
    private PagingService pagingSvc;
    private PaymentMethodService paymentMethodSvc;
    private CustomerLedgerEntryService customerLedgerEntrySvc;

    @Autowired
    public CustomerController(CustomerService svc, PagingService pagingSvc, PaymentMethodService paymentMethodSvc,
            CustomerLedgerEntryService customerLedgerEntrySvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
        this.paymentMethodSvc = paymentMethodSvc;
        this.customerLedgerEntrySvc = customerLedgerEntrySvc;
    }

    @GetMapping("/customerList")
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        CustomerList customerList = svc.list(page);
        
        PagingData pagingData = pagingSvc.generate(CUSTOMER_OBJECT_NAME, page, (int) customerList.getCount());

        model.addAttribute(MODEL_ATTR_PAGING, pagingData);
        model.addAttribute(MODEL_ATTR_CUSTOMERS, customerList.getData());

        return "customerList.html";
    }

    @GetMapping("/customerCard")
    public String card(@RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            Model model) {
        CustomerDTO customer = new CustomerDTO();

        if (code != null) {
            customer = svc.get(code);

            CustomerLedgerEntryList customerLedgerEntryList = customerLedgerEntrySvc.list(customer.getCode(), page);

            model.addAttribute("customerLedgerEntries", customerLedgerEntryList.getData());
            model.addAttribute("paging",
                    pagingSvc.generate("customerCard", code, page, (int) customerLedgerEntryList.getCount()));
        }

        PaymentMethodList paymentMethodList = paymentMethodSvc.list();

        model.addAttribute("customer", customer);
        model.addAttribute("paymentMethods", paymentMethodList.getData());

        return "customerCard.html";
    }

    @PostMapping("/customerCard")
    public RedirectView post(@ModelAttribute CustomerDTO customerModel) {
        CustomerDTO customer = svc.get(customerModel.getCode());

        if (customer == null){
            customer = new CustomerDTO();
            customer.setCode(customerModel.getCode());
            transferFields(customerModel, customer);
            svc.create(customer);
        } else {
            transferFields(customerModel, customer);
            svc.update(customerModel.getCode(), customer);
        }

        return new RedirectView("/customerList");
    }

    @GetMapping("/deleteCustomer")
    public RedirectView delete(@RequestParam(name = "code") String code) {
        svc.delete(code);

        return new RedirectView("/customerList");
    }

    private void transferFields(CustomerDTO source, CustomerDTO target) {
        target.setName(source.getName());
        target.setAddress(source.getAddress());
        target.setPaymentMethodCode(source.getPaymentMethodCode());
    }

}
