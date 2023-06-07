package org.roko.erp.itests.runner.generalledger;

import java.util.Date;

import org.roko.erp.dto.BankAccountDTO;
import org.roko.erp.dto.GeneralJournalBatchDTO;
import org.roko.erp.dto.GeneralJournalBatchLineDTO;
import org.roko.erp.dto.GeneralJournalBatchLineOperationType;
import org.roko.erp.dto.GeneralJournalBatchLineType;
import org.roko.erp.itests.clients.BankAccountClient;
import org.roko.erp.itests.clients.GeneralJournalBatchClient;
import org.roko.erp.itests.clients.GeneralJournalBatchLineClient;
import org.roko.erp.itests.runner.ITestFailedException;
import org.roko.erp.itests.runner.ITestRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GeneralJournalBatchTestRunner implements ITestRunner {

    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";

    private static final String UPDATED_CODE = "updated-code";
    private static final String UPDATED_NAME = "updated-name";

    private static final String TEST_DOCUMENT_CODE = "test-document-code";
    private static final Date TEST_DATE = new Date();
    private static final double TEST_AMOUNT = 12.34;

    private static final String TEST_BANK_ACCOUNT_CODE = "test-bank-account-code";
    private static final String TEST_BANK_ACCOUNT_NAME = "test-bank-account-name";

    private static final String UPDATED_DOCUMENT_CODE = "updated-document-code";
    private static final Date UPDATED_DATE = new Date();
    private static final double UPDATED_AMOUNT = 56.78;

    private GeneralJournalBatchClient client;

    private GeneralJournalBatchLineClient lineClient;

    private BankAccountClient bankAccountClient;

    @Autowired
    public GeneralJournalBatchTestRunner(GeneralJournalBatchClient client, GeneralJournalBatchLineClient lineClient,
            BankAccountClient bankAccountClient) {
        this.client = client;
        this.lineClient = lineClient;
        this.bankAccountClient = bankAccountClient;
    }

    @Override
    public void run() throws ITestFailedException {
        print("Running GeneralJournalBatch create test");
        client.create(generateGeneralJournalBatch());
        print("GeneralJournalBatch create test passed");

        print("Running GeneralJournalBatch read test");
        GeneralJournalBatchDTO generalJournalBatch = client.read(TEST_CODE);
        verifyGeneralJournalBatchRead(generalJournalBatch);
        print("GeneralJournalBatch read test passed");

        print("Running GeneralJournalBatch update test");
        generalJournalBatch = generateUpdatedGeneralJournalBatch();
        client.update(TEST_CODE, generalJournalBatch);
        generalJournalBatch = client.read(TEST_CODE);
        verifyGeneralJournalBatchUpdated(generalJournalBatch);
        print("GeneralJournalBatch update test passed");

        testGeneralJournalBatchLine();

        print("Running GeneralJournalBatch delete test");
        client.delete(TEST_CODE);
        generalJournalBatch = client.read(TEST_CODE);
        verifyGeneralJournalBatchDeleted(generalJournalBatch);
        print("GeneralJournalBatch delete test passed");
    }

    private void testGeneralJournalBatchLine() throws ITestFailedException {
        print("Running GeneralJournalBatchLine create test");
        BankAccountDTO bankAccount = generateBankAccount();
        bankAccountClient.create(bankAccount);
        int lineNo = lineClient.create(TEST_CODE, generateGeneralJournalBatchLine());
        print("GeneralJournalBatchLine create test passed");

        print("Running GeneralJournalBatchLine read test");
        GeneralJournalBatchLineDTO generalJournalBatchLine = lineClient.read(TEST_CODE, lineNo);
        verifyGeneralJournalbatchLineRead(generalJournalBatchLine);
        print("GeneralJournalBatchLine read test passed");

        print("Running GeneralJournalBatchLine update test");
        generalJournalBatchLine = generateGeneralJournalBatchLineUpdate();
        lineClient.update(TEST_CODE, lineNo, generalJournalBatchLine);
        generalJournalBatchLine = lineClient.read(TEST_CODE, lineNo);
        verifyGeneralJournalBatchLineUpdated(generalJournalBatchLine);
        print("GeneralJournalBatchLine update test passed");

        print("Running GeneralJournalBatchLine delete test");
        lineClient.delete(TEST_CODE, lineNo);
        generalJournalBatchLine = lineClient.read(TEST_CODE, lineNo);
        verifyGeneralJournalBatchLineDeleted(generalJournalBatchLine);
        bankAccountClient.delete(TEST_BANK_ACCOUNT_CODE);
        print("GeneralJournalBatchLine delete test passed");
    }

    private void verifyGeneralJournalBatchLineDeleted(GeneralJournalBatchLineDTO generalJournalBatchLine)
            throws ITestFailedException {
        if (generalJournalBatchLine != null) {
            throw new ITestFailedException("General journal batch line should not exist when deleted");
        }
    }

    private void verifyGeneralJournalBatchLineUpdated(GeneralJournalBatchLineDTO generalJournalBatchLine)
            throws ITestFailedException {
        if (!generalJournalBatchLine.getType().equals(GeneralJournalBatchLineType.VENDOR)) {
            throw new ITestFailedException(String.format("General journal batch line type issue: expected %s, got %s",
                    GeneralJournalBatchLineType.VENDOR, generalJournalBatchLine.getType()));
        }

        if (!generalJournalBatchLine.getCode().equals(UPDATED_CODE)) {
            throw new ITestFailedException(String.format("General journal batch line code issue: expected %s, got %s",
                    UPDATED_CODE, generalJournalBatchLine.getCode()));
        }

        if (!generalJournalBatchLine.getName().equals(UPDATED_NAME)) {
            throw new ITestFailedException(String.format("General journal batch line name issue: expected %s, got %s",
                    UPDATED_NAME, generalJournalBatchLine.getName()));
        }

        if (!generalJournalBatchLine.getOperationType().equals(GeneralJournalBatchLineOperationType.CREDIT_MEMO)) {
            throw new ITestFailedException(
                    String.format("General journal batch line op type issue: expected %s, got %s",
                            GeneralJournalBatchLineOperationType.CREDIT_MEMO,
                            generalJournalBatchLine.getOperationType()));
        }

        if (!generalJournalBatchLine.getDocumentCode().equals(UPDATED_DOCUMENT_CODE)) {
            throw new ITestFailedException(String.format("General journal batch line doc no issue: expected %s, got %s",
                    UPDATED_DOCUMENT_CODE, generalJournalBatchLine.getDocumentCode()));
        }

        if (generalJournalBatchLine.getAmount() != UPDATED_AMOUNT) {
            throw new ITestFailedException(String.format("General journal batch line amount issue: expected %s, got %s",
                    UPDATED_AMOUNT, generalJournalBatchLine.getAmount()));
        }

        if (!generalJournalBatchLine.getBankAccountCode().equals("")) {
            throw new ITestFailedException(
                    String.format("General journal batch line bank account code issue: expected %s, got %s",
                            "", generalJournalBatchLine.getBankAccountCode()));
        }

        if (!generalJournalBatchLine.getBankAccountName().equals("")) {
            throw new ITestFailedException(String.format("General journal batch line name issue: expected %s, got %s",
                    "", generalJournalBatchLine.getBankAccountName()));
        }
    }

    private GeneralJournalBatchLineDTO generateGeneralJournalBatchLineUpdate() {
        GeneralJournalBatchLineDTO result = new GeneralJournalBatchLineDTO();
        result.setType(GeneralJournalBatchLineType.VENDOR);
        result.setCode(UPDATED_CODE);
        result.setName(UPDATED_NAME);
        result.setOperationType(GeneralJournalBatchLineOperationType.CREDIT_MEMO);
        result.setDocumentCode(UPDATED_DOCUMENT_CODE);
        result.setDate(UPDATED_DATE);
        result.setAmount(UPDATED_AMOUNT);
        result.setBankAccountCode("");
        return result;
    }

    private void verifyGeneralJournalbatchLineRead(GeneralJournalBatchLineDTO generalJournalBatchLine)
            throws ITestFailedException {
        if (!generalJournalBatchLine.getType().equals(GeneralJournalBatchLineType.CUSTOMER)) {
            throw new ITestFailedException(String.format("General journal batch line type issue: expected %s, got %s",
                    GeneralJournalBatchLineType.CUSTOMER, generalJournalBatchLine.getType()));
        }

        if (!generalJournalBatchLine.getCode().equals(TEST_CODE)) {
            throw new ITestFailedException(String.format("General journal batch line code issue: expected %s, got %s",
                    TEST_CODE, generalJournalBatchLine.getCode()));
        }

        if (!generalJournalBatchLine.getName().equals(TEST_NAME)) {
            throw new ITestFailedException(String.format("General journal batch line name issue: expected %s, got %s",
                    TEST_NAME, generalJournalBatchLine.getName()));
        }

        if (!generalJournalBatchLine.getOperationType().equals(GeneralJournalBatchLineOperationType.ORDER)) {
            throw new ITestFailedException(
                    String.format("General journal batch line op type issue: expected %s, got %s",
                            GeneralJournalBatchLineOperationType.ORDER, generalJournalBatchLine.getOperationType()));
        }

        if (!generalJournalBatchLine.getDocumentCode().equals(TEST_DOCUMENT_CODE)) {
            throw new ITestFailedException(String.format("General journal batch line doc no issue: expected %s, got %s",
                    TEST_DOCUMENT_CODE, generalJournalBatchLine.getDocumentCode()));
        }

        if (generalJournalBatchLine.getAmount() != TEST_AMOUNT) {
            throw new ITestFailedException(String.format("General journal batch line amount issue: expected %s, got %s",
                    TEST_AMOUNT, generalJournalBatchLine.getAmount()));
        }

        if (!generalJournalBatchLine.getBankAccountCode().equals(TEST_BANK_ACCOUNT_CODE)) {
            throw new ITestFailedException(
                    String.format("General journal batch line bank account code issue: expected %s, got %s",
                            TEST_BANK_ACCOUNT_CODE, generalJournalBatchLine.getBankAccountCode()));
        }

        if (!generalJournalBatchLine.getBankAccountName().equals(TEST_BANK_ACCOUNT_NAME)) {
            throw new ITestFailedException(String.format("General journal batch line name issue: expected %s, got %s",
                    TEST_BANK_ACCOUNT_NAME, generalJournalBatchLine.getBankAccountName()));
        }
    }

    private BankAccountDTO generateBankAccount() {
        BankAccountDTO result = new BankAccountDTO();
        result.setCode(TEST_BANK_ACCOUNT_CODE);
        result.setName(TEST_BANK_ACCOUNT_NAME);
        return result;
    }

    private GeneralJournalBatchLineDTO generateGeneralJournalBatchLine() {
        GeneralJournalBatchLineDTO result = new GeneralJournalBatchLineDTO();
        result.setType(GeneralJournalBatchLineType.CUSTOMER);
        result.setCode(TEST_CODE);
        result.setName(TEST_NAME);
        result.setOperationType(GeneralJournalBatchLineOperationType.ORDER);
        result.setDocumentCode(TEST_DOCUMENT_CODE);
        result.setDate(TEST_DATE);
        result.setAmount(TEST_AMOUNT);
        result.setBankAccountCode(TEST_BANK_ACCOUNT_CODE);
        return result;
    }

    private void verifyGeneralJournalBatchDeleted(GeneralJournalBatchDTO generalJournalBatch)
            throws ITestFailedException {
        if (generalJournalBatch != null) {
            throw new ITestFailedException("General journal batch should not exist after deleted");
        }
    }

    private void verifyGeneralJournalBatchUpdated(GeneralJournalBatchDTO generalJournalBatch)
            throws ITestFailedException {
        if (!generalJournalBatch.getName().equals(UPDATED_NAME)) {
            throw new ITestFailedException(String.format("General journal batch name issue: expected %s, got %s",
                    UPDATED_NAME, generalJournalBatch.getName()));
        }
    }

    private GeneralJournalBatchDTO generateUpdatedGeneralJournalBatch() {
        GeneralJournalBatchDTO result = new GeneralJournalBatchDTO();
        result.setName(UPDATED_NAME);
        return result;
    }

    private void verifyGeneralJournalBatchRead(GeneralJournalBatchDTO generalJournalBatch) throws ITestFailedException {
        if (!generalJournalBatch.getCode().equals(TEST_CODE)) {
            throw new ITestFailedException(String.format("General journal batch code issue: expected %s, got %s",
                    TEST_CODE, generalJournalBatch.getCode()));
        }

        if (!generalJournalBatch.getName().equals(TEST_NAME)) {
            throw new ITestFailedException(String.format("General journal batch name issue: expected %s, got %s",
                    TEST_NAME, generalJournalBatch.getName()));
        }
    }

    private GeneralJournalBatchDTO generateGeneralJournalBatch() {
        GeneralJournalBatchDTO result = new GeneralJournalBatchDTO();
        result.setCode(TEST_CODE);
        result.setName(TEST_NAME);
        return result;
    }

    private void print(String msg) {
        System.out.println(msg);
    }
}
