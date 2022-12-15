package org.roko.erp.dto.list;

import java.util.List;

public class GeneralJournalBatchLineList extends BaseDTOList {
    
    private List<GeneralJournalbatchLineDTO> data;

    public List<GeneralJournalbatchLineDTO> getData() {
        return data;
    }

    public void setData(List<GeneralJournalbatchLineDTO> data) {
        this.data = data;
    }

}
