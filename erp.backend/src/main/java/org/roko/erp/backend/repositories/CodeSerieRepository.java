package org.roko.erp.backend.repositories;

import org.roko.erp.backend.model.CodeSerie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeSerieRepository extends JpaRepository<CodeSerie, String> {
    
}
