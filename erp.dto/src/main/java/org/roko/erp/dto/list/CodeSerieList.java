package org.roko.erp.dto.list;

import java.util.List;

import org.roko.erp.dto.CodeSerieDTO;

public class CodeSerieList extends BaseDTOList {
    
    private List<CodeSerieDTO> data;

    public List<CodeSerieDTO> getData() {
        return data;
    }

    public void setData(List<CodeSerieDTO> data) {
        this.data = data;
    }

}
