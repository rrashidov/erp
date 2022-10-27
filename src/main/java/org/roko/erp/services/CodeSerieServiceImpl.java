package org.roko.erp.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.model.CodeSerie;
import org.roko.erp.repositories.CodeSerieRepository;
import org.springframework.stereotype.Service;

@Service
public class CodeSerieServiceImpl implements CodeSerieService {

    private CodeSerieRepository repo;

    public CodeSerieServiceImpl(CodeSerieRepository repo) {
        this.repo = repo;
	}

	@Override
    public void create(CodeSerie codeSerie) {
        repo.save(codeSerie);
    }

    @Override
    public void update(String code, CodeSerie codeSerie) {
        CodeSerie codeSerieFromDB = repo.findById(code).get();

        transferFields(codeSerie, codeSerieFromDB);

        repo.save(codeSerieFromDB);
    }

    @Override
    public void delete(String code) {
        CodeSerie codeSerie = repo.findById(code).get();

        repo.delete(codeSerie);
    }

    @Override
    public CodeSerie get(String code) {
        Optional<CodeSerie> codeSerieOptional = repo.findById(code);

        if (codeSerieOptional.isPresent()) {
            return codeSerieOptional.get();
        }

        return null;
    }

    @Override
    public List<CodeSerie> list() {
        return repo.findAll();
    }

    @Override
    public long count() {
        return repo.count();
    }

    private void transferFields(CodeSerie source, CodeSerie target) {
        target.setName(source.getName());
        target.setFirstCode(source.getFirstCode());
        target.setLastCode(source.getLastCode());
    }

}
