package org.roko.erp.backend.repositories;

import org.roko.erp.backend.model.PostedSalesCreditMemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostedSalesCreditMemoRepository extends JpaRepository<PostedSalesCreditMemo, String> {

    @Query("SELECT COALESCE(SUM(postedSalesCreditMemoLine.amount), 0) FROM PostedSalesCreditMemoLine postedSalesCreditMemoLine WHERE postedSalesCreditMemoLine.postedSalesCreditMemoLineId.postedSalesCreditMemo = :postedSalesCreditMemo")
    public double amount(@Param("postedSalesCreditMemo") PostedSalesCreditMemo postedSalesCreditMemo);

}
