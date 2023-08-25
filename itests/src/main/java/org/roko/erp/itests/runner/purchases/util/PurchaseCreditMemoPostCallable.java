package org.roko.erp.itests.runner.purchases.util;

import java.util.List;

import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.itests.clients.PurchaseCreditMemoClient;
import org.roko.erp.itests.runner.util.AbstractBarrieredCallable;

public class PurchaseCreditMemoPostCallable extends AbstractBarrieredCallable {

    private final PurchaseCreditMemoClient purchaseCreditMemoClient;
    private final String code;

    public PurchaseCreditMemoPostCallable(PurchaseCreditMemoClient purchaseCreditMemoClient, Object barrier,
            String code, List<Object> feedbackList) {
        super(barrier, feedbackList);
        this.purchaseCreditMemoClient = purchaseCreditMemoClient;
        this.code = code;
    }

    @Override
    protected Boolean doCall() {
        purchaseCreditMemoClient.post(code);

        return purchaseCreditMemoPostedSuccessfully();
    }

    private Boolean purchaseCreditMemoPostedSuccessfully() {
        PurchaseDocumentDTO purchaseCreditMemo = purchaseCreditMemoClient.read(code);

        while ((purchaseCreditMemo != null) && (!purchaseCreditMemo.getPostStatus().equals("FAILED"))) {
            purchaseCreditMemo = purchaseCreditMemoClient.read(code);
        }

        return purchaseCreditMemo == null;
    }

}
