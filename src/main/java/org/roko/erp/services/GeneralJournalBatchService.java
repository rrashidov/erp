package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.GeneralJournalBatch;

public interface GeneralJournalBatchService {
    
    public void create(GeneralJournalBatch generalJournalBatch);

    public void update(String code, GeneralJournalBatch generalJournalBatch);

    public void delete(String code);

    public GeneralJournalBatch get(String code);

    public List<GeneralJournalBatch> list();

    public int count();
}
