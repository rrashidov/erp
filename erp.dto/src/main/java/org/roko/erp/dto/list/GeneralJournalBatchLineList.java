package org.roko.erp.dto.list;

import java.util.List;

import org.roko.erp.dto.GeneralJournalBatchLineDTO;

public class GeneralJournalBatchLineList extends BaseDTOList {
    
    private List<GeneralJournalBatchLineDTO> data;

    public List<GeneralJournalBatchLineDTO> getData() {
        return data;
    }

    public void setData(List<GeneralJournalBatchLineDTO> data) {
        this.data = data;
    }

}
