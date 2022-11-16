package org.roko.erp.components;

import org.roko.erp.model.BankAccount;
import org.roko.erp.model.Customer;
import org.roko.erp.model.Item;
import org.roko.erp.model.PaymentMethod;
import org.roko.erp.model.Vendor;
import org.roko.erp.services.BankAccountService;
import org.roko.erp.services.CustomerService;
import org.roko.erp.services.ItemService;
import org.roko.erp.services.PaymentMethodService;
import org.roko.erp.services.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class TestDataInitialization implements ApplicationListener<ContextRefreshedEvent> {

  private BankAccountService bankAccountSvc;
  private PaymentMethodService paymentMethodSvc;
  private CustomerService customerService;
  private VendorService vendorService;
  private ItemService itemSvc;

  @Autowired
  public TestDataInitialization(BankAccountService bankAccountSvc, PaymentMethodService paymentMethodSvc,
      CustomerService customerService, VendorService vendorService, ItemService itemSvc) {
    this.bankAccountSvc = bankAccountSvc;
    this.paymentMethodSvc = paymentMethodSvc;
    this.customerService = customerService;
    this.vendorService = vendorService;
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

    // init vendors
    Vendor v1 = new Vendor();
    v1.setCode("VEND001");
    v1.setName("Vendor 01");
    v1.setAddress("Address 01");
    v1.setPaymentMethod(pm01);
    vendorService.create(v1);

    Vendor v2 = new Vendor();
    v2.setCode("VEND002");
    v2.setName("Vendor 02");
    v2.setAddress("Address 02");
    v2.setPaymentMethod(pm02);
    vendorService.create(v2);

    Vendor v3 = new Vendor();
    v3.setCode("VEND003");
    v3.setName("Vendor 03");
    v3.setAddress("Address 03");
    v3.setPaymentMethod(pm03);
    vendorService.create(v3);

    // init items
    initItems();
  }

  private void initItems() {
    for (int i = 0; i < 100; i++) {
      Item i1 = new Item();
      i1.setCode("ITEM" + i);
      i1.setName("Test item " + i);
      i1.setSalesPrice(1.0 * i);
      i1.setPurchasePrice(1.0 * i);
      itemSvc.create(i1);
    }
  }
}