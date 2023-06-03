package org.roko.erp.itests.runner.sales;

import org.roko.erp.dto.CustomerDTO;
import org.roko.erp.dto.PaymentMethodDTO;
import org.roko.erp.itests.clients.CustomerClient;
import org.roko.erp.itests.clients.PaymentMethodClient;
import org.roko.erp.itests.runner.ITestFailedException;
import org.roko.erp.itests.runner.ITestRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class CustomerTestRunner implements ITestRunner {

    private static final String SECOND_PAYMENT_METHOD_CODE = "second-payment-method-code";
    private static final String SECOND_PAYMENT_METHOD_NAME = "second-payment-method-name";

    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";
    private static final String TEST_PAYMENT_METHOD_NAME = "test-payment-method-name";

    private static final String TEST_CUSTOMER_CODE = "test-customer-code";
    private static final String TEST_CUSTOMER_NAME = "test-customer-name";
    private static final String TEST_CUSTOMER_ADDRESS = "test-customer-address";

    private static final String UPDATED_CUSTOMER_NAME = "updated-customer-name";
    private static final String UPDATED_CUSTOMER_ADDRESS = "updated-customer-address";

    private CustomerClient client;

    private PaymentMethodClient paymentMethodClient;

    @Autowired
    public CustomerTestRunner(CustomerClient client, PaymentMethodClient paymentMethodClient) {
        this.client = client;
        this.paymentMethodClient = paymentMethodClient;
    }

    @Override
    public void run() throws ITestFailedException {
        print("Running Customer create test");
        PaymentMethodDTO paymentMethod = generatePaymentMethod();
        paymentMethodClient.create(paymentMethod);

        CustomerDTO customer = generateCustomer();
        client.create(customer);
        print("Customer create test passed");

        print("Running Customer read test");
        customer = client.read(TEST_CUSTOMER_CODE);
        verifyCustomerRead(customer);
        print("Customer read test passed");

        print("Running Customer update test");
        paymentMethod = generateSecondPaymentMethod();
        paymentMethodClient.create(paymentMethod);

        customer = generateCustomerUpdate();
        client.update(TEST_CUSTOMER_CODE, customer);
        customer = client.read(TEST_CUSTOMER_CODE);
        verifyCustomerUpdated(customer);
        print("Customer update test passed");

        print("Running Customer delete test");
        client.delete(TEST_CUSTOMER_CODE);
        customer = client.read(TEST_CUSTOMER_CODE);
        verifyCustomerDeleted(customer);

        paymentMethodClient.delete(TEST_PAYMENT_METHOD_CODE);
        paymentMethodClient.delete(SECOND_PAYMENT_METHOD_CODE);
        print("Customer delete test passed");
    }

    private void verifyCustomerDeleted(CustomerDTO customer) throws ITestFailedException {
        if (customer != null) {
            throw new ITestFailedException("Customer should not exist when deleted");
        }
    }

    private void verifyCustomerUpdated(CustomerDTO customer) throws ITestFailedException {
        if (!customer.getName().equals(UPDATED_CUSTOMER_NAME)) {
            throw new ITestFailedException(String.format("Customer name problem: expected %s, got %s",
                    UPDATED_CUSTOMER_NAME, customer.getName()));
        }

        if (!customer.getAddress().equals(UPDATED_CUSTOMER_ADDRESS)) {
            throw new ITestFailedException(String.format("Customer address problem: expected %s, got %s",
                    UPDATED_CUSTOMER_ADDRESS, customer.getAddress()));
        }

        if (!customer.getPaymentMethodCode().equals(SECOND_PAYMENT_METHOD_CODE)) {
            throw new ITestFailedException(String.format("Customer payment method code problem: expected %s, got %s",
                    SECOND_PAYMENT_METHOD_CODE, customer.getPaymentMethodCode()));
        }

        if (!customer.getPaymentMethodName().equals(SECOND_PAYMENT_METHOD_NAME)) {
            throw new ITestFailedException(String.format("Customer payment method name problem: expected %s, got %s",
                    SECOND_PAYMENT_METHOD_NAME, customer.getPaymentMethodName()));
        }
    }

    private PaymentMethodDTO generateSecondPaymentMethod() {
        PaymentMethodDTO result = new PaymentMethodDTO();
        result.setCode(SECOND_PAYMENT_METHOD_CODE);
        result.setName(SECOND_PAYMENT_METHOD_NAME);
        result.setBankAccountCode("");
        return result;
    }

    private CustomerDTO generateCustomerUpdate() {
        CustomerDTO result = new CustomerDTO();
        result.setName(UPDATED_CUSTOMER_NAME);
        result.setAddress(UPDATED_CUSTOMER_ADDRESS);
        result.setPaymentMethodCode(SECOND_PAYMENT_METHOD_CODE);
        return result;
    }

    private PaymentMethodDTO generatePaymentMethod() {
        PaymentMethodDTO result = new PaymentMethodDTO();
        result.setCode(TEST_PAYMENT_METHOD_CODE);
        result.setName(TEST_PAYMENT_METHOD_NAME);
        return result;
    }

    private void verifyCustomerRead(CustomerDTO customer) throws ITestFailedException {
        if (!customer.getCode().equals(TEST_CUSTOMER_CODE)) {
            throw new ITestFailedException(String.format("Customer code problem: expected: %s, got: %s",
                    TEST_CUSTOMER_CODE, customer.getCode()));
        }

        if (!customer.getName().equals(TEST_CUSTOMER_NAME)) {
            throw new ITestFailedException(String.format("Customer name problem: expected %s, got %s",
                    TEST_CUSTOMER_NAME, customer.getName()));
        }

        if (!customer.getAddress().equals(TEST_CUSTOMER_ADDRESS)) {
            throw new ITestFailedException(String.format("Customer address problem: expected %s, got %s",
                    TEST_CUSTOMER_ADDRESS, customer.getAddress()));
        }

        if (!customer.getPaymentMethodCode().equals(TEST_PAYMENT_METHOD_CODE)) {
            throw new ITestFailedException(String.format("Customer payment method code problem: expected %s, got %s",
                    TEST_PAYMENT_METHOD_CODE, customer.getPaymentMethodCode()));
        }

        if (!customer.getPaymentMethodName().equals(TEST_PAYMENT_METHOD_NAME)) {
            throw new ITestFailedException(String.format("Customer payment method name problem: expected %s, got %s",
                    TEST_PAYMENT_METHOD_NAME, customer.getPaymentMethodName()));
        }
    }

    private CustomerDTO generateCustomer() {
        CustomerDTO result = new CustomerDTO();
        result.setCode(TEST_CUSTOMER_CODE);
        result.setName(TEST_CUSTOMER_NAME);
        result.setAddress(TEST_CUSTOMER_ADDRESS);
        result.setPaymentMethodCode(TEST_PAYMENT_METHOD_CODE);
        return result;
    }

    private void print(String msg) {
        System.out.println(msg);
    }

}
