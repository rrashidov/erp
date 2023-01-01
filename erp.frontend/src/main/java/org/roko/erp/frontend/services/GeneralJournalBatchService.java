package org.roko.erp.frontend.services;

import org.roko.erp.dto.GeneralJournalBatchDTO;
import org.roko.erp.dto.list.GeneralJournalBatchList;

public interface GeneralJournalBatchService {
    
    public void create(GeneralJournalBatchDTO generalJournalBatch);

    public void update(String code, GeneralJournalBatchDTO generalJournalBatch);

    public void delete(String code);

    public GeneralJournalBatchDTO get(String code);

    public GeneralJournalBatchList list(int page);

}
