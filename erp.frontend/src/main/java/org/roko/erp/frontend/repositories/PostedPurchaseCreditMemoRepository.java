package org.roko.erp.frontend.repositories;

import org.roko.erp.frontend.model.PostedPurchaseCreditMemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostedPurchaseCreditMemoRepository extends JpaRepository<PostedPurchaseCreditMemo, String> {

    @Query("SELECT COALESCE(SUM(postedPurchaseCreditMemoLine.amount), 0) FROM PostedPurchaseCreditMemoLine postedPurchaseCreditMemoLine WHERE postedPurchaseCreditMemoLine.postedPurchaseCreditMemo = :postedPurchaseCreditMemo")
    public double amount(@Param("postedPurchaseCreditMemo") PostedPurchaseCreditMemo postedPurchaseCreditMemo);
}
