package org.roko.erp.repositories;

import org.roko.erp.model.GeneralJournalBatch;
import org.roko.erp.model.GeneralJournalBatchLine;
import org.roko.erp.model.jpa.GeneralJournalBatchLineId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralJournalBatchLineRepository
        extends JpaRepository<GeneralJournalBatchLine, GeneralJournalBatchLineId> {

    @Query("SELECT generalJournalBatchLine FROM GeneralJournalBatchLine generalJournalBatchLine WHERE generalJournalBatchLine.generalJournalBatch = :generalJournalbatch")
    public Page<GeneralJournalBatchLine> findFor(@Param("generalJournalbatch") GeneralJournalBatch generalJournalBatch,
            Pageable pageable);
}
