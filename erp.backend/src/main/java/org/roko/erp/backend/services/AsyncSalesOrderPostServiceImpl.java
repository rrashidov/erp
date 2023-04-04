package org.roko.erp.backend.services;

import org.roko.erp.backend.model.DocumentPostStatus;
import org.roko.erp.backend.model.SalesOrder;
import org.roko.erp.backend.services.exc.PostFailedException;
import org.springframework.stereotype.Service;

@Service
public class AsyncSalesOrderPostServiceImpl implements AsyncSalesOrderPostService {

    private SalesOrderService salesOrderSvc;
    private SalesOrderPostService salesOrderPostSvc;

    public AsyncSalesOrderPostServiceImpl(SalesOrderService salesOrderSvc, SalesOrderPostService salesOrderPostSvc) {
        this.salesOrderSvc = salesOrderSvc;
        this.salesOrderPostSvc = salesOrderPostSvc;
    }

    @Override
    public void post(String code) {
        try {
            salesOrderPostSvc.post(code);
        } catch (PostFailedException e) {
            updateSalesOrderPostStatus(code, e.getMessage());
        }
    }

    private void updateSalesOrderPostStatus(String code, String message) {
        SalesOrder salesOrder = salesOrderSvc.get(code);
        salesOrder.setPostStatus(DocumentPostStatus.FAILED);
        salesOrder.setPostStatusReason(message);
        salesOrderSvc.update(code, salesOrder);
    }
    
}
