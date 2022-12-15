package org.roko.erp.frontend.repositories;

import java.util.List;

import org.roko.erp.frontend.model.PostedPurchaseOrder;
import org.roko.erp.frontend.model.PostedPurchaseOrderLine;
import org.roko.erp.frontend.model.jpa.PostedPurchaseOrderLineId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

        @Query("SELECT postedPurchaseOrderLine FROM PostedPurchaseOrderLine postedPurchaseOrderLine WHERE postedPurchaseOrderLine.postedPurchaseOrder = :postedPurchaseOrder")
        public Page<PostedPurchaseOrderLine> findFor(
                        @Param("postedPurchaseOrder") PostedPurchaseOrder postedPurchaseOrder, Pageable pageable);

        @Query("SELECT COUNT(postedPurchaseOrderLine) FROM PostedPurchaseOrderLine postedPurchaseOrderLine WHERE postedPurchaseOrderLine.postedPurchaseOrder = :postedPurchaseOrder")
        public long count(
                        @Param("postedPurchaseOrder") PostedPurchaseOrder postedPurchaseOrder);

}
