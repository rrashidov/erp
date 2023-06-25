package org.roko.erp.itests.runner.sales;

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
import org.roko.erp.itests.clients.PostedSalesOrderClient;
import org.roko.erp.itests.clients.SalesOrderClient;
import org.roko.erp.itests.clients.SalesOrderLineClient;
import org.roko.erp.itests.runner.ITestFailedException;
import org.roko.erp.itests.runner.ITestRunner;
import org.roko.erp.itests.runner.purchases.PostPurchaseCreditMemoTestRunner;
import org.roko.erp.itests.runner.purchases.PostPurchaseOrderTestRunner;
import org.roko.erp.itests.runner.util.BusinessLogicSetupUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostSalesOrderTestRunner implements ITestRunner {

    private static final int TEST_QUANTITY = 50;
    private static final int TEST_PRICE = 1;
    private static final int TEST_AMOUNT = 50;

    private static final Logger LOGGER = LoggerFactory.getLogger("erp.itests");

    @Autowired
    private BusinessLogicSetupUtil util;

    @Autowired
    private SalesOrderClient salesOrderClient;

    @Autowired
    private SalesOrderLineClient salesOrderLineClient;

    @Autowired
    private PostedSalesOrderClient postedSalesOrderClient;

    @Autowired
    private CustomerClient customerClient;

    @Autowired
    private BankAccountClient bankAccountClient;

    @Autowired
    private ItemClient itemClient;

    @Override
    public void run() throws ITestFailedException {
        util.ensureSetup();
        util.ensureBankAccounts();
        util.ensurePaymentMethods();
        util.ensureItems();
        util.ensureCustomer();

        String code = createSalesOrder();
        createSalesOrderLine(code);

        LOGGER.info(String.format("Sales Order %s created", code));

        triggerSalesOrderPost(code);

        LOGGER.info(String.format("Sales Order %s post triggered", code));

        verifySalesOrderPostScheduled(code);

        LOGGER.info(String.format("Sales Order %s post scheduled", code));

        waitSalesOrderToBePosted(code);

        LOGGER.info(String.format("Sales Order %s posted", code));

        verifyPostedSalesOrder();

        verifyCustomerBalance();

        verifyBankAccountBalance();

        verifyItemInventory();

        LOGGER.info("Sales Order post test passed");
    }

    private void verifyItemInventory() throws ITestFailedException {
        double expectedItemIventory = PostPurchaseOrderTestRunner.TEST_QUANTITY - PostPurchaseCreditMemoTestRunner.TEST_QUANTITY - TEST_QUANTITY;

        ItemDTO item = itemClient.read(BusinessLogicSetupUtil.TEST_ITEM_CODE);

        if (item.getInventory() != expectedItemIventory) {
            throw new ITestFailedException(String.format("Item %s inventory issue: expected %f, got %f",
                    BusinessLogicSetupUtil.TEST_ITEM_CODE, expectedItemIventory, item.getInventory()));
        }

        LOGGER.info(String.format("Item %s verified", BusinessLogicSetupUtil.TEST_ITEM_CODE));
    }

    private void verifyBankAccountBalance() throws ITestFailedException {
        double expectedBankAccountBalance = BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_BALANCE + TEST_AMOUNT;

        BankAccountDTO bankAccount = bankAccountClient.read(BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE);

        if (bankAccount.getBalance() != expectedBankAccountBalance) {
            throw new ITestFailedException(String.format("Bank Account %s balance issue: expected %f, got %f",
                    BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE, expectedBankAccountBalance,
                    bankAccount.getBalance()));
        }

        LOGGER.info(String.format("Bank Account %s verified", BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE));
    }

    private void verifyCustomerBalance() throws ITestFailedException {
        CustomerDTO customer = customerClient.read(BusinessLogicSetupUtil.TEST_CUSTOMER_CODE);

        if (customer.getBalance() != 0) {
            throw new ITestFailedException(String.format("Customer %s balance issue: expected %f, got %f",
                    BusinessLogicSetupUtil.TEST_CUSTOMER_CODE, 0, customer.getBalance()));
        }
    }

    private void verifyPostedSalesOrder() throws ITestFailedException {
        // this is hack - we know what first code we put for the posted sales order
        // code series, so we know what code will be generated when posting the
        // sales order; this will have to be reconsidered for the future
        String code = "PSO001";

        PostedSalesDocumentDTO postedSalesOrder = postedSalesOrderClient.read(code);

        if (postedSalesOrder == null) {
            throw new ITestFailedException(String.format("Posted Sales Order %s not found", code));
        }

        PostedSalesDocumentLineList postedSalesOrderLineList = postedSalesOrderClient.readLines(code, 1);

        if (postedSalesOrderLineList.getCount() != 1) {
            throw new ITestFailedException(String.format("Posted Sales Order %s lines issue: expected %d, got %d", code,
                    1, postedSalesOrderLineList.getCount()));
        }

        PostedSalesDocumentLineDTO postedSalesOrderLine = postedSalesOrderLineList.getData().get(0);

        if (!postedSalesOrderLine.getItemCode().equals(BusinessLogicSetupUtil.TEST_ITEM_CODE)) {
            throw new ITestFailedException(String.format("Posted Sales Order %s line item issue: expected %s, got %s",
                    code, BusinessLogicSetupUtil.TEST_ITEM_CODE, postedSalesOrderLine.getItemCode()));
        }

        if (postedSalesOrderLine.getQuantity() != TEST_QUANTITY) {
            throw new ITestFailedException(
                    String.format("Posted Sales Order %s line quantity issue: expected %f, got %f",
                            code, TEST_QUANTITY, postedSalesOrderLine.getQuantity()));
        }

        if (postedSalesOrderLine.getPrice() != TEST_PRICE) {
            throw new ITestFailedException(String.format("Posted Sales Order %s line price issue: expected %f, got %f",
                    code, TEST_PRICE, postedSalesOrderLine.getPrice()));
        }

        if (postedSalesOrderLine.getAmount() != TEST_AMOUNT) {
            throw new ITestFailedException(String.format("Posted Sales Order %s line amount issue: expected %f, got %f",
                    code, TEST_AMOUNT, postedSalesOrderLine.getAmount()));
        }

        LOGGER.info(String.format("Posted Sales Order %s verified", code));
    }

    private void waitSalesOrderToBePosted(String code) {
        while (salesOrderClient.read(code) != null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    private void verifySalesOrderPostScheduled(String code) throws ITestFailedException {
        SalesDocumentDTO salesOrder = salesOrderClient.read(code);

        if (!salesOrder.getPostStatus().equals("SCHEDULED")) {
            throw new ITestFailedException(String.format("Sales Order %s post status issue: expected %s, got %s", code,
                    "SCHEDULED", salesOrder.getPostStatus()));
        }
    }

    private void triggerSalesOrderPost(String code) {
        salesOrderClient.post(code);
    }

    private void createSalesOrderLine(String code) {
        SalesDocumentLineDTO salesOrderLine = new SalesDocumentLineDTO();
        salesOrderLine.setItemCode(BusinessLogicSetupUtil.TEST_ITEM_CODE);
        salesOrderLine.setQuantity(TEST_QUANTITY);
        salesOrderLine.setPrice(TEST_PRICE);
        salesOrderLine.setAmount(TEST_AMOUNT);
        salesOrderLineClient.create(code, salesOrderLine);
    }

    private String createSalesOrder() {
        SalesDocumentDTO salesOrder = new SalesDocumentDTO();
        salesOrder.setCustomerCode(BusinessLogicSetupUtil.TEST_CUSTOMER_CODE);
        salesOrder.setPaymentMethodCode(BusinessLogicSetupUtil.TEST_PAYMENT_METHOD_CODE);
        salesOrder.setDate(new Date());
        return salesOrderClient.create(salesOrder);
    }

}