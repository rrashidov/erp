package org.roko.erp.repositories;

import java.util.List;

import org.roko.erp.model.PostedSalesOrder;
import org.roko.erp.model.PostedSalesOrderLine;
import org.roko.erp.model.jpa.PostedSalesOrderLineId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostedSalesOrderLineRepository extends JpaRepository<PostedSalesOrderLine, PostedSalesOrderLineId> {

    @Query("SELECT postedSalesOrderLine FROM PostedSalesOrderLine postedSalesOrderLine WHERE postedSalesOrderLine.postedSalesOrder = :postedSalesOrder")
    public List<PostedSalesOrderLine> findFor(@Param("postedSalesOrder") PostedSalesOrder postedSalesOrder);

    @Query("SELECT postedSalesOrderLine FROM PostedSalesOrderLine postedSalesOrderLine WHERE postedSalesOrderLine.postedSalesOrder = :postedSalesOrder")
    public Page<PostedSalesOrderLine> findFor(@Param("postedSalesOrder") PostedSalesOrder postedSalesOrder,
            Pageable pageable);

    @Query("SELECT COUNT(postedSalesOrderLine) FROM PostedSalesOrderLine postedSalesOrderLine WHERE postedSalesOrderLine.postedSalesOrder = :postedSalesOrder")
    public long count(@Param("postedSalesOrder") PostedSalesOrder postedSalesOrder);

}
