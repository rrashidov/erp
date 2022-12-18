package org.roko.erp.frontend.components;

import org.roko.erp.dto.BankAccountDTO;
import org.roko.erp.dto.ItemDTO;
import org.roko.erp.dto.PaymentMethodDTO;
import org.roko.erp.frontend.model.CodeSerie;
import org.roko.erp.frontend.model.Customer;
import org.roko.erp.frontend.model.GeneralJournalBatch;
import org.roko.erp.frontend.model.Setup;
import org.roko.erp.frontend.model.Vendor;
import org.roko.erp.frontend.services.BankAccountService;
import org.roko.erp.frontend.services.CodeSerieService;
import org.roko.erp.frontend.services.CustomerService;
import org.roko.erp.frontend.services.GeneralJournalBatchService;
import org.roko.erp.frontend.services.ItemService;
import org.roko.erp.frontend.services.PaymentMethodService;
import org.roko.erp.frontend.services.SetupService;
import org.roko.erp.frontend.services.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class TestDataInitialization implements ApplicationListener<ContextRefreshedEvent> {

  private BankAccountService bankAccountSvc;
  private PaymentMethodService paymentMethodSvc;
  private CustomerService customerService;
  private VendorService vendorService;
  private ItemService itemSvc;
  private GeneralJournalBatchService generalJournalBatchSvc;
  private CodeSerieService codeSerieSvc;
  private SetupService setupSvc;
  private Environment env;

  @Autowired
  public TestDataInitialization(BankAccountService bankAccountSvc, PaymentMethodService paymentMethodSvc,
      GeneralJournalBatchService generalJournalBatchSvc,
      CustomerService customerService, VendorService vendorService, ItemService itemSvc,
      CodeSerieService codeSerieSvc, SetupService setupSvc, Environment env) {
    this.bankAccountSvc = bankAccountSvc;
    this.paymentMethodSvc = paymentMethodSvc;
    this.generalJournalBatchSvc = generalJournalBatchSvc;
    this.customerService = customerService;
    this.vendorService = vendorService;
    this.itemSvc = itemSvc;
    this.codeSerieSvc = codeSerieSvc;
    this.setupSvc = setupSvc;
    this.env = env;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if (activeProfileSpecified()) {
      return ;
    }

    // init bank accounts
    BankAccountDTO ba01 = new BankAccountDTO();
    ba01.setCode("BA01");
    ba01.setName("Cash");
    bankAccountSvc.create(ba01);

    BankAccountDTO ba02 = new BankAccountDTO();
    ba02.setCode("BA02");
    ba02.setName("Bank");
    bankAccountSvc.create(ba02);

    initBulkBankAccounts();

    // init payment methods
    PaymentMethodDTO pm01 = new PaymentMethodDTO();
    pm01.setCode("PM01");
    pm01.setName("Delayed");
    paymentMethodSvc.create(pm01);

    PaymentMethodDTO pm02 = new PaymentMethodDTO();
    pm02.setCode("PM02");
    pm02.setName("Cash");
    //pm02.setBankAccount(ba01);
    paymentMethodSvc.create(pm02);

    PaymentMethodDTO pm03 = new PaymentMethodDTO();
    pm03.setCode("PM03");
    pm03.setName("By card");
    //pm03.setBankAccount(ba02);
    paymentMethodSvc.create(pm03);

    initBulkPaymentMethods();

    initBulkGeneralJournalBatches();

    // init customers
    Customer c1 = new Customer();
    c1.setCode("CUST01");
    c1.setName("Customer 01");
    c1.setAddress("Test address");
    //c1.setPaymentMethod(pm01);
    customerService.create(c1);

    Customer c2 = new Customer();
    c2.setCode("CUST02");
    c2.setName("Customer 02");
    c2.setAddress("Test address");
    //c2.setPaymentMethod(pm02);
    customerService.create(c2);

    Customer c3 = new Customer();
    c3.setCode("CUST03");
    c3.setName("Customer 03");
    c3.setAddress("Test address");
    //c3.setPaymentMethod(pm03);
    customerService.create(c3);

    //initBulkCustomers(pm03);

    // init vendors
    Vendor v1 = new Vendor();
    v1.setCode("VEND001");
    v1.setName("Vendor 01");
    v1.setAddress("Address 01");
    //v1.setPaymentMethod(pm01);
    vendorService.create(v1);

    Vendor v2 = new Vendor();
    v2.setCode("VEND002");
    v2.setName("Vendor 02");
    v2.setAddress("Address 02");
    //v2.setPaymentMethod(pm02);
    vendorService.create(v2);

    Vendor v3 = new Vendor();
    v3.setCode("VEND003");
    v3.setName("Vendor 03");
    v3.setAddress("Address 03");
    //v3.setPaymentMethod(pm03);
    vendorService.create(v3);

    //initBulkVendors(pm03);

    // init items
    initItems();

    // init code series
    initCodeSeries();
  }

  private boolean activeProfileSpecified() {
    return env.getActiveProfiles().length != 0;
  }

  private void initCodeSeries() {
    CodeSerie cs1 = new CodeSerie();
    cs1.setCode("CS01");
    cs1.setName("Sales Order");
    cs1.setFirstCode("SO000000");
    cs1.setLastCode("SO000000");
    codeSerieSvc.create(cs1);

    CodeSerie cs2 = new CodeSerie();
    cs2.setCode("CS02");
    cs2.setName("Sales Credit Memo");
    cs2.setFirstCode("SCM000000");
    cs2.setLastCode("SCM000000");
    codeSerieSvc.create(cs2);

    CodeSerie cs3 = new CodeSerie();
    cs3.setCode("CS03");
    cs3.setName("Posted Sales Order");
    cs3.setFirstCode("PSO000000");
    cs3.setLastCode("PSO000000");
    codeSerieSvc.create(cs3);

    CodeSerie cs4 = new CodeSerie();
    cs4.setCode("CS04");
    cs4.setName("Posted Sales Credit Memo");
    cs4.setFirstCode("PSCM000000");
    cs4.setLastCode("PSCM000000");
    codeSerieSvc.create(cs4);

    CodeSerie cs5 = new CodeSerie();
    cs5.setCode("CS05");
    cs5.setName("Purchase Order");
    cs5.setFirstCode("PO000000");
    cs5.setLastCode("PO000000");
    codeSerieSvc.create(cs5);

    CodeSerie cs6 = new CodeSerie();
    cs6.setCode("CS06");
    cs6.setName("Purchase Credit Memo");
    cs6.setFirstCode("PCM000000");
    cs6.setLastCode("PCM000000");
    codeSerieSvc.create(cs6);

    CodeSerie cs7 = new CodeSerie();
    cs7.setCode("CS07");
    cs7.setName("Posted Purchase Order");
    cs7.setFirstCode("PPO000000");
    cs7.setLastCode("PPO000000");
    codeSerieSvc.create(cs7);

    CodeSerie cs8 = new CodeSerie();
    cs8.setCode("CS08");
    cs8.setName("Posted Purchase Credit Memo");
    cs8.setFirstCode("PPCM000000");
    cs8.setLastCode("PPCM000000");
    codeSerieSvc.create(cs8);

    Setup setup = setupSvc.get();
    setup.setSalesOrderCodeSerie(cs1);
    setup.setSalesCreditMemoCodeSerie(cs2);
    setup.setPostedSalesOrderCodeSerie(cs3);
    setup.setPostedSalesCreditMemoCodeSerie(cs4);
    setup.setPurchaseOrderCodeSerie(cs5);
    setup.setPurchaseCreditMemoCodeSerie(cs6);
    setup.setPostedPurchaseOrderCodeSerie(cs7);
    setup.setPostedPurchaseCreditMemoCodeSerie(cs8);

    setupSvc.update(setup);
  }

  // private void initBulkVendors(PaymentMethod pm01) {
  //   for (int i = 0; i < 100; i++) {
  //     Vendor v1 = new Vendor();
  //     v1.setCode("VEND000" + i);
  //     v1.setName("Vendor " + i);
  //     v1.setAddress("Address " + i);
  //     v1.setPaymentMethod(pm01);
  //     vendorService.create(v1);
  //   }
  // }

  // private void initBulkCustomers(PaymentMethod pm01) {
  //   for (int i = 0; i < 100; i++) {
  //     Customer c1 = new Customer();
  //     c1.setCode("CUST00" + i);
  //     c1.setName("Customer " + i);
  //     c1.setAddress("Test address");
  //     c1.setPaymentMethod(pm01);
  //     customerService.create(c1);
  //   }
  // }

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
      PaymentMethodDTO pm01 = new PaymentMethodDTO();
      pm01.setCode("PM00" + i);
      pm01.setName("Payment Method " + i);
      paymentMethodSvc.create(pm01);
    }
  }

  private void initBulkBankAccounts() {
    for (int i = 0; i < 100; i++) {
      BankAccountDTO ba01 = new BankAccountDTO();
      ba01.setCode("BA00" + i);
      ba01.setName("Bank Account " + i);
      bankAccountSvc.create(ba01);
    }
  }

  private void initItems() {
    for (int i = 0; i < 100; i++) {
      ItemDTO i1 = new ItemDTO();
      i1.setCode("ITEM" + i);
      i1.setName("Test item " + i);
      i1.setSalesPrice(1.0 * i);
      i1.setPurchasePrice(1.0 * i);
      itemSvc.create(i1);
    }
  }
}