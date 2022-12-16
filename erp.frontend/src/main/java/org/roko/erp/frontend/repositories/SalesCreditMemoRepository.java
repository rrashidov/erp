package org.roko.erp.frontend.repositories;

import org.roko.erp.frontend.model.SalesCreditMemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesCreditMemoRepository extends JpaRepository<SalesCreditMemo, String> {

    @Query("SELECT COALESCE(SUM(salesCreditMemoLine.amount), 0) FROM SalesCreditMemoLine salesCreditMemoLine WHERE salesCreditMemoLine.salesCreditMemoLineId.salesCreditMemo = :salesCreditMemo")
    public double amount(@Param("salesCreditMemo") SalesCreditMemo salesCreditMemo);

}