package org.roko.erp.repositories;

import java.util.List;

import org.roko.erp.model.PostedPurchaseOrder;
import org.roko.erp.model.PostedPurchaseOrderLine;
import org.roko.erp.model.jpa.PostedPurchaseOrderLineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostedPurchaseOrderLineRepository
                extends JpaRepository<PostedPurchaseOrderLine, PostedPurchaseOrderLineId> {

        @Query("SELECT postedPurchaseOrderLine FROM PostedPurchaseOrderLine postedPurchaseOrderLine WHERE postedPurchaseOrderLine.postedPurchaseOrder = :postedPurchaseOrder")
        public List<PostedPurchaseOrderLine> findFor(
                        @Param("postedPurchaseOrder") PostedPurchaseOrder postedPurchaseOrder);

        @Query("SELECT COUNT(postedPurchaseOrderLine) FROM PostedPurchaseOrderLine postedPurchaseOrderLine WHERE postedPurchaseOrderLine.postedPurchaseOrder = :postedPurchaseOrder")
        public long count(
                        @Param("postedPurchaseOrder") PostedPurchaseOrder postedPurchaseOrder);

}
