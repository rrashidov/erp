package org.roko.erp.itests.runner.sales.util;

import java.util.List;
import java.util.concurrent.Callable;

import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.itests.clients.SalesCreditMemoClient;

public class SalesCreditMemoPostCallable implements Callable<Boolean> {

    private SalesCreditMemoClient salesCreditMemoClient;
    private Object barrier;
    private String code;
    private List<Object> feedbackList;

    public SalesCreditMemoPostCallable(SalesCreditMemoClient salesCreditMemoClient, Object barrier, String code,
            List<Object> feedbackList) {
                this.salesCreditMemoClient = salesCreditMemoClient;
                this.barrier = barrier;
                this.code = code;
                this.feedbackList = feedbackList;
    }

    @Override
    public Boolean call() throws Exception {
        synchronized(barrier){
            try {
                feedbackList.add(new Object());
                barrier.wait();
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }

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
