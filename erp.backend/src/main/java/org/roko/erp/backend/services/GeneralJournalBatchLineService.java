package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.GeneralJournalBatch;
import org.roko.erp.backend.model.GeneralJournalBatchLine;
import org.roko.erp.backend.model.jpa.GeneralJournalBatchLineId;
import org.roko.erp.dto.GeneralJournalBatchLineDTO;

public interface GeneralJournalBatchLineService {
    
    public void create(GeneralJournalBatchLine generalJournalBatchLine);

    public void update(GeneralJournalBatchLineId id, GeneralJournalBatchLine generalJournalBatchLine);

    public GeneralJournalBatchLine get(GeneralJournalBatchLineId generalJournalBatchLineId);

    public List<GeneralJournalBatchLine> list(GeneralJournalBatch generalJournalBatch);

    public List<GeneralJournalBatchLine> list(GeneralJournalBatch generalJournalBatch, int page);

    public void delete(GeneralJournalBatchLineId generalJournalBatchLineId);

    public long count(GeneralJournalBatch generalJournalBatch);

    public int maxLineNo(GeneralJournalBatch generalJournalBatch);

    public GeneralJournalBatchLineDTO toDTO(GeneralJournalBatchLine generalJournalBatchLine);

    public GeneralJournalBatchLine fromDTO(GeneralJournalBatchLineDTO dto);
}
