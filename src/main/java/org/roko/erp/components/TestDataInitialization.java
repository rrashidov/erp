package org.roko.erp.components;

import org.roko.erp.model.BankAccount;
import org.roko.erp.model.Customer;
import org.roko.erp.model.Item;
import org.roko.erp.model.PaymentMethod;
import org.roko.erp.services.BankAccountService;
import org.roko.erp.services.CustomerService;
import org.roko.erp.services.ItemService;
import org.roko.erp.services.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class TestDataInitialization implements ApplicationListener<ContextRefreshedEvent> {

    private BankAccountService bankAccountSvc;
    private PaymentMethodService paymentMethodSvc;
    private CustomerService customerService;
    private ItemService itemSvc;

    @Autowired
    public TestDataInitialization(BankAccountService bankAccountSvc, PaymentMethodService paymentMethodSvc,
        CustomerService customerService, ItemService itemSvc) {
        this.bankAccountSvc = bankAccountSvc;
        this.paymentMethodSvc = paymentMethodSvc;
        this.customerService = customerService;
        this.itemSvc = itemSvc;
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

      // init items
      Item i1 = new Item();
      i1.setCode("ITEM01");
      i1.setName("Test item 01");
      i1.setSalesPrice(1.0);
      i1.setPurchasePrice(1.0);
      itemSvc.create(i1);

      Item i2 = new Item();
      i2.setCode("ITEM02");
      i2.setName("Test item 02");
      i2.setSalesPrice(2.0);
      i2.setPurchasePrice(2.0);
      itemSvc.create(i2);

      Item i3 = new Item();
      i3.setCode("ITEM03");
      i3.setName("Test item 03");
      i3.setSalesPrice(3.0);
      i3.setPurchasePrice(3.0);
      itemSvc.create(i3);

    }
}