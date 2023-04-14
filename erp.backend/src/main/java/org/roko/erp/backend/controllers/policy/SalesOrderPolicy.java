package org.roko.erp.backend.controllers.policy;

import org.roko.erp.backend.model.DocumentPostStatus;
import org.roko.erp.backend.model.SalesOrder;
import org.roko.erp.backend.services.SalesOrderLineService;
import org.roko.erp.backend.services.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SalesOrderPolicy {

    private static final String NOT_FOUND_TMPL = "Sales Order %s not found";
    private static final String HAS_LINES_TMPL = "Sales Order %s has lines";
    private static final String SCHEDULED_FOR_POSTING_TMPL = "Sales Order %s is scheduled for posting";

    private SalesOrderService svc;
    private SalesOrderLineService salesOrderLineSvc;

    @Autowired
    public SalesOrderPolicy(SalesOrderService svc, SalesOrderLineService salesOrderLineSvc) {
        this.svc = svc;
        this.salesOrderLineSvc = salesOrderLineSvc;
    }

    public PolicyResult canDelete(String code) {
        SalesOrder salesOrder = svc.get(code);

        if (salesOrder == null) {
            return new PolicyResult(false, String.format(NOT_FOUND_TMPL, code));
        }

        if (salesOrder.getPostStatus().equals(DocumentPostStatus.SCHEDULED)) {
            return new PolicyResult(false, String.format(SCHEDULED_FOR_POSTING_TMPL, code));
        }

        if (salesOrderLineSvc.count(salesOrder) > 0) {
            return new PolicyResult(false, String.format(HAS_LINES_TMPL, code));
        }
        
        return new PolicyResult(true, "");
    }
}
