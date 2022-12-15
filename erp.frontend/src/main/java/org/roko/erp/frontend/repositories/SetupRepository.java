package org.roko.erp.frontend.repositories;

import org.roko.erp.frontend.model.Setup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SetupRepository extends JpaRepository<Setup, String> {
    
}
