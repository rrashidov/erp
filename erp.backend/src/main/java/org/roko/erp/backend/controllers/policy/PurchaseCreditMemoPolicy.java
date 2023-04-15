package org.roko.erp.backend.controllers.policy;

import org.roko.erp.backend.model.DocumentPostStatus;
import org.roko.erp.backend.model.PurchaseCreditMemo;
import org.roko.erp.backend.services.PurchaseCreditMemoLineService;
import org.roko.erp.backend.services.PurchaseCreditMemoService;
import org.springframework.stereotype.Component;

@Component
public class PurchaseCreditMemoPolicy implements EntityPolicy {

    private static final String NOT_FOUND_TMPL = "Purchase Credit Memo %s not found";
    private static final String SCHEDULED_FOR_POSTING_TMPL = "Purchase Credit Memo %s is scheduled for posting";
    private static final String HAS_LINES_TMPL = "Purchase Credit Memo %s has lines";

    private PurchaseCreditMemoService purchaseCreditMemoSvc;
    private PurchaseCreditMemoLineService purchaseCreditMemoLineSvc;

    public PurchaseCreditMemoPolicy(PurchaseCreditMemoService purchaseCreditMemoSvc,
            PurchaseCreditMemoLineService purchaseCreditMemoLineSvc) {
        this.purchaseCreditMemoSvc = purchaseCreditMemoSvc;
        this.purchaseCreditMemoLineSvc = purchaseCreditMemoLineSvc;
    }

    @Override
    public PolicyResult canDelete(String code) {
        PurchaseCreditMemo purchaseCreditMemo = purchaseCreditMemoSvc.get(code);

        if (purchaseCreditMemo == null){
            return new PolicyResult(false, String.format(NOT_FOUND_TMPL, code));
        }

        if (purchaseCreditMemo.getPostStatus().equals(DocumentPostStatus.SCHEDULED)) {
            return new PolicyResult(false, String.format(SCHEDULED_FOR_POSTING_TMPL, code));
        }

        if (purchaseCreditMemoLineSvc.count(purchaseCreditMemo) > 0) {
            return new PolicyResult(false, String.format(HAS_LINES_TMPL, code));
        }

        return new PolicyResult(true, "");
    }

}
