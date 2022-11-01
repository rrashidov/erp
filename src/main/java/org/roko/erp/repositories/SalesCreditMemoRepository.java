package org.roko.erp.repositories;

import org.roko.erp.model.SalesCreditMemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesCreditMemoRepository extends JpaRepository<SalesCreditMemo, String> {
    
}
