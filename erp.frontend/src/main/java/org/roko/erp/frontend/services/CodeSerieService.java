package org.roko.erp.frontend.services;

import org.roko.erp.dto.CodeSerieDTO;
import org.roko.erp.dto.list.CodeSerieList;

public interface CodeSerieService {
    
    public void create(CodeSerieDTO codeSerie);

    public void update(String code, CodeSerieDTO codeSerie);

    public void delete(String code);

    public CodeSerieDTO get(String code);

    public CodeSerieList list();

    public CodeSerieList list(int page);

}
