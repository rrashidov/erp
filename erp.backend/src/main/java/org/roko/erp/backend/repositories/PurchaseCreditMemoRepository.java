package org.roko.erp.backend.repositories;

import java.math.BigDecimal;

import org.roko.erp.backend.model.PurchaseCreditMemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseCreditMemoRepository extends JpaRepository<PurchaseCreditMemo, String> {

    @Query("SELECT COALESCE(SUM(purchaseCreditMemoLine.amount), 0) FROM PurchaseCreditMemoLine purchaseCreditMemoLine WHERE purchaseCreditMemoLine.purchaseCreditMemo = :purchaseCreditMemo")
    public BigDecimal amount(@Param("purchaseCreditMemo") PurchaseCreditMemo purchaseCreditMemo);
}
