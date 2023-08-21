package org.roko.erp.itests.runner.purchases;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.dto.PurchaseDocumentLineDTO;
import org.roko.erp.itests.clients.ItemClient;
import org.roko.erp.itests.clients.PurchaseCreditMemoClient;
import org.roko.erp.itests.clients.PurchaseCreditMemoLineClient;
import org.roko.erp.itests.runner.purchases.util.PurchaseCreditMemoPostCallable;
import org.roko.erp.itests.runner.util.AbstractParallelDocumentPostTestRunner;
import org.roko.erp.itests.runner.util.BusinessLogicSetupUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParallelPurchaseCreditMemoPostTestRunner extends AbstractParallelDocumentPostTestRunner {

    @Autowired
    private PurchaseCreditMemoClient purchaseCreditMemoClient;

    @Autowired
    private PurchaseCreditMemoLineClient purchaseCreditMemoLineClient;

    @Autowired
    private ItemClient itemClient;

    @Override
    protected void ensureStartingConditions() {
    }

    @Override
    protected String createDocument() {
        PurchaseDocumentDTO purchaseCreditMemo = new PurchaseDocumentDTO();
        purchaseCreditMemo.setVendorCode(BusinessLogicSetupUtil.TEST_VENDOR_CODE);
        purchaseCreditMemo.setPaymentMethodCode(BusinessLogicSetupUtil.TEST_PAYMENT_METHOD_CODE);
        purchaseCreditMemo.setDate(new Date());

        String code = purchaseCreditMemoClient.create(purchaseCreditMemo);

        createPurchaseCreditMemoLine(code);

        return code;
    }

    @Override
    protected String getDocumentType() {
        return "Purchase Credit Memo";
    }

    @Override
    protected Callable<Boolean> createCallable(String code, List<Object> feedbackList, Object BARRIER) {
        return new PurchaseCreditMemoPostCallable(purchaseCreditMemoClient, BARRIER, code, feedbackList);
    }

    private void createPurchaseCreditMemoLine(String code) {
        double itemInventory = itemClient.read(BusinessLogicSetupUtil.TEST_ITEM_CODE).getInventory();

        PurchaseDocumentLineDTO purchaseDocumentLine = new PurchaseDocumentLineDTO();
        purchaseDocumentLine.setItemCode(BusinessLogicSetupUtil.TEST_ITEM_CODE);
        purchaseDocumentLine.setQuantity(itemInventory);
        purchaseDocumentLine.setPrice(1.0);
        purchaseDocumentLine.setAmount(itemInventory);

        purchaseCreditMemoLineClient.create(code, purchaseDocumentLine);
    }

}
