package org.roko.erp.repositories;

import org.roko.erp.model.PurchaseCreditMemoLine;
import org.roko.erp.model.jpa.PurchaseCreditMemoLineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseCreditMemoLineRepository extends JpaRepository<PurchaseCreditMemoLine, PurchaseCreditMemoLineId> {
    
}
