package org.roko.erp.frontend.repositories;

import org.roko.erp.frontend.model.GeneralJournalBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralJournalBatchRepository extends JpaRepository<GeneralJournalBatch, String> {
    
}
