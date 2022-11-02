package org.roko.erp.repositories;

import java.util.List;

import org.roko.erp.model.SalesCreditMemo;
import org.roko.erp.model.SalesCreditMemoLine;
import org.roko.erp.model.jpa.SalesCreditMemoLineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesCreditMemoLineRepository extends JpaRepository<SalesCreditMemoLine, SalesCreditMemoLineId> {
    
    @Query("SELECT salesCreditMemoLine FROM SalesCreditMemoLine salesCreditMemoLine WHERE salesCreditMemoLine.salesCreditMemo = :salesCreditMemo")
    public List<SalesCreditMemoLine> findForSalesCreditMemo(@Param("salesCreditMemo") SalesCreditMemo salesCreditMemo);

    @Query("SELECT COUNT(salesCreditMemoLine) FROM SalesCreditMemoLine salesCreditMemoLine WHERE salesCreditMemoLine.salesCreditMemo = :salesCreditMemo")
    public long countForSalesCreditMemo(@Param("salesCreditMemo") SalesCreditMemo salesCreditMemo);
}
