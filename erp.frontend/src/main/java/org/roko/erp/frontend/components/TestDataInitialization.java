package org.roko.erp.frontend.components;

import org.roko.erp.dto.BankAccountDTO;
import org.roko.erp.dto.CodeSerieDTO;
import org.roko.erp.dto.CustomerDTO;
import org.roko.erp.dto.GeneralJournalBatchDTO;
import org.roko.erp.dto.ItemDTO;
import org.roko.erp.dto.PaymentMethodDTO;
import org.roko.erp.dto.SetupDTO;
import org.roko.erp.dto.VendorDTO;
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
    pm02.setBankAccountCode("BA01");
    paymentMethodSvc.create(pm02);

    PaymentMethodDTO pm03 = new PaymentMethodDTO();
    pm03.setCode("PM03");
    pm03.setName("By card");
    pm03.setBankAccountCode("BA02");
    paymentMethodSvc.create(pm03);

    initBulkPaymentMethods();

    initBulkGeneralJournalBatches();

    // init customers
    CustomerDTO c1 = new CustomerDTO();
    c1.setCode("CUST01");
    c1.setName("Customer 01");
    c1.setAddress("Test address");
    c1.setPaymentMethodCode("PM01");
    customerService.create(c1);

    CustomerDTO c2 = new CustomerDTO();
    c2.setCode("CUST02");
    c2.setName("Customer 02");
    c2.setAddress("Test address");
    c2.setPaymentMethodCode("PM02");
    customerService.create(c2);

    CustomerDTO c3 = new CustomerDTO();
    c3.setCode("CUST03");
    c3.setName("Customer 03");
    c3.setAddress("Test address");
    c3.setPaymentMethodCode("PM03");
    customerService.create(c3);

    initBulkCustomers("PM03");

    // init vendors
    VendorDTO v1 = new VendorDTO();
    v1.setCode("VEND001");
    v1.setName("Vendor 01");
    v1.setAddress("Address 01");
    v1.setPaymentMethodCode("PM01");
    vendorService.create(v1);

    VendorDTO v2 = new VendorDTO();
    v2.setCode("VEND002");
    v2.setName("Vendor 02");
    v2.setAddress("Address 02");
    v2.setPaymentMethodCode("PM02");
    vendorService.create(v2);

    VendorDTO v3 = new VendorDTO();
    v3.setCode("VEND003");
    v3.setName("Vendor 03");
    v3.setAddress("Address 03");
    v3.setPaymentMethodCode("PM03");
    vendorService.create(v3);

    initBulkVendors();

    // init items
    initItems();

    // init code series
    initCodeSeries();
  }

  private boolean activeProfileSpecified() {
    return env.getActiveProfiles().length != 0;
  }

  private void initCodeSeries() {
    CodeSerieDTO cs1 = new CodeSerieDTO();
    cs1.setCode("CS01");
    cs1.setName("Sales Order");
    cs1.setFirstCode("BSO000000");
    cs1.setLastCode("BSO000000");
    codeSerieSvc.create(cs1);

    CodeSerieDTO cs2 = new CodeSerieDTO();
    cs2.setCode("CS02");
    cs2.setName("Sales Credit Memo");
    cs2.setFirstCode("SCM000000");
    cs2.setLastCode("SCM000000");
    codeSerieSvc.create(cs2);

    CodeSerieDTO cs3 = new CodeSerieDTO();
    cs3.setCode("CS03");
    cs3.setName("Posted Sales Order");
    cs3.setFirstCode("PSO000000");
    cs3.setLastCode("PSO000000");
    codeSerieSvc.create(cs3);

    CodeSerieDTO cs4 = new CodeSerieDTO();
    cs4.setCode("CS04");
    cs4.setName("Posted Sales Credit Memo");
    cs4.setFirstCode("PSCM000000");
    cs4.setLastCode("PSCM000000");
    codeSerieSvc.create(cs4);

    CodeSerieDTO cs5 = new CodeSerieDTO();
    cs5.setCode("CS05");
    cs5.setName("Purchase Order");
    cs5.setFirstCode("PO000000");
    cs5.setLastCode("PO000000");
    codeSerieSvc.create(cs5);

    CodeSerieDTO cs6 = new CodeSerieDTO();
    cs6.setCode("CS06");
    cs6.setName("Purchase Credit Memo");
    cs6.setFirstCode("PCM000000");
    cs6.setLastCode("PCM000000");
    codeSerieSvc.create(cs6);

    CodeSerieDTO cs7 = new CodeSerieDTO();
    cs7.setCode("CS07");
    cs7.setName("Posted Purchase Order");
    cs7.setFirstCode("PPO000000");
    cs7.setLastCode("PPO000000");
    codeSerieSvc.create(cs7);

    CodeSerieDTO cs8 = new CodeSerieDTO();
    cs8.setCode("CS08");
    cs8.setName("Posted Purchase Credit Memo");
    cs8.setFirstCode("PPCM000000");
    cs8.setLastCode("PPCM000000");
    codeSerieSvc.create(cs8);

    SetupDTO setup = setupSvc.get();
    setup.setSalesOrderCodeSerieCode("CS01");
    setup.setSalesCreditMemoCodeSerieCode("CS02");
    setup.setPostedSalesOrderCodeSerieCode("CS03");
    setup.setPostedSalesCreditMemoCodeSerieCode("CS04");
    setup.setPurchaseOrderCodeSerieCode("CS05");
    setup.setPurchaseCreditMemoCodeSerieCode("CS06");
    setup.setPostedPurchaseOrderCodeSerieCode("CS07");
    setup.setPostedPurchaseCreditMemoCodeSerieCode("CS08");

    setupSvc.update(setup);
  }

  private void initBulkVendors() {
    for (int i = 0; i < 100; i++) {
      VendorDTO v1 = new VendorDTO();
      v1.setCode("VEND000" + i);
      v1.setName("Vendor " + i);
      v1.setAddress("Address " + i);
      v1.setPaymentMethodCode("PM01");
      vendorService.create(v1);
    }
  }

  private void initBulkCustomers(String pmCode) {
    for (int i = 0; i < 100; i++) {
      CustomerDTO c1 = new CustomerDTO();
      c1.setCode("CUST00" + i);
      c1.setName("Customer " + i);
      c1.setAddress("Test address");
      c1.setPaymentMethodCode(pmCode);
      customerService.create(c1);
    }
  }

  private void initBulkGeneralJournalBatches() {
    for (int i = 0; i < 110; i++) {
      GeneralJournalBatchDTO genJournalBatch = new GeneralJournalBatchDTO();
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