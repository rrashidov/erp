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

    private static final int TEST_QUANTITY = 20;
    private static final int TEST_PRICE = 1;
    private static final int TEST_AMOUNT = 20;

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
        util.ensureSetup();
        util.ensureBankAccounts();
        util.ensurePaymentMethods();
        util.ensureVendor();
        util.ensureItems();

        String code = createPurchaseCreditMemo();
        createPurchaseCreditMemoLine(code);

        LOGGER.info(String.format("Purchase Credit Memo %s created", code));

        triggerPurchaseCreditMemoPost(code);

        LOGGER.info(String.format("Purchase Credit Memo %s triggered for posting", code));

        assertPurchaseCreditMemoPostStatus(code);

        LOGGER.info(String.format("Purchase Credit Memo %s scheduled for posting", code));

        waitForPurchaseCreditMemoToBePosted(code);

        LOGGER.info(String.format("Purchase Credit Memo %s posted", code));

        verifyPostedPurchaseCreditMemo();

        verifyVendorBalance();

        verifyBankAccountBalance();

        verifyItemInventory();

        LOGGER.info("Purchase Credit Memo post test passed");
    }

    private void verifyItemInventory() throws ITestFailedException {
        ItemDTO item = itemClient.read(BusinessLogicSetupUtil.TEST_ITEM_CODE);

        double expectedInventory = PostPurchaseOrderTestRunner.TEST_QUANTITY - TEST_QUANTITY;

        if (item.getInventory() != expectedInventory) {
            throw new ITestFailedException(String.format("Item %s inventory issue: expected %f, got %f",
                    BusinessLogicSetupUtil.TEST_ITEM_CODE, expectedInventory, item.getInventory()));
        }

        LOGGER.info(String.format("Item %s inventory verified", BusinessLogicSetupUtil.TEST_ITEM_CODE));
    }

    private void verifyBankAccountBalance() throws ITestFailedException {
        BankAccountDTO bankAccount = bankAccountClient.read(BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE);

        double expectedBankAccountBalance = BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_BALANCE + TEST_AMOUNT;

        if (bankAccount.getBalance() != expectedBankAccountBalance) {
            throw new ITestFailedException(String.format("Bank Account %s balance issue: expected %f, got %f",
                    BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE, expectedBankAccountBalance,
                    bankAccount.getBalance()));
        }

        LOGGER.info(String.format("Bank Account %s verified", BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE));
    }

    private void verifyVendorBalance() throws ITestFailedException {
        VendorDTO vendor = vendorClient.read(BusinessLogicSetupUtil.TEST_VENDOR_CODE);

        if (vendor.getBalance() != 0) {
            throw new ITestFailedException(String.format("Vendor %s balance issue: expected %f, got %f",
                    BusinessLogicSetupUtil.TEST_VENDOR_CODE, 0, vendor.getBalance()));
        }

        LOGGER.info(String.format("Vendor %s verified", BusinessLogicSetupUtil.TEST_VENDOR_CODE));
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

        if (postedPurchaseDocumentLine.getQuantity() != TEST_QUANTITY) {
            throw new ITestFailedException(String.format("Purchase Credit Memo %s line qty issue: expected %d, got %d", code,
                    TEST_QUANTITY, postedPurchaseDocumentLine.getQuantity()));
        }

        if (postedPurchaseDocumentLine.getPrice() != TEST_PRICE) {
            throw new ITestFailedException(
                    String.format("Purchase Credit Memo %s line price issue: expected %d, got %d", code,
                            TEST_PRICE, postedPurchaseDocumentLine.getPrice()));
        }

        if (postedPurchaseDocumentLine.getAmount() != TEST_AMOUNT) {
            throw new ITestFailedException(
                    String.format("Purchase Credit Memo %s line amount issue: expected %d, got %d", code,
                            TEST_AMOUNT, postedPurchaseDocumentLine.getAmount()));
        }

        LOGGER.info(String.format("Posted Purchase Credit Memo %s verified", code));
    }

    private void waitForPurchaseCreditMemoToBePosted(String code) {
        while (purchaseCreditMemoClient.read(code) != null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
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

    private void createPurchaseCreditMemoLine(String code) {
        PurchaseDocumentLineDTO purchaseCreditMemoLine = new PurchaseDocumentLineDTO();
        purchaseCreditMemoLine.setItemCode(BusinessLogicSetupUtil.TEST_ITEM_CODE);
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
