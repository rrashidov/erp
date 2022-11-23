package org.roko.erp.repositories;

import org.roko.erp.model.GeneralJournalBatchLine;
import org.roko.erp.model.jpa.GeneralJournalBatchLineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralJournalBatchLineRepository extends JpaRepository<GeneralJournalBatchLine, GeneralJournalBatchLineId> {
    
}
