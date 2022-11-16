package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.CodeSerie;

public interface CodeSerieService {
    
    public void create(CodeSerie codeSerie);

    public void update(String code, CodeSerie codeSerie);

    public void delete(String code);

    public CodeSerie get(String code);

    public List<CodeSerie> list();

    public int count();
}
