package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.Customer;
import org.roko.erp.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CustomerController {
    
    private static final String MODEL_ATTR_CUSTOMERS = "customers";
    private static final String MODEL_ATTR_PAGING = "paging";

    private static final String CUSTOMER_OBJECT_NAME = "customer";
    private CustomerService svc;
    private PagingService pagingSvc;

    @Autowired
    public CustomerController(CustomerService svc, PagingService pagingSvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
    }

    @GetMapping("/customerList")
    public String list(@RequestParam(name = "page", required = false) Long page, Model model){
        PagingData pagingData = pagingSvc.generate(CUSTOMER_OBJECT_NAME, page, svc.count());

        List<Customer> customerList = svc.list();

        model.addAttribute(MODEL_ATTR_PAGING, pagingData);
        model.addAttribute(MODEL_ATTR_CUSTOMERS, customerList);

        return "customerList.html";
    }
}
