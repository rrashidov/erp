package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.CodeSerie;
import org.roko.erp.model.dto.CodeSerieDTO;

public interface CodeSerieService {
    
    public void create(CodeSerie codeSerie);

    public void update(String code, CodeSerie codeSerie);

    public void delete(String code);

    public CodeSerie get(String code);

    public List<CodeSerie> list();

    public List<CodeSerie> list(int page);

    public int count();

    public String generate(String code);

    public CodeSerieDTO toDTO(CodeSerie codeSerie);

    public CodeSerie fromDTO(CodeSerieDTO dto);
}
