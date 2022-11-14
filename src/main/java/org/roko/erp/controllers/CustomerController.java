package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.controllers.model.CustomerModel;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.Customer;
import org.roko.erp.services.CustomerLedgerEntryService;
import org.roko.erp.services.CustomerService;
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
    public String list(@RequestParam(name = "page", required = false) Long page, Model model) {
        PagingData pagingData = pagingSvc.generate(CUSTOMER_OBJECT_NAME, page, svc.count());

        List<Customer> customerList = svc.list();

        model.addAttribute(MODEL_ATTR_PAGING, pagingData);
        model.addAttribute(MODEL_ATTR_CUSTOMERS, customerList);

        return "customerList.html";
    }

    @GetMapping("/customerCard")
    public String card(@RequestParam(name = "code", required = false) String code, Model model) {
        CustomerModel customerModel = new CustomerModel();

        if (code != null) {
            Customer customer = svc.get(code);
            customerModel.setCode(customer.getCode());
            customerModel.setName(customer.getName());
            customerModel.setAddress(customer.getAddress());
            customerModel.setPaymentMethodCode(customer.getPaymentMethod().getCode());

            model.addAttribute("customerLedgerEntries", customerLedgerEntrySvc.findFor(customer));
            model.addAttribute("paging", pagingSvc.generate("customerLedgerEntry", null, customerLedgerEntrySvc.count(customer)));
        }

        model.addAttribute("customer", customerModel);
        model.addAttribute("paymentMethods", paymentMethodSvc.list());

        return "customerCard.html";
    }

    @PostMapping("/customerCard")
    public RedirectView post(@ModelAttribute CustomerModel customerModel) {
        Customer customer = new Customer();

        transferFields(customerModel, customer);

        svc.create(customer);

        return new RedirectView("/customerList");
    }

    @GetMapping("/deleteCustomer")
    public RedirectView delete(@RequestParam(name = "code") String code) {
        svc.delete(code);

        return new RedirectView("/customerList");
    }

    private void transferFields(CustomerModel customerModel, Customer customer) {
        customer.setCode(customerModel.getCode());
        customer.setName(customerModel.getName());
        customer.setAddress(customerModel.getAddress());
        customer.setPaymentMethod(paymentMethodSvc.get(customerModel.getPaymentMethodCode()));
    }

}
