package org.roko.erp.backend.repositories;

import org.roko.erp.backend.model.Setup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SetupRepository extends JpaRepository<Setup, String> {
    
}
