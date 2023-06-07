package org.roko.erp.itests.runner.purchases;

import org.roko.erp.dto.PaymentMethodDTO;
import org.roko.erp.dto.VendorDTO;
import org.roko.erp.itests.clients.PaymentMethodClient;
import org.roko.erp.itests.clients.VendorClient;
import org.roko.erp.itests.runner.ITestFailedException;
import org.roko.erp.itests.runner.ITestRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VendorTestRunner implements ITestRunner {

    private static final String SECOND_PAYMENT_METHOD_CODE = "second-payment-method-code";
    private static final String SECOND_PAYMENT_METHOD_NAME = "second-payment-method-name";

    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";
    private static final String TEST_PAYMENT_METHOD_NAME = "test-payment-method-name";

    private static final String TEST_VENDOR_CODE = "test-vendor-code";
    private static final String TEST_VENDOR_NAME = "test-vendor-name";
    private static final String TEST_VENDOR_ADDRESS = "test-vendor-address";

    private static final String UPDATED_VENDOR_NAME = "updated-vendor-name";
    private static final String UPDATED_VENDOR_ADDRESS = "updated-vendor-address";

    private VendorClient client;

    private PaymentMethodClient paymentMethodClient;

    @Autowired
    public VendorTestRunner(VendorClient client, PaymentMethodClient paymentMethodClient) {
        this.client = client;
        this.paymentMethodClient = paymentMethodClient;
    }

    @Override
    public void run() throws ITestFailedException {
        print("Running Vendor create test");
        PaymentMethodDTO paymentMethod = generatePaymentMethod();
        paymentMethodClient.create(paymentMethod);

        VendorDTO vendor = generateVendor();
        client.create(vendor);
        print("Vendor create test pased");

        print("Running Vendor read test");
        vendor = client.read(TEST_VENDOR_CODE);
        verifyVendorRead(vendor);
        print("Vendor read test pased");

        print("Running Vendor update test");
        paymentMethod = generateSecondPaymentMethod();
        paymentMethodClient.create(paymentMethod);

        vendor = generateVendorUpdate();
        client.update(TEST_VENDOR_CODE, vendor);
        vendor = client.read(TEST_VENDOR_CODE);
        verifyVendorUpdated(vendor);
        print("Vendor update test pased");

        print("Running Vendor delete test");
        client.delete(TEST_VENDOR_CODE);
        vendor = client.read(TEST_VENDOR_CODE);
        verifyVendorDeleted(vendor);

        paymentMethodClient.delete(TEST_PAYMENT_METHOD_CODE);
        paymentMethodClient.delete(SECOND_PAYMENT_METHOD_CODE);
        print("Vendor delete test pased");
    }

    private void verifyVendorDeleted(VendorDTO vendor) throws ITestFailedException {
        if (vendor != null) {
            throw new ITestFailedException("Vendor should not exist when deleted");
        }
    }

    private void verifyVendorUpdated(VendorDTO vendor) throws ITestFailedException {
        if (!vendor.getName().equals(UPDATED_VENDOR_NAME)) {
            throw new ITestFailedException(
                    String.format("Vendor name issue: expected %s, got %s", UPDATED_VENDOR_NAME, vendor.getName()));
        }

        if (!vendor.getAddress().equals(UPDATED_VENDOR_ADDRESS)) {
            throw new ITestFailedException(String.format("Vendor address issue: expected %s, got %s",
                    UPDATED_VENDOR_ADDRESS, vendor.getAddress()));
        }

        if (!vendor.getPaymentMethodCode().equals(SECOND_PAYMENT_METHOD_CODE)) {
            throw new ITestFailedException(String.format("Vendor payment method code issue: expected %s, got %s",
                    SECOND_PAYMENT_METHOD_CODE, vendor.getPaymentMethodCode()));
        }

        if (!vendor.getPaymentMethodName().equals(SECOND_PAYMENT_METHOD_NAME)) {
            throw new ITestFailedException(String.format("Vendor payment method name issue: expected %s, got %s",
                    SECOND_PAYMENT_METHOD_NAME, vendor.getPaymentMethodName()));
        }
    }

    private VendorDTO generateVendorUpdate() {
        VendorDTO result = new VendorDTO();
        result.setName(UPDATED_VENDOR_NAME);
        result.setAddress(UPDATED_VENDOR_ADDRESS);
        result.setPaymentMethodCode(SECOND_PAYMENT_METHOD_CODE);
        return result;
    }

    private PaymentMethodDTO generateSecondPaymentMethod() {
        PaymentMethodDTO result = new PaymentMethodDTO();
        result.setCode(SECOND_PAYMENT_METHOD_CODE);
        result.setName(SECOND_PAYMENT_METHOD_NAME);
        return result;
    }

    private void verifyVendorRead(VendorDTO vendor) throws ITestFailedException {
        if (!vendor.getCode().equals(TEST_VENDOR_CODE)) {
            throw new ITestFailedException(
                    String.format("Vendor code issue: expected %s, got %s", TEST_VENDOR_CODE, vendor.getCode()));
        }

        if (!vendor.getName().equals(TEST_VENDOR_NAME)) {
            throw new ITestFailedException(
                    String.format("Vendor name issue: expected %s, got %s", TEST_VENDOR_NAME, vendor.getName()));
        }

        if (!vendor.getAddress().equals(TEST_VENDOR_ADDRESS)) {
            throw new ITestFailedException(String.format("Vendor address issue: expected %s, got %s",
                    TEST_VENDOR_ADDRESS, vendor.getAddress()));
        }

        if (!vendor.getPaymentMethodCode().equals(TEST_PAYMENT_METHOD_CODE)) {
            throw new ITestFailedException(String.format("Vendor payment method code issue: expected %s, got %s",
                    TEST_PAYMENT_METHOD_CODE, vendor.getPaymentMethodCode()));
        }

        if (!vendor.getPaymentMethodName().equals(TEST_PAYMENT_METHOD_NAME)) {
            throw new ITestFailedException(String.format("Vendor payment method name issue: expected %s, got %s",
                    TEST_PAYMENT_METHOD_NAME, vendor.getPaymentMethodName()));
        }
    }

    private PaymentMethodDTO generatePaymentMethod() {
        PaymentMethodDTO result = new PaymentMethodDTO();
        result.setCode(TEST_PAYMENT_METHOD_CODE);
        result.setName(TEST_PAYMENT_METHOD_NAME);
        return result;
    }

    private VendorDTO generateVendor() {
        VendorDTO result = new VendorDTO();
        result.setCode(TEST_VENDOR_CODE);
        result.setName(TEST_VENDOR_NAME);
        result.setAddress(TEST_VENDOR_ADDRESS);
        result.setPaymentMethodCode(TEST_PAYMENT_METHOD_CODE);
        return result;
    }

    private void print(String msg) {
        System.out.println(msg);
    }
}
