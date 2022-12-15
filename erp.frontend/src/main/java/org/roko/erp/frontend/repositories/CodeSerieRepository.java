package org.roko.erp.frontend.repositories;

import org.roko.erp.frontend.model.CodeSerie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeSerieRepository extends JpaRepository<CodeSerie, String> {
    
}
