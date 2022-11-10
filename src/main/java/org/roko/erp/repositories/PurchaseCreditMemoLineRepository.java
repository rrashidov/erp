package org.roko.erp.repositories;

import java.util.List;

import org.roko.erp.model.PurchaseCreditMemo;
import org.roko.erp.model.PurchaseCreditMemoLine;
import org.roko.erp.model.jpa.PurchaseCreditMemoLineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseCreditMemoLineRepository
        extends JpaRepository<PurchaseCreditMemoLine, PurchaseCreditMemoLineId> {

    @Query("SELECT purchaseCreditMemoLine FROM PurchaseCreditMemoLine purchaseCreditMemoLine WHERE purchaseCreditMemoLine.purchaseCreditMemo = :purchaseCreditMemo")
    public List<PurchaseCreditMemoLine> findFor(@Param("purchaseCreditMemo") PurchaseCreditMemo purchaseCreditMemo);

    @Query("SELECT COUNT(purchaseCreditMemoLine) FROM PurchaseCreditMemoLine purchaseCreditMemoLine WHERE purchaseCreditMemoLine.purchaseCreditMemo = :purchaseCreditMemo")
    public long count(@Param("purchaseCreditMemo") PurchaseCreditMemo purchaseCreditMemo);
}
