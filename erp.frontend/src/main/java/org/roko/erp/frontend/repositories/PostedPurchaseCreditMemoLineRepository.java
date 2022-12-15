package org.roko.erp.frontend.repositories;

import java.util.List;

import org.roko.erp.frontend.model.PostedPurchaseCreditMemo;
import org.roko.erp.frontend.model.PostedPurchaseCreditMemoLine;
import org.roko.erp.frontend.model.jpa.PostedPurchaseCreditMemoLineId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

        @Query("SELECT postedPurchaseCreditMemoLine FROM PostedPurchaseCreditMemoLine postedPurchaseCreditMemoLine WHERE postedPurchaseCreditMemoLine.postedPurchaseCreditMemo = :postedPurchaseCreditMemo")
        public Page<PostedPurchaseCreditMemoLine> findFor(
                        @Param("postedPurchaseCreditMemo") PostedPurchaseCreditMemo postedPurchaseCreditMemo,
                        Pageable pageable);

        @Query("SELECT COUNT(postedPurchaseCreditMemoLine) FROM PostedPurchaseCreditMemoLine postedPurchaseCreditMemoLine WHERE postedPurchaseCreditMemoLine.postedPurchaseCreditMemo = :postedPurchaseCreditMemo")
        public long count(@Param("postedPurchaseCreditMemo") PostedPurchaseCreditMemo postedPurchaseCreditMemo);
}
