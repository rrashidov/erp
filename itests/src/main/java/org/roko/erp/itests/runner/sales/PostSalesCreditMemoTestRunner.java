package org.roko.erp.itests.runner.sales;

import java.math.BigDecimal;
import java.util.Date;

import org.roko.erp.dto.BankAccountDTO;
import org.roko.erp.dto.CustomerDTO;
import org.roko.erp.dto.ItemDTO;
import org.roko.erp.dto.PostedSalesDocumentDTO;
import org.roko.erp.dto.PostedSalesDocumentLineDTO;
import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.dto.SalesDocumentLineDTO;
import org.roko.erp.dto.list.PostedSalesDocumentLineList;
import org.roko.erp.itests.clients.BankAccountClient;
import org.roko.erp.itests.clients.CustomerClient;
import org.roko.erp.itests.clients.ItemClient;
import org.roko.erp.itests.clients.PostedSalesCreditMemoClient;
import org.roko.erp.itests.clients.SalesCreditMemoClient;
import org.roko.erp.itests.clients.SalesCreditMemoLineClient;
import org.roko.erp.itests.runner.ITestFailedException;
import org.roko.erp.itests.runner.ITestRunner;
import org.roko.erp.itests.runner.util.BusinessLogicSetupUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostSalesCreditMemoTestRunner implements ITestRunner {

    private static final BigDecimal TEST_QUANTITY = new BigDecimal("10");
    private static final BigDecimal TEST_PRICE = new BigDecimal("1");
    private static final BigDecimal TEST_AMOUNT = new BigDecimal("10");

    private static final Logger LOGGER = LoggerFactory.getLogger("erp.itests");

    private static final long POST_WAIT_TIMEOUT = 10 * 60 * 1000;

    @Autowired
    private BusinessLogicSetupUtil util;

    @Autowired
    private SalesCreditMemoClient salesCreditMemoClient;

    @Autowired
    private SalesCreditMemoLineClient salesCreditMemoLineClient;

    @Autowired
    private PostedSalesCreditMemoClient postedSalesCreditMemoClient;

    @Autowired
    private CustomerClient customerClient;

    @Autowired
    private BankAccountClient bankAccountClient;

    @Autowired
    private ItemClient itemClient;

    @Override
    public void run() throws ITestFailedException {
        ensureNeededObjects();

        happyPathTest();

        delayedPaymentMethodTest();

        noBalanceBankAccountTest();
    }

    private void noBalanceBankAccountTest() throws ITestFailedException {
        String code = createSalesCreditMEmoWithNoBalanceBankAccountPaymentMethod();
        createSalesCreditMemoLine(code, BusinessLogicSetupUtil.TEST_ITEM_CODE);

        LOGGER.info(String.format("Sales Credit Memo %s created", code));

        triggerSalesCreditMemoPost(code);

        LOGGER.info(String.format("Sales Credit Memo %s triggered for posting", code));

        verifySalesCreditMemoPostFailed(code);

        LOGGER.info("Sales Credit Memo post no balance bank account test passed");
    }

    private void verifySalesCreditMemoPostFailed(String code) throws ITestFailedException {
        String salesCreditMemoPostStatus = "";
        boolean timeoutReached = false;
        long start = System.currentTimeMillis();

        while ((!salesCreditMemoPostStatus.equals("FAILED")) && !timeoutReached) {
            salesCreditMemoPostStatus = salesCreditMemoClient.read(code).getPostStatus();

            timeoutReached = (System.currentTimeMillis() - start) > POST_WAIT_TIMEOUT;
        }

        if (timeoutReached) {
            throw new ITestFailedException(String.format("Sales Credit Memo %s post status failed: expected %s, got %s",
                    code, "FAILED", salesCreditMemoPostStatus));
        }
    }

    private String createSalesCreditMEmoWithNoBalanceBankAccountPaymentMethod() {
        SalesDocumentDTO salesCreditMemo = new SalesDocumentDTO();
        salesCreditMemo.setCustomerCode(BusinessLogicSetupUtil.TEST_CUSTOMER_CODE);
        salesCreditMemo.setPaymentMethodCode(BusinessLogicSetupUtil.NO_BALANCE_PAYMENT_METHOD_CODE);
        salesCreditMemo.setDate(new Date());
        return salesCreditMemoClient.create(salesCreditMemo);
    }

    private void delayedPaymentMethodTest() throws ITestFailedException {
        String code = createSalesCreditMEmoWithDelayedPaymentMethod();
        createSalesCreditMemoLine(code, BusinessLogicSetupUtil.TEST_ITEM_CODE_2);

        BigDecimal initialCustomerBalance = getCustomerBalance(BusinessLogicSetupUtil.TEST_CUSTOMER_CODE_2);

        LOGGER.info(String.format("Sales Credit Memo %s created", code));

        triggerSalesCreditMemoPost(code);

        LOGGER.info(String.format("Sales Credit Memo %s triggered for posting", code));

        waitSalesCreditMemoToBePosted(code);

        LOGGER.info(String.format("Sales Credit Memo %s posted", code));

        verifyCustomerBalance(BusinessLogicSetupUtil.TEST_CUSTOMER_CODE_2, initialCustomerBalance.subtract(TEST_AMOUNT));

        LOGGER.info("Sales Credit Memo post with delayed payment method test passed");
    }

    private BigDecimal getCustomerBalance(String customerCode) {
        return customerClient.read(customerCode).getBalance();
    }

    private String createSalesCreditMEmoWithDelayedPaymentMethod() {
        SalesDocumentDTO salesCreditMemo = new SalesDocumentDTO();
        salesCreditMemo.setCustomerCode(BusinessLogicSetupUtil.TEST_CUSTOMER_CODE_2);
        salesCreditMemo.setPaymentMethodCode(BusinessLogicSetupUtil.DELAYED_PAYMENT_METHOD_CODE);
        salesCreditMemo.setDate(new Date());
        return salesCreditMemoClient.create(salesCreditMemo);
    }

    private void happyPathTest() throws ITestFailedException {
        String code = createSalesCreditMemo();
        createSalesCreditMemoLine(code, BusinessLogicSetupUtil.TEST_ITEM_CODE);

        BigDecimal initialItemInventory = getItemInventory(BusinessLogicSetupUtil.TEST_ITEM_CODE);

        LOGGER.info(String.format("Sales Credit Memo %s created", code));

        triggerSalesCreditMemoPost(code);

        LOGGER.info(String.format("Sales Credit Memo %s post triggered", code));

        verifySalesCreditMemoPostScheduled(code);

        LOGGER.info(String.format("Sales Credit Memo %s post scheduled", code));

        waitSalesCreditMemoToBePosted(code);

        LOGGER.info(String.format("Sales Credit Memo %s posted", code));

        verifyPostedSalesCreditMemo();

        verifyCustomerBalance(BusinessLogicSetupUtil.TEST_CUSTOMER_CODE, new BigDecimal(0));

        verifyBankAccountBalance();

        verifyItemInventory(initialItemInventory);

        LOGGER.info("Sales Credit Memo post test passed");
    }

    private BigDecimal getItemInventory(String itemCode) {
        return itemClient.read(itemCode).getInventory();
    }

    private void ensureNeededObjects() throws ITestFailedException {
        util.ensureSetup();
        util.ensureBankAccounts();
        util.ensurePaymentMethods();
        util.ensureItems();
        util.ensureCustomers();
    }

    private void verifyItemInventory(BigDecimal  initialItemInventory) throws ITestFailedException {
        BigDecimal expectedItemIventory = initialItemInventory.add(TEST_QUANTITY);

        ItemDTO item = itemClient.read(BusinessLogicSetupUtil.TEST_ITEM_CODE);

        if (item.getInventory().compareTo(expectedItemIventory) != 0) {
            throw new ITestFailedException(String.format("Item %s inventory issue: expected %f, got %f",
                    BusinessLogicSetupUtil.TEST_ITEM_CODE, expectedItemIventory, item.getInventory()));
        }

        LOGGER.info(String.format("Item %s verified", BusinessLogicSetupUtil.TEST_ITEM_CODE));
    }

    private void verifyBankAccountBalance() throws ITestFailedException {
        BigDecimal expectedBankAccountBalance = BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_BALANCE.subtract(TEST_AMOUNT);

        BankAccountDTO bankAccount = bankAccountClient.read(BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE);

        if (bankAccount.getBalance().compareTo(expectedBankAccountBalance) != 0) {
            throw new ITestFailedException(String.format("Bank Account %s balance issue: expected %f, got %f",
                    BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE, expectedBankAccountBalance,
                    bankAccount.getBalance()));
        }

        LOGGER.info(String.format("Bank Account %s verified", BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE));
    }

    private void verifyCustomerBalance(String customerCode, BigDecimal expectedBalance) throws ITestFailedException {
        CustomerDTO customer = customerClient.read(customerCode);

        if (customer.getBalance().compareTo(expectedBalance) != 0) {
            throw new ITestFailedException(String.format("Customer %s balance issue: expected %f, got %f",
                    customerCode, expectedBalance, customer.getBalance()));
        }

        LOGGER.info(String.format("Customer %s balance verified", BusinessLogicSetupUtil.TEST_CUSTOMER_CODE));
    }

    private void verifyPostedSalesCreditMemo() throws ITestFailedException {
        // this is hack - we know what first code we put for the posted sales credit memo
        // code series, so we know what code will be generated when posting the
        // sales credit memo; this will have to be reconsidered for the future
        String code = "PSCM001";

        PostedSalesDocumentDTO postedSalesCreditMemo = postedSalesCreditMemoClient.read(code);

        if (postedSalesCreditMemo == null) {
            throw new ITestFailedException(String.format("Posted Sales Credit Memo %s not found", code));
        }

        PostedSalesDocumentLineList postedSalesCreditMemoLineList = postedSalesCreditMemoClient.readLines(code, 1);

        if (postedSalesCreditMemoLineList.getCount() != 1) {
            throw new ITestFailedException(String.format("Posted Sales Credit Memo %s lines issue: expected %d, got %d", code,
                    1, postedSalesCreditMemoLineList.getCount()));
        }

        PostedSalesDocumentLineDTO postedSalesCreditMemoLine = postedSalesCreditMemoLineList.getData().get(0);

        if (!postedSalesCreditMemoLine.getItemCode().equals(BusinessLogicSetupUtil.TEST_ITEM_CODE)) {
            throw new ITestFailedException(String.format("Posted Sales Credit Memo %s line item issue: expected %s, got %s",
                    code, BusinessLogicSetupUtil.TEST_ITEM_CODE, postedSalesCreditMemoLine.getItemCode()));
        }

        if (postedSalesCreditMemoLine.getQuantity().compareTo(TEST_QUANTITY) != 0) {
            throw new ITestFailedException(
                    String.format("Posted Sales Credit Memo %s line quantity issue: expected %f, got %f",
                            code, TEST_QUANTITY, postedSalesCreditMemoLine.getQuantity()));
        }

        if (postedSalesCreditMemoLine.getPrice().compareTo(TEST_PRICE) != 0) {
            throw new ITestFailedException(String.format("Posted Sales Credit Memo %s line price issue: expected %f, got %f",
                    code, TEST_PRICE, postedSalesCreditMemoLine.getPrice()));
        }

        if (postedSalesCreditMemoLine.getAmount().compareTo(TEST_AMOUNT) != 0) {
            throw new ITestFailedException(String.format("Posted Sales Credit Memo %s line amount issue: expected %f, got %f",
                    code, postedSalesCreditMemoLine.getAmount()));
        }

        LOGGER.info(String.format("Posted Sales Credit Memo %s verified", code));
    }

    private void waitSalesCreditMemoToBePosted(String code) {
        while (salesCreditMemoClient.read(code) != null) {
        }
    }

    private void verifySalesCreditMemoPostScheduled(String code) throws ITestFailedException {
        SalesDocumentDTO salesCreditMemo = salesCreditMemoClient.read(code);

        if (!salesCreditMemo.getPostStatus().equals("SCHEDULED")) {
            throw new ITestFailedException(String.format("Sales Credit Memo %s post status issue: expected %s, got %s", code,
                    "SCHEDULED", salesCreditMemo.getPostStatus()));
        }
    }

    private void triggerSalesCreditMemoPost(String code) {
        salesCreditMemoClient.post(code);
    }

    private void createSalesCreditMemoLine(String code, String itemCode) {
        SalesDocumentLineDTO salesCreditMemoLine = new SalesDocumentLineDTO();
        salesCreditMemoLine.setItemCode(itemCode);
        salesCreditMemoLine.setQuantity(TEST_QUANTITY);
        salesCreditMemoLine.setPrice(TEST_PRICE);
        salesCreditMemoLine.setAmount(TEST_AMOUNT);
        salesCreditMemoLineClient.create(code, salesCreditMemoLine);
    }

    private String createSalesCreditMemo() {
        SalesDocumentDTO salesCreditMemo = new SalesDocumentDTO();
        salesCreditMemo.setCustomerCode(BusinessLogicSetupUtil.TEST_CUSTOMER_CODE);
        salesCreditMemo.setPaymentMethodCode(BusinessLogicSetupUtil.TEST_PAYMENT_METHOD_CODE);
        salesCreditMemo.setDate(new Date());
        return salesCreditMemoClient.create(salesCreditMemo);
    }
    
}
