package org.roko.erp.repositories;

import org.roko.erp.model.PostedPurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostedPurchaseOrderRepository extends JpaRepository<PostedPurchaseOrder, String> {

}
