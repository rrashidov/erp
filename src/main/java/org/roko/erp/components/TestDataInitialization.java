package org.roko.erp.components;

import org.roko.erp.model.BankAccount;
import org.roko.erp.model.Customer;
import org.roko.erp.model.GeneralJournalBatch;
import org.roko.erp.model.Item;
import org.roko.erp.model.PaymentMethod;
import org.roko.erp.model.Vendor;
import org.roko.erp.services.BankAccountService;
import org.roko.erp.services.CustomerService;
import org.roko.erp.services.GeneralJournalBatchService;
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
  private GeneralJournalBatchService generalJournalBatchSvc;

  @Autowired
  public TestDataInitialization(BankAccountService bankAccountSvc, PaymentMethodService paymentMethodSvc,
      GeneralJournalBatchService generalJournalBatchSvc,
      CustomerService customerService, VendorService vendorService, ItemService itemSvc) {
    this.bankAccountSvc = bankAccountSvc;
    this.paymentMethodSvc = paymentMethodSvc;
    this.generalJournalBatchSvc = generalJournalBatchSvc;
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

    initBulkBankAccounts();

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

    initBulkPaymentMethods();

    initBulkGeneralJournalBatches();

    // init customers
    Customer c1 = new Customer();
    c1.setCode("CUST01");
    c1.setName("Customer 01");
    c1.setAddress("Test address");
    c1.setPaymentMethod(pm01);
    customerService.create(c1);

    Customer c2 = new Customer();
    c2.setCode("CUST02");
    c2.setName("Customer 02");
    c2.setAddress("Test address");
    c2.setPaymentMethod(pm02);
    customerService.create(c2);

    Customer c3 = new Customer();
    c3.setCode("CUST03");
    c3.setName("Customer 03");
    c3.setAddress("Test address");
    c3.setPaymentMethod(pm03);
    customerService.create(c3);

    initBulkCustomers(pm03);

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

    initBulkVendors(pm03);

    // init items
    initItems();
  }

  private void initBulkVendors(PaymentMethod pm01) {
    for (int i = 0; i < 100; i++) {
      Vendor v1 = new Vendor();
      v1.setCode("VEND000" + i);
      v1.setName("Vendor " + i);
      v1.setAddress("Address " + i);
      v1.setPaymentMethod(pm01);
      vendorService.create(v1);  
    }
  }

  private void initBulkCustomers(PaymentMethod pm01) {
    for (int i = 0; i < 100; i++) {
      Customer c1 = new Customer();
      c1.setCode("CUST00" + i);
      c1.setName("Customer " + i);
      c1.setAddress("Test address");
      c1.setPaymentMethod(pm01);
      customerService.create(c1);  
    }
  }

  private void initBulkGeneralJournalBatches() {
    for (int i = 0; i < 110; i++) {
      GeneralJournalBatch genJournalBatch = new GeneralJournalBatch();
      genJournalBatch.setCode("GEN" + i);
      genJournalBatch.setName("General Journal Batch " + i);
      
      generalJournalBatchSvc.create(genJournalBatch);
    }
  }

  private void initBulkPaymentMethods() {
    for (int i = 0; i < 100; i++) {
      PaymentMethod pm01 = new PaymentMethod();
      pm01.setCode("PM00" + i);
      pm01.setName("Payment Method " + i);
      paymentMethodSvc.create(pm01);
    }
  }

  private void initBulkBankAccounts() {
    for (int i = 0; i < 100; i++) {
      BankAccount ba01 = new BankAccount();
      ba01.setCode("BA00" + i);
      ba01.setName("Bank Account " + i);
      bankAccountSvc.create(ba01);
    }
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