package org.roko.erp.repositories;

import org.roko.erp.model.PurchaseCreditMemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseCreditMemoRepository extends JpaRepository<PurchaseCreditMemo, String> {

    @Query("SELECT COALESCE(SUM(purchaseCreditMemoLine.amount), 0) FROM PurchaseCreditMemoLine purchaseCreditMemoLine WHERE purchaseCreditMemoLine.purchaseCreditMemo = :purchaseCreditMemo")
    public double amount(@Param("purchaseCreditMemo") PurchaseCreditMemo purchaseCreditMemo);
}
