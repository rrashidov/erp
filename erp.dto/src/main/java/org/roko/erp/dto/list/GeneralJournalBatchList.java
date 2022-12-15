package org.roko.erp.dto.list;

import java.util.List;

import org.roko.erp.dto.GeneralJournalBatchDTO;

public class GeneralJournalBatchList extends BaseDTOList {
    
    private List<GeneralJournalBatchDTO> data;

    public List<GeneralJournalBatchDTO> getData() {
        return data;
    }

    public void setData(List<GeneralJournalBatchDTO> data) {
        this.data = data;
    }

}
