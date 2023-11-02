package org.roko.erp.backend.repositories;

import java.math.BigDecimal;

import org.roko.erp.backend.model.PostedSalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostedSalesOrderRepository extends JpaRepository<PostedSalesOrder, String> {

    @Query("SELECT COALESCE(SUM(postedSalesOrderLine.amount), 0) FROM PostedSalesOrderLine postedSalesOrderLine WHERE postedSalesOrderLine.postedSalesOrderLineId.postedSalesOrder = :postedSalesOrder")
    public BigDecimal amount(@Param("postedSalesOrder") PostedSalesOrder postedSalesOrder);

}
