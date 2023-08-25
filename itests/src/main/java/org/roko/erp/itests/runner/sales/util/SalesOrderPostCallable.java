package org.roko.erp.itests.runner.sales.util;

import java.util.List;

import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.itests.clients.SalesOrderClient;
import org.roko.erp.itests.runner.util.AbstractBarrieredCallable;

public class SalesOrderPostCallable extends AbstractBarrieredCallable {

    private SalesOrderClient salesOrderClient;
    private String code;

    public SalesOrderPostCallable(SalesOrderClient salesOrderClient, Object barrier, String code,
            List<Object> feedbackList) {
        super(barrier, feedbackList);
        this.salesOrderClient = salesOrderClient;
        this.code = code;
    }

    @Override
    protected Boolean doCall() {
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
