package org.roko.erp.itests.runner.sales;

import java.util.Calendar;
import java.util.Date;

import org.roko.erp.dto.CodeSerieDTO;
import org.roko.erp.dto.CustomerDTO;
import org.roko.erp.dto.ItemDTO;
import org.roko.erp.dto.PaymentMethodDTO;
import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.dto.SalesDocumentLineDTO;
import org.roko.erp.dto.SetupDTO;
import org.roko.erp.itests.clients.CodeSerieClient;
import org.roko.erp.itests.clients.CustomerClient;
import org.roko.erp.itests.clients.ItemClient;
import org.roko.erp.itests.clients.PaymentMethodClient;
import org.roko.erp.itests.clients.SalesCreditMemoClient;
import org.roko.erp.itests.clients.SalesCreditMemoLineClient;
import org.roko.erp.itests.clients.SetupClient;
import org.roko.erp.itests.runner.BaseTestRunner;
import org.roko.erp.itests.runner.ITestFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SalesCreditMemoTestRunner extends BaseTestRunner {

    private static final String SECOND_ITEM_CODE = "second-item-code";
    private static final String SECOND_ITEM_NAME = "second-item-name";

    private static final String TEST_ITEM_CODE = "test-item-code";
    private static final String TEST_ITEM_NAME = "test-item-name";
    private static final double TEST_ITEM_PURCHASE_PRICE = 1.0;
    private static final double TEST_ITEM_SALES_PRICE = 2.0;

    private static final String EXPECTED_SALES_CreditMemo_CODE = "SCM001";

    private static final String CODE_SERIE_CODE = "CS01";
    private static final String CODE_SERIE_NAME = "SalesCreditMemo code serie";
    private static final String CODE_SERIE_USED_CODE = "SCM000";

    private static final String SECOND_CUSTOMER_CODE = "second-customer-code";
    private static final String SECOND_CUSTOMER_NAME = "second-customer-name";
    private static final String SECOND_CUSTOMER_ADDRESS = "second-customer-address";

    private static final String TEST_CUSTOMER_CODE = "test-customer-code";
    private static final String TEST_CUSTOMER_NAME = "test-customer-name";
    private static final String TEST_CUSTOMER_ADDRESS = "test-customer-address";

    private static final String SECOND_PAYMENT_METHOD_CODE = "second-payment-method-code";
    private static final String SECOND_PAYMENT_METHOD_NAME = "second-payment-method-name";

    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";
    private static final String TEST_PAYMENT_METHOD_NAME = "test-payment-method-name";

    private static final double TEST_DOUBLE = 1.0;
    private static final double UPDATED_DOUBLE = 3.0;

    private static final Date TEST_DATE = Calendar.getInstance().getTime();

    private SalesCreditMemoClient client;

    private SalesCreditMemoLineClient linesClient;

    private PaymentMethodClient paymentMethodClient;

    private CustomerClient customerClient;

    private CodeSerieClient codeSerieClient;

    private SetupClient setupClient;

    private ItemClient itemClient;

    @Autowired
    public SalesCreditMemoTestRunner(SalesCreditMemoClient client, SalesCreditMemoLineClient linesClient, 
            PaymentMethodClient paymentMethodClient,
            CustomerClient customerClient,
            CodeSerieClient codeSerieClient, SetupClient setupClient, ItemClient itemClient) {
        this.client = client;
        this.linesClient = linesClient;
        this.paymentMethodClient = paymentMethodClient;
        this.customerClient = customerClient;
        this.codeSerieClient = codeSerieClient;
        this.setupClient = setupClient;
        this.itemClient = itemClient;
    }

    @Override
    public void run() throws ITestFailedException {
        LOGGER.info("Running SalesCreditMemo create test");
        initPaymentMethods();
        initCustomers();
        initSalesCreditMemoCodeSerie();

        SalesDocumentDTO salesCreditMemo = generateSalesCreditMemo();
        String salesCreditMemoCode = client.create(salesCreditMemo);
        LOGGER.info("SalesCreditMemo create test passed");

        LOGGER.info("Running SalesCreditMemo read test");
        salesCreditMemo = client.read(salesCreditMemoCode);
        verifySalesCreditMemoRead(salesCreditMemo);
        LOGGER.info("SalesCreditMemo read test passed");

        LOGGER.info("Running SalesCreditMemo update test");
        salesCreditMemo = generateSalesCreditMemoUpdate();
        client.update(salesCreditMemoCode, salesCreditMemo);
        salesCreditMemo = client.read(salesCreditMemoCode);
        verifySalesCreditMemoUpdated(salesCreditMemo);
        LOGGER.info("SalesCreditMemo update test passed");

        testSalesCreditMemoLines(salesCreditMemoCode);

        LOGGER.info("Running SalesCreditMemo delete test");
        client.delete(salesCreditMemoCode);
        salesCreditMemo = client.read(salesCreditMemoCode);
        verifySalesCreditMemoDeleted(salesCreditMemo);

        customerClient.delete(TEST_CUSTOMER_CODE);
        customerClient.delete(SECOND_CUSTOMER_CODE);

        paymentMethodClient.delete(TEST_PAYMENT_METHOD_CODE);
        paymentMethodClient.delete(SECOND_PAYMENT_METHOD_CODE);
        LOGGER.info("SalesCreditMemo delete test passed");
    }

    private void testSalesCreditMemoLines(String id) throws ITestFailedException {
        LOGGER.info("Running SalesCreditMemoLine create test");
        ItemDTO item = createItem();
        itemClient.create(item);
        item = generateSecondItem();
        itemClient.create(item);

        SalesDocumentLineDTO salesCreditMemoLine = generateSalesCreditMemoLine(id);
        int lineNo = linesClient.create(id, salesCreditMemoLine);
        LOGGER.info("Create sales credit memo line lineno: " + lineNo);
        LOGGER.info("SalesCreditMemoLine create test passed");

        LOGGER.info("Running SalesCreditMemoLine read test");
        salesCreditMemoLine = linesClient.read(id, lineNo);
        verifySalesCreditMemoLineRead(salesCreditMemoLine);
        LOGGER.info("SalesCreditMemoLine read test passed");
        
        LOGGER.info("Running SalesCreditMemoLine update test");
        salesCreditMemoLine = generateSalesCreditMemoLineUpdate();
        linesClient.update(id, lineNo, salesCreditMemoLine);
        salesCreditMemoLine = linesClient.read(id, lineNo);
        verifySalesCreditMemoLineUpdated(salesCreditMemoLine);
        LOGGER.info("SalesCreditMemoLine update test passed");
        
        LOGGER.info("Running SalesCreditMemoLine delete test");
        linesClient.delete(id, lineNo);
        salesCreditMemoLine = linesClient.read(id, lineNo);
        verifySalesCreditMemoLineDeleted(salesCreditMemoLine);

        itemClient.delete(TEST_ITEM_CODE);
        itemClient.delete(SECOND_ITEM_CODE);
        LOGGER.info("SalesCreditMemoLine delete test passed");
    }

    private void verifySalesCreditMemoLineDeleted(SalesDocumentLineDTO salesCreditMemoLine) throws ITestFailedException {
        if (salesCreditMemoLine != null) {
            throw new ITestFailedException("SalesCreditMemoLine should not exist when deleted");
        }
    }

    private void verifySalesCreditMemoLineUpdated(SalesDocumentLineDTO salesCreditMemoLine) throws ITestFailedException {
        if (!salesCreditMemoLine.getItemCode().equals(SECOND_ITEM_CODE)) {
            throw new ITestFailedException(String.format("SalesCreditMemoLine item code issue: expected %s, got %s",
            SECOND_ITEM_CODE, salesCreditMemoLine.getItemCode()));
        }

        if (!salesCreditMemoLine.getItemName().equals(SECOND_ITEM_NAME)) {
            throw new ITestFailedException(String.format("SalesCreditMemoLine item name issue: expected %s, got %s",
            SECOND_ITEM_NAME, salesCreditMemoLine.getItemName()));
        }

        if (!(salesCreditMemoLine.getQuantity() == UPDATED_DOUBLE)) {
            throw new ITestFailedException(String.format("SalesCreditMemoLine quantity issue: expected %f, got %f",
            UPDATED_DOUBLE, salesCreditMemoLine.getQuantity()));
        }

        if (!(salesCreditMemoLine.getPrice() == UPDATED_DOUBLE)) {
            throw new ITestFailedException(String.format("SalesCreditMemoLine price issue: expected %f, got %f", UPDATED_DOUBLE,
                    salesCreditMemoLine.getPrice()));
        }

        if (!(salesCreditMemoLine.getAmount() == UPDATED_DOUBLE)) {
            throw new ITestFailedException(String.format("SalesCreditMemoLine amount issue: expected %f, got %f",
            UPDATED_DOUBLE, salesCreditMemoLine.getAmount()));
        }
    }

    private SalesDocumentLineDTO generateSalesCreditMemoLineUpdate() {
        SalesDocumentLineDTO result = new SalesDocumentLineDTO();
        result.setItemCode(SECOND_ITEM_CODE);
        result.setQuantity(UPDATED_DOUBLE);
        result.setPrice(UPDATED_DOUBLE);
        result.setAmount(UPDATED_DOUBLE);
        return result;
    }

    private ItemDTO generateSecondItem() {
        ItemDTO result = new ItemDTO();
        result.setCode(SECOND_ITEM_CODE);
        result.setName(SECOND_ITEM_NAME);
        result.setSalesPrice(UPDATED_DOUBLE);
        result.setPurchasePrice(UPDATED_DOUBLE);
        return result;
    }

    private void verifySalesCreditMemoLineRead(SalesDocumentLineDTO salesCreditMemoLine)
            throws ITestFailedException {
        if (!salesCreditMemoLine.getItemCode().equals(TEST_ITEM_CODE)) {
            throw new ITestFailedException(String.format("SalesCreditMemoLine item code issue: expected %s, got %s",
                    TEST_ITEM_CODE, salesCreditMemoLine.getItemCode()));
        }

        if (!salesCreditMemoLine.getItemName().equals(TEST_ITEM_NAME)) {
            throw new ITestFailedException(String.format("SalesCreditMemoLine item name issue: expected %s, got %s",
                    TEST_ITEM_NAME, salesCreditMemoLine.getItemName()));
        }

        if (!(salesCreditMemoLine.getQuantity() == TEST_DOUBLE)) {
            throw new ITestFailedException(String.format("SalesCreditMemoLine quantity issue: expected %f, got %f",
                    TEST_DOUBLE, salesCreditMemoLine.getQuantity()));
        }

        if (!(salesCreditMemoLine.getPrice() == TEST_DOUBLE)) {
            throw new ITestFailedException(String.format("SalesCreditMemoLine price issue: expected %f, got %f", TEST_DOUBLE,
                    salesCreditMemoLine.getPrice()));
        }

        if (!(salesCreditMemoLine.getAmount() == TEST_DOUBLE)) {
            throw new ITestFailedException(String.format("SalesCreditMemoLine amount issue: expected %f, got %f",
                    TEST_DOUBLE, salesCreditMemoLine.getAmount()));
        }
    }

    private SalesDocumentLineDTO generateSalesCreditMemoLine(String id) {
        SalesDocumentLineDTO result = new SalesDocumentLineDTO();
        result.setItemCode(TEST_ITEM_CODE);
        result.setQuantity(TEST_DOUBLE);
        result.setPrice(1.0);
        result.setAmount(1.0);
        return result;
    }

    private ItemDTO createItem() {
        ItemDTO result = new ItemDTO();
        result.setCode(TEST_ITEM_CODE);
        result.setName(TEST_ITEM_NAME);
        result.setPurchasePrice(TEST_ITEM_PURCHASE_PRICE);
        result.setSalesPrice(TEST_ITEM_SALES_PRICE);
        return result;
    }

    private void verifySalesCreditMemoDeleted(SalesDocumentDTO salesCreditMemo) throws ITestFailedException {
        if (salesCreditMemo != null){
            throw new ITestFailedException("Sales CreditMemo should not exist when deleted");
        }
    }

    private void verifySalesCreditMemoUpdated(SalesDocumentDTO salesCreditMemo) throws ITestFailedException {
        if (!salesCreditMemo.getCustomerCode().equals(SECOND_CUSTOMER_CODE)) {
            throw new ITestFailedException(String.format("Sales CreditMemo customer code issue: expected %s, got %s",
                    SECOND_CUSTOMER_CODE, salesCreditMemo.getCustomerCode()));
        }

        if (!salesCreditMemo.getCustomerName().equals(SECOND_CUSTOMER_NAME)) {
            throw new ITestFailedException(String.format("Sales CreditMemo customer name issue: expected %s, got %s",
                    SECOND_CUSTOMER_NAME, salesCreditMemo.getCustomerName()));
        }

        if (!salesCreditMemo.getPaymentMethodCode().equals(SECOND_PAYMENT_METHOD_CODE)) {
            throw new ITestFailedException(String.format("Sales CreditMemo payment method code issue: expected %s, got %s",
                    SECOND_PAYMENT_METHOD_CODE, salesCreditMemo.getPaymentMethodCode()));
        }

        if (!salesCreditMemo.getPaymentMethodName().equals(SECOND_PAYMENT_METHOD_NAME)) {
            throw new ITestFailedException(String.format("Sales CreditMemo payment method name issue: expected %s, got %s",
                    SECOND_PAYMENT_METHOD_NAME, salesCreditMemo.getPaymentMethodName()));
        }
    }

    private SalesDocumentDTO generateSalesCreditMemoUpdate() {
        SalesDocumentDTO result = new SalesDocumentDTO();
        result.setCustomerCode(SECOND_CUSTOMER_CODE);
        result.setDate(TEST_DATE);
        result.setPaymentMethodCode(SECOND_PAYMENT_METHOD_CODE);
        return result;
    }

    private void verifySalesCreditMemoRead(SalesDocumentDTO salesCreditMemo) throws ITestFailedException {
        if (!salesCreditMemo.getCode().equals(EXPECTED_SALES_CreditMemo_CODE)) {
            throw new ITestFailedException(
                    String.format("Sales CreditMemo code issue: expected %s, got %s", EXPECTED_SALES_CreditMemo_CODE,
                            salesCreditMemo.getCode()));
        }

        if (!salesCreditMemo.getCustomerCode().equals(TEST_CUSTOMER_CODE)) {
            throw new ITestFailedException(String.format("Sales CreditMemo customer code issue: expected %s, got %s",
                    TEST_CUSTOMER_CODE, salesCreditMemo.getCustomerCode()));
        }

        if (!salesCreditMemo.getCustomerName().equals(TEST_CUSTOMER_NAME)) {
            throw new ITestFailedException(String.format("Sales CreditMemo customer name issue: expected %s, got %s",
                    TEST_CUSTOMER_NAME, salesCreditMemo.getCustomerName()));
        }

        if (!salesCreditMemo.getPaymentMethodCode().equals(TEST_PAYMENT_METHOD_CODE)) {
            throw new ITestFailedException(String.format("Sales CreditMemo payment method code issue: expected %s, got %s",
                    TEST_PAYMENT_METHOD_CODE, salesCreditMemo.getPaymentMethodCode()));
        }

        if (!salesCreditMemo.getPaymentMethodName().equals(TEST_PAYMENT_METHOD_NAME)) {
            throw new ITestFailedException(String.format("Sales CreditMemo payment method name issue: expected %s, got %s",
                    TEST_PAYMENT_METHOD_NAME, salesCreditMemo.getPaymentMethodName()));
        }
    }

    private SalesDocumentDTO generateSalesCreditMemo() {
        SalesDocumentDTO result = new SalesDocumentDTO();
        result.setCustomerCode(TEST_CUSTOMER_CODE);
        result.setDate(TEST_DATE);
        result.setPaymentMethodCode(TEST_PAYMENT_METHOD_CODE);
        return result;
    }

    private void initSalesCreditMemoCodeSerie() {
        CodeSerieDTO salesCreditMemoCodeSerie = generateCodeSerie();
        codeSerieClient.create(salesCreditMemoCodeSerie);

        SetupDTO setup = setupClient.read();
        setup.setSalesCreditMemoCodeSerieCode(CODE_SERIE_CODE);
        setupClient.update(setup);
    }

    private CodeSerieDTO generateCodeSerie() {
        CodeSerieDTO result = new CodeSerieDTO();
        result.setCode(CODE_SERIE_CODE);
        result.setName(CODE_SERIE_NAME);
        result.setFirstCode(CODE_SERIE_USED_CODE);
        result.setLastCode(CODE_SERIE_USED_CODE);
        return result;
    }

    private void initCustomers() {
        CustomerDTO customer = generateCustomer();
        customerClient.create(customer);
        customer = generateSecondCustomer();
        customerClient.create(customer);
    }

    private CustomerDTO generateSecondCustomer() {
        CustomerDTO result = new CustomerDTO();
        result.setCode(SECOND_CUSTOMER_CODE);
        result.setName(SECOND_CUSTOMER_NAME);
        result.setAddress(SECOND_CUSTOMER_ADDRESS);
        result.setPaymentMethodCode(TEST_PAYMENT_METHOD_CODE);
        return result;
    }

    private CustomerDTO generateCustomer() {
        CustomerDTO result = new CustomerDTO();
        result.setCode(TEST_CUSTOMER_CODE);
        result.setName(TEST_CUSTOMER_NAME);
        result.setAddress(TEST_CUSTOMER_ADDRESS);
        result.setPaymentMethodCode(TEST_PAYMENT_METHOD_CODE);
        return result;
    }

    private void initPaymentMethods() {
        PaymentMethodDTO paymentMethod = generatePaymentMethod();
        paymentMethodClient.create(paymentMethod);
        paymentMethod = generateSecondPaymentMethod();
        paymentMethodClient.create(paymentMethod);
    }

    private PaymentMethodDTO generateSecondPaymentMethod() {
        PaymentMethodDTO result = new PaymentMethodDTO();
        result.setCode(SECOND_PAYMENT_METHOD_CODE);
        result.setName(SECOND_PAYMENT_METHOD_NAME);
        return result;
    }

    private PaymentMethodDTO generatePaymentMethod() {
        PaymentMethodDTO result = new PaymentMethodDTO();
        result.setCode(TEST_PAYMENT_METHOD_CODE);
        result.setName(TEST_PAYMENT_METHOD_NAME);
        return result;
    }

}
