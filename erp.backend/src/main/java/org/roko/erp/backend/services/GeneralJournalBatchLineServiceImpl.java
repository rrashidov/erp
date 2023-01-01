package org.roko.erp.backend.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.backend.model.GeneralJournalBatch;
import org.roko.erp.backend.model.GeneralJournalBatchLine;
import org.roko.erp.backend.model.GeneralJournalBatchLineOperationType;
import org.roko.erp.backend.model.GeneralJournalBatchLineType;
import org.roko.erp.backend.model.jpa.GeneralJournalBatchLineId;
import org.roko.erp.backend.repositories.GeneralJournalBatchLineRepository;
import org.roko.erp.dto.GeneralJournalBatchLineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class GeneralJournalBatchLineServiceImpl implements GeneralJournalBatchLineService {

    private GeneralJournalBatchLineRepository repo;
    private BankAccountService bankAccountSvc;

    @Autowired
    public GeneralJournalBatchLineServiceImpl(GeneralJournalBatchLineRepository repo,
            GeneralJournalBatchService generalJournalBatchSvc, BankAccountService bankAccountSvc) {
        this.repo = repo;
        this.bankAccountSvc = bankAccountSvc;
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
    public List<GeneralJournalBatchLine> list(GeneralJournalBatch generalJournalBatch) {
        return repo.findFor(generalJournalBatch);
    }

    @Override
    public List<GeneralJournalBatchLine> list(GeneralJournalBatch generalJournalBatch, int page) {
        return repo.findFor(generalJournalBatch, PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE)).toList();
    }

    @Override
    public void delete(GeneralJournalBatchLineId generalJournalBatchLineId) {
        GeneralJournalBatchLine generalJournalBatchLine = repo.findById(generalJournalBatchLineId).get();

        repo.delete(generalJournalBatchLine);
    }

    @Override
    public int count(GeneralJournalBatch generalJournalBatch) {
        return repo.count(generalJournalBatch);
    }

    @Override
    public GeneralJournalBatchLine fromDTO(GeneralJournalBatchLineDTO dto) {
        GeneralJournalBatchLine generalJournalBatchLine = new GeneralJournalBatchLine();
        generalJournalBatchLine.setSourceType(GeneralJournalBatchLineType.valueOf(dto.getType()));
        generalJournalBatchLine.setSourceCode(dto.getCode());
        generalJournalBatchLine.setSourceName(dto.getName());
        generalJournalBatchLine.setOperationType(GeneralJournalBatchLineOperationType.valueOf(dto.getOperationType()));
        generalJournalBatchLine.setDate(dto.getDate());
        generalJournalBatchLine.setAmount(dto.getAmount());
        generalJournalBatchLine.setDocumentCode(dto.getDocumentCode());
        generalJournalBatchLine.setTarget(bankAccountSvc.get(dto.getBankAccountCode()));
        return generalJournalBatchLine;
    }

    @Override
    public GeneralJournalBatchLineDTO toDTO(GeneralJournalBatchLine generalJournalBatchLine) {
        GeneralJournalBatchLineDTO dto = new GeneralJournalBatchLineDTO();
        dto.setGeneralJournalBatchCode(
                generalJournalBatchLine.getGeneralJournalBatchLineId().getGeneralJournalBatch().getCode());
        dto.setLineNo(generalJournalBatchLine.getGeneralJournalBatchLineId().getLineNo());
        dto.setType(generalJournalBatchLine.getSourceType().name());
        dto.setCode(generalJournalBatchLine.getSourceCode());
        dto.setName(generalJournalBatchLine.getSourceName());
        dto.setDocumentCode(generalJournalBatchLine.getDocumentCode());
        dto.setOperationType(generalJournalBatchLine.getOperationType().name());
        dto.setDate(generalJournalBatchLine.getDate());
        dto.setAmount(generalJournalBatchLine.getAmount());
        if (generalJournalBatchLine.getTarget() != null) {
            dto.setBankAccountCode(generalJournalBatchLine.getTarget().getCode());
            dto.setBankAccountName(generalJournalBatchLine.getTarget().getName());    
        }
        return dto;
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
