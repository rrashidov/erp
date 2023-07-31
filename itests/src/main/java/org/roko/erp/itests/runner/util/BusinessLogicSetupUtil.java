package org.roko.erp.itests.runner.util;

import java.util.Date;

import org.roko.erp.dto.BankAccountDTO;
import org.roko.erp.dto.CodeSerieDTO;
import org.roko.erp.dto.CustomerDTO;
import org.roko.erp.dto.GeneralJournalBatchDTO;
import org.roko.erp.dto.GeneralJournalBatchLineDTO;
import org.roko.erp.dto.GeneralJournalBatchLineOperationType;
import org.roko.erp.dto.GeneralJournalBatchLineType;
import org.roko.erp.dto.ItemDTO;
import org.roko.erp.dto.PaymentMethodDTO;
import org.roko.erp.dto.SetupDTO;
import org.roko.erp.dto.VendorDTO;
import org.roko.erp.itests.clients.BankAccountClient;
import org.roko.erp.itests.clients.CodeSerieClient;
import org.roko.erp.itests.clients.CustomerClient;
import org.roko.erp.itests.clients.GeneralJournalBatchClient;
import org.roko.erp.itests.clients.GeneralJournalBatchLineClient;
import org.roko.erp.itests.clients.ItemClient;
import org.roko.erp.itests.clients.PaymentMethodClient;
import org.roko.erp.itests.clients.SetupClient;
import org.roko.erp.itests.clients.VendorClient;
import org.roko.erp.itests.runner.ITestFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessLogicSetupUtil {

    public static final String TEST_VENDOR_CODE = "VEND01";
    private static final String TEST_VENDOR_NAME = "test-vendor-name";
    private static final String TEST_VENDOR_ADDRESS = "test-vendor-address";

    public static final String TEST_VENDOR_CODE_2 = "VEND02";
    private static final String TEST_VENDOR_NAME_2 = "test-vendor-name-2";
    private static final String TEST_VENDOR_ADDRESS_2 = "test-vendor-address-2";

    public static final String TEST_CUSTOMER_CODE = "CUST01";
    private static final String TEST_CUSTOMER_NAME = "test-customer-name";
    private static final String TEST_CUSTOMER_ADDRESS = "test-customer-address";

    public static final String TEST_CUSTOMER_CODE_2 = "CUST02";
    private static final String TEST_CUSTOMER_NAME_2 = "test-customer-name-2";
    private static final String TEST_CUSTOMER_ADDRESS_2 = "test-customer-address-2";

    public static final String TEST_PAYMENT_METHOD_CODE = "PM01";
    private static final String TEST_PAYMENT_METHOD_NAME = "test-payment-method-name";

    public static final String NO_BALANCE_PAYMENT_METHOD_CODE = "PM02";
    private static final String NO_BALANCE_PAYMENT_METHOD_NAME = "no-balance-payment-method-name";

    public static final String DELAYED_PAYMENT_METHOD_CODE = "PM03";
    private static final String DELAYED_PAYMENT_METHOD_NAME = "delayed-payment-method-name";

    public static final String TEST_GENERAP_JOURNAL_BATCH_CODE = "GEN001";
    private static final String TEST_GENERAL_JOURNAL_BATCH_NAME = "test-general-journal-batch-name";

    public static final String TEST_BANK_ACCOUNT_CODE = "BA001";
    private static final String TEST_BANK_ACCOUNT_NAME = "test-bank-account-name";
    public static final double TEST_BANK_ACCOUNT_BALANCE = 1000.0;

    public static final String TEST_BANK_ACCOUNT_CODE_2 = "BA002";
    private static final String TEST_BANK_ACCOUNT_NAME_2 = "test-bank-account-name-2";

    public static final String NO_BALANCE_BANK_ACCOUNT_CODE = "BA003";
    private static final String NO_BALANCE_BANK_ACCOUNT_NAME = "no-balance-bank-account-name";

    public static final String TEST_ITEM_CODE = "ITEM001";
    public static final String TEST_ITEM_CODE_2 = "ITEM002";
    private static final String TEST_ITEM_NAME = "test-item-name";
    private static final String TEST_ITEM_NAME_2 = "test-item-name-2";
    private static final double TEST_ITEM_PRICE = 1.0;

    public static final String NO_INVENTORY_ITEM_CODE = "ITEM003";
    private static final String NO_INVENTORY_ITEM_NAME = "no-iventory-item-name";

    private static final String SO_CS_CODE = "CS01";
    private static final String SO_CS_NAME = "Sales Orders";
    private static final String SO_CS_LAST_CODE = "SO000";

    private static final String SCM_CS_CODE = "CS02";
    private static final String SCM_CS_NAME = "Sales Credit Memo";
    private static final String SCM_CS_LAST_CODE = "SCM000";

    private static final String PSO_CS_CODE = "CS03";
    private static final String PSO_CS_NAME = "Posted Sales Order";
    private static final String PSO_CS_LAST_CODE = "PSO000";

    private static final String PSCM_CS_CODE = "CS04";
    private static final String PSCM_CS_NAME = "Posted Sales Credit Memo";
    private static final String PSCM_CS_LAST_CODE = "PSCM000";

    private static final String PO_CS_CODE = "CS05";
    private static final String PO_CS_NAME = "Purchase Order";
    private static final String PO_CS_LAST_CODE = "PO000";

    private static final String PCM_CS_CODE = "CS06";
    private static final String PCM_CS_NAME = "Purchase Credit Memo";
    private static final String PCM_CS_LAST_CODE = "PCM000";

    private static final String PPO_CS_CODE = "CS07";
    private static final String PPO_CS_NAME = "Posted Purchase Order";
    private static final String PPO_CS_LAST_CODE = "PPO000";

    private static final String PPCM_CS_CODE = "CS08";
    private static final String PPCM_CS_NAME = "Posted Purchase Credit Memo";
    private static final String PPCM_CS_LAST_CODE = "PPCM000";

    @Autowired
    private CodeSerieClient codeSerieClient;

    @Autowired
    private SetupClient setupClient;

    @Autowired
    private ItemClient itemClient;

    @Autowired
    private BankAccountClient bankAccountClient;

    @Autowired
    private GeneralJournalBatchClient generalJournalBatchClient;

    @Autowired
    private GeneralJournalBatchLineClient generalJournalBatchLineClient;

    @Autowired
    private PaymentMethodClient paymentMethodClient;

    @Autowired
    private CustomerClient customerClient;

    @Autowired
    private VendorClient vendorClient;

    public void ensureVendors() {
        VendorDTO vendor = vendorClient.read(TEST_VENDOR_CODE);
        if (vendor == null) {
            vendor = new VendorDTO();
            vendor.setCode(TEST_VENDOR_CODE);
            vendor.setName(TEST_VENDOR_NAME);
            vendor.setAddress(TEST_VENDOR_ADDRESS);
            vendor.setPaymentMethodCode(TEST_PAYMENT_METHOD_CODE);
            vendorClient.create(vendor);
        }

        vendor = vendorClient.read(TEST_VENDOR_CODE_2);
        if (vendor == null) {
            vendor = new VendorDTO();
            vendor.setCode(TEST_VENDOR_CODE_2);
            vendor.setName(TEST_VENDOR_NAME_2);
            vendor.setAddress(TEST_VENDOR_ADDRESS_2);
            vendor.setPaymentMethodCode(TEST_PAYMENT_METHOD_CODE);
            vendorClient.create(vendor);
        }
    }

    public void ensureCustomer() {
        CustomerDTO customer = customerClient.read(TEST_CUSTOMER_CODE);
        if (customer == null) {
            customer = new CustomerDTO();
            customer.setCode(TEST_CUSTOMER_CODE);
            customer.setName(TEST_CUSTOMER_NAME);
            customer.setAddress(TEST_CUSTOMER_ADDRESS);
            customer.setPaymentMethodCode(TEST_PAYMENT_METHOD_CODE);
            customerClient.create(customer);
        }

        customer = customerClient.read(TEST_CUSTOMER_CODE_2);
        if (customer == null) {
            customer = new CustomerDTO();
            customer.setCode(TEST_CUSTOMER_CODE_2);
            customer.setName(TEST_CUSTOMER_NAME_2);
            customer.setAddress(TEST_CUSTOMER_ADDRESS_2);
            customer.setPaymentMethodCode(DELAYED_PAYMENT_METHOD_CODE);
            customerClient.create(customer);
        }
    }

    public void ensurePaymentMethods() {
        PaymentMethodDTO paymentMethod = paymentMethodClient.read(TEST_PAYMENT_METHOD_CODE);
        if (paymentMethod == null) {
            paymentMethod = new PaymentMethodDTO();
            paymentMethod.setCode(TEST_PAYMENT_METHOD_CODE);
            paymentMethod.setName(TEST_PAYMENT_METHOD_NAME);
            paymentMethod.setBankAccountCode(TEST_BANK_ACCOUNT_CODE);
            paymentMethodClient.create(paymentMethod);
        }

        paymentMethod = paymentMethodClient.read(NO_BALANCE_PAYMENT_METHOD_CODE);
        if (paymentMethod == null) {
            paymentMethod = new PaymentMethodDTO();
            paymentMethod.setCode(NO_BALANCE_PAYMENT_METHOD_CODE);
            paymentMethod.setName(NO_BALANCE_PAYMENT_METHOD_NAME);
            paymentMethod.setBankAccountCode(NO_BALANCE_BANK_ACCOUNT_CODE);
            paymentMethodClient.create(paymentMethod);
        }

        paymentMethod = paymentMethodClient.read(DELAYED_PAYMENT_METHOD_CODE);
        if (paymentMethod == null) {
            paymentMethod = new PaymentMethodDTO();
            paymentMethod.setCode(DELAYED_PAYMENT_METHOD_CODE);
            paymentMethod.setName(DELAYED_PAYMENT_METHOD_NAME);
            paymentMethodClient.create(paymentMethod);
        }
    }

    public void ensureBankAccounts() {
        BankAccountDTO bankAccount = bankAccountClient.read(TEST_BANK_ACCOUNT_CODE);
        if (bankAccount == null) {
            bankAccount = new BankAccountDTO();
            bankAccount.setCode(TEST_BANK_ACCOUNT_CODE);
            bankAccount.setName(TEST_BANK_ACCOUNT_NAME);
            bankAccountClient.create(bankAccount);
        }

        ensureBankAccountBalance();

        bankAccount = bankAccountClient.read(TEST_BANK_ACCOUNT_CODE_2);
        if (bankAccount == null) {
            bankAccount = new BankAccountDTO();
            bankAccount.setCode(TEST_BANK_ACCOUNT_CODE_2);
            bankAccount.setName(TEST_BANK_ACCOUNT_NAME_2);
            bankAccountClient.create(bankAccount);
        }

        bankAccount = bankAccountClient.read(NO_BALANCE_BANK_ACCOUNT_CODE);
        if (bankAccount == null) {
            bankAccount = new BankAccountDTO();
            bankAccount.setCode(NO_BALANCE_BANK_ACCOUNT_CODE);
            bankAccount.setName(NO_BALANCE_BANK_ACCOUNT_NAME);
            bankAccountClient.create(bankAccount);
        }
    }

    private void ensureBankAccountBalance() {
        BankAccountDTO bankAccount = bankAccountClient.read(TEST_BANK_ACCOUNT_CODE);

        double balanceDifference = TEST_BANK_ACCOUNT_BALANCE - bankAccount.getBalance();

        GeneralJournalBatchDTO generalJournalBatch = generalJournalBatchClient.read(TEST_GENERAP_JOURNAL_BATCH_CODE);
        if (generalJournalBatch == null) {
            generalJournalBatch = new GeneralJournalBatchDTO();
            generalJournalBatch.setCode(TEST_GENERAP_JOURNAL_BATCH_CODE);
            generalJournalBatch.setName(TEST_GENERAL_JOURNAL_BATCH_NAME);
            generalJournalBatchClient.create(generalJournalBatch);
        }

        GeneralJournalBatchLineDTO generalJournalBatchLine = new GeneralJournalBatchLineDTO();
        generalJournalBatchLine.setType(GeneralJournalBatchLineType.BANK_ACCOUNT);
        generalJournalBatchLine.setCode(TEST_BANK_ACCOUNT_CODE);
        generalJournalBatchLine.setOperationType(GeneralJournalBatchLineOperationType.EMPTY);
        generalJournalBatchLine.setDate(new Date());
        generalJournalBatchLine.setDocumentCode("0");
        generalJournalBatchLine.setAmount(balanceDifference);
        generalJournalBatchLineClient.create(TEST_GENERAP_JOURNAL_BATCH_CODE, generalJournalBatchLine);

        generalJournalBatchClient.post(TEST_GENERAP_JOURNAL_BATCH_CODE);

        waitGeneralJournalBatchPosted();
    }

    public void waitGeneralJournalBatchPosted() {
        GeneralJournalBatchDTO generalJournalBatch = generalJournalBatchClient.read(TEST_GENERAP_JOURNAL_BATCH_CODE);

        while (!generalJournalBatch.getPostStatus().equals("READY")) {
            waitASecond();
            generalJournalBatch = generalJournalBatchClient.read(TEST_GENERAP_JOURNAL_BATCH_CODE);
        }
    }

    public void ensureItems(){
        ItemDTO item = itemClient.read(TEST_ITEM_CODE);
        if (item == null) {
            item = new ItemDTO();
            item.setCode(TEST_ITEM_CODE);
            item.setName(TEST_ITEM_NAME);
            item.setPurchasePrice(TEST_ITEM_PRICE);
            item.setSalesPrice(TEST_ITEM_PRICE);
            itemClient.create(item);
        }

        item = itemClient.read(TEST_ITEM_CODE_2);
        if (item == null) {
            item = new ItemDTO();
            item.setCode(TEST_ITEM_CODE_2);
            item.setName(TEST_ITEM_NAME_2);
            item.setPurchasePrice(TEST_ITEM_PRICE);
            item.setSalesPrice(TEST_ITEM_PRICE);
            itemClient.create(item);
        }

        item = itemClient.read(NO_INVENTORY_ITEM_CODE);
        if (item == null) {
            item = new ItemDTO();
            item.setCode(NO_INVENTORY_ITEM_CODE);
            item.setName(NO_INVENTORY_ITEM_NAME);
            item.setPurchasePrice(TEST_ITEM_PRICE);
            item.setSalesPrice(TEST_ITEM_PRICE);
            itemClient.create(item);
        }
    }
    
    public void ensureSetup() throws ITestFailedException {
        ensureCodeSeries();

        SetupDTO setup = setupClient.read();
        setup.setSalesOrderCodeSerieCode(SO_CS_CODE);
        setup.setSalesCreditMemoCodeSerieCode(SCM_CS_CODE);
        setup.setPostedSalesOrderCodeSerieCode(PSO_CS_CODE);
        setup.setPostedSalesCreditMemoCodeSerieCode(PSCM_CS_CODE);
        setup.setPurchaseOrderCodeSerieCode(PO_CS_CODE);
        setup.setPurchaseCreditMemoCodeSerieCode(PCM_CS_CODE);
        setup.setPostedPurchaseOrderCodeSerieCode(PPO_CS_CODE);
        setup.setPostedPurchaseCreditMemoCodeSerieCode(PPCM_CS_CODE);
        setupClient.update(setup);
    }

    private void ensureCodeSeries() {
        ensureSalesOrderCodeSerie();
        ensureSalesCreditMemoCodeSerie();
        ensurePostedSalesOrderCodeSerie();
        ensurePostedSalesCreditMemoCodeSerie();

        ensurePurchaseOrderCodeSerie();
        ensurePurchaseCreditMemoCodeSerie();
        ensurePostedPurchaseOrderCodeSerie();
        ensurePostedPurchaseCreditMemoCodeSerie();
    }

    private void ensurePostedPurchaseCreditMemoCodeSerie() {
        CodeSerieDTO codeSerie = codeSerieClient.read(PPCM_CS_CODE);
        if (codeSerie == null) {
            codeSerie = new CodeSerieDTO();
            codeSerie.setCode(PPCM_CS_CODE);
            codeSerie.setName(PPCM_CS_NAME);
            codeSerie.setFirstCode(PPCM_CS_LAST_CODE);
            codeSerie.setLastCode(PPCM_CS_LAST_CODE);
            codeSerieClient.create(codeSerie);
        }
    }

    private void ensurePostedPurchaseOrderCodeSerie() {
        CodeSerieDTO codeSerie = codeSerieClient.read(PPO_CS_CODE);
        if (codeSerie == null) {
            codeSerie = new CodeSerieDTO();
            codeSerie.setCode(PPO_CS_CODE);
            codeSerie.setName(PPO_CS_NAME);
            codeSerie.setFirstCode(PPO_CS_LAST_CODE);
            codeSerie.setLastCode(PPO_CS_LAST_CODE);
            codeSerieClient.create(codeSerie);
        }
    }

    private void ensurePurchaseCreditMemoCodeSerie() {
        CodeSerieDTO codeSerie = codeSerieClient.read(PCM_CS_CODE);
        if (codeSerie == null) {
            codeSerie = new CodeSerieDTO();
            codeSerie.setCode(PCM_CS_CODE);
            codeSerie.setName(PCM_CS_NAME);
            codeSerie.setFirstCode(PCM_CS_LAST_CODE);
            codeSerie.setLastCode(PCM_CS_LAST_CODE);
            codeSerieClient.create(codeSerie);
        }
    }

    private void ensurePurchaseOrderCodeSerie() {
        CodeSerieDTO codeSerie = codeSerieClient.read(PO_CS_CODE);
        if (codeSerie == null) {
            codeSerie = new CodeSerieDTO();
            codeSerie.setCode(PO_CS_CODE);
            codeSerie.setName(PO_CS_NAME);
            codeSerie.setFirstCode(PO_CS_LAST_CODE);
            codeSerie.setLastCode(PO_CS_LAST_CODE);
            codeSerieClient.create(codeSerie);
        }
    }

    private void ensurePostedSalesCreditMemoCodeSerie() {
        CodeSerieDTO codeSerie = codeSerieClient.read(PSCM_CS_CODE);
        if (codeSerie == null) {
            codeSerie = new CodeSerieDTO();
            codeSerie.setCode(PSCM_CS_CODE);
            codeSerie.setName(PSCM_CS_NAME);
            codeSerie.setFirstCode(PSCM_CS_LAST_CODE);
            codeSerie.setLastCode(PSCM_CS_LAST_CODE);
            codeSerieClient.create(codeSerie);
        }
    }

    private void ensurePostedSalesOrderCodeSerie() {
        CodeSerieDTO codeSerie = codeSerieClient.read(PSO_CS_CODE);
        if (codeSerie == null) {
            codeSerie = new CodeSerieDTO();
            codeSerie.setCode(PSO_CS_CODE);
            codeSerie.setName(PSO_CS_NAME);
            codeSerie.setFirstCode(PSO_CS_LAST_CODE);
            codeSerie.setLastCode(PSO_CS_LAST_CODE);
            codeSerieClient.create(codeSerie);
        }
    }

    private void ensureSalesCreditMemoCodeSerie() {
        CodeSerieDTO codeSerie = codeSerieClient.read(SCM_CS_CODE);
        if (codeSerie == null) {
            codeSerie = new CodeSerieDTO();
            codeSerie.setCode(SCM_CS_CODE);
            codeSerie.setName(SCM_CS_NAME);
            codeSerie.setFirstCode(SCM_CS_LAST_CODE);
            codeSerie.setLastCode(SCM_CS_LAST_CODE);
            codeSerieClient.create(codeSerie);
        }
    }

    private void ensureSalesOrderCodeSerie() {
        CodeSerieDTO codeSerie = codeSerieClient.read(SO_CS_CODE);
        if (codeSerie == null) {
            codeSerie = new CodeSerieDTO();
            codeSerie.setCode(SO_CS_CODE);
            codeSerie.setName(SO_CS_NAME);
            codeSerie.setFirstCode(SO_CS_LAST_CODE);
            codeSerie.setLastCode(SO_CS_LAST_CODE);
            codeSerieClient.create(codeSerie);
        }
    }

    private void waitASecond() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
