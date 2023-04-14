package org.roko.erp.backend.controllers.policy;

import org.roko.erp.backend.model.DocumentPostStatus;
import org.roko.erp.backend.model.SalesCreditMemo;
import org.roko.erp.backend.services.SalesCreditMemoLineService;
import org.roko.erp.backend.services.SalesCreditMemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SalesCreditMemoPolicy implements EntityPolicy {

    private static final String NOT_FOUND_TMPL = "Sales Credit Memo %s not found";

    private SalesCreditMemoService salesCreditMemoSvc;

    private SalesCreditMemoLineService salesCreditMemoLineSvc;

    @Autowired
    public SalesCreditMemoPolicy(SalesCreditMemoService salesCreditMemoSvc, SalesCreditMemoLineService salesCreditMemoLineSvc) {
        this.salesCreditMemoSvc = salesCreditMemoSvc;
        this.salesCreditMemoLineSvc = salesCreditMemoLineSvc;
    }

    @Override
    public PolicyResult canDelete(String code) {
        SalesCreditMemo salesCreditMemo = salesCreditMemoSvc.get(code);

        if (salesCreditMemo == null){
            return new PolicyResult(false, String.format(NOT_FOUND_TMPL, code));
        }

        if (salesCreditMemo.getPostStatus().equals(DocumentPostStatus.SCHEDULED)) {
            return new PolicyResult(false, String.format("Sales Credit Memo %s is scheduled for posting", code));
        }

        if (salesCreditMemoLineSvc.count(salesCreditMemo) > 0) {
            return new PolicyResult(false, String.format("Sales Credit Memo %s has lines", code));
        }

        return new PolicyResult(true, "");
    }
    
}
