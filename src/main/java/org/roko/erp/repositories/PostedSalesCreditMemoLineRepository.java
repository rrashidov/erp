package org.roko.erp.repositories;

import org.roko.erp.model.PostedSalesCreditMemoLine;
import org.roko.erp.model.jpa.PostedSalesCreditMemoLineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostedSalesCreditMemoLineRepository
        extends JpaRepository<PostedSalesCreditMemoLine, PostedSalesCreditMemoLineId> {

}
