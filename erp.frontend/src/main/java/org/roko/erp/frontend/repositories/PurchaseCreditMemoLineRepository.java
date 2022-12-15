package org.roko.erp.frontend.repositories;

import java.util.List;

import org.roko.erp.frontend.model.PurchaseCreditMemo;
import org.roko.erp.frontend.model.PurchaseCreditMemoLine;
import org.roko.erp.frontend.model.jpa.PurchaseCreditMemoLineId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseCreditMemoLineRepository
        extends JpaRepository<PurchaseCreditMemoLine, PurchaseCreditMemoLineId> {

    @Query("SELECT purchaseCreditMemoLine FROM PurchaseCreditMemoLine purchaseCreditMemoLine WHERE purchaseCreditMemoLine.purchaseCreditMemo = :purchaseCreditMemo")
    public List<PurchaseCreditMemoLine> findFor(@Param("purchaseCreditMemo") PurchaseCreditMemo purchaseCreditMemo);

    @Query("SELECT purchaseCreditMemoLine FROM PurchaseCreditMemoLine purchaseCreditMemoLine WHERE purchaseCreditMemoLine.purchaseCreditMemo = :purchaseCreditMemo")
    public Page<PurchaseCreditMemoLine> findFor(@Param("purchaseCreditMemo") PurchaseCreditMemo purchaseCreditMemo,
            Pageable pageable);

    @Query("SELECT COUNT(purchaseCreditMemoLine) FROM PurchaseCreditMemoLine purchaseCreditMemoLine WHERE purchaseCreditMemoLine.purchaseCreditMemo = :purchaseCreditMemo")
    public long count(@Param("purchaseCreditMemo") PurchaseCreditMemo purchaseCreditMemo);

    @Query("SELECT COALESCE(MAX(purchaseCreditMemoLine.purchaseCreditMemoLineId.lineNo), 0) FROM PurchaseCreditMemoLine purchaseCreditMemoLine WHERE purchaseCreditMemoLine.purchaseCreditMemo = :purchaseCreditMemo")
    public int maxLineNo(@Param("purchaseCreditMemo") PurchaseCreditMemo purchaseCreditMemo);
}
