package org.roko.erp.itests.runner.sales.util;

import java.util.List;
import java.util.concurrent.Callable;

import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.itests.clients.SalesOrderClient;

public class SalesOrderPostCallable implements Callable<Boolean> {

    private SalesOrderClient salesOrderClient;
    private Object barrier;
    private String code;

    private List<Object> feedbackList;

    public SalesOrderPostCallable(SalesOrderClient salesOrderClient, Object barrier, String code,
            List<Object> feedbackList) {
        this.salesOrderClient = salesOrderClient;
        this.barrier = barrier;
        this.code = code;
        this.feedbackList = feedbackList;
    }

    @Override
    public Boolean call() throws Exception {
        synchronized (barrier) {
            try {
                feedbackList.add(new Object());
                barrier.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        salesOrderClient.post(code);

        return salesOrderSuccessfullyPosted();
    }

    private Boolean salesOrderSuccessfullyPosted() {
        SalesDocumentDTO salesOrder = salesOrderClient.read(code);

        while ((salesOrder != null) && (!salesOrder.getPostStatus().equals("FAILED"))) {
            salesOrder = salesOrderClient.read(code);
        }

        return salesOrder == null;
    }
}
