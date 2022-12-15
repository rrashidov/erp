package org.roko.erp.frontend.services;

import java.util.List;

import org.roko.erp.frontend.model.GeneralJournalBatch;

public interface GeneralJournalBatchService {
    
    public void create(GeneralJournalBatch generalJournalBatch);

    public void update(String code, GeneralJournalBatch generalJournalBatch);

    public void delete(String code);

    public GeneralJournalBatch get(String code);

    public List<GeneralJournalBatch> list();

    public List<GeneralJournalBatch> list(int page);

    public int count();
}
