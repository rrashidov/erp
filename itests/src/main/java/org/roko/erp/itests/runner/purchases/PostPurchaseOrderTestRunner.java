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

    private static final int TEST_QUANTITY = 120;
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
        util.ensureSetup();
        util.ensureBankAccounts();
        util.ensurePaymentMethods();
        util.ensureVendor();
        util.ensureItems();

        String code = createPurchaseOrder();
        createPurchaseOrderLine(code);

        LOGGER.info(String.format("Purchase Order %s created", code));

        triggerPurchaseOrderPost(code);

        LOGGER.info(String.format("Purchase Order %s triggered for posting", code));

        assertPurchaseOrderPostState(code);

        LOGGER.info(String.format("Purchase Order %s scheduled for posting", code));

        waitForPurchaseOrderToBePosted(code);

        LOGGER.info(String.format("Purchase Order %s posted", code));

        verifyPostedPurchaseOrder();

        verifyVendorBalance();

        verifyBankAccountBalance();

        verifyItemInventory();
    }

    private void verifyItemInventory() throws ITestFailedException {
        ItemDTO item = itemClient.read(BusinessLogicSetupUtil.TEST_ITEM_CODE);

        if (item.getInventory() != TEST_QUANTITY) {
            throw new ITestFailedException(String.format("Item %s inventory issue: expected %d, got %d",
                    BusinessLogicSetupUtil.TEST_ITEM_CODE, TEST_QUANTITY, item.getInventory()));
        }

        LOGGER.info(String.format("Item %s inventory verified", BusinessLogicSetupUtil.TEST_ITEM_CODE));
    }

    private void verifyBankAccountBalance() throws ITestFailedException {
        BankAccountDTO bankAccount = bankAccountClient.read(BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE);

        double expectedBankAccountBalance = BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_BALANCE - TEST_AMOUNT;

        if (bankAccount.getBalance() != expectedBankAccountBalance) {
            throw new ITestFailedException(String.format("Bank Account %s balance issue: expected %d, got %d",
                    BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE, expectedBankAccountBalance,
                    bankAccount.getBalance()));
        }

        LOGGER.info(String.format("Bank Account %s verified", BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE));
    }

    private void verifyVendorBalance() throws ITestFailedException {
        VendorDTO vendor = vendorClient.read(BusinessLogicSetupUtil.TEST_VENDOR_CODE);

        if (vendor.getBalance() != 0) {
            throw new ITestFailedException(String.format("Vendor %s balance issue: expected %d, got %d",
                    BusinessLogicSetupUtil.TEST_VENDOR_CODE, 0, vendor.getBalance()));
        }

        LOGGER.info(String.format("Vendor %s verified", BusinessLogicSetupUtil.TEST_VENDOR_CODE));
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
                Thread.sleep(10_000);
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

    private void createPurchaseOrderLine(String code) {
        PurchaseDocumentLineDTO purchaseOrderLine = new PurchaseDocumentLineDTO();
        purchaseOrderLine.setItemCode(BusinessLogicSetupUtil.TEST_ITEM_CODE);
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
