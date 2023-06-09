package org.roko.erp.itests.runner.purchases;

import java.util.Calendar;
import java.util.Date;

import org.roko.erp.dto.CodeSerieDTO;
import org.roko.erp.dto.VendorDTO;
import org.roko.erp.dto.ItemDTO;
import org.roko.erp.dto.PaymentMethodDTO;
import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.dto.PurchaseDocumentLineDTO;
import org.roko.erp.dto.SetupDTO;
import org.roko.erp.itests.clients.CodeSerieClient;
import org.roko.erp.itests.clients.VendorClient;
import org.roko.erp.itests.clients.ItemClient;
import org.roko.erp.itests.clients.PaymentMethodClient;
import org.roko.erp.itests.clients.PurchaseOrderClient;
import org.roko.erp.itests.clients.PurchaseOrderLineClient;
import org.roko.erp.itests.clients.SetupClient;
import org.roko.erp.itests.runner.ITestFailedException;
import org.roko.erp.itests.runner.ITestRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderTestRunner implements ITestRunner {

    private static final String SECOND_ITEM_CODE = "second-item-code";
    private static final String SECOND_ITEM_NAME = "second-item-name";

    private static final String TEST_ITEM_CODE = "test-item-code";
    private static final String TEST_ITEM_NAME = "test-item-name";
    private static final double TEST_ITEM_SALES_PRICE = 1.0;
    private static final double TEST_ITEM_PURCHASE_PRICE = 2.0;

    private static final String EXPECTED_PURCHASE_ORDER_CODE = "PO001";

    private static final String CODE_SERIE_CODE = "CS01";
    private static final String CODE_SERIE_NAME = "PurchaseOrder code serie";
    private static final String CODE_SERIE_USED_CODE = "PO000";

    private static final String SECOND_Vendor_CODE = "second-Vendor-code";
    private static final String SECOND_Vendor_NAME = "second-Vendor-name";
    private static final String SECOND_Vendor_ADDRESS = "second-Vendor-address";

    private static final String TEST_Vendor_CODE = "test-Vendor-code";
    private static final String TEST_Vendor_NAME = "test-Vendor-name";
    private static final String TEST_Vendor_ADDRESS = "test-Vendor-address";

    private static final String SECOND_PAYMENT_METHOD_CODE = "second-payment-method-code";
    private static final String SECOND_PAYMENT_METHOD_NAME = "second-payment-method-name";

    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";
    private static final String TEST_PAYMENT_METHOD_NAME = "test-payment-method-name";

    private static final double TEST_DOUBLE = 1.0;
    private static final double UPDATED_DOUBLE = 3.0;

    private static final Date TEST_DATE = Calendar.getInstance().getTime();

    private PurchaseOrderClient client;

    private PurchaseOrderLineClient linesClient;

    private PaymentMethodClient paymentMethodClient;

    private VendorClient vendorClient;

    private CodeSerieClient codeSerieClient;

    private SetupClient setupClient;

    private ItemClient itemClient;

    @Autowired
    public PurchaseOrderTestRunner(PurchaseOrderClient client, PurchaseOrderLineClient linesClient, 
            PaymentMethodClient paymentMethodClient,
            VendorClient VendorClient,
            CodeSerieClient codeSerieClient, SetupClient setupClient, ItemClient itemClient) {
        this.client = client;
        this.linesClient = linesClient;
        this.paymentMethodClient = paymentMethodClient;
        this.vendorClient = VendorClient;
        this.codeSerieClient = codeSerieClient;
        this.setupClient = setupClient;
        this.itemClient = itemClient;
    }

    @Override
    public void run() throws ITestFailedException {
        print("Running PurchaseOrder create test");
        initPaymentMethods();
        initVendors();
        initPurchaseOrderCodeSerie();

        PurchaseDocumentDTO purchaseOrder = generatePurchaseOrder();
        String purchaseOrderCode = client.create(purchaseOrder);
        print("PurchaseOrder create test passed");

        print("Running PurchaseOrder read test");
        purchaseOrder = client.read(purchaseOrderCode);
        verifyPurchaseOrderRead(purchaseOrder);
        print("PurchaseOrder read test passed");

        print("Running PurchaseOrder update test");
        purchaseOrder = generatePurchaseOrderUpdate();
        client.update(purchaseOrderCode, purchaseOrder);
        purchaseOrder = client.read(purchaseOrderCode);
        verifyPurchaseOrderUpdated(purchaseOrder);
        print("PurchaseOrder update test passed");

        testPurchaseOrderLines(purchaseOrderCode);

        print("Running PurchaseOrder delete test");
        client.delete(purchaseOrderCode);
        purchaseOrder = client.read(purchaseOrderCode);
        verifyPurchaseOrderDeleted(purchaseOrder);

        vendorClient.delete(TEST_Vendor_CODE);
        vendorClient.delete(SECOND_Vendor_CODE);

        paymentMethodClient.delete(TEST_PAYMENT_METHOD_CODE);
        paymentMethodClient.delete(SECOND_PAYMENT_METHOD_CODE);
        print("PurchaseOrder delete test passed");
    }

    private void testPurchaseOrderLines(String id) throws ITestFailedException {
        print("Running PurchaseOrderLine create test");
        ItemDTO item = createItem();
        itemClient.create(item);
        item = generateSecondItem();
        itemClient.create(item);

        PurchaseDocumentLineDTO purchaseOrderLine = generatePurchaseOrderLine(id);
        int lineNo = linesClient.create(id, purchaseOrderLine);
        print("PurchaseOrderLine create test passed");

        print("Running PurchaseOrderLine read test");
        purchaseOrderLine = linesClient.read(id, lineNo);
        verifyPurchaseOrderLineRead(purchaseOrderLine);
        print("PurchaseOrderLine read test passed");
        
        print("Running PurchaseOrderLine update test");
        purchaseOrderLine = generatePurchaseOrderLineUpdate();
        linesClient.update(id, lineNo, purchaseOrderLine);
        purchaseOrderLine = linesClient.read(id, lineNo);
        verifyPurchaseOrderLineUpdated(purchaseOrderLine);
        print("PurchaseOrderLine update test passed");
        
        print("Running PurchaseOrderLine delete test");
        linesClient.delete(id, lineNo);
        purchaseOrderLine = linesClient.read(id, lineNo);
        verifyPurchaseOrderLineDeleted(purchaseOrderLine);

        itemClient.delete(TEST_ITEM_CODE);
        itemClient.delete(SECOND_ITEM_CODE);
        print("PurchaseOrderLine delete test passed");
    }

    private void verifyPurchaseOrderLineDeleted(PurchaseDocumentLineDTO purchaseOrderLine) throws ITestFailedException {
        if (purchaseOrderLine != null) {
            throw new ITestFailedException("PurchaseOrderLine should not exist when deleted");
        }
    }

    private void verifyPurchaseOrderLineUpdated(PurchaseDocumentLineDTO purchaseOrderLine) throws ITestFailedException {
        if (!purchaseOrderLine.getItemCode().equals(SECOND_ITEM_CODE)) {
            throw new ITestFailedException(String.format("PurchaseOrderLine item code issue: expected %s, got %s",
            SECOND_ITEM_CODE, purchaseOrderLine.getItemCode()));
        }

        if (!purchaseOrderLine.getItemName().equals(SECOND_ITEM_NAME)) {
            throw new ITestFailedException(String.format("PurchaseOrderLine item name issue: expected %s, got %s",
            SECOND_ITEM_NAME, purchaseOrderLine.getItemName()));
        }

        if (!(purchaseOrderLine.getQuantity() == UPDATED_DOUBLE)) {
            throw new ITestFailedException(String.format("PurchaseOrderLine quantity issue: expected %f, got %f",
            UPDATED_DOUBLE, purchaseOrderLine.getQuantity()));
        }

        if (!(purchaseOrderLine.getPrice() == UPDATED_DOUBLE)) {
            throw new ITestFailedException(String.format("PurchaseOrderLine price issue: expected %f, got %f", UPDATED_DOUBLE,
                    purchaseOrderLine.getPrice()));
        }

        if (!(purchaseOrderLine.getAmount() == UPDATED_DOUBLE)) {
            throw new ITestFailedException(String.format("PurchaseOrderLine amount issue: expected %f, got %f",
            UPDATED_DOUBLE, purchaseOrderLine.getAmount()));
        }
    }

    private PurchaseDocumentLineDTO generatePurchaseOrderLineUpdate() {
        PurchaseDocumentLineDTO result = new PurchaseDocumentLineDTO();
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
        result.setPurchasePrice(UPDATED_DOUBLE);
        result.setPurchasePrice(UPDATED_DOUBLE);
        return result;
    }

    private void verifyPurchaseOrderLineRead(PurchaseDocumentLineDTO purchaseOrderLine)
            throws ITestFailedException {
        if (!purchaseOrderLine.getItemCode().equals(TEST_ITEM_CODE)) {
            throw new ITestFailedException(String.format("PurchaseOrderLine item code issue: expected %s, got %s",
                    TEST_ITEM_CODE, purchaseOrderLine.getItemCode()));
        }

        if (!purchaseOrderLine.getItemName().equals(TEST_ITEM_NAME)) {
            throw new ITestFailedException(String.format("PurchaseOrderLine item name issue: expected %s, got %s",
                    TEST_ITEM_NAME, purchaseOrderLine.getItemName()));
        }

        if (!(purchaseOrderLine.getQuantity() == TEST_DOUBLE)) {
            throw new ITestFailedException(String.format("PurchaseOrderLine quantity issue: expected %f, got %f",
                    TEST_DOUBLE, purchaseOrderLine.getQuantity()));
        }

        if (!(purchaseOrderLine.getPrice() == TEST_DOUBLE)) {
            throw new ITestFailedException(String.format("PurchaseOrderLine price issue: expected %f, got %f", TEST_DOUBLE,
                    purchaseOrderLine.getPrice()));
        }

        if (!(purchaseOrderLine.getAmount() == TEST_DOUBLE)) {
            throw new ITestFailedException(String.format("PurchaseOrderLine amount issue: expected %f, got %f",
                    TEST_DOUBLE, purchaseOrderLine.getAmount()));
        }
    }

    private PurchaseDocumentLineDTO generatePurchaseOrderLine(String id) {
        PurchaseDocumentLineDTO result = new PurchaseDocumentLineDTO();
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

    private void verifyPurchaseOrderDeleted(PurchaseDocumentDTO purchaseOrder) throws ITestFailedException {
        if (purchaseOrder != null){
            throw new ITestFailedException("Purchase order should not exist when deleted");
        }
    }

    private void verifyPurchaseOrderUpdated(PurchaseDocumentDTO purchaseOrder) throws ITestFailedException {
        if (!purchaseOrder.getVendorCode().equals(SECOND_Vendor_CODE)) {
            throw new ITestFailedException(String.format("Purchase order Vendor code issue: expected %s, got %s",
                    SECOND_Vendor_CODE, purchaseOrder.getVendorCode()));
        }

        if (!purchaseOrder.getVendorName().equals(SECOND_Vendor_NAME)) {
            throw new ITestFailedException(String.format("Purchase order Vendor name issue: expected %s, got %s",
                    SECOND_Vendor_NAME, purchaseOrder.getVendorName()));
        }

        if (!purchaseOrder.getPaymentMethodCode().equals(SECOND_PAYMENT_METHOD_CODE)) {
            throw new ITestFailedException(String.format("Purchase order payment method code issue: expected %s, got %s",
                    SECOND_PAYMENT_METHOD_CODE, purchaseOrder.getPaymentMethodCode()));
        }

        if (!purchaseOrder.getPaymentMethodName().equals(SECOND_PAYMENT_METHOD_NAME)) {
            throw new ITestFailedException(String.format("Purchase order payment method name issue: expected %s, got %s",
                    SECOND_PAYMENT_METHOD_NAME, purchaseOrder.getPaymentMethodName()));
        }
    }

    private PurchaseDocumentDTO generatePurchaseOrderUpdate() {
        PurchaseDocumentDTO result = new PurchaseDocumentDTO();
        result.setVendorCode(SECOND_Vendor_CODE);
        result.setDate(TEST_DATE);
        result.setPaymentMethodCode(SECOND_PAYMENT_METHOD_CODE);
        return result;
    }

    private void verifyPurchaseOrderRead(PurchaseDocumentDTO purchaseOrder) throws ITestFailedException {
        if (!purchaseOrder.getCode().equals(EXPECTED_PURCHASE_ORDER_CODE)) {
            throw new ITestFailedException(
                    String.format("Purchase order code issue: expected %s, got %s", EXPECTED_PURCHASE_ORDER_CODE,
                            purchaseOrder.getCode()));
        }

        if (!purchaseOrder.getVendorCode().equals(TEST_Vendor_CODE)) {
            throw new ITestFailedException(String.format("Purchase order Vendor code issue: expected %s, got %s",
                    TEST_Vendor_CODE, purchaseOrder.getVendorCode()));
        }

        if (!purchaseOrder.getVendorName().equals(TEST_Vendor_NAME)) {
            throw new ITestFailedException(String.format("Purchase order Vendor name issue: expected %s, got %s",
                    TEST_Vendor_NAME, purchaseOrder.getVendorName()));
        }

        if (!purchaseOrder.getPaymentMethodCode().equals(TEST_PAYMENT_METHOD_CODE)) {
            throw new ITestFailedException(String.format("Purchase order payment method code issue: expected %s, got %s",
                    TEST_PAYMENT_METHOD_CODE, purchaseOrder.getPaymentMethodCode()));
        }

        if (!purchaseOrder.getPaymentMethodName().equals(TEST_PAYMENT_METHOD_NAME)) {
            throw new ITestFailedException(String.format("Purchase order payment method name issue: expected %s, got %s",
                    TEST_PAYMENT_METHOD_NAME, purchaseOrder.getPaymentMethodName()));
        }
    }

    private PurchaseDocumentDTO generatePurchaseOrder() {
        PurchaseDocumentDTO result = new PurchaseDocumentDTO();
        result.setVendorCode(TEST_Vendor_CODE);
        result.setDate(TEST_DATE);
        result.setPaymentMethodCode(TEST_PAYMENT_METHOD_CODE);
        return result;
    }

    private void initPurchaseOrderCodeSerie() {
        CodeSerieDTO PurchaseOrderCodeSerie = generateCodeSerie();
        codeSerieClient.create(PurchaseOrderCodeSerie);

        SetupDTO setup = setupClient.read();
        setup.setPurchaseOrderCodeSerieCode(CODE_SERIE_CODE);
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

    private void initVendors() {
        VendorDTO Vendor = generateVendor();
        vendorClient.create(Vendor);
        Vendor = generateSecondVendor();
        vendorClient.create(Vendor);
    }

    private VendorDTO generateSecondVendor() {
        VendorDTO result = new VendorDTO();
        result.setCode(SECOND_Vendor_CODE);
        result.setName(SECOND_Vendor_NAME);
        result.setAddress(SECOND_Vendor_ADDRESS);
        result.setPaymentMethodCode(TEST_PAYMENT_METHOD_CODE);
        return result;
    }

    private VendorDTO generateVendor() {
        VendorDTO result = new VendorDTO();
        result.setCode(TEST_Vendor_CODE);
        result.setName(TEST_Vendor_NAME);
        result.setAddress(TEST_Vendor_ADDRESS);
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

    private void print(String msg) {
        System.out.println(msg);
    }

}
