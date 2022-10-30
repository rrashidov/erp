package org.roko.erp.repositories;

import java.util.List;

import org.roko.erp.model.SalesOrder;
import org.roko.erp.model.SalesOrderLine;
import org.roko.erp.model.jpa.SalesOrderLineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesOrderLineRepository
        extends JpaRepository<SalesOrderLine, SalesOrderLineId> {

    @Query("SELECT salesOrderLine FROM SalesOrderLine salesOrderLine WHERE salesOrderLine.salesOrder = :salesOrder")
    public List<SalesOrderLine> findForSalesOrder(@Param("salesOrder") SalesOrder salesOrder);

    @Query("SELECT COUNT(salesOrderLine) FROM SalesOrderLine salesOrderLine WHERE salesOrderLine.salesOrder = :salesOrder")
    public long countForSalesOrder(@Param("salesOrder") SalesOrder salesOrder);
}
