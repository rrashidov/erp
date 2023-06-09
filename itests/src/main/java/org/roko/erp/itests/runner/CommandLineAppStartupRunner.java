package org.roko.erp.itests.runner;

import org.roko.erp.itests.runner.admin.CodeSerieTestRunner;
import org.roko.erp.itests.runner.admin.SetupTestRunner;
import org.roko.erp.itests.runner.generalledger.BankAccountTestRunner;
import org.roko.erp.itests.runner.generalledger.GeneralJournalBatchTestRunner;
import org.roko.erp.itests.runner.generalledger.PaymentMethodTestRunner;
import org.roko.erp.itests.runner.inventory.ItemTestRunner;
import org.roko.erp.itests.runner.sales.SalesOrderTestRunner;
import org.roko.erp.itests.runner.purchases.PurchaseCreditMemoTestRunner;
import org.roko.erp.itests.runner.purchases.PurchaseOrderTestRunner;
import org.roko.erp.itests.runner.purchases.VendorTestRunner;
import org.roko.erp.itests.runner.sales.CustomerTestRunner;
import org.roko.erp.itests.runner.sales.SalesCreditMemoTestRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    @Autowired
    private BankAccountTestRunner bankAccountTestRunner;

    @Autowired
    private PaymentMethodTestRunner paymentMethodTestRunner;

    @Autowired
    private GeneralJournalBatchTestRunner generalJournalBatchTestRunner;

    @Autowired
    private ItemTestRunner itemTestRunner;

    @Autowired
    private CustomerTestRunner customerTestRunner;

    @Autowired
    private SalesOrderTestRunner salesOrderTestRunner;

    @Autowired
    private SalesCreditMemoTestRunner salesCreditMemoTestRunner;

    @Autowired
    private VendorTestRunner vendorTestRunner;

    @Autowired
    private PurchaseOrderTestRunner purchaseOrderTestRunner;

    @Autowired
    private PurchaseCreditMemoTestRunner purchaseCreditMemoTestRunner;

    @Autowired
    private CodeSerieTestRunner codeSerieTestRunner;

    @Autowired
    private SetupTestRunner setupTestRunner;

    @Override
    public void run(String...args) throws Exception {
        try {
            codeSerieTestRunner.run();
            setupTestRunner.run();
            
            bankAccountTestRunner.run();
            paymentMethodTestRunner.run();
            generalJournalBatchTestRunner.run();

            itemTestRunner.run();

            customerTestRunner.run();
            salesOrderTestRunner.run();
            salesCreditMemoTestRunner.run();

            vendorTestRunner.run();
            purchaseOrderTestRunner.run();
            purchaseCreditMemoTestRunner.run();

            System.out.println("All OK!");
        } catch (ITestFailedException e) {
            System.out.println("Integration tests failed: " + e.getMessage());
            System.out.println();
        }
    }
}