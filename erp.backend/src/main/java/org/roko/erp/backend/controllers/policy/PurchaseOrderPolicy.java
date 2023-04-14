package org.roko.erp.backend.controllers.policy;

import org.roko.erp.backend.model.DocumentPostStatus;
import org.roko.erp.backend.model.PurchaseOrder;
import org.roko.erp.backend.services.PurchaseOrderLineService;
import org.roko.erp.backend.services.PurchaseOrderService;

public class PurchaseOrderPolicy implements EntityPolicy {

    private static final String NOT_FOUND_TMPL = "Purchase Order %s not found";

    private PurchaseOrderService purchaseOrderSvc;
    private PurchaseOrderLineService purchaseOrderLineSvc;

    public PurchaseOrderPolicy(PurchaseOrderService purchaseOrderSvc, PurchaseOrderLineService purchaseOrderLineSvc) {
        this.purchaseOrderSvc = purchaseOrderSvc;
        this.purchaseOrderLineSvc = purchaseOrderLineSvc;
    }

    @Override
    public PolicyResult canDelete(String code) {
        PurchaseOrder purchaseOrder = purchaseOrderSvc.get(code);

        if (purchaseOrder == null) {
            return new PolicyResult(false, String.format(NOT_FOUND_TMPL, code));
        }

        if (purchaseOrder.getPostStatus().equals(DocumentPostStatus.SCHEDULED)) {
            return new PolicyResult(false, String.format("Purchase Order %s is scheduled for posting", code));
        }

        if (purchaseOrderLineSvc.count(purchaseOrder) > 0) {
            return new PolicyResult(false, String.format("Purchase Order %s has lines", code));
        }

        return new PolicyResult(true, "");
    }
    
}
