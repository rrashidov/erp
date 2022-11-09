package org.roko.erp.repositories;

import java.util.List;

import org.roko.erp.model.PurchaseOrder;
import org.roko.erp.model.PurchaseOrderLine;
import org.roko.erp.model.jpa.PurchaseOrderLineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderLineRepository extends JpaRepository<PurchaseOrderLine, PurchaseOrderLineId> {

    @Query("SELECT purchaseOrderLine FROM PurchaseOrderLine purchaseOrderLine WHERE purchaseOrderLine.purchaseOrder = :purchaseOrder")
    public List<PurchaseOrderLine> listForPurchaseOrder(@Param("purchaseOrder") PurchaseOrder purchaseOrder);

    @Query("SELECT COUNT(purchaseOrderLine) FROM PurchaseOrderLine purchaseOrderLine WHERE purchaseOrderLine.purchaseOrder = :purchaseOrder")
    public long countForPurchaseOrder(@Param("purchaseOrder") PurchaseOrder purchaseOrder);
}