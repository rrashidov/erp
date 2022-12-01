package org.roko.erp.backend.repositories;

import org.roko.erp.backend.model.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, String> {

    @Query("SELECT COALESCE(SUM(salesOrderLine.amount), 0) FROM SalesOrderLine salesOrderLine WHERE salesOrderLine.salesOrderLineId.salesOrder = :salesOrder")
    public double amount(@Param("salesOrder")SalesOrder salesOrder);
}
