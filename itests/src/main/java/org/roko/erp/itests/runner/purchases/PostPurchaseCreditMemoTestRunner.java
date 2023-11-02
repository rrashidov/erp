package org.roko.erp.itests.runner.purchases;

import java.math.BigDecimal;
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
import org.roko.erp.itests.clients.PostedPurchaseCreditMemoClient;
import org.roko.erp.itests.clients.PurchaseCreditMemoClient;
import org.roko.erp.itests.clients.PurchaseCreditMemoLineClient;
import org.roko.erp.itests.clients.VendorClient;
import org.roko.erp.itests.runner.ITestFailedException;
import org.roko.erp.itests.runner.ITestRunner;
import org.roko.erp.itests.runner.util.BusinessLogicSetupUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostPurchaseCreditMemoTestRunner implements ITestRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger("erp.itests");

    private static final BigDecimal TEST_QUANTITY = new BigDecimal("20");
    private static final BigDecimal TEST_PRICE = new BigDecimal("1");
    private static final BigDecimal TEST_AMOUNT = new BigDecimal("20");

    private static final long POST_WAIT_TIMEOUT = 10 * 60 * 1000;

    @Autowired
    private BusinessLogicSetupUtil util;

    @Autowired
    private PurchaseCreditMemoClient purchaseCreditMemoClient;

    @Autowired
    private PurchaseCreditMemoLineClient purchaseCreditMemoLineClient;

    @Autowired
    private PostedPurchaseCreditMemoClient postedPurchaseCreditMemoClient;

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

        noInventoryItemTest();
    }

    private void noInventoryItemTest() throws ITestFailedException {
        String code = createPurchaseCreditMemo();
        createPurchaseCreditMemoLine(code, BusinessLogicSetupUtil.NO_INVENTORY_ITEM_CODE);

        LOGGER.info(String.format("Purchase Credit Memo %s created", code));

        triggerPurchaseCreditMemoPost(code);

        LOGGER.info(String.format("Purchase Credit Memo %s triggered for posting", code));

        verifyPurchaseCreditMemoPostFailed(code);

        LOGGER.info("Purchase Credit Memo post no inventory item test passed");
    }

    private void verifyPurchaseCreditMemoPostFailed(String code) throws ITestFailedException {
        String purchaseCreditMemoPostStatus = "";
        boolean timeoutReached = false;
        long start = System.currentTimeMillis();

        while ((!purchaseCreditMemoPostStatus.equals("FAILED")) && !timeoutReached) {
            purchaseCreditMemoPostStatus = purchaseCreditMemoClient.read(code).getPostStatus();

            timeoutReached = (System.currentTimeMillis() - start) > POST_WAIT_TIMEOUT;
        }

        if (timeoutReached) {
            throw new ITestFailedException(String.format("Purchase Credit Memo %s post status failed: expected %s, got %s",
                    code, "FAILED", purchaseCreditMemoPostStatus));
        }
    }

    private void delayedPaymentMethodTest() throws ITestFailedException {
        String code = createPurchaseCreditMemoWithDelayedPaymentMethod();
        createPurchaseCreditMemoLine(code, BusinessLogicSetupUtil.TEST_ITEM_CODE_2);

        BigDecimal initialVendorBalance = getVendorBalance(BusinessLogicSetupUtil.TEST_VENDOR_CODE_2);

        LOGGER.info(String.format("Purchase Credit Memo %s created", code));

        triggerPurchaseCreditMemoPost(code);

        LOGGER.info(String.format("Purchase Credit Memo %s triggered for posting", code));

        waitForPurchaseCreditMemoToBePosted(code);

        LOGGER.info(String.format("Purchase Credit Memo %s posted", code));

        verifyVendorBalance(BusinessLogicSetupUtil.TEST_VENDOR_CODE_2, initialVendorBalance.subtract(TEST_AMOUNT));

        LOGGER.info("Purchase Credit Memo post with delayed payment method test passed");
    }

    private BigDecimal getVendorBalance(String vendorCode) {
        return vendorClient.read(vendorCode).getBalance();
    }

    private String createPurchaseCreditMemoWithDelayedPaymentMethod() {
        PurchaseDocumentDTO purchaseCreditMemo = new PurchaseDocumentDTO();
        purchaseCreditMemo.setVendorCode(BusinessLogicSetupUtil.TEST_VENDOR_CODE_2);
        purchaseCreditMemo.setPaymentMethodCode(BusinessLogicSetupUtil.DELAYED_PAYMENT_METHOD_CODE);
        purchaseCreditMemo.setDate(new Date());
        return purchaseCreditMemoClient.create(purchaseCreditMemo);
    }

    private void happyPathTest() throws ITestFailedException {
        String code = createPurchaseCreditMemo();
        createPurchaseCreditMemoLine(code, BusinessLogicSetupUtil.TEST_ITEM_CODE);

        BigDecimal initialItemInventory = getItemInventory(BusinessLogicSetupUtil.TEST_ITEM_CODE);

        LOGGER.info(String.format("Purchase Credit Memo %s created", code));

        triggerPurchaseCreditMemoPost(code);

        LOGGER.info(String.format("Purchase Credit Memo %s triggered for posting", code));

        assertPurchaseCreditMemoPostStatus(code);

        LOGGER.info(String.format("Purchase Credit Memo %s scheduled for posting", code));

        waitForPurchaseCreditMemoToBePosted(code);

        LOGGER.info(String.format("Purchase Credit Memo %s posted", code));

        verifyPostedPurchaseCreditMemo();

        verifyVendorBalance(BusinessLogicSetupUtil.TEST_VENDOR_CODE, new BigDecimal(0));

        verifyBankAccountBalance();

        verifyItemInventory(initialItemInventory);

        LOGGER.info("Purchase Credit Memo post test passed");
    }

    private BigDecimal getItemInventory(String itemCode) {
        return itemClient.read(itemCode).getInventory();
    }

    private void ensureNeededObjects() throws ITestFailedException {
        util.ensureSetup();
        util.ensureBankAccounts();
        util.ensurePaymentMethods();
        util.ensureVendors();
        util.ensureItems();
    }

    private void verifyItemInventory(BigDecimal  initialItemInventory) throws ITestFailedException {
        ItemDTO item = itemClient.read(BusinessLogicSetupUtil.TEST_ITEM_CODE);

        BigDecimal expectedInventory = initialItemInventory.subtract(TEST_QUANTITY);

        if (item.getInventory().compareTo(expectedInventory) != 0) {
            throw new ITestFailedException(String.format("Item %s inventory issue: expected %f, got %f",
                    BusinessLogicSetupUtil.TEST_ITEM_CODE, expectedInventory, item.getInventory()));
        }

        LOGGER.info(String.format("Item %s inventory verified", BusinessLogicSetupUtil.TEST_ITEM_CODE));
    }

    private void verifyBankAccountBalance() throws ITestFailedException {
        BankAccountDTO bankAccount = bankAccountClient.read(BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE);

        BigDecimal expectedBankAccountBalance = BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_BALANCE.add(TEST_AMOUNT);

        if (bankAccount.getBalance().compareTo(expectedBankAccountBalance) != 0) {
            throw new ITestFailedException(String.format("Bank Account %s balance issue: expected %f, got %f",
                    BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE, expectedBankAccountBalance,
                    bankAccount.getBalance()));
        }

        LOGGER.info(String.format("Bank Account %s verified", BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE));
    }

    private void verifyVendorBalance(String vendorCode, BigDecimal expectedBalance) throws ITestFailedException {
        VendorDTO vendor = vendorClient.read(vendorCode);

        if (vendor.getBalance().compareTo(expectedBalance) != 0) {
            throw new ITestFailedException(String.format("Vendor %s balance issue: expected %f, got %f",
                    vendorCode, expectedBalance, vendor.getBalance()));
        }

        LOGGER.info(String.format("Vendor %s verified", vendorCode));
    }

    private void verifyPostedPurchaseCreditMemo() throws ITestFailedException {
        // this is hack - we know what first code we put for the posted purchase credit memo
        // code series, so we know what code will be generated when posting the
        // purchase credit memo; this will have to be reconsidered for the future
        String code = "PPCM001";

        PostedPurchaseDocumentDTO postedPurchaseCreditMemo = postedPurchaseCreditMemoClient.read(code);

        if (postedPurchaseCreditMemo == null){
            throw new ITestFailedException(String.format("Posted Purchase Credit Memo %s not found", code));
        }

        PostedPurchaseDocumentLineList postedPurchaseDocumentLineList = postedPurchaseCreditMemoClient.readLines(code, 1);

        if (postedPurchaseDocumentLineList.getCount() != 1) {
            throw new ITestFailedException(
                    String.format("Posted Purchase Credit Memo %s line count issue: expected %s, got %s", code, 1,
                            postedPurchaseDocumentLineList.getCount()));
        }

        PostedPurchaseDocumentLineDTO postedPurchaseDocumentLine = postedPurchaseDocumentLineList.getData().get(0);

        if (!postedPurchaseDocumentLine.getItemCode().equals(BusinessLogicSetupUtil.TEST_ITEM_CODE)) {
            throw new ITestFailedException(String.format("Purchase Credit Memo %s line item issue: expected %s, got %s", code,
                    BusinessLogicSetupUtil.TEST_ITEM_CODE, postedPurchaseDocumentLine.getItemCode()));
        }

        if (postedPurchaseDocumentLine.getQuantity().compareTo(TEST_QUANTITY) != 0) {
            throw new ITestFailedException(String.format("Purchase Credit Memo %s line qty issue: expected %f, got %f", code,
                    TEST_QUANTITY, postedPurchaseDocumentLine.getQuantity()));
        }

        if (postedPurchaseDocumentLine.getPrice().compareTo(TEST_PRICE) != 0) {
            throw new ITestFailedException(
                    String.format("Purchase Credit Memo %s line price issue: expected %f, got %f", code,
                            TEST_PRICE, postedPurchaseDocumentLine.getPrice()));
        }

        if (postedPurchaseDocumentLine.getAmount().compareTo(TEST_AMOUNT) != 0) {
            throw new ITestFailedException(
                    String.format("Purchase Credit Memo %s line amount issue: expected %f, got %f", code,
                            TEST_AMOUNT, postedPurchaseDocumentLine.getAmount()));
        }

        LOGGER.info(String.format("Posted Purchase Credit Memo %s verified", code));
    }

    private void waitForPurchaseCreditMemoToBePosted(String code) {
        while (purchaseCreditMemoClient.read(code) != null) {
        }
    }

    private void assertPurchaseCreditMemoPostStatus(String code) throws ITestFailedException {
        PurchaseDocumentDTO purchaseCreditMemo = purchaseCreditMemoClient.read(code);

        if (!purchaseCreditMemo.getPostStatus().equals("SCHEDULED")) {
            throw new ITestFailedException(
                    String.format("Purchase Credit Memo %s post status issue: expected %s, got %s", code, "SCHEDULED",
                            purchaseCreditMemo.getPostStatus()));
        }
    }

    private void triggerPurchaseCreditMemoPost(String code) {
        purchaseCreditMemoClient.post(code);
    }

    private void createPurchaseCreditMemoLine(String code, String itemCode) {
        PurchaseDocumentLineDTO purchaseCreditMemoLine = new PurchaseDocumentLineDTO();
        purchaseCreditMemoLine.setItemCode(itemCode);
        purchaseCreditMemoLine.setQuantity(TEST_QUANTITY);
        purchaseCreditMemoLine.setPrice(TEST_PRICE);
        purchaseCreditMemoLine.setAmount(TEST_AMOUNT);

        purchaseCreditMemoLineClient.create(code, purchaseCreditMemoLine);
    }

    private String createPurchaseCreditMemo() {
        PurchaseDocumentDTO purchaseCreditMemo = new PurchaseDocumentDTO();
        purchaseCreditMemo.setVendorCode(BusinessLogicSetupUtil.TEST_VENDOR_CODE);
        purchaseCreditMemo.setPaymentMethodCode(BusinessLogicSetupUtil.TEST_PAYMENT_METHOD_CODE);
        purchaseCreditMemo.setDate(new Date());
        return purchaseCreditMemoClient.create(purchaseCreditMemo);
    }
    
}
