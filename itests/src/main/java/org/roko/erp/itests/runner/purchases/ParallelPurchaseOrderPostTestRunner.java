package org.roko.erp.itests.runner.purchases;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.dto.PurchaseDocumentLineDTO;
import org.roko.erp.itests.clients.PurchaseOrderClient;
import org.roko.erp.itests.clients.PurchaseOrderLineClient;
import org.roko.erp.itests.runner.purchases.util.PurchaseOrderPostCallable;
import org.roko.erp.itests.runner.util.AbstractParallelDocumentPostTestRunner;
import org.roko.erp.itests.runner.util.BusinessLogicSetupUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParallelPurchaseOrderPostTestRunner extends AbstractParallelDocumentPostTestRunner {

    @Autowired
    private PurchaseOrderClient purchaseOrderClient;

    @Autowired
    private PurchaseOrderLineClient purchaseOrderLineClient;

    @Autowired
    private BusinessLogicSetupUtil util;

    @Override
    protected void ensureStartingConditions() {
        util.ensureBankAccounts();
    }

    @Override
    protected String createDocument() {
        PurchaseDocumentDTO purchaseOrder = new PurchaseDocumentDTO();
        purchaseOrder.setVendorCode(BusinessLogicSetupUtil.TEST_VENDOR_CODE);
        purchaseOrder.setPaymentMethodCode(BusinessLogicSetupUtil.TEST_PAYMENT_METHOD_CODE);
        purchaseOrder.setDate(new Date());

        String code = purchaseOrderClient.create(purchaseOrder);

        createPurchaseOrderLine(code);

        return code;
    }

    @Override
    protected String getDocumentType() {
        return "Purchase Order";
    }

    @Override
    protected Callable<Boolean> createCallable(String code, List<Object> feedbackList, Object BARRIER) {
        return new PurchaseOrderPostCallable(purchaseOrderClient, BARRIER, code, feedbackList);
    }

    private void createPurchaseOrderLine(String code) {
        PurchaseDocumentLineDTO purchaseOrderLine = new PurchaseDocumentLineDTO();
        purchaseOrderLine.setItemCode(BusinessLogicSetupUtil.TEST_ITEM_CODE);
        purchaseOrderLine.setQuantity(new BigDecimal(1));
        purchaseOrderLine.setPrice(BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_BALANCE);
        purchaseOrderLine.setAmount(BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_BALANCE);

        purchaseOrderLineClient.create(code, purchaseOrderLine);
    }

}
