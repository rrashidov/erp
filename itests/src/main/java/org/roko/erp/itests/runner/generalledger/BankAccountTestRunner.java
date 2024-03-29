package org.roko.erp.itests.runner.generalledger;

import org.roko.erp.dto.BankAccountDTO;
import org.roko.erp.itests.clients.BankAccountClient;
import org.roko.erp.itests.runner.BaseTestRunner;
import org.roko.erp.itests.runner.ITestFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BankAccountTestRunner extends BaseTestRunner {

    private static final String TEST_BANK_ACCOUNT_CODE = "test-bank-account-code";
    private static final String TEST_BANK_ACCOUNT_NAME = "test-bank-account-name";

    private static final String UPDATED_BANK_ACCOUNT_NAME = "updated-bank-account-name";

    private BankAccountClient client;

    @Autowired
    public BankAccountTestRunner(BankAccountClient client) {
        this.client = client;
    }

    @Override
    public void run() throws ITestFailedException {
        LOGGER.info("Running BankAccount create test");
        BankAccountDTO bankAccount = generateBankAccount();
        client.create(bankAccount);
        LOGGER.info("BankAccount create test passed");

        LOGGER.info("Running BankAccount read test");
        bankAccount = client.read(TEST_BANK_ACCOUNT_CODE);
        verifyBankAccountRead(bankAccount);
        LOGGER.info("BankAccount read test passed");
        
        LOGGER.info("Running BankAccount update test");
        bankAccount = generateUpdatedBankAccount();
        client.update(TEST_BANK_ACCOUNT_CODE, bankAccount);
        bankAccount = client.read(TEST_BANK_ACCOUNT_CODE);
        verifyUpdatedBankAccount(bankAccount);
        LOGGER.info("BankAccount update test passed");
        
        LOGGER.info("Running BankAccount delete test");
        client.delete(TEST_BANK_ACCOUNT_CODE);
        bankAccount = client.read(TEST_BANK_ACCOUNT_CODE);
        verifyBankAccountDeleted(bankAccount);
        LOGGER.info("BankAccount delete test passed");
        
    }

    private void verifyBankAccountDeleted(BankAccountDTO bankAccount) throws ITestFailedException {
        if (bankAccount != null) {
            throw new ITestFailedException("BankAccount expected to be deleted, but not");
        }
    }

    private void verifyUpdatedBankAccount(BankAccountDTO bankAccount) throws ITestFailedException {
        if (!bankAccount.getName().equals(UPDATED_BANK_ACCOUNT_NAME)) {
            throw new ITestFailedException(String.format("BankAccount name problem: expected %s, got %s", UPDATED_BANK_ACCOUNT_NAME, bankAccount.getName()));
        }
    }

    private BankAccountDTO generateUpdatedBankAccount() {
        BankAccountDTO result = new BankAccountDTO();
        result.setName(UPDATED_BANK_ACCOUNT_NAME);
        return result;
    }

    private void verifyBankAccountRead(BankAccountDTO bankAccount) throws ITestFailedException {
        if (!bankAccount.getCode().equals(TEST_BANK_ACCOUNT_CODE)) {
            throw new ITestFailedException(String.format("BankAccount code problem: expected %s, got %s", TEST_BANK_ACCOUNT_CODE, bankAccount.getCode()));
        }

        if (!bankAccount.getName().equals(TEST_BANK_ACCOUNT_NAME)) {
            throw new ITestFailedException(String.format("BankAccount name problem: expected %s, got %s", TEST_BANK_ACCOUNT_NAME, bankAccount.getName()));
        }
    }

    private BankAccountDTO generateBankAccount() {
        BankAccountDTO result = new BankAccountDTO();
        result.setCode(TEST_BANK_ACCOUNT_CODE);
        result.setName(TEST_BANK_ACCOUNT_NAME);
        return result;
    }

}
