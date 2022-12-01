package org.roko.erp.backend.repositories;

import org.roko.erp.backend.model.GeneralJournalBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralJournalBatchRepository extends JpaRepository<GeneralJournalBatch, String> {
    
}
