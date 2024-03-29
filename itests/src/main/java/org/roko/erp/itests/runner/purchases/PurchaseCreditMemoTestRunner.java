package org.roko.erp.itests.runner.purchases;

import java.math.BigDecimal;
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
import org.roko.erp.itests.clients.PurchaseCreditMemoClient;
import org.roko.erp.itests.clients.PurchaseCreditMemoLineClient;
import org.roko.erp.itests.clients.SetupClient;
import org.roko.erp.itests.runner.BaseTestRunner;
import org.roko.erp.itests.runner.ITestFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PurchaseCreditMemoTestRunner extends BaseTestRunner {

    private static final String SECOND_ITEM_CODE = "second-item-code";
    private static final String SECOND_ITEM_NAME = "second-item-name";

    private static final String TEST_ITEM_CODE = "test-item-code";
    private static final String TEST_ITEM_NAME = "test-item-name";
    private static final BigDecimal TEST_ITEM_SALES_PRICE = new BigDecimal("1");
    private static final BigDecimal TEST_ITEM_PURCHASE_PRICE = new BigDecimal("2");

    private static final String EXPECTED_PURCHASE_CREDITMEMO_CODE = "PO001";

    private static final String CODE_SERIE_CODE = "CS01";
    private static final String CODE_SERIE_NAME = "PurchaseCreditMemo code serie";
    private static final String CODE_SERIE_USED_CODE = "PO000";

    private static final String SECOND_VENDOR_CODE = "second-Vendor-code";
    private static final String SECOND_VENDOR_NAME = "second-Vendor-name";
    private static final String SECOND_VENDOR_ADDRESS = "second-Vendor-address";

    private static final String TEST_VENDOR_CODE = "test-Vendor-code";
    private static final String TEST_VENDOR_NAME = "test-Vendor-name";
    private static final String TEST_VENDOR_ADDRESS = "test-Vendor-address";

    private static final String SECOND_PAYMENT_METHOD_CODE = "second-payment-method-code";
    private static final String SECOND_PAYMENT_METHOD_NAME = "second-payment-method-name";

    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";
    private static final String TEST_PAYMENT_METHOD_NAME = "test-payment-method-name";

    private static final BigDecimal TEST_BIGDECIMAL = new BigDecimal("1");
    private static final BigDecimal UPDATED_BIGDECIMAL = new BigDecimal("3");

    private static final Date TEST_DATE = Calendar.getInstance().getTime();

    private PurchaseCreditMemoClient client;

    private PurchaseCreditMemoLineClient linesClient;

    private PaymentMethodClient paymentMethodClient;

    private VendorClient vendorClient;

    private CodeSerieClient codeSerieClient;

    private SetupClient setupClient;

    private ItemClient itemClient;

    @Autowired
    public PurchaseCreditMemoTestRunner(PurchaseCreditMemoClient client, PurchaseCreditMemoLineClient linesClient, 
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
        LOGGER.info("Running PurchaseCreditMemo create test");
        initPaymentMethods();
        initVendors();
        initPurchaseCreditMemoCodeSerie();

        PurchaseDocumentDTO purchaseCreditMemo = generatePurchaseCreditMemo();
        String purchaseCreditMemoCode = client.create(purchaseCreditMemo);
        LOGGER.info("PurchaseCreditMemo create test passed");

        LOGGER.info("Running PurchaseCreditMemo read test");
        purchaseCreditMemo = client.read(purchaseCreditMemoCode);
        verifyPurchaseCreditMemoRead(purchaseCreditMemo);
        LOGGER.info("PurchaseCreditMemo read test passed");

        LOGGER.info("Running PurchaseCreditMemo update test");
        purchaseCreditMemo = generatePurchaseCreditMemoUpdate();
        client.update(purchaseCreditMemoCode, purchaseCreditMemo);
        purchaseCreditMemo = client.read(purchaseCreditMemoCode);
        verifyPurchaseCreditMemoUpdated(purchaseCreditMemo);
        LOGGER.info("PurchaseCreditMemo update test passed");

        testPurchaseCreditMemoLines(purchaseCreditMemoCode);

        LOGGER.info("Running PurchaseCreditMemo delete test");
        client.delete(purchaseCreditMemoCode);
        purchaseCreditMemo = client.read(purchaseCreditMemoCode);
        verifyPurchaseCreditMemoDeleted(purchaseCreditMemo);

        vendorClient.delete(TEST_VENDOR_CODE);
        vendorClient.delete(SECOND_VENDOR_CODE);

        paymentMethodClient.delete(TEST_PAYMENT_METHOD_CODE);
        paymentMethodClient.delete(SECOND_PAYMENT_METHOD_CODE);
        LOGGER.info("PurchaseCreditMemo delete test passed");
    }

    private void testPurchaseCreditMemoLines(String id) throws ITestFailedException {
        LOGGER.info("Running PurchaseCreditMemoLine create test");
        ItemDTO item = createItem();
        itemClient.create(item);
        item = generateSecondItem();
        itemClient.create(item);

        PurchaseDocumentLineDTO purchaseCreditMemoLine = generatePurchaseCreditMemoLine(id);
        int lineNo = linesClient.create(id, purchaseCreditMemoLine);
        LOGGER.info("PurchaseCreditMemoLine create test passed");

        LOGGER.info("Running PurchaseCreditMemoLine read test");
        purchaseCreditMemoLine = linesClient.read(id, lineNo);
        verifyPurchaseCreditMemoLineRead(purchaseCreditMemoLine);
        LOGGER.info("PurchaseCreditMemoLine read test passed");
        
        LOGGER.info("Running PurchaseCreditMemoLine update test");
        purchaseCreditMemoLine = generatePurchaseCreditMemoLineUpdate();
        linesClient.update(id, lineNo, purchaseCreditMemoLine);
        purchaseCreditMemoLine = linesClient.read(id, lineNo);
        verifyPurchaseCreditMemoLineUpdated(purchaseCreditMemoLine);
        LOGGER.info("PurchaseCreditMemoLine update test passed");
        
        LOGGER.info("Running PurchaseCreditMemoLine delete test");
        linesClient.delete(id, lineNo);
        purchaseCreditMemoLine = linesClient.read(id, lineNo);
        verifyPurchaseCreditMemoLineDeleted(purchaseCreditMemoLine);

        itemClient.delete(TEST_ITEM_CODE);
        itemClient.delete(SECOND_ITEM_CODE);
        LOGGER.info("PurchaseCreditMemoLine delete test passed");
    }

    private void verifyPurchaseCreditMemoLineDeleted(PurchaseDocumentLineDTO purchaseCreditMemoLine) throws ITestFailedException {
        if (purchaseCreditMemoLine != null) {
            throw new ITestFailedException("PurchaseCreditMemoLine should not exist when deleted");
        }
    }

    private void verifyPurchaseCreditMemoLineUpdated(PurchaseDocumentLineDTO purchaseCreditMemoLine) throws ITestFailedException {
        if (!purchaseCreditMemoLine.getItemCode().equals(SECOND_ITEM_CODE)) {
            throw new ITestFailedException(String.format("PurchaseCreditMemoLine item code issue: expected %s, got %s",
            SECOND_ITEM_CODE, purchaseCreditMemoLine.getItemCode()));
        }

        if (!purchaseCreditMemoLine.getItemName().equals(SECOND_ITEM_NAME)) {
            throw new ITestFailedException(String.format("PurchaseCreditMemoLine item name issue: expected %s, got %s",
            SECOND_ITEM_NAME, purchaseCreditMemoLine.getItemName()));
        }

        if (purchaseCreditMemoLine.getQuantity().compareTo(UPDATED_BIGDECIMAL) != 0) {
            throw new ITestFailedException(String.format("PurchaseCreditMemoLine quantity issue: expected %f, got %f",
            UPDATED_BIGDECIMAL, purchaseCreditMemoLine.getQuantity()));
        }

        if (purchaseCreditMemoLine.getPrice().compareTo(UPDATED_BIGDECIMAL) != 0) {
            throw new ITestFailedException(String.format("PurchaseCreditMemoLine price issue: expected %f, got %f", UPDATED_BIGDECIMAL,
                    purchaseCreditMemoLine.getPrice()));
        }

        if (purchaseCreditMemoLine.getAmount().compareTo(UPDATED_BIGDECIMAL) != 0) {
            throw new ITestFailedException(String.format("PurchaseCreditMemoLine amount issue: expected %f, got %f",
            UPDATED_BIGDECIMAL, purchaseCreditMemoLine.getAmount()));
        }
    }

    private PurchaseDocumentLineDTO generatePurchaseCreditMemoLineUpdate() {
        PurchaseDocumentLineDTO result = new PurchaseDocumentLineDTO();
        result.setItemCode(SECOND_ITEM_CODE);
        result.setQuantity(UPDATED_BIGDECIMAL);
        result.setPrice(UPDATED_BIGDECIMAL);
        result.setAmount(UPDATED_BIGDECIMAL);
        return result;
    }

    private ItemDTO generateSecondItem() {
        ItemDTO result = new ItemDTO();
        result.setCode(SECOND_ITEM_CODE);
        result.setName(SECOND_ITEM_NAME);
        result.setPurchasePrice(UPDATED_BIGDECIMAL);
        result.setPurchasePrice(UPDATED_BIGDECIMAL);
        return result;
    }

    private void verifyPurchaseCreditMemoLineRead(PurchaseDocumentLineDTO purchaseCreditMemoLine)
            throws ITestFailedException {
        if (!purchaseCreditMemoLine.getItemCode().equals(TEST_ITEM_CODE)) {
            throw new ITestFailedException(String.format("PurchaseCreditMemoLine item code issue: expected %s, got %s",
                    TEST_ITEM_CODE, purchaseCreditMemoLine.getItemCode()));
        }

        if (!purchaseCreditMemoLine.getItemName().equals(TEST_ITEM_NAME)) {
            throw new ITestFailedException(String.format("PurchaseCreditMemoLine item name issue: expected %s, got %s",
                    TEST_ITEM_NAME, purchaseCreditMemoLine.getItemName()));
        }

        if (purchaseCreditMemoLine.getQuantity().compareTo(TEST_BIGDECIMAL) != 0) {
            throw new ITestFailedException(String.format("PurchaseCreditMemoLine quantity issue: expected %f, got %f",
                    TEST_BIGDECIMAL, purchaseCreditMemoLine.getQuantity()));
        }

        if (purchaseCreditMemoLine.getPrice().compareTo(TEST_BIGDECIMAL) != 0) {
            throw new ITestFailedException(String.format("PurchaseCreditMemoLine price issue: expected %f, got %f", TEST_BIGDECIMAL,
                    purchaseCreditMemoLine.getPrice()));
        }

        if (purchaseCreditMemoLine.getAmount().compareTo(TEST_BIGDECIMAL) != 0) {
            throw new ITestFailedException(String.format("PurchaseCreditMemoLine amount issue: expected %f, got %f",
                    TEST_BIGDECIMAL, purchaseCreditMemoLine.getAmount()));
        }
    }

    private PurchaseDocumentLineDTO generatePurchaseCreditMemoLine(String id) {
        PurchaseDocumentLineDTO result = new PurchaseDocumentLineDTO();
        result.setItemCode(TEST_ITEM_CODE);
        result.setQuantity(TEST_BIGDECIMAL);
        result.setPrice(new BigDecimal(1));
        result.setAmount(new BigDecimal(1));
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

    private void verifyPurchaseCreditMemoDeleted(PurchaseDocumentDTO purchaseCreditMemo) throws ITestFailedException {
        if (purchaseCreditMemo != null){
            throw new ITestFailedException("Purchase credit memo should not exist when deleted");
        }
    }

    private void verifyPurchaseCreditMemoUpdated(PurchaseDocumentDTO purchaseCreditMemo) throws ITestFailedException {
        if (!purchaseCreditMemo.getVendorCode().equals(SECOND_VENDOR_CODE)) {
            throw new ITestFailedException(String.format("Purchase credit memo Vendor code issue: expected %s, got %s",
                    SECOND_VENDOR_CODE, purchaseCreditMemo.getVendorCode()));
        }

        if (!purchaseCreditMemo.getVendorName().equals(SECOND_VENDOR_NAME)) {
            throw new ITestFailedException(String.format("Purchase credit memo Vendor name issue: expected %s, got %s",
                    SECOND_VENDOR_NAME, purchaseCreditMemo.getVendorName()));
        }

        if (!purchaseCreditMemo.getPaymentMethodCode().equals(SECOND_PAYMENT_METHOD_CODE)) {
            throw new ITestFailedException(String.format("Purchase credit memo payment method code issue: expected %s, got %s",
                    SECOND_PAYMENT_METHOD_CODE, purchaseCreditMemo.getPaymentMethodCode()));
        }

        if (!purchaseCreditMemo.getPaymentMethodName().equals(SECOND_PAYMENT_METHOD_NAME)) {
            throw new ITestFailedException(String.format("Purchase credit memo payment method name issue: expected %s, got %s",
                    SECOND_PAYMENT_METHOD_NAME, purchaseCreditMemo.getPaymentMethodName()));
        }
    }

    private PurchaseDocumentDTO generatePurchaseCreditMemoUpdate() {
        PurchaseDocumentDTO result = new PurchaseDocumentDTO();
        result.setVendorCode(SECOND_VENDOR_CODE);
        result.setDate(TEST_DATE);
        result.setPaymentMethodCode(SECOND_PAYMENT_METHOD_CODE);
        return result;
    }

    private void verifyPurchaseCreditMemoRead(PurchaseDocumentDTO PurchaseCreditMemo) throws ITestFailedException {
        if (!PurchaseCreditMemo.getCode().equals(EXPECTED_PURCHASE_CREDITMEMO_CODE)) {
            throw new ITestFailedException(
                    String.format("Purchase credit memo code issue: expected %s, got %s", EXPECTED_PURCHASE_CREDITMEMO_CODE,
                            PurchaseCreditMemo.getCode()));
        }

        if (!PurchaseCreditMemo.getVendorCode().equals(TEST_VENDOR_CODE)) {
            throw new ITestFailedException(String.format("Purchase credit memo Vendor code issue: expected %s, got %s",
                    TEST_VENDOR_CODE, PurchaseCreditMemo.getVendorCode()));
        }

        if (!PurchaseCreditMemo.getVendorName().equals(TEST_VENDOR_NAME)) {
            throw new ITestFailedException(String.format("Purchase credit memo Vendor name issue: expected %s, got %s",
                    TEST_VENDOR_NAME, PurchaseCreditMemo.getVendorName()));
        }

        if (!PurchaseCreditMemo.getPaymentMethodCode().equals(TEST_PAYMENT_METHOD_CODE)) {
            throw new ITestFailedException(String.format("Purchase credit memo payment method code issue: expected %s, got %s",
                    TEST_PAYMENT_METHOD_CODE, PurchaseCreditMemo.getPaymentMethodCode()));
        }

        if (!PurchaseCreditMemo.getPaymentMethodName().equals(TEST_PAYMENT_METHOD_NAME)) {
            throw new ITestFailedException(String.format("Purchase credit memo payment method name issue: expected %s, got %s",
                    TEST_PAYMENT_METHOD_NAME, PurchaseCreditMemo.getPaymentMethodName()));
        }
    }

    private PurchaseDocumentDTO generatePurchaseCreditMemo() {
        PurchaseDocumentDTO result = new PurchaseDocumentDTO();
        result.setVendorCode(TEST_VENDOR_CODE);
        result.setDate(TEST_DATE);
        result.setPaymentMethodCode(TEST_PAYMENT_METHOD_CODE);
        return result;
    }

    private void initPurchaseCreditMemoCodeSerie() {
        CodeSerieDTO purchaseCreditMemoCodeSerie = generateCodeSerie();
        codeSerieClient.create(purchaseCreditMemoCodeSerie);

        SetupDTO setup = setupClient.read();
        setup.setPurchaseCreditMemoCodeSerieCode(CODE_SERIE_CODE);
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
        result.setCode(SECOND_VENDOR_CODE);
        result.setName(SECOND_VENDOR_NAME);
        result.setAddress(SECOND_VENDOR_ADDRESS);
        result.setPaymentMethodCode(TEST_PAYMENT_METHOD_CODE);
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
