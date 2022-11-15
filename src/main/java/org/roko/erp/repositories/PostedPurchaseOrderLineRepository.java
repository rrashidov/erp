package org.roko.erp.repositories;

import org.roko.erp.model.PostedPurchaseOrderLine;
import org.roko.erp.model.jpa.PostedPurchaseOrderLineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostedPurchaseOrderLineRepository
        extends JpaRepository<PostedPurchaseOrderLine, PostedPurchaseOrderLineId> {

}
