package org.roko.erp.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.model.GeneralJournalBatch;
import org.roko.erp.repositories.GeneralJournalBatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeneralJournalBatchServiceImpl implements GeneralJournalBatchService {

    private GeneralJournalBatchRepository repo;

    @Autowired
    public GeneralJournalBatchServiceImpl(GeneralJournalBatchRepository repo) {
        this.repo = repo;
	}

	@Override
    public void create(GeneralJournalBatch generalJournalBatch) {
        repo.save(generalJournalBatch);
    }

    @Override
    public void update(String code, GeneralJournalBatch generalJournalBatch) {
        GeneralJournalBatch generalJournalBatchFromDB = repo.findById(code).get();

        transferFields(generalJournalBatch, generalJournalBatchFromDB);

        repo.save(generalJournalBatchFromDB);
    }

    @Override
    public void delete(String code) {
        GeneralJournalBatch generalJournalBatch = repo.findById(code).get();

        repo.delete(generalJournalBatch);
    }

    @Override
    public GeneralJournalBatch get(String code) {
        Optional<GeneralJournalBatch> generalJournalBatchOptional = repo.findById(code);

        if (generalJournalBatchOptional.isPresent()) {
            return generalJournalBatchOptional.get();
        }

        return null;
    }

    @Override
    public List<GeneralJournalBatch> list() {
        return repo.findAll();
    }

    @Override
    public long count() {
        return repo.count();
    }
    
    private void transferFields(GeneralJournalBatch source,
            GeneralJournalBatch target) {
                target.setDescription(source.getDescription());
    }

}
