package org.roko.erp.repositories;

import org.roko.erp.model.PostedSalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostedSalesOrderRepository extends JpaRepository<PostedSalesOrder, String> {

}
