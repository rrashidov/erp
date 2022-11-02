package org.roko.erp.repositories;

import org.roko.erp.model.SalesCreditMemoLine;
import org.roko.erp.model.jpa.SalesCreditMemoLineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesCreditMemoLineRepository extends JpaRepository<SalesCreditMemoLine, SalesCreditMemoLineId> {
    
}
