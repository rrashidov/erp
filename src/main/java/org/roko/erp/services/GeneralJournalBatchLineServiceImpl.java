package org.roko.erp.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.controllers.paging.PagingServiceImpl;
import org.roko.erp.model.GeneralJournalBatch;
import org.roko.erp.model.GeneralJournalBatchLine;
import org.roko.erp.model.jpa.GeneralJournalBatchLineId;
import org.roko.erp.repositories.GeneralJournalBatchLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class GeneralJournalBatchLineServiceImpl implements GeneralJournalBatchLineService {

    private GeneralJournalBatchLineRepository repo;

    @Autowired
    public GeneralJournalBatchLineServiceImpl(GeneralJournalBatchLineRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(GeneralJournalBatchLine generalJournalBatchLine) {
        repo.save(generalJournalBatchLine);
    }

    @Override
    public void update(GeneralJournalBatchLineId id, GeneralJournalBatchLine generalJournalBatchLine) {
        GeneralJournalBatchLine generalJournalBatchLineFromDB = repo.findById(id).get();

        transferFields(generalJournalBatchLine, generalJournalBatchLineFromDB);

        repo.save(generalJournalBatchLineFromDB);
    }

    @Override
    public GeneralJournalBatchLine get(GeneralJournalBatchLineId generalJournalBatchLineId) {
        Optional<GeneralJournalBatchLine> generalJournalBatchLineOptional = repo.findById(generalJournalBatchLineId);

        if (generalJournalBatchLineOptional.isPresent()) {
            return generalJournalBatchLineOptional.get();
        }
        return null;
    }

    @Override
    public List<GeneralJournalBatchLine> list(GeneralJournalBatch generalJournalBatch, int page) {
        return repo.findFor(generalJournalBatch, PageRequest.of(page - 1, PagingServiceImpl.RECORDS_PER_PAGE)).toList();
    }

    @Override
    public void delete(GeneralJournalBatchLineId generalJournalBatchLineId) {
        GeneralJournalBatchLine generalJournalBatchLine = repo.findById(generalJournalBatchLineId).get();

        repo.delete(generalJournalBatchLine);
    }

    private void transferFields(GeneralJournalBatchLine source,
            GeneralJournalBatchLine target) {
        target.setSourceType(source.getSourceType());
        target.setSourceCode(source.getSourceCode());
        target.setOperationType(source.getOperationType());
        target.setDocumentCode(source.getDocumentCode());
        target.setOperationType(source.getOperationType());
        target.setDate(source.getDate());
        target.setAmount(source.getAmount());
        target.setTarget(source.getTarget());
    }

}
