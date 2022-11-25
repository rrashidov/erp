package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.GeneralJournalBatch;
import org.roko.erp.model.GeneralJournalBatchLine;
import org.roko.erp.model.jpa.GeneralJournalBatchLineId;

public interface GeneralJournalBatchLineService {
    
    public void create(GeneralJournalBatchLine generalJournalBatchLine);

    public void update(GeneralJournalBatchLineId id, GeneralJournalBatchLine generalJournalBatchLine);

    public GeneralJournalBatchLine get(GeneralJournalBatchLineId generalJournalBatchLineId);

    public List<GeneralJournalBatchLine> list(GeneralJournalBatch generalJournalBatch);

    public List<GeneralJournalBatchLine> list(GeneralJournalBatch generalJournalBatch, int page);

    public void delete(GeneralJournalBatchLineId generalJournalBatchLineId);

    public int count(GeneralJournalBatch generalJournalBatch);
}
