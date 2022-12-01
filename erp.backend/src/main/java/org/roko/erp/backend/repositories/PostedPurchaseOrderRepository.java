package org.roko.erp.backend.repositories;

import org.roko.erp.backend.model.PostedPurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostedPurchaseOrderRepository extends JpaRepository<PostedPurchaseOrder, String> {

    @Query("SELECT COALESCE(SUM(postedPurchaseOrderLine.amount), 0) FROM PostedPurchaseOrderLine postedPurchaseOrderLine WHERE postedPurchaseOrderLine.postedPurchaseOrder = :postedPurchaseOrder")
    public double amount(@Param("postedPurchaseOrder") PostedPurchaseOrder postedPurchaseOrder);
}
