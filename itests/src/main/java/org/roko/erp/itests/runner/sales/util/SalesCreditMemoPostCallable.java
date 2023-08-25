package org.roko.erp.itests.runner.sales.util;

import java.util.List;

import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.itests.clients.SalesCreditMemoClient;
import org.roko.erp.itests.runner.util.AbstractBarrieredCallable;

public class SalesCreditMemoPostCallable extends AbstractBarrieredCallable {

    private SalesCreditMemoClient salesCreditMemoClient;
    private String code;

    public SalesCreditMemoPostCallable(SalesCreditMemoClient salesCreditMemoClient, Object barrier, String code,
            List<Object> feedbackList) {
        super(barrier, feedbackList);
        this.salesCreditMemoClient = salesCreditMemoClient;
        this.code = code;
    }

    @Override
    protected Boolean doCall() {
        salesCreditMemoClient.post(code);

        return salesCreditMemoPostedSuccessfully();
    }

    private Boolean salesCreditMemoPostedSuccessfully() {
        SalesDocumentDTO salesCreditMemo = salesCreditMemoClient.read(code);

        while ((salesCreditMemo != null) && (!salesCreditMemo.getPostStatus().equals("FAILED"))) {
            salesCreditMemo = salesCreditMemoClient.read(code);
        }

        return salesCreditMemo == null;
    }

}
