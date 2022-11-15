package org.roko.erp.repositories;

import org.roko.erp.model.PostedPurchaseCreditMemoLine;
import org.roko.erp.model.jpa.PostedPurchaseCreditMemoLineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostedPurchaseCreditMemoLineRepository
        extends JpaRepository<PostedPurchaseCreditMemoLine, PostedPurchaseCreditMemoLineId> {

}
