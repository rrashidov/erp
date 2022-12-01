package org.roko.erp.backend.repositories;

import java.util.List;

import org.roko.erp.backend.model.SalesOrder;
import org.roko.erp.backend.model.SalesOrderLine;
import org.roko.erp.backend.model.jpa.SalesOrderLineId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesOrderLineRepository
        extends JpaRepository<SalesOrderLine, SalesOrderLineId> {

    @Query("SELECT salesOrderLine FROM SalesOrderLine salesOrderLine WHERE salesOrderLine.salesOrder = :salesOrder")
    public List<SalesOrderLine> findForSalesOrder(@Param("salesOrder") SalesOrder salesOrder);

    @Query("SELECT salesOrderLine FROM SalesOrderLine salesOrderLine WHERE salesOrderLine.salesOrder = :salesOrder")
    public Page<SalesOrderLine> findForSalesOrder(@Param("salesOrder") SalesOrder salesOrder, Pageable pageable);

    @Query("SELECT COUNT(salesOrderLine) FROM SalesOrderLine salesOrderLine WHERE salesOrderLine.salesOrder = :salesOrder")
    public long countForSalesOrder(@Param("salesOrder") SalesOrder salesOrder);

    @Query("SELECT COALESCE(MAX(salesOrderLine.salesOrderLineId.lineNo), 0) FROM SalesOrderLine salesOrderLine WHERE salesOrderLine.salesOrder = :salesOrder")
    public int maxLineNo(@Param("salesOrder") SalesOrder salesOrder);
}
