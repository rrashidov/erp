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
import org.roko.erp.itests.clients.SalesOrderClient;
import org.roko.erp.itests.clients.SalesOrderLineClient;
import org.roko.erp.itests.clients.SetupClient;
import org.roko.erp.itests.runner.BaseTestRunner;
import org.roko.erp.itests.runner.ITestFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SalesOrderTestRunner extends BaseTestRunner {

    private static final String SECOND_ITEM_CODE = "second-item-code";
    private static final String SECOND_ITEM_NAME = "second-item-name";

    private static final String TEST_ITEM_CODE = "test-item-code";
    private static final String TEST_ITEM_NAME = "test-item-name";
    private static final double TEST_ITEM_PURCHASE_PRICE = 1.0;
    private static final double TEST_ITEM_SALES_PRICE = 2.0;

    private static final String EXPECTED_SALES_ORDER_CODE = "SO001";

    private static final String CODE_SERIE_CODE = "CS01";
    private static final String CODE_SERIE_NAME = "SalesOrder code serie";
    private static final String CODE_SERIE_USED_CODE = "SO000";

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

    private SalesOrderClient client;

    private SalesOrderLineClient linesClient;

    private PaymentMethodClient paymentMethodClient;

    private CustomerClient customerClient;

    private CodeSerieClient codeSerieClient;

    private SetupClient setupClient;

    private ItemClient itemClient;

    @Autowired
    public SalesOrderTestRunner(SalesOrderClient client, SalesOrderLineClient linesClient, 
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
        LOGGER.info("Running SalesOrder create test");
        initPaymentMethods();
        initCustomers();
        initSalesOrderCodeSerie();

        SalesDocumentDTO salesOrder = generateSalesOrder();
        String salesOrderCode = client.create(salesOrder);
        LOGGER.info("SalesOrder create test passed");

        LOGGER.info("Running SalesOrder read test");
        salesOrder = client.read(salesOrderCode);
        verifySalesOrderRead(salesOrder);
        LOGGER.info("SalesOrder read test passed");

        LOGGER.info("Running SalesOrder update test");
        salesOrder = generateSalesOrderUpdate();
        client.update(salesOrderCode, salesOrder);
        salesOrder = client.read(salesOrderCode);
        verifySalesOrderUpdated(salesOrder);
        LOGGER.info("SalesOrder update test passed");

        testSalesOrderLines(salesOrderCode);

        LOGGER.info("Running SalesOrder delete test");
        client.delete(salesOrderCode);
        salesOrder = client.read(salesOrderCode);
        verifySalesOrderDeleted(salesOrder);

        customerClient.delete(TEST_CUSTOMER_CODE);
        customerClient.delete(SECOND_CUSTOMER_CODE);

        paymentMethodClient.delete(TEST_PAYMENT_METHOD_CODE);
        paymentMethodClient.delete(SECOND_PAYMENT_METHOD_CODE);
        LOGGER.info("SalesOrder delete test passed");
    }

    private void testSalesOrderLines(String id) throws ITestFailedException {
        LOGGER.info("Running SalesOrderLine create test");
        ItemDTO item = createItem();
        itemClient.create(item);
        item = generateSecondItem();
        itemClient.create(item);

        SalesDocumentLineDTO salesOrderLine = generateSalesOrderLine(id);
        int lineNo = linesClient.create(id, salesOrderLine);
        LOGGER.info("SalesOrderLine create test passed");

        LOGGER.info("Running SalesOrderLine read test");
        salesOrderLine = linesClient.read(id, lineNo);
        verifySalesOrderLineRead(salesOrderLine);
        LOGGER.info("SalesOrderLine read test passed");
        
        LOGGER.info("Running SalesOrderLine update test");
        salesOrderLine = generateSalesOrderLineUpdate();
        linesClient.update(id, lineNo, salesOrderLine);
        salesOrderLine = linesClient.read(id, lineNo);
        verifySalesOrderLineUpdated(salesOrderLine);
        LOGGER.info("SalesOrderLine update test passed");
        
        LOGGER.info("Running SalesOrderLine delete test");
        linesClient.delete(id, lineNo);
        salesOrderLine = linesClient.read(id, lineNo);
        verifySalesOrderLineDeleted(salesOrderLine);

        itemClient.delete(TEST_ITEM_CODE);
        itemClient.delete(SECOND_ITEM_CODE);
        LOGGER.info("SalesOrderLine delete test passed");
    }

    private void verifySalesOrderLineDeleted(SalesDocumentLineDTO salesOrderLine) throws ITestFailedException {
        if (salesOrderLine != null) {
            throw new ITestFailedException("SalesOrderLine should not exist when deleted");
        }
    }

    private void verifySalesOrderLineUpdated(SalesDocumentLineDTO salesOrderLine) throws ITestFailedException {
        if (!salesOrderLine.getItemCode().equals(SECOND_ITEM_CODE)) {
            throw new ITestFailedException(String.format("SalesOrderLine item code issue: expected %s, got %s",
            SECOND_ITEM_CODE, salesOrderLine.getItemCode()));
        }

        if (!salesOrderLine.getItemName().equals(SECOND_ITEM_NAME)) {
            throw new ITestFailedException(String.format("SalesOrderLine item name issue: expected %s, got %s",
            SECOND_ITEM_NAME, salesOrderLine.getItemName()));
        }

        if (!(salesOrderLine.getQuantity() == UPDATED_DOUBLE)) {
            throw new ITestFailedException(String.format("SalesOrderLine quantity issue: expected %f, got %f",
            UPDATED_DOUBLE, salesOrderLine.getQuantity()));
        }

        if (!(salesOrderLine.getPrice() == UPDATED_DOUBLE)) {
            throw new ITestFailedException(String.format("SalesOrderLine price issue: expected %f, got %f", UPDATED_DOUBLE,
                    salesOrderLine.getPrice()));
        }

        if (!(salesOrderLine.getAmount() == UPDATED_DOUBLE)) {
            throw new ITestFailedException(String.format("SalesOrderLine amount issue: expected %f, got %f",
            UPDATED_DOUBLE, salesOrderLine.getAmount()));
        }
    }

    private SalesDocumentLineDTO generateSalesOrderLineUpdate() {
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

    private void verifySalesOrderLineRead(SalesDocumentLineDTO salesOrderLine)
            throws ITestFailedException {
        if (!salesOrderLine.getItemCode().equals(TEST_ITEM_CODE)) {
            throw new ITestFailedException(String.format("SalesOrderLine item code issue: expected %s, got %s",
                    TEST_ITEM_CODE, salesOrderLine.getItemCode()));
        }

        if (!salesOrderLine.getItemName().equals(TEST_ITEM_NAME)) {
            throw new ITestFailedException(String.format("SalesOrderLine item name issue: expected %s, got %s",
                    TEST_ITEM_NAME, salesOrderLine.getItemName()));
        }

        if (!(salesOrderLine.getQuantity() == TEST_DOUBLE)) {
            throw new ITestFailedException(String.format("SalesOrderLine quantity issue: expected %f, got %f",
                    TEST_DOUBLE, salesOrderLine.getQuantity()));
        }

        if (!(salesOrderLine.getPrice() == TEST_DOUBLE)) {
            throw new ITestFailedException(String.format("SalesOrderLine price issue: expected %f, got %f", TEST_DOUBLE,
                    salesOrderLine.getPrice()));
        }

        if (!(salesOrderLine.getAmount() == TEST_DOUBLE)) {
            throw new ITestFailedException(String.format("SalesOrderLine amount issue: expected %f, got %f",
                    TEST_DOUBLE, salesOrderLine.getAmount()));
        }
    }

    private SalesDocumentLineDTO generateSalesOrderLine(String id) {
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

    private void verifySalesOrderDeleted(SalesDocumentDTO salesOrder) throws ITestFailedException {
        if (salesOrder != null){
            throw new ITestFailedException("Sales order should not exist when deleted");
        }
    }

    private void verifySalesOrderUpdated(SalesDocumentDTO salesOrder) throws ITestFailedException {
        if (!salesOrder.getCustomerCode().equals(SECOND_CUSTOMER_CODE)) {
            throw new ITestFailedException(String.format("Sales order customer code issue: expected %s, got %s",
                    SECOND_CUSTOMER_CODE, salesOrder.getCustomerCode()));
        }

        if (!salesOrder.getCustomerName().equals(SECOND_CUSTOMER_NAME)) {
            throw new ITestFailedException(String.format("Sales order customer name issue: expected %s, got %s",
                    SECOND_CUSTOMER_NAME, salesOrder.getCustomerName()));
        }

        if (!salesOrder.getPaymentMethodCode().equals(SECOND_PAYMENT_METHOD_CODE)) {
            throw new ITestFailedException(String.format("Sales order payment method code issue: expected %s, got %s",
                    SECOND_PAYMENT_METHOD_CODE, salesOrder.getPaymentMethodCode()));
        }

        if (!salesOrder.getPaymentMethodName().equals(SECOND_PAYMENT_METHOD_NAME)) {
            throw new ITestFailedException(String.format("Sales order payment method name issue: expected %s, got %s",
                    SECOND_PAYMENT_METHOD_NAME, salesOrder.getPaymentMethodName()));
        }
    }

    private SalesDocumentDTO generateSalesOrderUpdate() {
        SalesDocumentDTO result = new SalesDocumentDTO();
        result.setCustomerCode(SECOND_CUSTOMER_CODE);
        result.setDate(TEST_DATE);
        result.setPaymentMethodCode(SECOND_PAYMENT_METHOD_CODE);
        return result;
    }

    private void verifySalesOrderRead(SalesDocumentDTO salesOrder) throws ITestFailedException {
        if (!salesOrder.getCode().equals(EXPECTED_SALES_ORDER_CODE)) {
            throw new ITestFailedException(
                    String.format("Sales order code issue: expected %s, got %s", EXPECTED_SALES_ORDER_CODE,
                            salesOrder.getCode()));
        }

        if (!salesOrder.getCustomerCode().equals(TEST_CUSTOMER_CODE)) {
            throw new ITestFailedException(String.format("Sales order customer code issue: expected %s, got %s",
                    TEST_CUSTOMER_CODE, salesOrder.getCustomerCode()));
        }

        if (!salesOrder.getCustomerName().equals(TEST_CUSTOMER_NAME)) {
            throw new ITestFailedException(String.format("Sales order customer name issue: expected %s, got %s",
                    TEST_CUSTOMER_NAME, salesOrder.getCustomerName()));
        }

        if (!salesOrder.getPaymentMethodCode().equals(TEST_PAYMENT_METHOD_CODE)) {
            throw new ITestFailedException(String.format("Sales order payment method code issue: expected %s, got %s",
                    TEST_PAYMENT_METHOD_CODE, salesOrder.getPaymentMethodCode()));
        }

        if (!salesOrder.getPaymentMethodName().equals(TEST_PAYMENT_METHOD_NAME)) {
            throw new ITestFailedException(String.format("Sales order payment method name issue: expected %s, got %s",
                    TEST_PAYMENT_METHOD_NAME, salesOrder.getPaymentMethodName()));
        }
    }

    private SalesDocumentDTO generateSalesOrder() {
        SalesDocumentDTO result = new SalesDocumentDTO();
        result.setCustomerCode(TEST_CUSTOMER_CODE);
        result.setDate(TEST_DATE);
        result.setPaymentMethodCode(TEST_PAYMENT_METHOD_CODE);
        return result;
    }

    private void initSalesOrderCodeSerie() {
        CodeSerieDTO salesOrderCodeSerie = generateCodeSerie();
        codeSerieClient.create(salesOrderCodeSerie);

        SetupDTO setup = setupClient.read();
        setup.setSalesOrderCodeSerieCode(CODE_SERIE_CODE);
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
