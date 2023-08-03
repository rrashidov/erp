package org.roko.erp.itests.runner.generalledger;

import java.util.Date;

import org.roko.erp.dto.BankAccountDTO;
import org.roko.erp.dto.CustomerDTO;
import org.roko.erp.dto.GeneralJournalBatchLineDTO;
import org.roko.erp.dto.GeneralJournalBatchLineOperationType;
import org.roko.erp.dto.GeneralJournalBatchLineType;
import org.roko.erp.itests.clients.BankAccountClient;
import org.roko.erp.itests.clients.CustomerClient;
import org.roko.erp.itests.clients.GeneralJournalBatchClient;
import org.roko.erp.itests.clients.GeneralJournalBatchLineClient;
import org.roko.erp.itests.runner.ITestFailedException;
import org.roko.erp.itests.runner.ITestRunner;
import org.roko.erp.itests.runner.util.BusinessLogicSetupUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostGenJnlBatchTestRunner implements ITestRunner {

    private static final double TEST_AMOUNT = 123.00;

    private static final Logger LOGGER = LoggerFactory.getLogger("erp.itests");

    @Autowired
    private BusinessLogicSetupUtil util;

    @Autowired
    private GeneralJournalBatchClient generalJournalBatchClient;

    @Autowired
    private GeneralJournalBatchLineClient generalJournalBatchLineClient;

    @Autowired
    private BankAccountClient bankAccountClient;

    @Autowired
    private CustomerClient customerClient;

    @Override
    public void run() throws ITestFailedException {
        LOGGER.info("Run general journal batch post tests");

        freeEntryPostTest();

        transferBetweenBankAccountsTest();

        postCustomerDocumentTest();

        postCustomerPaymentTest();

        postCustomerCreditMemoTest();

        postCustomerRefundTest();

        LOGGER.info("General journal batch post tests passed");
    }

    private void postCustomerDocumentTest() throws ITestFailedException {
        LOGGER.info("Running post customer document test");

        util.ensureCustomers();
        util.ensureBankAccounts();

        createCustomerDocumentGenJnlLine(BusinessLogicSetupUtil.TEST_CUSTOMER_CODE_3);

        postGeneralJournalBatchSynchronously();

        assertCustomerBalance(BusinessLogicSetupUtil.TEST_CUSTOMER_CODE_3, TEST_AMOUNT);

        LOGGER.info("Post customer document test passed");
    }

    private void postCustomerCreditMemoTest() throws ITestFailedException {
        LOGGER.info("Running post customer credit memo test");

        util.ensureCustomers();
        util.ensureBankAccounts();

        createCustomerCreditMemoGenJnlLine(BusinessLogicSetupUtil.TEST_CUSTOMER_CODE_3);

        postGeneralJournalBatchSynchronously();

        assertCustomerBalance(BusinessLogicSetupUtil.TEST_CUSTOMER_CODE_3, -TEST_AMOUNT);

        LOGGER.info("Post customer credit memo test passed");
    }

    private void postCustomerPaymentTest() throws ITestFailedException {
        LOGGER.info("Running post customer payment test");

        util.ensureCustomers();
        util.ensureBankAccounts();

        createCustomerPaymentGenJnlLine(BusinessLogicSetupUtil.TEST_CUSTOMER_CODE_3,
                BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE);

        postGeneralJournalBatchSynchronously();

        assertCustomerBalance(BusinessLogicSetupUtil.TEST_CUSTOMER_CODE_3, 0);

        assertBankAccountBalance(BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE,
                BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_BALANCE + TEST_AMOUNT);

        LOGGER.info("Post customer payment test passed");
    }

    private void postCustomerRefundTest() throws ITestFailedException {
        LOGGER.info("Running post customer refund test");

        util.ensureCustomers();
        util.ensureBankAccounts();

        createCustomerRefundGenJnlLine(BusinessLogicSetupUtil.TEST_CUSTOMER_CODE_3,
                BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE);

        postGeneralJournalBatchSynchronously();

        assertCustomerBalance(BusinessLogicSetupUtil.TEST_CUSTOMER_CODE_3, 0);

        assertBankAccountBalance(BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE,
                BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_BALANCE - TEST_AMOUNT);

        LOGGER.info("Post customer refund test passed");
    }

    private void assertCustomerBalance(String code, double expectedBalance) throws ITestFailedException {
        CustomerDTO customer = customerClient.read(code);

        if (customer.getBalance() != expectedBalance) {
            throw new ITestFailedException(String.format("Customer %s balance issue: expected %f, got %f", code,
                    expectedBalance, customer.getBalance()));
        }
    }

    private void transferBetweenBankAccountsTest() throws ITestFailedException {
        LOGGER.info("Running transfer between bank accounts test");

        util.ensureBankAccounts();

        createBankTransferGenJnlLine(
                BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE_2,
                BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE);

        postGeneralJournalBatchSynchronously();

        assertBankAccountBalance(BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE,
                BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_BALANCE - TEST_AMOUNT);
        assertBankAccountBalance(BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_CODE_2, TEST_AMOUNT);

        LOGGER.info("Transfer between bank accounts test passed");
    }

    private void freeEntryPostTest() throws ITestFailedException {
        LOGGER.info("Running free entry post test");

        util.ensureBankAccounts();

        LOGGER.info("Free entry post test passed");
    }

    private void postGeneralJournalBatchSynchronously() throws ITestFailedException {
        generalJournalBatchClient.post(BusinessLogicSetupUtil.TEST_GENERAP_JOURNAL_BATCH_CODE);

        util.waitGeneralJournalBatchPosted();
    }

    private void assertBankAccountBalance(String code, double expectedBalance) throws ITestFailedException {
        BankAccountDTO bankAccount = bankAccountClient.read(code);

        if (bankAccount.getBalance() != expectedBalance) {
            throw new ITestFailedException(String.format("Bank account balance assert failed: expected %f, got %f",
                    expectedBalance, bankAccount.getBalance()));
        }
    }

    private void createCustomerDocumentGenJnlLine(String code) {
        createGeneralJournalBatchLine(GeneralJournalBatchLineType.CUSTOMER, code,
                GeneralJournalBatchLineOperationType.ORDER, null);
    }

    private void createCustomerCreditMemoGenJnlLine(String code) {
        createGeneralJournalBatchLine(GeneralJournalBatchLineType.CUSTOMER, code,
                GeneralJournalBatchLineOperationType.CREDIT_MEMO, null);
    }

    private void createCustomerPaymentGenJnlLine(String code, String bankAccountCode) {
        createGeneralJournalBatchLine(GeneralJournalBatchLineType.CUSTOMER, code,
                GeneralJournalBatchLineOperationType.PAYMENT, bankAccountCode);
    }

    private void createCustomerRefundGenJnlLine(String code, String bankAccountCode) {
        createGeneralJournalBatchLine(GeneralJournalBatchLineType.CUSTOMER, code,
                GeneralJournalBatchLineOperationType.REFUND, bankAccountCode);
    }

    private void createBankTransferGenJnlLine(String sourceBankAccount, String targetBankAccount) {
        createGeneralJournalBatchLine(GeneralJournalBatchLineType.BANK_ACCOUNT, sourceBankAccount,
                GeneralJournalBatchLineOperationType.EMPTY, targetBankAccount);
    }

    private void createGeneralJournalBatchLine(GeneralJournalBatchLineType generalJournalBatchLineType,
            String generalJournalBatchLineCode,
            GeneralJournalBatchLineOperationType generalJournalBatchLineOperationType, String bankAccountCode) {
        GeneralJournalBatchLineDTO genJnlBatchLine = new GeneralJournalBatchLineDTO();
        genJnlBatchLine.setGeneralJournalBatchCode(BusinessLogicSetupUtil.TEST_GENERAP_JOURNAL_BATCH_CODE);
        genJnlBatchLine.setType(generalJournalBatchLineType);
        genJnlBatchLine.setCode(generalJournalBatchLineCode);
        genJnlBatchLine.setOperationType(generalJournalBatchLineOperationType);
        genJnlBatchLine.setDate(new Date());
        genJnlBatchLine.setDocumentCode("0");
        genJnlBatchLine.setAmount(TEST_AMOUNT);
        if (bankAccountCode != null) {
            genJnlBatchLine.setBankAccountCode(bankAccountCode);
        }

        generalJournalBatchLineClient.create(BusinessLogicSetupUtil.TEST_GENERAP_JOURNAL_BATCH_CODE, genJnlBatchLine);
    }

}
