package org.roko.erp.backend.repositories;

import java.util.List;

import org.roko.erp.backend.model.PurchaseOrder;
import org.roko.erp.backend.model.PurchaseOrderLine;
import org.roko.erp.backend.model.jpa.PurchaseOrderLineId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderLineRepository extends JpaRepository<PurchaseOrderLine, PurchaseOrderLineId> {

    @Query("SELECT purchaseOrderLine FROM PurchaseOrderLine purchaseOrderLine WHERE purchaseOrderLine.purchaseOrder = :purchaseOrder")
    public List<PurchaseOrderLine> listForPurchaseOrder(@Param("purchaseOrder") PurchaseOrder purchaseOrder);

    @Query("SELECT purchaseOrderLine FROM PurchaseOrderLine purchaseOrderLine WHERE purchaseOrderLine.purchaseOrder = :purchaseOrder")
    public Page<PurchaseOrderLine> listForPurchaseOrder(@Param("purchaseOrder") PurchaseOrder purchaseOrder,
            Pageable pageable);

    @Query("SELECT COUNT(purchaseOrderLine) FROM PurchaseOrderLine purchaseOrderLine WHERE purchaseOrderLine.purchaseOrder = :purchaseOrder")
    public long countForPurchaseOrder(@Param("purchaseOrder") PurchaseOrder purchaseOrder);

    @Query("SELECT COALESCE(MAX(purchaseOrderLine.purchaseOrderLineId.lineNo), 0) FROM PurchaseOrderLine purchaseOrderLine WHERE purchaseOrderLine.purchaseOrder = :purchaseOrder")
    public int maxLineNo(@Param("purchaseOrder") PurchaseOrder purchaseOrder);
}
