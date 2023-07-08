package org.roko.erp.itests.runner.purchases;

import java.util.Date;

import org.roko.erp.dto.BankAccountDTO;
import org.roko.erp.dto.ItemDTO;
import org.roko.erp.dto.PostedPurchaseDocumentDTO;
import org.roko.erp.dto.PostedPurchaseDocumentLineDTO;
import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.dto.PurchaseDocumentLineDTO;
import org.roko.erp.dto.VendorDTO;
import org.roko.erp.dto.list.PostedPurchaseDocumentLineList;
import org.roko.erp.itests.clients.BankAccountClient;
import org.roko.erp.itests.clients.ItemClient;
import org.roko.erp.itests.clients.PostedPurchaseOrderClient;
import org.roko.erp.itests.clients.PurchaseOrderClient;
import org.roko.erp.itests.clients.PurchaseOrderLineClient;
import org.roko.erp.itests.clients.VendorClient;
import org.roko.erp.itests.runner.ITestFailedException;
import org.roko.erp.itests.runner.ITestRunner;
import org.roko.erp.itests.runner.util.BusinessLogicSetupUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostPurchaseOrderTestRunner implements ITestRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger("erp.itests");

    public static final int TEST_QUANTITY = 120;
    private static final int TEST_PRICE = 1;
    private static final int TEST_AMOUNT = 120;

    @Autowired
    private BusinessLogicSetupUtil util;

    @Autowired
    private PurchaseOrderClient purchaseOrderClient;

    @Autowired
    private PurchaseOrderLineClient purchaseOrderLineClient;

    @Autowired
    private PostedPurchaseOrderClient postedPurchaseOrderClient;

    @Autowired
    private VendorClient vendorClient;

    @Autowired
    private BankAccountClient bankAccountClient;

    @Autowired
    private ItemClient itemClient;

    @Override
    public void run() throws ITestFailedException {
        ensureNeededObjects();

        happyPathTest();

        delayedPaymentMethodTest();
    }

    private void delayedPaymentMethodTest() throws ITestFailedException {
        String code = createPurchaseOrderWithDelayedPaymentMethod();
        createPurchaseOrderLine(code, BusinessLogicSetupUtil.TEST_ITEM_CODE_2);

        LOGGER.info(String.format("Purchase Order %s created", code));

        triggerPurchaseOrderPost(code);

        LOGGER.info(String.format("Purchase Order %s triggered for posting", code));

        waitForPurchaseOrderToBePosted(code);

        LOGGER.info(String.format("Purchase Order %s posted", code));

        verifyVendorBalance(BusinessLogicSetupUtil.TEST_VENDOR_CODE_2, TEST_AMOUNT);

        LOGGER.info("Purchase Order post with delayed payment method test passed");
    }

    private String createPurchaseOrderWithDelayedPaymentMethod() {
        PurchaseDocumentDTO purchaseOrder = new PurchaseDocumentDTO();
        purchaseOrder.setVendorCode(BusinessLogicSetupUtil.TEST_VENDOR_CODE_2);
        purchaseOrder.setPaymentMethodCode(BusinessLogicSetupUtil.DELAYED_PAYMENT_METHOD_CODE);
        purchaseOrder.setDate(new Date());
        return purchaseOrderClient.create(purchaseOrder);
    }

    private void happyPathTest() throws ITestFailedException {
        String code = createPurchaseOrder();
        createPurchaseOrderLine(code, BusinessLogicSetupUtil.TEST_ITEM_CODE);

        LOGGER.info(String.format("Purchase Order %s created", code));

        triggerPurchaseOrderPost(code);

        LOGGER.info(String.format("Purchase Order %s triggered for posting", code));

        assertPurchaseOrderPostState(code);

        LOGGER.info(String.format("Purchase Order %s scheduled for posting", code));

        waitForPurchaseOrderToBePosted(code);

        LOGGER.info(String.format("Purchase Order %s posted", code));

        verifyPostedPurchaseOrder();

        verifyVendorBalance(BusinessLogicSetupUtil.TEST_VENDOR_CODE, 0);

        verifyBankAccountBalance();

        verifyItemInventory();

        LOGGER.info("Purchase Order post happy path test passed");
    }

    private void ensureNeededObjects() throws ITestFailedException {
        util.ensureSetup();
        util.ensureBankAccounts();
        util.ensurePaymentMethods();
        util.ensureVendor();
        util.ensureItems();
    }

    private void verifyItemInventory() throws ITestFailedException {
        ItemDTO item = itemClient.read(BusinessLogicSetupUtil.TEST_ITEM_CODE);

        if (item.getInventory() != TEST_QUANTITY) {
            throw new ITestFailedException(String.format("Item %s inventory issue: expected %f, got %f",
                    BusinessLogicSetupUtil.TEST_ITEM_CODE, TEST_QUANTITY, item.getInventory()));
        }

        LOGGER.info(String.format("Item %s inventory verified", BusinessLogicSetupUtil.TEST_ITEM_CODE));
    }

    private void verifyBankAccountBalance() throws ITestFailedException {
        BankAccountDTO bankAccount = bankAccountClient.read(BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE);

        double expectedBankAccountBalance = BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_BALANCE - TEST_AMOUNT;

        if (bankAccount.getBalance() != expectedBankAccountBalance) {
            throw new ITestFailedException(String.format("Bank Account %s balance issue: expected %f, got %f",
                    BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE, expectedBankAccountBalance,
                    bankAccount.getBalance()));
        }

        LOGGER.info(String.format("Bank Account %s verified", BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE));
    }

    private void verifyVendorBalance(String vendorCode, double expectedBalance) throws ITestFailedException {
        VendorDTO vendor = vendorClient.read(vendorCode);

        if (vendor.getBalance() != expectedBalance) {
            throw new ITestFailedException(String.format("Vendor %s balance issue: expected %f, got %f",
                    vendorCode, expectedBalance, vendor.getBalance()));
        }

        LOGGER.info(String.format("Vendor %s verified", vendorCode));
    }

    private void verifyPostedPurchaseOrder() throws ITestFailedException {
        // this is hack - we know what first code we put for the posted purchase order
        // code series, so we know what code will be generated when posting the
        // purchase order; this will have to be reconsidered for the future
        String code = "PPO001";
        PostedPurchaseDocumentDTO postedPurchaseOrder = postedPurchaseOrderClient.read(code);

        if (postedPurchaseOrder == null) {
            throw new ITestFailedException(String.format("Posted Purchase Order %s not found", code));
        }

        PostedPurchaseDocumentLineList postedPurchaseDocumentLineList = postedPurchaseOrderClient.readLines(code, 1);

        if (postedPurchaseDocumentLineList.getCount() != 1) {
            throw new ITestFailedException(
                    String.format("Posted Purchase Order %s line count issue: expected %s, got %s", code, 1,
                            postedPurchaseDocumentLineList.getCount()));
        }

        PostedPurchaseDocumentLineDTO postedPurchaseDocumentLine = postedPurchaseDocumentLineList.getData().get(0);

        if (!postedPurchaseDocumentLine.getItemCode().equals(BusinessLogicSetupUtil.TEST_ITEM_CODE)) {
            throw new ITestFailedException(String.format("Purchase Order %s line item issue: expected %s, got %s", code,
                    BusinessLogicSetupUtil.TEST_ITEM_CODE, postedPurchaseDocumentLine.getItemCode()));
        }

        if (postedPurchaseDocumentLine.getQuantity() != TEST_QUANTITY) {
            throw new ITestFailedException(String.format("Purchase Order %s line qty issue: expected %d, got %d", code,
                    TEST_QUANTITY, postedPurchaseDocumentLine.getQuantity()));
        }

        if (postedPurchaseDocumentLine.getPrice() != TEST_PRICE) {
            throw new ITestFailedException(
                    String.format("Purchase Order %s line price issue: expected %d, got %d", code,
                            TEST_PRICE, postedPurchaseDocumentLine.getPrice()));
        }

        if (postedPurchaseDocumentLine.getAmount() != TEST_AMOUNT) {
            throw new ITestFailedException(
                    String.format("Purchase Order %s line amount issue: expected %d, got %d", code,
                            TEST_AMOUNT, postedPurchaseDocumentLine.getAmount()));
        }

        LOGGER.info(String.format("Posted Purchase Order %s verified", code));
    }

    private void waitForPurchaseOrderToBePosted(String code) {
        while (purchaseOrderClient.read(code) != null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    private void assertPurchaseOrderPostState(String code) {
        PurchaseDocumentDTO purchaseOrder = purchaseOrderClient.read(code);

        assert (purchaseOrder.getPostStatus().equals("SCHEDULED"));
    }

    private void triggerPurchaseOrderPost(String code) {
        purchaseOrderClient.post(code);
    }

    private void createPurchaseOrderLine(String code, String itemCode) {
        PurchaseDocumentLineDTO purchaseOrderLine = new PurchaseDocumentLineDTO();
        purchaseOrderLine.setItemCode(itemCode);
        purchaseOrderLine.setQuantity(TEST_QUANTITY);
        purchaseOrderLine.setPrice(TEST_PRICE);
        purchaseOrderLine.setAmount(TEST_AMOUNT);

        purchaseOrderLineClient.create(code, purchaseOrderLine);
    }

    private String createPurchaseOrder() {
        PurchaseDocumentDTO purchaseOrder = new PurchaseDocumentDTO();
        purchaseOrder.setVendorCode(BusinessLogicSetupUtil.TEST_VENDOR_CODE);
        purchaseOrder.setPaymentMethodCode(BusinessLogicSetupUtil.TEST_PAYMENT_METHOD_CODE);
        purchaseOrder.setDate(new Date());
        return purchaseOrderClient.create(purchaseOrder);
    }

}
