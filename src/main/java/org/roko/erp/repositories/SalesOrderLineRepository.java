package org.roko.erp.repositories;

import org.roko.erp.model.SalesOrderLine;
import org.roko.erp.model.jpa.SalesOrderLineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesOrderLineRepository extends JpaRepository<SalesOrderLine, SalesOrderLineId> {
}
