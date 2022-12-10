package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.GeneralJournalBatch;
import org.roko.erp.model.dto.GeneralJournalBatchDTO;

public interface GeneralJournalBatchService {
    
    public void create(GeneralJournalBatch generalJournalBatch);

    public void update(String code, GeneralJournalBatch generalJournalBatch);

    public void delete(String code);

    public GeneralJournalBatch get(String code);

    public List<GeneralJournalBatch> list();

    public List<GeneralJournalBatch> list(int page);

    public int count();

    public GeneralJournalBatchDTO toDTO(GeneralJournalBatch generalJournalBatch);

    public GeneralJournalBatch fromDTO(GeneralJournalBatchDTO dto);
}
