package org.roko.erp.backend.controllers.policy;

import org.roko.erp.backend.model.DocumentPostStatus;
import org.roko.erp.backend.model.GeneralJournalBatch;
import org.roko.erp.backend.services.GeneralJournalBatchLineService;
import org.roko.erp.backend.services.GeneralJournalBatchService;
import org.springframework.stereotype.Component;

@Component
public class GeneralJournalBatchPolicy implements EntityPolicy {

    private static final String NOT_FOUND_TMPL = "General Journal Batch %s not found";
    private static final String SCHEDULED_FOR_POSTING_TMPL = "General Journal Batch %s is scheduled for posting";
    private static final String HAS_LINES_TMPL = "General Journal Batch %s has lines";

    private GeneralJournalBatchService generalJournalBatchSvc;
    private GeneralJournalBatchLineService generalJournalBatchLineSvc;

    public GeneralJournalBatchPolicy(GeneralJournalBatchService generalJournalBatchSvc,
            GeneralJournalBatchLineService generalJournalBatchLineSvc) {
        this.generalJournalBatchSvc = generalJournalBatchSvc;
        this.generalJournalBatchLineSvc = generalJournalBatchLineSvc;
    }

    @Override
    public PolicyResult canDelete(String code) {
        GeneralJournalBatch generalJournalBatch = generalJournalBatchSvc.get(code);

        if (generalJournalBatch == null) {
            return new PolicyResult(false, String.format(NOT_FOUND_TMPL, code));
        }

        if (generalJournalBatch.getPostStatus().equals(DocumentPostStatus.SCHEDULED)) {
            return new PolicyResult(false, String.format(SCHEDULED_FOR_POSTING_TMPL, code));
        }

        if (generalJournalBatchLineSvc.count(generalJournalBatch) > 0) {
            return new PolicyResult(false, String.format(HAS_LINES_TMPL, code));
        }

        return new PolicyResult(true, "");
    }

}
