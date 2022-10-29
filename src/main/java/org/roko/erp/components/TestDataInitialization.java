package org.roko.erp.components;

import org.roko.erp.model.BankAccount;
import org.roko.erp.model.Customer;
import org.roko.erp.model.PaymentMethod;
import org.roko.erp.services.BankAccountService;
import org.roko.erp.services.CustomerService;
import org.roko.erp.services.PaymentMethodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class TestDataInitialization implements ApplicationListener<ContextRefreshedEvent> {

    private BankAccountService bankAccountSvc;
    private PaymentMethodService paymentMethodSvc;
    private CustomerService customerService;

    @Autowired
    public TestDataInitialization(BankAccountService bankAccountSvc, PaymentMethodService paymentMethodSvc,
        CustomerService customerService) {
        this.bankAccountSvc = bankAccountSvc;
        this.paymentMethodSvc = paymentMethodSvc;
        this.customerService = customerService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
      // init bank accounts
      BankAccount ba01 = new BankAccount();
      ba01.setCode("BA01");
      ba01.setName("Cash");
      bankAccountSvc.create(ba01);

      BankAccount ba02 = new BankAccount();
      ba02.setCode("BA02");
      ba02.setName("Bank");
      bankAccountSvc.create(ba02);

      // init payment methods
      PaymentMethod pm01 = new PaymentMethod();
      pm01.setCode("PM01");
      pm01.setName("Delayed");
      paymentMethodSvc.create(pm01);

      PaymentMethod pm02 = new PaymentMethod();
      pm02.setCode("PM02");
      pm02.setName("Cash");
      pm02.setBankAccount(ba01);
      paymentMethodSvc.create(pm02);

      PaymentMethod pm03 = new PaymentMethod();
      pm03.setCode("PM03");
      pm03.setName("By card");
      pm03.setBankAccount(ba02);
      paymentMethodSvc.create(pm03);

      // init customers
      Customer c1 = new Customer();
      c1.setCode("CUST01");
      c1.setName("Customer 01");
      c1.setAddress("Test address");
      c1.setPaymentMethod(pm01);
      customerService.create(c1);
    }
}