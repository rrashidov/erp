package org.roko.erp.itests.runner.generalledger;

import org.roko.erp.dto.BankAccountDTO;
import org.roko.erp.dto.PaymentMethodDTO;
import org.roko.erp.itests.clients.BankAccountClient;
import org.roko.erp.itests.clients.PaymentMethodClient;
import org.roko.erp.itests.runner.ITestFailedException;
import org.roko.erp.itests.runner.ITestRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodTestRunner implements ITestRunner {

    private static final String TEST_BANK_ACCOUNT_CODE = "test-bank-account-code";
    private static final String TEST_BANK_ACCOUNT_NAME = "test-bank-account-name";

    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";
    private static final String TEST_PAYMENT_METHOD_NAME = "test-payment-method-name";

    private static final String UPDATED_PAYMENT_METHOD_NAME = "updated-payment-method-name";

    private PaymentMethodClient client;
    private BankAccountClient bankAccountClient;

    @Autowired
    public PaymentMethodTestRunner(PaymentMethodClient client, BankAccountClient bankAccountClient) {
        this.client = client;
        this.bankAccountClient = bankAccountClient;
    }

    @Override
    public void run() throws ITestFailedException {
        print("Running PaymentMethod create test");
        PaymentMethodDTO paymentMethod = generatePaymentMethodDTO();
        client.create(paymentMethod);
        print("PaymentMethod create test passed");

        print("Running PaymentMethod read test");
        paymentMethod = client.read(TEST_PAYMENT_METHOD_CODE);
        verifyPaymentMethodRead(paymentMethod);
        print("PaymentMethod read test passed");
        
        print("Running PaymentMethod update test");
        BankAccountDTO bankAccount = generateBankAccount();
        bankAccountClient.create(bankAccount);
        paymentMethod = generateUpdatedPaymentMethod();
        client.update(TEST_PAYMENT_METHOD_CODE, paymentMethod);
        paymentMethod = client.read(TEST_PAYMENT_METHOD_CODE);
        verifyUpdatedPaymentMethod(paymentMethod);
        print("PaymentMethod update test passed");

        print("Running PaymentMethod delete test");
        client.delete(TEST_PAYMENT_METHOD_CODE);
        paymentMethod = client.read(TEST_PAYMENT_METHOD_CODE);
        verifyPaymentMethodDeleted(paymentMethod);
        bankAccountClient.delete(TEST_BANK_ACCOUNT_CODE);
        print("PaymentMethod delete test passed");

    }

    private void verifyPaymentMethodDeleted(PaymentMethodDTO paymentMethod) throws ITestFailedException {
        if (paymentMethod != null) {
            throw new ITestFailedException("PaymentMethod should not exist after deleted");
        }
    }

    private void verifyUpdatedPaymentMethod(PaymentMethodDTO paymentMethod) throws ITestFailedException {
        if (!paymentMethod.getName().equals(UPDATED_PAYMENT_METHOD_NAME)) {
            throw new ITestFailedException(String.format("PaymentMethod name issue: expected %s, got %s", UPDATED_PAYMENT_METHOD_NAME, paymentMethod.getName()));
        }

        if (!paymentMethod.getBankAccountCode().equals(TEST_BANK_ACCOUNT_CODE)) {
            throw new ITestFailedException(String.format("PaymentMethod bank account code issue: expected %s, got %s", TEST_BANK_ACCOUNT_CODE, paymentMethod.getBankAccountCode()));
        }

        if (!paymentMethod.getBankAccountName().equals(TEST_BANK_ACCOUNT_NAME)) {
            throw new ITestFailedException(String.format("PaymentMethod bank account name issue: expected %s, got %s", TEST_BANK_ACCOUNT_NAME, paymentMethod.getBankAccountName()));
        }
    }

    private PaymentMethodDTO generateUpdatedPaymentMethod() {
        PaymentMethodDTO result = new PaymentMethodDTO();
        result.setName(UPDATED_PAYMENT_METHOD_NAME);
        result.setBankAccountCode(TEST_BANK_ACCOUNT_CODE);
        return result;
    }

    private BankAccountDTO generateBankAccount() {
        BankAccountDTO result = new BankAccountDTO();
        result.setCode(TEST_BANK_ACCOUNT_CODE);
        result.setName(TEST_BANK_ACCOUNT_NAME);
        return result;
    }

    private void verifyPaymentMethodRead(PaymentMethodDTO paymentMethod) throws ITestFailedException {
        if (!paymentMethod.getCode().equals(TEST_PAYMENT_METHOD_CODE)) {
            throw new ITestFailedException(String.format("Payment method code issue: expected %s, got %s", TEST_PAYMENT_METHOD_CODE, paymentMethod.getCode()));
        }

        if (!paymentMethod.getName().equals(TEST_PAYMENT_METHOD_NAME)) {
            throw new ITestFailedException(String.format("Payment method name issue: expected %s, got %s", TEST_PAYMENT_METHOD_NAME, paymentMethod.getName()));
        }
    }

    private PaymentMethodDTO generatePaymentMethodDTO() {
        PaymentMethodDTO result = new PaymentMethodDTO();
        result.setCode(TEST_PAYMENT_METHOD_CODE);
        result.setName(TEST_PAYMENT_METHOD_NAME);
        return result;
    }

    private void print(String msg) {
        System.out.println(msg);
    }

}
