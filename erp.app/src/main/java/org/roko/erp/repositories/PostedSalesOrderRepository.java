package org.roko.erp.repositories;

import org.roko.erp.model.PostedSalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostedSalesOrderRepository extends JpaRepository<PostedSalesOrder, String> {

    @Query("SELECT COALESCE(SUM(postedSalesOrderLine.amount), 0) FROM PostedSalesOrderLine postedSalesOrderLine WHERE postedSalesOrderLine.postedSalesOrderLineId.postedSalesOrder = :postedSalesOrder")
    public double amount(@Param("postedSalesOrder") PostedSalesOrder postedSalesOrder);

}
