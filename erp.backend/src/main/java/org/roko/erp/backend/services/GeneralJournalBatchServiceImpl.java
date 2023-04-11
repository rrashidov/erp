package org.roko.erp.backend.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.backend.model.GeneralJournalBatch;
import org.roko.erp.backend.repositories.GeneralJournalBatchRepository;
import org.roko.erp.dto.GeneralJournalBatchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    public List<GeneralJournalBatch> list(int page) {
        return repo.findAll(PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE)).toList();
    }

    @Override
    public int count() {
        return new Long(repo.count()).intValue();
    }
    
    @Override
    public GeneralJournalBatch fromDTO(GeneralJournalBatchDTO dto) {
        GeneralJournalBatch generalJournalBatch = new GeneralJournalBatch();
        generalJournalBatch.setCode(dto.getCode());
        generalJournalBatch.setName(dto.getName());
        return generalJournalBatch;
    }

    @Override
    public GeneralJournalBatchDTO toDTO(GeneralJournalBatch generalJournalBatch) {
        GeneralJournalBatchDTO dto = new GeneralJournalBatchDTO();
        dto.setCode(generalJournalBatch.getCode());
        dto.setName(generalJournalBatch.getName());
        dto.setPostStatus(generalJournalBatch.getPostStatus().name());
        dto.setPostStatusReason(generalJournalBatch.getPostStatusReason());
        return dto;
    }

    private void transferFields(GeneralJournalBatch source,
            GeneralJournalBatch target) {
                target.setName(source.getName());
                target.setPostStatus(source.getPostStatus());
                target.setPostStatusReason(source.getPostStatusReason());
    }

}
