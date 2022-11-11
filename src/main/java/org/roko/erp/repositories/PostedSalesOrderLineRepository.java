package org.roko.erp.repositories;

import org.roko.erp.model.PostedSalesOrderLine;
import org.roko.erp.model.jpa.PostedSalesOrderLineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostedSalesOrderLineRepository extends JpaRepository<PostedSalesOrderLine, PostedSalesOrderLineId> {

}
