package org.roko.erp.frontend.services;

import org.roko.erp.dto.GeneralJournalBatchLineDTO;
import org.roko.erp.dto.list.GeneralJournalBatchLineList;

public interface GeneralJournalBatchLineService {

    public void create(String code, GeneralJournalBatchLineDTO generalJournalBatchLine);

    public void update(String code, int lineNo, GeneralJournalBatchLineDTO generalJournalBatchLine);

    public GeneralJournalBatchLineDTO get(String code, int lineNo);

    public GeneralJournalBatchLineList list(String code, int page);

    public void delete(String code, int lineNo);

}
