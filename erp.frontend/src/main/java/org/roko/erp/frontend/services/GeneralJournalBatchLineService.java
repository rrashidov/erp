package org.roko.erp.frontend.services;

import java.util.List;

import org.roko.erp.frontend.model.GeneralJournalBatch;
import org.roko.erp.frontend.model.GeneralJournalBatchLine;
import org.roko.erp.frontend.model.jpa.GeneralJournalBatchLineId;

public interface GeneralJournalBatchLineService {
    
    public void create(GeneralJournalBatchLine generalJournalBatchLine);

    public void update(GeneralJournalBatchLineId id, GeneralJournalBatchLine generalJournalBatchLine);

    public GeneralJournalBatchLine get(GeneralJournalBatchLineId generalJournalBatchLineId);

    public List<GeneralJournalBatchLine> list(GeneralJournalBatch generalJournalBatch);

    public List<GeneralJournalBatchLine> list(GeneralJournalBatch generalJournalBatch, int page);

    public void delete(GeneralJournalBatchLineId generalJournalBatchLineId);

    public int count(GeneralJournalBatch generalJournalBatch);
}
