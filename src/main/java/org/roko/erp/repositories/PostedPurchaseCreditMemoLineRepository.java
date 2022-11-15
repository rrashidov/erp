package org.roko.erp.repositories;

import java.util.List;

import org.roko.erp.model.PostedPurchaseCreditMemo;
import org.roko.erp.model.PostedPurchaseCreditMemoLine;
import org.roko.erp.model.jpa.PostedPurchaseCreditMemoLineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostedPurchaseCreditMemoLineRepository
                extends JpaRepository<PostedPurchaseCreditMemoLine, PostedPurchaseCreditMemoLineId> {

        @Query("SELECT postedPurchaseCreditMemoLine FROM PostedPurchaseCreditMemoLine postedPurchaseCreditMemoLine WHERE postedPurchaseCreditMemoLine.postedPurchaseCreditMemo = :postedPurchaseCreditMemo")
        public List<PostedPurchaseCreditMemoLine> findFor(
                        @Param("postedPurchaseCreditMemo") PostedPurchaseCreditMemo postedPurchaseCreditMemo);

        @Query("SELECT COUNT(postedPurchaseCreditMemoLine) FROM PostedPurchaseCreditMemoLine postedPurchaseCreditMemoLine WHERE postedPurchaseCreditMemoLine.postedPurchaseCreditMemo = :postedPurchaseCreditMemo")
        public long count(@Param("postedPurchaseCreditMemo") PostedPurchaseCreditMemo postedPurchaseCreditMemo);
}
