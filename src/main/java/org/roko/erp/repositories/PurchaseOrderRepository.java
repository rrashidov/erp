package org.roko.erp.repositories;

import org.roko.erp.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, String> {

    @Query("SELECT COALESCE(SUM(purchaseOrderLine.amount), 0) FROM PurchaseOrderLine purchaseOrderLine WHERE purchaseOrderLine.purchaseOrder = :purchaseOrder")
    public double amount(@Param("purchaseOrder") PurchaseOrder purchaseOrder);
}
